package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.PublicacionAdminCompleta;
import mx.edu.utez.libriflow.model.PublicacionAdministrador;
import mx.edu.utez.libriflow.model.PublicacionResumen;
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
 * administradas directamente por LibriFlow (PublicacionAdministradorDao).
 *
 * @author Alejandro Mena Pereyda
 * @since 25/08/2026
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PublicacionAdministradorDaoTest {

    private PublicacionAdministradorDao dao;

    private static int idPublicacionGenerado = -1;
    private static int idLibroExistente = -1;

    @BeforeEach
    void setUp() {
        dao = new PublicacionAdministradorDao();

        if (idLibroExistente == -1) {
            idLibroExistente = obtenerIdMinimo("libro", "id_libro");
        }
    }

    /**
     * Este método se encarga de probar la creación y registro de una nueva
     * publicación oficial realizada por el administrador en la base de datos.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(1)
    void create() {
        PublicacionAdministrador entidad = new PublicacionAdministrador();
        entidad.setIdLibro(idLibroExistente);
        entidad.setSinopsis("Publicación oficial de administración para pruebas JUnit.");
        entidad.setCantidad(5);
        entidad.setEsVenta(1);
        entidad.setEsRenta(0);
        entidad.setPrecio(299.99);

        idPublicacionGenerado = dao.create(entidad);

        assertTrue(idPublicacionGenerado > 0, "Debe retornar el ID autogenerado mayor a 0");
    }

    /**
     * Este método se encarga de probar la consulta y obtención del listado
     * de resumen de las publicaciones activas para el catálogo.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(2)
    void getResumenCatalogo() {
        List<PublicacionResumen> catalogo = dao.getResumenCatalogo();
        assertNotNull(catalogo, "La lista del catálogo no debe ser nula");
    }

    /**
     * Este método se encarga de probar la obtención detallada e integral
     * de una publicación del administrador a partir de su identificador.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(3)
    void getPublicacionAdminCompleta() {
        assertTrue(idPublicacionGenerado > 0, "Se requiere un ID válido generado previamente");

        PublicacionAdminCompleta detalle = dao.getPublicacionAdminCompleta(idPublicacionGenerado);
        assertNotNull(detalle, "Debe retornar el detalle de la publicación recién creada");
        assertEquals(idPublicacionGenerado, detalle.getIdPublicacionLf());
        assertEquals("Publicación oficial de administración para pruebas JUnit.", detalle.getSinopsis());
    }

    /**
     * Este método se encarga de probar la disminución secuencial del inventario
     * disponible para una publicación de administración.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(4)
    void disminuirInventario() {
        assertTrue(idPublicacionGenerado > 0, "Se requiere un ID válido generado previamente");

        boolean disminuido = dao.disminuirInventario(idPublicacionGenerado);
        assertTrue(disminuido, "Debe descontar 1 unidad del inventario exitosamente");
    }

    /**
     * Este método se encarga de probar la baja lógica (cambio de estado a INACTIVO)
     * de una publicación del administrador.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(5)
    void darDeBajaPublicacionAdmin() {
        assertTrue(idPublicacionGenerado > 0, "Se requiere un ID válido generado previamente");

        boolean dadoDeBaja = dao.darDeBajaPublicacionAdmin(idPublicacionGenerado);
        assertTrue(dadoDeBaja, "Debe cambiar el estado de la publicación a INACTIVO");
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