package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Imagen;
import mx.edu.utez.libriflow.model.PublicacionAdministrador;
import mx.edu.utez.libriflow.model.PublicacionUsuario;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Esta clase se encarga de realizar las pruebas unitarias e integración para
 * verificar el correcto funcionamiento de los métodos del DAO de imágenes (ImagenDao),
 * asegurando la correcta inserción y actualización de imágenes tanto para publicaciones
 * de usuarios como de administración.
 *
 * @author Alejandro Mena Pereyda
 * @since 25/08/2026
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ImagenDaoTest {

    private ImagenDao imagenDao;
    private PublicacionUsuarioDao usuarioDao;
    private PublicacionAdministradorDao adminDao;

    // Identificadores de publicaciones generados dinámicamente en tiempo de ejecución
    private static int idPublicacionUsCreada = -1;
    private static int idPublicacionLfCreada = -1;

    // Identificadores obtenidos dinámicamente de la base de datos sin datos quemados
    private static int idUsuarioExistente = -1;
    private static int idLibroExistente = -1;

    @BeforeEach
    void setUp() {
        imagenDao = new ImagenDao();
        usuarioDao = new PublicacionUsuarioDao();
        adminDao = new PublicacionAdministradorDao();

        // Obtención dinámica de IDs existentes para evitar dependencias de datos hardcodeados
        if (idUsuarioExistente == -1 || idLibroExistente == -1) {
            idUsuarioExistente = obtenerIdMinimo("usuario", "id_usuario");
            idLibroExistente = obtenerIdMinimo("libro", "id_libro");
        }
    }

    /**
     * Este método se encarga de probar el registro de una nueva imagen ligada
     * a una publicación realizada por un usuario.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(1)
    void createUs() {
        PublicacionUsuario pubUs = new PublicacionUsuario();
        pubUs.setIdUsuario(idUsuarioExistente);
        pubUs.setIdLibro(idLibroExistente);
        pubUs.setSinopsis("Publicación de prueba para registro de imagen de usuario");
        pubUs.setPrecio(100.00);

        idPublicacionUsCreada = usuarioDao.create(pubUs);
        assertTrue(idPublicacionUsCreada > 0, "Debe crearse la publicación de usuario para asociar la imagen");

        Imagen imagenUs = new Imagen();
        imagenUs.setIdPublicacionUs(idPublicacionUsCreada);
        imagenUs.setImagen("rutas/test_usuario_portada.jpg");

        boolean registrado = imagenDao.createUs(imagenUs, 1);
        assertTrue(registrado, "La imagen de la publicación del usuario debe registrarse correctamente");
    }

    /**
     * Este método se encarga de probar la actualización de la ruta de una imagen
     * existente perteneciente a una publicación de usuario según su tipo.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(2)
    void actualizarImagenUs() {
        assertTrue(idPublicacionUsCreada > 0, "Se requiere una publicación válida creada previamente");

        String nuevaRuta = "rutas/test_usuario_portada_actualizada.jpg";
        boolean actualizado = imagenDao.actualizarImagenUs(idPublicacionUsCreada, 1, nuevaRuta);

        assertTrue(actualizado, "La ruta de la imagen debe actualizarse correctamente");
    }

    /**
     * Este método se encarga de probar el registro de una imagen asociada a una
     * publicación administrada directamente por LibriFlow.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(3)
    void createLf() {
        PublicacionAdministrador pubLf = new PublicacionAdministrador();
        pubLf.setIdLibro(idLibroExistente);
        pubLf.setSinopsis("Publicación de prueba para registro de imagen de administración");
        pubLf.setCantidad(2);
        pubLf.setEsVenta(1);
        pubLf.setEsRenta(0);
        pubLf.setPrecio(150.00);

        idPublicacionLfCreada = adminDao.create(pubLf);
        assertTrue(idPublicacionLfCreada > 0, "Debe crearse la publicación de administración para asociar la imagen");

        Imagen imagenLf = new Imagen();
        imagenLf.setIdPublicacionLibriflow(idPublicacionLfCreada);
        imagenLf.setImagen("rutas/test_admin_portada.jpg");

        boolean registrado = imagenDao.createLf(imagenLf, 1);
        assertTrue(registrado, "La imagen de la publicación de administración debe registrarse correctamente");
    }

    /**
     * Este método se encarga de realizar la limpieza de los datos de prueba
     * generados en la base de datos al finalizar el ciclo de ejecución.
     *
     * @author Alejandro Mena Pereyda
     * @since 25/08/2026
     */
    @Test
    @Order(4)
    void tearDownLimpieza() {
        if (idPublicacionUsCreada > 0) {
            usuarioDao.deletePublicacionById(idPublicacionUsCreada);
        }
        if (idPublicacionLfCreada > 0) {
            adminDao.darDeBajaPublicacionAdmin(idPublicacionLfCreada);
        }
    }

    /**
     * Consulta y devuelve el primer ID válido registrado en una tabla dada de la base de datos
     * para evitar dependencia de datos numéricos hardcodeados.
     *
     * @param tabla Nombre de la tabla a consultar.
     * @param columna Nombre de la columna de clave primaria.
     * @return El identificador mínimo encontrado o -1 si falla.
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