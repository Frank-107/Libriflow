package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.PublicacionResumen;
import mx.edu.utez.libriflow.model.PublicacionUsuario;
import mx.edu.utez.libriflow.model.PublicacionUsuarioCompleta;
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
 * Pruebas de integración para {@link PublicacionUsuarioDao} utilizando
 * una instancia temporal de Oracle ejecutada mediante Docker y Testcontainers.
 *
 * Cada prueba crea los datos que necesita para evitar dependencias con registros
 * preexistentes o con otras pruebas.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
class PublicacionUsuarioDaoTest extends OracleTestBase {

    private PublicacionUsuarioDao dao;

    /**
     * Inicializa el DAO antes de ejecutar cada prueba.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @BeforeEach
    void setUp() {
        dao = new PublicacionUsuarioDao();
    }

    /**
     * Comprueba que una publicación pueda registrarse correctamente.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void create() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Create");

        PublicacionUsuario publicacion = new PublicacionUsuario();
        publicacion.setIdUsuario(idUsuario);
        publicacion.setIdLibro(idLibro);
        publicacion.setSinopsis("Sinopsis creada desde JUnit.");
        publicacion.setPrecio(250.00);

        int idPublicacion = dao.create(publicacion);

        assertTrue(idPublicacion > 0, "create debe devolver un ID válido");

        PublicacionUsuario guardada = dao.getById(idPublicacion);
        assertNotNull(guardada);
        assertEquals(idUsuario, guardada.getIdUsuario());
        assertEquals(idLibro, guardada.getIdLibro());
        assertEquals("PENDIENTE", guardada.getEstado());
        assertEquals(250.00, guardada.getPrecio(), 0.001);
    }

    /**
     * Comprueba que se obtengan todas las publicaciones registradas.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getAll() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro1 = crearLibroPrueba("Libro Lista 1");
        int idLibro2 = crearLibroPrueba("Libro Lista 2");

        int id1 = crearPublicacionPrueba(idUsuario, idLibro1, "PENDIENTE", "Sinopsis 1", 100.00);
        int id2 = crearPublicacionPrueba(idUsuario, idLibro2, "ACTIVO", "Sinopsis 2", 200.00);

        List<PublicacionUsuario> publicaciones = dao.getAll();

        assertNotNull(publicaciones);
        assertEquals(2, publicaciones.size());
        assertTrue(publicaciones.stream().anyMatch(p -> p.getIdPublicacionUs() == id1));
        assertTrue(publicaciones.stream().anyMatch(p -> p.getIdPublicacionUs() == id2));
    }

    /**
     * Comprueba la consulta de una publicación por su identificador.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getById() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro GetById");
        int idPublicacion = crearPublicacionPrueba(
                idUsuario,
                idLibro,
                "PENDIENTE",
                "Sinopsis GetById",
                180.00
        );

        PublicacionUsuario resultado = dao.getById(idPublicacion);

        assertNotNull(resultado);
        assertEquals(idPublicacion, resultado.getIdPublicacionUs());
        assertEquals(idUsuario, resultado.getIdUsuario());
        assertEquals(idLibro, resultado.getIdLibro());
        assertEquals("Sinopsis GetById", resultado.getSinopsis());
        assertEquals("PENDIENTE", resultado.getEstado());
    }

    /**
     * Comprueba que un identificador inexistente devuelva {@code null}.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getByIdInexistente() {
        assertNull(dao.getById(-999));
    }

    /**
     * Comprueba la actualización de los datos principales de una publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void update() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Update");
        int idPublicacion = crearPublicacionPrueba(
                idUsuario,
                idLibro,
                "PENDIENTE",
                "Sinopsis original",
                120.00
        );

        PublicacionUsuario publicacion = new PublicacionUsuario();
        publicacion.setIdPublicacionUs(idPublicacion);
        publicacion.setIdUsuario(idUsuario);
        publicacion.setIdLibro(idLibro);
        publicacion.setSinopsis("Sinopsis actualizada");
        publicacion.setPrecio(350.00);
        publicacion.setEstado("ACTIVO");

        boolean actualizado = dao.update(publicacion);

        assertTrue(actualizado);

        PublicacionUsuario resultado = dao.getById(idPublicacion);
        assertNotNull(resultado);
        assertEquals("Sinopsis actualizada", resultado.getSinopsis());
        assertEquals(350.00, resultado.getPrecio(), 0.001);
        assertEquals("ACTIVO", resultado.getEstado());
    }

    /**
     * Comprueba la eliminación CRUD de una publicación y de sus imágenes asociadas.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void delete() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Delete");
        int idPublicacion = crearPublicacionPrueba(
                idUsuario,
                idLibro,
                "PENDIENTE",
                "Publicación a eliminar",
                90.00
        );
        crearImagenPrueba(idPublicacion, 1, "delete.jpg");

        boolean eliminado = dao.delete(idPublicacion);

        assertTrue(eliminado);
        assertNull(dao.getById(idPublicacion));
        assertEquals(0, contarRegistros("imagen", "id_publicacion_us", idPublicacion));
    }

    /**
     * Comprueba la consulta completa de una publicación junto con sus imágenes.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getPublicacionUsuarioCompleta() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Completo");
        int idPublicacion = crearPublicacionPrueba(
                idUsuario,
                idLibro,
                "ACTIVO",
                "Sinopsis completa",
                300.00
        );

        crearImagenPrueba(idPublicacion, 1, "principal.jpg");
        crearImagenPrueba(idPublicacion, 2, "reverso.jpg");
        crearImagenPrueba(idPublicacion, 3, "interior.jpg");

        PublicacionUsuarioCompleta resultado = dao.getPublicacionUsuarioCompleta(idPublicacion);

        assertNotNull(resultado);
        assertEquals(idPublicacion, resultado.getIdPublicacion());
        assertEquals(idUsuario, resultado.getIdPropietario());
        assertEquals(idLibro, resultado.getIdLibro());
        assertEquals("Libro Completo", resultado.getTitulo());
        assertEquals("principal.jpg", resultado.getImagenPrincipal());
        assertEquals("reverso.jpg", resultado.getImagenReverso());
        assertEquals("interior.jpg", resultado.getImagenInterior());
    }

    /**
     * Comprueba la consulta de resúmenes de publicaciones por usuario y orden.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getResumenPublicacionesPorUsuarioConOrden() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Resumen Orden");
        int idPublicacion = crearPublicacionPrueba(
                idUsuario,
                idLibro,
                "ACTIVO",
                "Resumen",
                140.00
        );
        crearImagenPrueba(idPublicacion, 1, "resumen.jpg");

        List<PublicacionResumen> lista =
                dao.getResumenPublicacionesPorUsuario(idUsuario, "antiguas");

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(idPublicacion, lista.getFirst().getIdPublicacion());
    }

    /**
     * Comprueba la consulta sobrecargada de resúmenes por usuario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getResumenPublicacionesPorUsuario() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Resumen");
        int idPublicacion = crearPublicacionPrueba(
                idUsuario,
                idLibro,
                "ACTIVO",
                "Resumen simple",
                160.00
        );
        crearImagenPrueba(idPublicacion, 1, "resumen-simple.jpg");

        List<PublicacionResumen> lista = dao.getResumenPublicacionesPorUsuario(idUsuario);

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(idPublicacion, lista.getFirst().getIdPublicacion());
    }

    /**
     * Comprueba la obtención de publicaciones activas a partir de una lista de IDs.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getPublicacionesByArreglo() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Arreglo");
        int idPublicacion = crearPublicacionPrueba(
                idUsuario,
                idLibro,
                "ACTIVO",
                "Publicación arreglo",
                220.00
        );
        crearImagenPrueba(idPublicacion, 1, "arreglo.jpg");

        List<PublicacionResumen> lista = dao.getPublicacionesByArreglo(List.of(idPublicacion));

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(idPublicacion, lista.getFirst().getIdPublicacion());
    }

    /**
     * Comprueba el conteo de publicaciones activas de un usuario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void contarPublicacionesPorUsuario() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro1 = crearLibroPrueba("Libro Conteo 1");
        int idLibro2 = crearLibroPrueba("Libro Conteo 2");

        crearPublicacionPrueba(idUsuario, idLibro1, "ACTIVO", "Activa", 100.00);
        crearPublicacionPrueba(idUsuario, idLibro2, "PENDIENTE", "Pendiente", 120.00);

        assertEquals(1, dao.contarPublicacionesPorUsuario(idUsuario));
    }

    /**
     * Comprueba la búsqueda y filtrado de publicaciones de usuarios.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void buscarYFiltrarPublicacionesUs() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Java Docker");
        int idPublicacion = crearPublicacionPrueba(
                idUsuario,
                idLibro,
                "ACTIVO",
                "Libro para filtro",
                280.00
        );
        crearImagenPrueba(idPublicacion, 1, "filtro.jpg");

        List<PublicacionResumen> lista =
                dao.buscarYFiltrarPublicacionesUs("ACTIVO", "Docker", "Tecnología");

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(idPublicacion, lista.getFirst().getIdPublicacion());
    }

    /**
     * Comprueba una transición válida de estado de PENDIENTE a ACTIVO.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void cambiarEstadoPublicacion() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Estado");
        int idPublicacion = crearPublicacionPrueba(
                idUsuario,
                idLibro,
                "PENDIENTE",
                "Cambio estado",
                130.00
        );

        assertTrue(dao.cambiarEstadoPublicacion(idPublicacion, "ACTIVO"));
        assertEquals("ACTIVO", dao.getById(idPublicacion).getEstado());
    }

    /**
     * Comprueba que un estado no permitido sea rechazado por el DAO.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void cambiarEstadoPublicacionInvalido() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Estado Inválido");
        int idPublicacion = crearPublicacionPrueba(
                idUsuario,
                idLibro,
                "PENDIENTE",
                "Estado inválido",
                110.00
        );

        assertFalse(dao.cambiarEstadoPublicacion(idPublicacion, "DESCONOCIDO"));
        assertEquals("PENDIENTE", dao.getById(idPublicacion).getEstado());
    }

    /**
     * Comprueba la baja lógica de una publicación mediante el estado RECHAZADO.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void darDeBajaPublicacionUsuario() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Baja");
        int idPublicacion = crearPublicacionPrueba(
                idUsuario,
                idLibro,
                "ACTIVO",
                "Publicación baja",
                170.00
        );

        assertTrue(dao.darDeBajaPublicacionUsuario(idPublicacion));
        assertEquals("RECHAZADO", dao.getById(idPublicacion).getEstado());
    }

    /**
     * Comprueba la actualización transaccional del libro y de su publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void actualizarPublicacionCompleta() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Antes Update Completo");
        int idPublicacion = crearPublicacionPrueba(
                idUsuario,
                idLibro,
                "PENDIENTE",
                "Sinopsis original",
                190.00
        );
        crearImagenPrueba(idPublicacion, 1, "update-completo.jpg");

        boolean resultado = dao.actualizarPublicacionCompleta(
                idPublicacion,
                idUsuario,
                "Título Editado Test",
                "Autor Editado Test",
                "Editorial Test",
                "Fantasía",
                "Sinopsis actualizada",
                299.00
        );

        assertTrue(resultado);

        PublicacionUsuarioCompleta actualizada = dao.getPublicacionUsuarioCompleta(idPublicacion);
        assertNotNull(actualizada);
        assertEquals("Título Editado Test", actualizada.getTitulo());
        assertEquals("Autor Editado Test", actualizada.getAutor());
        assertEquals("Sinopsis actualizada", actualizada.getSinopsis());
        assertEquals(299.00, actualizada.getPrecio(), 0.001);
    }

    /**
     * Comprueba la eliminación de una publicación realizada por su propietario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void eliminarPublicacionPropietario() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Eliminar Propietario");
        int idPublicacion = crearPublicacionPrueba(
                idUsuario,
                idLibro,
                "PENDIENTE",
                "Eliminar propietario",
                95.00
        );
        crearImagenPrueba(idPublicacion, 1, "propietario.jpg");

        boolean eliminado = dao.eliminarPublicacionPropietario(idPublicacion, idUsuario);

        assertTrue(eliminado);
        assertNull(dao.getById(idPublicacion));
        assertEquals(0, contarRegistros("libro", "id_libro", idLibro));
        assertEquals(0, contarRegistros("imagen", "id_publicacion_us", idPublicacion));
    }

    /**
     * Comprueba la eliminación física de una publicación PENDIENTE junto con
     * sus imágenes y libro asociado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void deletePublicacionById() {
        int idUsuario = crearUsuarioPrueba();
        int idLibro = crearLibroPrueba("Libro Delete Especial");
        int idPublicacion = crearPublicacionPrueba(
                idUsuario,
                idLibro,
                "PENDIENTE",
                "Eliminar especial",
                80.00
        );
        crearImagenPrueba(idPublicacion, 1, "delete-especial.jpg");

        boolean eliminado = dao.deletePublicacionById(idPublicacion);

        assertTrue(eliminado);
        assertNull(dao.getById(idPublicacion));
        assertEquals(0, contarRegistros("libro", "id_libro", idLibro));
        assertEquals(0, contarRegistros("imagen", "id_publicacion_us", idPublicacion));
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
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_USUARIO"})) {

            ps.setString(1, "Usuario Test");
            ps.setString(2, "ApellidoP");
            ps.setString(3, "ApellidoM");
            ps.setString(4, "pubus_" + System.nanoTime() + "@test.com");
            ps.setString(5, "7771234567");
            ps.setString(6, "ACTIVA");

            assertEquals(1, ps.executeUpdate());

            try (ResultSet rs = ps.getGeneratedKeys()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo crear el usuario de prueba", e);
        }
    }

    /**
     * Inserta un libro directamente en la base de datos de pruebas.
     *
     * @param titulo Título del libro de prueba.
     * @return Identificador generado del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearLibroPrueba(String titulo) {
        String sql = """
                INSERT INTO libro(titulo, autor, editorial, genero)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_LIBRO"})) {

            ps.setString(1, titulo);
            ps.setString(2, "Autor Test");
            ps.setString(3, "Editorial Test");
            ps.setString(4, "Tecnología");

            assertEquals(1, ps.executeUpdate());

            try (ResultSet rs = ps.getGeneratedKeys()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo crear el libro de prueba", e);
        }
    }

    /**
     * Inserta una publicación de usuario directamente en la base de datos de pruebas.
     *
     * @param idUsuario Identificador del usuario propietario.
     * @param idLibro Identificador del libro asociado.
     * @param estado Estado inicial de la publicación.
     * @param sinopsis Sinopsis de la publicación.
     * @param precio Precio de la publicación.
     * @return Identificador generado de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearPublicacionPrueba(
            int idUsuario,
            int idLibro,
            String estado,
            String sinopsis,
            double precio) {

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
            ps.setString(3, sinopsis);
            ps.setDouble(4, precio);
            ps.setString(5, estado);

            assertEquals(1, ps.executeUpdate());

            try (ResultSet rs = ps.getGeneratedKeys()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo crear la publicación de prueba", e);
        }
    }

    /**
     * Inserta una imagen relacionada con una publicación de usuario.
     *
     * @param idPublicacion Identificador de la publicación.
     * @param tipo Tipo de imagen: 1 principal, 2 reverso o 3 interior.
     * @param imagen Nombre o ruta utilizada como imagen de prueba.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private void crearImagenPrueba(
            int idPublicacion,
            int tipo,
            String imagen) {

        String sql = """
                INSERT INTO imagen(id_publicacion_us, imagen, tipo)
                VALUES (?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPublicacion);
            ps.setString(2, imagen);
            ps.setInt(3, tipo);

            assertEquals(1, ps.executeUpdate());

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo crear la imagen de prueba", e);
        }
    }

    /**
     * Cuenta registros de una tabla utilizando una columna y un valor entero.
     * Se utiliza únicamente para comprobar eliminaciones dentro de la base de pruebas.
     *
     * @param tabla Tabla que será consultada.
     * @param columna Columna utilizada en la condición.
     * @param valor Valor entero que será buscado.
     * @return Cantidad de registros encontrados.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int contarRegistros(
            String tabla,
            String columna,
            int valor) {

        String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE " + columna + " = ?";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, valor);

            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo realizar el conteo de prueba", e);
        }
    }
}