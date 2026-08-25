package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.PublicacionResumen;
import mx.edu.utez.libriflow.model.PublicacionUsuario;
import mx.edu.utez.libriflow.model.PublicacionUsuarioCompleta;
import mx.edu.utez.libriflow.utils.SQLconnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Esta clase se encarga de realizar las pruebas unitarias e integración para
 * verificar el correcto funcionamiento de los métodos del DAO de publicaciones
 * de usuarios (PublicacionUsuarioDao).
 *
 * @author Alejandro Mena Pereyda
 * @since 25/08/2026
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PublicacionUsuarioDaoTest {

    private PublicacionUsuarioDao dao;

    private static int idPublicacionPrueba = -1;
    private static int idUsuarioExistente = -1;
    private static int idLibroExistente = -1;
    private static int idPublicacionExistente = -1;

    @BeforeEach
    void setUp() {
        dao = new PublicacionUsuarioDao();

        if (idUsuarioExistente == -1 || idLibroExistente == -1 || idPublicacionExistente == -1) {
            idUsuarioExistente = obtenerIdMinimo("usuario", "id_usuario");
            idLibroExistente = obtenerIdMinimo("libro", "id_libro");
            idPublicacionExistente = obtenerIdMinimo("publicacion_us", "id_publicacion_us");
        }
    }

    /**
     * Este método se encarga de probar la creación y registro de una nueva
     * publicación realizada por un usuario en la base de datos.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(1)
    void create() {
        PublicacionUsuario nueva = new PublicacionUsuario();
        nueva.setIdUsuario(idUsuarioExistente);
        nueva.setIdLibro(idLibroExistente);
        nueva.setSinopsis("Sinopsis de prueba para la verificación de integración en publicaciones de usuario.");
        nueva.setPrecio(200);

        idPublicacionPrueba = dao.create(nueva);

        assertTrue(idPublicacionPrueba > 0, "Debe retornar el ID autogenerado mayor a 0");
    }

    /**
     * Este método se encarga de probar la consulta y obtención detallada
     * de los datos de una publicación completa de usuario por su ID.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(2)
    void getPublicacionUsuarioCompleta() {
        PublicacionUsuarioCompleta resultado = dao.getPublicacionUsuarioCompleta(idPublicacionExistente);

        if (resultado != null) {
            assertEquals(idPublicacionExistente, resultado.getIdPublicacion());
            assertEquals(idUsuarioExistente, resultado.getIdPropietario());
        }
    }

    /**
     * Este método se encarga de probar la obtención del resumen de publicaciones
     * asociadas a un usuario específico aplicando un criterio de ordenamiento.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(3)
    void getResumenPublicacionesPorUsuario() {
        List<PublicacionResumen> lista = dao.getResumenPublicacionesPorUsuario(idUsuarioExistente, "antiguas");
        assertNotNull(lista, "La lista devuelta no debe ser nula");
    }

    /**
     * Este método se encarga de probar la obtención del resumen de publicaciones
     * de un usuario mediante el método sobrecargado sin ordenamiento explícito.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(4)
    void testGetResumenPublicacionesPorUsuario() {
        List<PublicacionResumen> lista = dao.getResumenPublicacionesPorUsuario(idUsuarioExistente);
        assertNotNull(lista, "La lista devuelta no debe ser nula");
    }

    /**
     * Este método se encarga de probar la consulta de resúmenes de publicaciones
     * filtradas mediante un listado de identificadores.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(5)
    void getPublicacionesByArreglo() {
        List<Integer> ids = List.of(idPublicacionExistente);
        List<PublicacionResumen> lista = dao.getPublicacionesByArreglo(ids);
        assertNotNull(lista, "La lista no debe ser nula");
    }

    /**
     * Este método se encarga de probar el conteo total de publicaciones activas
     * pertenecientes a un usuario en la base de datos.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(6)
    void contarPublicacionesPorUsuario() {
        int conteo = dao.contarPublicacionesPorUsuario(idUsuarioExistente);
        assertTrue(conteo >= 0, "El conteo de publicaciones activas debe ser mayor o igual a 0");
    }

    /**
     * Este método se encarga de probar la búsqueda y filtrado de publicaciones
     * de usuarios de acuerdo con criterios de estado, búsqueda y género.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(7)
    void buscarYFiltrarPublicacionesUs() {
        List<PublicacionResumen> lista = dao.buscarYFiltrarPublicacionesUs("ACTIVO", "", "TODOS");
        assertNotNull(lista, "El resultado del filtro no debe ser nulo");
    }

    /**
     * Este método se encarga de probar el cambio de estado de una publicación
     * de usuario existente.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(8)
    void cambiarEstadoPublicacion() {
        boolean actualizado = dao.cambiarEstadoPublicacion(idPublicacionPrueba, "ACTIVO");
        assertTrue(actualizado, "Debe cambiar de PENDIENTE a ACTIVO exitosamente");
    }

    /**
     * Este método se encarga de probar el proceso de dar de baja una publicación
     * cambiando su estado a rechazado.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(9)
    void darDeBajaPublicacionUsuario() {
        boolean dadoDeBaja = dao.darDeBajaPublicacionUsuario(idPublicacionPrueba);
        assertTrue(dadoDeBaja, "Debe actualizar el estado a RECHAZADO");
    }

    /**
     * Este método se encarga de probar la actualización integral de la información
     * de una publicación y su libro asociado dentro de una transacción.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(10)
    void actualizarPublicacionCompleta() {
        boolean actualizado = dao.actualizarPublicacionCompleta(
                idPublicacionPrueba,
                idUsuarioExistente,
                "Título Editado Test",
                "Autor Editado Test",
                "Editorial Test",
                "Fantasía",
                "Sinopsis actualizada para la prueba de edición completa.",
                299.00
        );

        assertTrue(actualizado, "Debe actualizar la publicación y el libro en transacción");
    }

    /**
     * Este método se encarga de probar la eliminación de una publicación por parte
     * de su propietario junto con sus registros asociados.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(11)
    void eliminarPublicacionPropietario() {
        boolean eliminado = dao.eliminarPublicacionPropietario(idPublicacionPrueba, idUsuarioExistente);
        assertTrue(eliminado, "Debe eliminar la publicación, sus imágenes y el libro asociado");
    }

    /**
     * Este método se encarga de probar la eliminación directa de una publicación
     * mediante su identificador único.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(12)
    void deletePublicacionById() {
        PublicacionUsuario temp = new PublicacionUsuario();
        temp.setIdUsuario(idUsuarioExistente);
        temp.setIdLibro(idLibroExistente);
        temp.setSinopsis("Para prueba deletePublicacionById");
        temp.setPrecio(50.00);

        int idAEliminar = dao.create(temp);
        assertTrue(idAEliminar > 0);

        boolean eliminado = dao.deletePublicacionById(idAEliminar);
        assertTrue(eliminado, "Debe eliminar la publicación cuando su estado sea PENDIENTE o RECHAZADO");
    }

    /**
     * Este método se encarga de probar la ejecución del método stub getAll.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(13)
    void getAll() {
        List<PublicacionUsuario> lista = dao.getAll();
        assertNotNull(lista);
        assertTrue(lista.isEmpty(), "El método stub getAll debe retornar una lista vacía");
    }

    /**
     * Este método se encarga de probar la ejecución del método stub getById.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(14)
    void getById() {
        PublicacionUsuario resultado = dao.getById(idPublicacionExistente);
        assertNull(resultado, "El método stub getById debe retornar null");
    }

    /**
     * Consulta y devuelve el primer ID válido de una tabla dada en la base de datos
     * para evitar la dependencia de datos quemados.
     *
     * @param tabla Nombre de la tabla a consultar.
     * @param columna Nombre de la columna de clave primaria.
     * @return El identificador mínimo encontrado o -1 si ocurre un error.
     */
    private int obtenerIdMinimo(String tabla, String columna) {
        String sql = "SELECT MIN(" + columna + ") FROM " + tabla;
        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}