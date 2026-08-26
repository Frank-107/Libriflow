package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Resena;
import mx.edu.utez.libriflow.testconfig.OracleTestBase;
import mx.edu.utez.libriflow.utils.SQLconnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para {@link ResenaDao} utilizando una instancia
 * temporal de Oracle ejecutada mediante Docker y Testcontainers.
 *
 * Cada prueba crea los usuarios, libros, publicaciones y transacciones
 * que necesita para evitar depender de IDs preexistentes.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
class ResenaDaoTest extends OracleTestBase {

    private ResenaDao resenaDao;

    /**
     * Inicializa el DAO antes de cada prueba.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @BeforeEach
    void setUp() {
        resenaDao = new ResenaDao();
    }

    /**
     * Comprueba que una reseña válida pueda registrarse correctamente.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("Create registra una reseña válida")
    void create() {

        int idUsuario =
                crearUsuarioPrueba(
                        "Usuario Create"
                );

        int idPublicacion =
                crearPublicacionLfPrueba(
                        "Libro Create"
                );

        String comentario =
                "Excelente libro "
                        + System.nanoTime();

        Resena resena =
                crearResena(
                        idUsuario,
                        idPublicacion,
                        comentario,
                        5
                );

        assertTrue(
                resenaDao.create(resena)
        );

        int idResena =
                obtenerIdResena(
                        idUsuario,
                        idPublicacion,
                        comentario
                );

        assertTrue(idResena > 0);

        Resena guardada =
                resenaDao.getById(idResena);

        assertNotNull(guardada);
        assertEquals(
                idUsuario,
                guardada.getIdUsuario()
        );
        assertEquals(
                idPublicacion,
                guardada.getIdPublicacionLf()
        );
        assertEquals(
                comentario,
                guardada.getComentario()
        );
        assertEquals(
                5,
                guardada.getCalificacion()
        );
        assertNotNull(
                guardada.getFecha()
        );
    }

    /**
     * Comprueba que create rechace una llave foránea inexistente.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("Create falla con relaciones inexistentes")
    void createConRelacionesInexistentes() {

        Resena resena =
                crearResena(
                        -999,
                        -999,
                        "Reseña inválida",
                        5
                );

        assertFalse(
                resenaDao.create(resena)
        );
    }

    /**
     * Comprueba que getAll incluya reseñas registradas realmente.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("GetAll obtiene las reseñas")
    void getAll() {

        int idUsuario =
                crearUsuarioPrueba(
                        "Usuario Lista"
                );

        int idPublicacion =
                crearPublicacionLfPrueba(
                        "Libro Lista"
                );

        String comentario =
                "Comentario lista "
                        + System.nanoTime();

        assertTrue(
                resenaDao.create(
                        crearResena(
                                idUsuario,
                                idPublicacion,
                                comentario,
                                4
                        )
                )
        );

        int idResena =
                obtenerIdResena(
                        idUsuario,
                        idPublicacion,
                        comentario
                );

        List<Resena> lista =
                resenaDao.getAll();

        assertNotNull(lista);

        assertTrue(
                lista.stream()
                        .anyMatch(
                                r ->
                                        r.getIdResena()
                                                == idResena
                        )
        );
    }

    /**
     * Comprueba la búsqueda de una reseña mediante su ID.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("GetById obtiene una reseña existente")
    void getById() {

        int idUsuario =
                crearUsuarioPrueba(
                        "Usuario ById"
                );

        int idPublicacion =
                crearPublicacionLfPrueba(
                        "Libro ById"
                );

        String comentario =
                "Comentario ById "
                        + System.nanoTime();

        assertTrue(
                resenaDao.create(
                        crearResena(
                                idUsuario,
                                idPublicacion,
                                comentario,
                                3
                        )
                )
        );

        int idResena =
                obtenerIdResena(
                        idUsuario,
                        idPublicacion,
                        comentario
                );

        Resena resena =
                resenaDao.getById(
                        idResena
                );

        assertNotNull(resena);
        assertEquals(
                idResena,
                resena.getIdResena()
        );
        assertEquals(
                comentario,
                resena.getComentario()
        );
        assertNotNull(
                resena.getNombreUsuario()
        );
    }

    /**
     * Comprueba que getById devuelva null para un ID inexistente.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("GetById devuelve null para un registro inexistente")
    void getByIdInexistente() {

        assertNull(
                resenaDao.getById(-999)
        );
    }

    /**
     * Comprueba la actualización del comentario y calificación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("Update modifica una reseña")
    void update() {

        int idUsuario =
                crearUsuarioPrueba(
                        "Usuario Update"
                );

        int idPublicacion =
                crearPublicacionLfPrueba(
                        "Libro Update"
                );

        String comentarioOriginal =
                "Comentario original "
                        + System.nanoTime();

        assertTrue(
                resenaDao.create(
                        crearResena(
                                idUsuario,
                                idPublicacion,
                                comentarioOriginal,
                                3
                        )
                )
        );

        int idResena =
                obtenerIdResena(
                        idUsuario,
                        idPublicacion,
                        comentarioOriginal
                );

        Resena resena =
                resenaDao.getById(
                        idResena
                );

        assertNotNull(resena);

        String comentarioNuevo =
                "Comentario actualizado "
                        + System.nanoTime();

        resena.setComentario(
                comentarioNuevo
        );
        resena.setCalificacion(
                5
        );

        assertTrue(
                resenaDao.update(resena)
        );

        Resena actualizada =
                resenaDao.getById(
                        idResena
                );

        assertNotNull(actualizada);
        assertEquals(
                comentarioNuevo,
                actualizada.getComentario()
        );
        assertEquals(
                5,
                actualizada.getCalificacion()
        );
    }

    /**
     * Comprueba que update devuelva false si la reseña no existe.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("Update devuelve false para un ID inexistente")
    void updateInexistente() {

        Resena resena =
                new Resena();

        resena.setIdResena(-999);
        resena.setComentario(
                "No existe"
        );
        resena.setCalificacion(
                1
        );

        assertFalse(
                resenaDao.update(resena)
        );
    }

    /**
     * Comprueba que una reseña pueda eliminarse.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("Delete elimina una reseña existente")
    void delete() {

        int idUsuario =
                crearUsuarioPrueba(
                        "Usuario Delete"
                );

        int idPublicacion =
                crearPublicacionLfPrueba(
                        "Libro Delete"
                );

        String comentario =
                "Comentario delete "
                        + System.nanoTime();

        assertTrue(
                resenaDao.create(
                        crearResena(
                                idUsuario,
                                idPublicacion,
                                comentario,
                                4
                        )
                )
        );

        int idResena =
                obtenerIdResena(
                        idUsuario,
                        idPublicacion,
                        comentario
                );

        assertTrue(
                resenaDao.delete(idResena)
        );

        assertNull(
                resenaDao.getById(idResena)
        );
    }

    /**
     * Comprueba que delete devuelva false para un ID inexistente.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("Delete devuelve false para un ID inexistente")
    void deleteInexistente() {

        assertFalse(
                resenaDao.delete(-999)
        );
    }

    /**
     * Comprueba que se recuperen únicamente las reseñas de la publicación indicada.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("GetResenasByPublicacion obtiene reseñas de una publicación")
    void getResenasByPublicacion() {

        int idUsuario =
                crearUsuarioPrueba(
                        "Usuario Publicacion"
                );

        int idPublicacion =
                crearPublicacionLfPrueba(
                        "Libro Reseñas"
                );

        String comentario =
                "Reseña publicación "
                        + System.nanoTime();

        assertTrue(
                resenaDao.create(
                        crearResena(
                                idUsuario,
                                idPublicacion,
                                comentario,
                                5
                        )
                )
        );

        int idResena =
                obtenerIdResena(
                        idUsuario,
                        idPublicacion,
                        comentario
                );

        List<Resena> lista =
                resenaDao.getResenasByPublicacion(
                        idPublicacion
                );

        assertNotNull(lista);

        assertTrue(
                lista.stream()
                        .anyMatch(
                                r ->
                                        r.getIdResena()
                                                == idResena
                        )
        );

        Resena encontrada =
                lista.stream()
                        .filter(
                                r ->
                                        r.getIdResena()
                                                == idResena
                        )
                        .findFirst()
                        .orElseThrow();

        assertEquals(
                idPublicacion,
                encontrada.getIdPublicacionLf()
        );

        assertNotNull(
                encontrada.getNombreUsuario()
        );
    }

    /**
     * Comprueba que un usuario con una compra registrada pueda ser identificado
     * por usuarioHaCompradoORentado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("UsuarioHaCompradoORentado detecta una compra")
    void usuarioHaCompradoORentadoCompra() {

        int idUsuario =
                crearUsuarioPrueba(
                        "Usuario Compra"
                );

        int idPublicacion =
                crearPublicacionLfPrueba(
                        "Libro Compra"
                );

        crearOperacionPrueba(
                idUsuario,
                idPublicacion,
                "COMPRA"
        );

        assertTrue(
                resenaDao.usuarioHaCompradoORentado(
                        idUsuario,
                        idPublicacion
                )
        );
    }

    /**
     * Comprueba que una renta también sea reconocida como operación válida.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("UsuarioHaCompradoORentado detecta una renta")
    void usuarioHaCompradoORentadoRenta() {

        int idUsuario =
                crearUsuarioPrueba(
                        "Usuario Renta"
                );

        int idPublicacion =
                crearPublicacionLfPrueba(
                        "Libro Renta"
                );

        crearOperacionPrueba(
                idUsuario,
                idPublicacion,
                "RENTA"
        );

        assertTrue(
                resenaDao.usuarioHaCompradoORentado(
                        idUsuario,
                        idPublicacion
                )
        );
    }

    /**
     * Comprueba que se devuelva false cuando el usuario no tiene operaciones
     * relacionadas con la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("UsuarioHaCompradoORentado devuelve false sin operación")
    void usuarioHaCompradoORentadoSinOperacion() {

        int idUsuario =
                crearUsuarioPrueba(
                        "Usuario Sin Operacion"
                );

        int idPublicacion =
                crearPublicacionLfPrueba(
                        "Libro Sin Operacion"
                );

        assertFalse(
                resenaDao.usuarioHaCompradoORentado(
                        idUsuario,
                        idPublicacion
                )
        );
    }

    /**
     * Construye una reseña para las pruebas.
     *
     * @param idUsuario Identificador del usuario.
     * @param idPublicacion Identificador de la publicación.
     * @param comentario Comentario de la reseña.
     * @param calificacion Calificación asignada.
     * @return Entidad preparada para insertarse.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private Resena crearResena(
            int idUsuario,
            int idPublicacion,
            String comentario,
            int calificacion) {

        Resena resena =
                new Resena();

        resena.setIdUsuario(
                idUsuario
        );

        resena.setIdPublicacionLf(
                idPublicacion
        );

        resena.setComentario(
                comentario
        );

        resena.setCalificacion(
                calificacion
        );

        return resena;
    }

    /**
     * Crea un usuario válido dentro de la base temporal.
     *
     * @param nombre Nombre descriptivo.
     * @return Identificador generado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearUsuarioPrueba(
            String nombre) {

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

            ps.setString(
                    1,
                    nombre
            );

            ps.setString(
                    2,
                    "ApellidoP"
            );

            ps.setString(
                    3,
                    "ApellidoM"
            );

            ps.setString(
                    4,
                    "resena_"
                            + System.nanoTime()
                            + "@test.com"
            );

            ps.setString(
                    5,
                    "7771234567"
            );

            ps.setString(
                    6,
                    "ACTIVA"
            );

            assertEquals(
                    1,
                    ps.executeUpdate()
            );

            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo crear el usuario de prueba. ERROR SQL: "
                            + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Crea un libro y su publicación oficial de LibriFlow.
     *
     * @param titulo Título descriptivo del libro.
     * @return Identificador generado de PUBLICACION_LF.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearPublicacionLfPrueba(
            String titulo) {

        int idLibro =
                crearLibroPrueba(
                        titulo
                );

        String sql = """
                INSERT INTO publicacion_lf(
                    id_libro,
                    sinopsis,
                    cantidad,
                    es_venta,
                    es_renta,
                    precio,
                    estado
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_PUBLICACION_LF"}
             )) {

            ps.setInt(
                    1,
                    idLibro
            );

            ps.setString(
                    2,
                    "Publicación para ResenaDaoTest"
            );

            ps.setInt(
                    3,
                    10
            );

            ps.setInt(
                    4,
                    1
            );

            ps.setInt(
                    5,
                    1
            );

            ps.setDouble(
                    6,
                    250.00
            );

            ps.setString(
                    7,
                    "ACTIVO"
            );

            assertEquals(
                    1,
                    ps.executeUpdate()
            );

            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo crear la publicación de prueba. ERROR SQL: "
                            + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Crea un libro auxiliar para la publicación.
     *
     * @param titulo Título del libro.
     * @return Identificador generado.
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

            ps.setString(
                    1,
                    titulo
            );

            ps.setString(
                    2,
                    "Autor Resena Test"
            );

            ps.setString(
                    3,
                    "Editorial Resena Test"
            );

            ps.setString(
                    4,
                    "Tecnología"
            );

            assertEquals(
                    1,
                    ps.executeUpdate()
            );

            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo crear el libro de prueba. ERROR SQL: "
                            + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Registra una transacción y su detalle para simular una compra o renta
     * realizada por el usuario.
     *
     * @param idUsuario Comprador.
     * @param idPublicacion Publicación oficial involucrada.
     * @param tipoOperacion COMPRA o RENTA.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private void crearOperacionPrueba(
            int idUsuario,
            int idPublicacion,
            String tipoOperacion) {

        int idTransaccion =
                crearTransaccionPrueba(
                        idUsuario
                );

        String sql = """
                INSERT INTO detalle_transaccion(
                    id_transaccion,
                    id_publicacion_lf,
                    tipo_operacion,
                    precio,
                    ganancia_libriflow,
                    ganancia_vendedor
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idTransaccion
            );

            ps.setInt(
                    2,
                    idPublicacion
            );

            ps.setString(
                    3,
                    tipoOperacion
            );

            ps.setDouble(
                    4,
                    250.00
            );

            ps.setDouble(
                    5,
                    250.00
            );

            ps.setDouble(
                    6,
                    0.00
            );

            assertEquals(
                    1,
                    ps.executeUpdate()
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo crear la operación de prueba. ERROR SQL: "
                            + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Crea una transacción principal para el usuario.
     *
     * @param idUsuario Identificador del comprador.
     * @return Identificador generado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearTransaccionPrueba(
            int idUsuario) {

        String sql = """
                INSERT INTO transaccion(
                    id_comprador,
                    fecha,
                    subtotal,
                    costo_envio,
                    total,
                    estado
                )
                VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_TRANSACCION"}
             )) {

            ps.setInt(
                    1,
                    idUsuario
            );

            ps.setDouble(
                    2,
                    250.00
            );

            ps.setDouble(
                    3,
                    0.00
            );

            ps.setDouble(
                    4,
                    250.00
            );

            ps.setString(
                    5,
                    "COMPLETADA"
            );

            assertEquals(
                    1,
                    ps.executeUpdate()
            );

            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo crear la transacción de prueba. ERROR SQL: "
                            + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Localiza la reseña recién insertada utilizando sus datos de prueba.
     *
     * @param idUsuario Usuario de la reseña.
     * @param idPublicacion Publicación de la reseña.
     * @param comentario Comentario único utilizado en la prueba.
     * @return Identificador de la reseña.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int obtenerIdResena(
            int idUsuario,
            int idPublicacion,
            String comentario) {

        String sql = """
                SELECT id_resena
                FROM resena
                WHERE id_usuario = ?
                  AND id_publicacion_lf = ?
                  AND comentario = ?
                ORDER BY id_resena DESC
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idUsuario
            );

            ps.setInt(
                    2,
                    idPublicacion
            );

            ps.setString(
                    3,
                    comentario
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                assertTrue(
                        rs.next(),
                        "Debe encontrarse la reseña creada"
                );

                return rs.getInt(
                        "ID_RESENA"
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo consultar la reseña creada. ERROR SQL: "
                            + e.getMessage(),
                    e
            );
        }
    }
}