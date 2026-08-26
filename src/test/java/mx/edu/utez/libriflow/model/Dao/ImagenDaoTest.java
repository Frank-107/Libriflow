package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Imagen;
import mx.edu.utez.libriflow.testconfig.OracleTestBase;
import mx.edu.utez.libriflow.utils.SQLconnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Esta clase se encarga de probar las operaciones del DAO de imágenes
 * utilizando una instancia temporal de Oracle ejecutada mediante Docker
 * y Testcontainers.
 *
 * Cada prueba crea sus propios registros para evitar dependencias con
 * información preexistente o con el orden de ejecución de otras pruebas.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
class ImagenDaoTest extends OracleTestBase {

    private ImagenDao imagenDao;

    /**
     * Inicializa el DAO antes de ejecutar cada prueba.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @BeforeEach
    void setUp() {
        imagenDao = new ImagenDao();
    }

    /**
     * Comprueba el registro de una imagen asociada a una publicación
     * realizada por un usuario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void createUs() {

        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Imagen Usuario");

        int idPublicacion = crearPublicacionUsuarioPrueba(
                idUsuario,
                idLibro
        );

        Imagen imagen = new Imagen();
        imagen.setIdPublicacionUs(idPublicacion);
        imagen.setImagen("rutas/test_usuario_portada.jpg");

        boolean creado = imagenDao.createUs(
                imagen,
                1
        );

        assertTrue(creado);

        int idImagen = obtenerIdImagenUsuario(
                idPublicacion,
                1
        );

        assertTrue(idImagen > 0);

        Imagen guardada = imagenDao.getById(idImagen);

        assertNotNull(guardada);
        assertEquals(idPublicacion, guardada.getIdPublicacionUs());
        assertEquals("rutas/test_usuario_portada.jpg", guardada.getImagen());
    }

    /**
     * Comprueba el registro de una imagen asociada a una publicación
     * administrada directamente por LibriFlow.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void createLf() {

        int idLibro = crearLibroPrueba("Libro Imagen LibriFlow");

        int idPublicacion = crearPublicacionAdministradorPrueba(
                idLibro
        );

        Imagen imagen = new Imagen();
        imagen.setIdPublicacionLibriflow(idPublicacion);
        imagen.setImagen("rutas/test_admin_portada.jpg");

        boolean creado = imagenDao.createLf(
                imagen,
                1
        );

        assertTrue(creado);

        int idImagen = obtenerIdImagenLibriFlow(
                idPublicacion,
                1
        );

        assertTrue(idImagen > 0);

        Imagen guardada = imagenDao.getById(idImagen);

        assertNotNull(guardada);
        assertEquals(idPublicacion, guardada.getIdPublicacionLibriflow());
        assertEquals("rutas/test_admin_portada.jpg", guardada.getImagen());
    }

    /**
     * Comprueba que se obtengan todas las imágenes registradas.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getAll() {

        int idUsuario = crearUsuarioPrueba();

        int idLibroUs = crearLibroPrueba(
                "Libro Imagen Lista Usuario"
        );

        int idLibroLf = crearLibroPrueba(
                "Libro Imagen Lista Admin"
        );

        int idPublicacionUs = crearPublicacionUsuarioPrueba(
                idUsuario,
                idLibroUs
        );

        int idPublicacionLf = crearPublicacionAdministradorPrueba(
                idLibroLf
        );

        Imagen imagenUs = new Imagen();
        imagenUs.setIdPublicacionUs(idPublicacionUs);
        imagenUs.setImagen("lista-usuario.jpg");

        Imagen imagenLf = new Imagen();
        imagenLf.setIdPublicacionLibriflow(idPublicacionLf);
        imagenLf.setImagen("lista-admin.jpg");

        assertTrue(imagenDao.createUs(imagenUs, 1));
        assertTrue(imagenDao.createLf(imagenLf, 1));

        int idImagenUs = obtenerIdImagenUsuario(
                idPublicacionUs,
                1
        );

        int idImagenLf = obtenerIdImagenLibriFlow(
                idPublicacionLf,
                1
        );

        List<Imagen> imagenes = imagenDao.getAll();

        assertNotNull(imagenes);
        assertEquals(2, imagenes.size());

        assertTrue(
                imagenes.stream().anyMatch(
                        imagen -> imagen.getIdImagen() == idImagenUs
                )
        );

        assertTrue(
                imagenes.stream().anyMatch(
                        imagen -> imagen.getIdImagen() == idImagenLf
                )
        );
    }

    /**
     * Comprueba la consulta de una imagen mediante su identificador.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getById() {

        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Imagen GetById");

        int idPublicacion = crearPublicacionUsuarioPrueba(
                idUsuario,
                idLibro
        );

        Imagen imagen = new Imagen();
        imagen.setIdPublicacionUs(idPublicacion);
        imagen.setImagen("imagen-getbyid.jpg");

        assertTrue(
                imagenDao.createUs(imagen, 2)
        );

        int idImagen = obtenerIdImagenUsuario(
                idPublicacion,
                2
        );

        Imagen resultado = imagenDao.getById(idImagen);

        assertNotNull(resultado);
        assertEquals(idImagen, resultado.getIdImagen());
        assertEquals(idPublicacion, resultado.getIdPublicacionUs());
        assertEquals("imagen-getbyid.jpg", resultado.getImagen());
    }

    /**
     * Comprueba que consultar un identificador inexistente devuelva null.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getByIdInexistente() {

        assertNull(
                imagenDao.getById(-999)
        );
    }

    /**
     * Comprueba la actualización CRUD de la ruta de una imagen por su ID.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void update() {

        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Imagen Update");

        int idPublicacion = crearPublicacionUsuarioPrueba(
                idUsuario,
                idLibro
        );

        Imagen imagen = new Imagen();
        imagen.setIdPublicacionUs(idPublicacion);
        imagen.setImagen("imagen-original.jpg");

        assertTrue(
                imagenDao.createUs(imagen, 1)
        );

        int idImagen = obtenerIdImagenUsuario(
                idPublicacion,
                1
        );

        Imagen actualizada = imagenDao.getById(idImagen);

        assertNotNull(actualizada);

        actualizada.setImagen(
                "imagen-actualizada.jpg"
        );

        assertTrue(
                imagenDao.update(actualizada)
        );

        Imagen resultado = imagenDao.getById(idImagen);

        assertNotNull(resultado);
        assertEquals(
                "imagen-actualizada.jpg",
                resultado.getImagen()
        );
    }

    /**
     * Comprueba la eliminación de una imagen mediante su identificador.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void delete() {

        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Imagen Delete");

        int idPublicacion = crearPublicacionUsuarioPrueba(
                idUsuario,
                idLibro
        );

        Imagen imagen = new Imagen();
        imagen.setIdPublicacionUs(idPublicacion);
        imagen.setImagen("imagen-delete.jpg");

        assertTrue(
                imagenDao.createUs(imagen, 3)
        );

        int idImagen = obtenerIdImagenUsuario(
                idPublicacion,
                3
        );

        assertTrue(
                imagenDao.delete(idImagen)
        );

        assertNull(
                imagenDao.getById(idImagen)
        );
    }

    /**
     * Comprueba que eliminar una imagen inexistente devuelva false.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void deleteInexistente() {

        assertFalse(
                imagenDao.delete(-999)
        );
    }

    /**
     * Comprueba la actualización de una imagen de usuario utilizando
     * el identificador de la publicación y el tipo de imagen.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void actualizarImagenUs() {

        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba(
                "Libro Imagen Actualizar Especial"
        );

        int idPublicacion = crearPublicacionUsuarioPrueba(
                idUsuario,
                idLibro
        );

        Imagen imagen = new Imagen();
        imagen.setIdPublicacionUs(idPublicacion);
        imagen.setImagen(
                "rutas/test_usuario_portada.jpg"
        );

        assertTrue(
                imagenDao.createUs(imagen, 1)
        );

        boolean actualizado =
                imagenDao.actualizarImagenUs(
                        idPublicacion,
                        1,
                        "rutas/test_usuario_portada_actualizada.jpg"
                );

        assertTrue(actualizado);

        int idImagen = obtenerIdImagenUsuario(
                idPublicacion,
                1
        );

        Imagen resultado = imagenDao.getById(idImagen);

        assertNotNull(resultado);
        assertEquals(
                "rutas/test_usuario_portada_actualizada.jpg",
                resultado.getImagen()
        );
    }

    /**
     * Comprueba que la actualización de una imagen inexistente
     * devuelva false.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void actualizarImagenUsInexistente() {

        assertFalse(
                imagenDao.actualizarImagenUs(
                        -999,
                        1,
                        "no-existe.jpg"
                )
        );
    }

    /**
     * Inserta un usuario válido directamente en la base de datos de pruebas.
     *
     * @return Identificador generado del usuario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearUsuarioPrueba() {

        String sql = """
                INSERT INTO usuario(
                    nombre,
                    apellido_paterno,
                    apellido_materno,
                    correo_electronico,
                    telefono,
                    estado_cuenta
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_USUARIO"}
             )) {

            ps.setString(1, "Usuario Imagen");
            ps.setString(2, "ApellidoP");
            ps.setString(3, "ApellidoM");
            ps.setString(
                    4,
                    "imagen_" + System.nanoTime() + "@test.com"
            );
            ps.setString(5, "7771234567");
            ps.setString(6, "ACTIVA");

            assertEquals(
                    1,
                    ps.executeUpdate()
            );

            try (ResultSet rs = ps.getGeneratedKeys()) {

                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo crear el usuario de prueba",
                    e
            );
        }
    }

    /**
     * Inserta un libro válido directamente en la base de datos de pruebas.
     *
     * @param titulo Título del libro que se desea registrar.
     * @return Identificador generado del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearLibroPrueba(
            String titulo) {

        String sql = """
                INSERT INTO libro(
                    titulo,
                    autor,
                    editorial,
                    genero
                )
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_LIBRO"}
             )) {

            ps.setString(1, titulo);
            ps.setString(2, "Autor Imagen Test");
            ps.setString(3, "Editorial Imagen Test");
            ps.setString(4, "Tecnología");

            assertEquals(
                    1,
                    ps.executeUpdate()
            );

            try (ResultSet rs = ps.getGeneratedKeys()) {

                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo crear el libro de prueba",
                    e
            );
        }
    }

    /**
     * Inserta una publicación de usuario válida para las pruebas de imágenes.
     *
     * @param idUsuario Identificador del usuario propietario.
     * @param idLibro Identificador del libro relacionado.
     * @return Identificador generado de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearPublicacionUsuarioPrueba(
            int idUsuario,
            int idLibro) {

        String sql = """
                INSERT INTO publicacion_us(
                    id_usuario,
                    id_libro,
                    sinopsis,
                    precio,
                    estado
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_PUBLICACION_US"}
             )) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idLibro);
            ps.setString(
                    3,
                    "Publicación para prueba de ImagenDao."
            );
            ps.setDouble(4, 100.00);
            ps.setString(5, "PENDIENTE");

            assertEquals(
                    1,
                    ps.executeUpdate()
            );

            try (ResultSet rs = ps.getGeneratedKeys()) {

                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo crear la publicación de usuario",
                    e
            );
        }
    }

    /**
     * Inserta una publicación oficial válida para las pruebas de imágenes.
     *
     * @param idLibro Identificador del libro relacionado.
     * @return Identificador generado de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearPublicacionAdministradorPrueba(
            int idLibro) {

        String sql = """
                INSERT INTO publicacion_lf(
                    id_libro,
                    sinopsis,
                    cantidad,
                    es_venta,
                    es_renta,
                    precio
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_PUBLICACION_LF"}
             )) {

            ps.setInt(1, idLibro);
            ps.setString(
                    2,
                    "Publicación oficial para prueba de ImagenDao."
            );
            ps.setInt(3, 3);
            ps.setInt(4, 1);
            ps.setInt(5, 0);
            ps.setDouble(6, 150.00);

            assertEquals(
                    1,
                    ps.executeUpdate()
            );

            try (ResultSet rs = ps.getGeneratedKeys()) {

                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo crear la publicación de administración",
                    e
            );
        }
    }

    /**
     * Obtiene el ID de una imagen asociada a una publicación de usuario
     * y a un tipo específico.
     *
     * @param idPublicacion Identificador de la publicación de usuario.
     * @param tipo Tipo de imagen.
     * @return Identificador de la imagen encontrada.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int obtenerIdImagenUsuario(
            int idPublicacion,
            int tipo) {

        String sql = """
                SELECT id_imagen
                FROM imagen
                WHERE id_publicacion_us = ?
                AND tipo = ?
                """;

        return obtenerIdImagen(
                sql,
                idPublicacion,
                tipo
        );
    }

    /**
     * Obtiene el ID de una imagen asociada a una publicación oficial
     * y a un tipo específico.
     *
     * @param idPublicacion Identificador de la publicación oficial.
     * @param tipo Tipo de imagen.
     * @return Identificador de la imagen encontrada.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int obtenerIdImagenLibriFlow(
            int idPublicacion,
            int tipo) {

        String sql = """
                SELECT id_imagen
                FROM imagen
                WHERE id_publicacion_lf = ?
                AND tipo = ?
                """;

        return obtenerIdImagen(
                sql,
                idPublicacion,
                tipo
        );
    }

    /**
     * Ejecuta una consulta auxiliar para recuperar el identificador
     * de una imagen.
     *
     * @param sql Consulta que será ejecutada.
     * @param idPublicacion Identificador de la publicación.
     * @param tipo Tipo de imagen.
     * @return Identificador encontrado o -1 si no existe.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int obtenerIdImagen(
            String sql,
            int idPublicacion,
            int tipo) {

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, idPublicacion);
            ps.setInt(2, tipo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("id_imagen");
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo obtener el ID de la imagen",
                    e
            );
        }

        return -1;
    }
}