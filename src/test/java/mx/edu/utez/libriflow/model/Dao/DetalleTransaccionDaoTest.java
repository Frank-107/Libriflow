package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.DetalleTransaccion;
import mx.edu.utez.libriflow.model.Movimiento;
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
 * Pruebas de integración para {@link DetalleTransaccionDao} utilizando
 * una instancia temporal de Oracle ejecutada mediante Docker y Testcontainers.
 *
 * Cada prueba genera los usuarios, libros, publicaciones y transacciones
 * que necesita para evitar IDs quemados y dependencias entre pruebas.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
class DetalleTransaccionDaoTest extends OracleTestBase {

    private DetalleTransaccionDao dao;

    /**
     * Inicializa el DAO antes de ejecutar cada prueba.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @BeforeEach
    void setUp() {
        dao = new DetalleTransaccionDao();
    }

    /**
     * Comprueba la creación de un detalle correspondiente a la compra
     * de una publicación de usuario y verifica que la publicación cambie
     * automáticamente de ACTIVO a VENDIDO.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void createCompraUsuarioCambiaEstadoAVendido() {

        int idVendedor = crearUsuarioPrueba("Vendedor");
        int idComprador = crearUsuarioPrueba("Comprador");
        int idLibro = crearLibroPrueba("Libro Compra Usuario");

        int idPublicacionUs = crearPublicacionUsuarioPrueba(
                idVendedor,
                idLibro,
                "ACTIVO"
        );

        int idTransaccion = crearTransaccionPrueba(
                idComprador,
                200.00
        );

        DetalleTransaccion detalle = crearDetalleUsuario(
                idTransaccion,
                idPublicacionUs,
                idVendedor,
                "COMPRA",
                200.00,
                20.00,
                180.00
        );

        int idDetalle = dao.create(detalle);

        assertTrue(
                idDetalle > 0,
                "La compra debe crear un detalle válido"
        );

        assertEquals(
                "VENDIDO",
                obtenerEstadoPublicacionUsuario(idPublicacionUs)
        );

        DetalleTransaccion guardado = dao.getById(idDetalle);

        assertNotNull(guardado);
        assertEquals(idTransaccion, guardado.getIdTransaccion());
        assertEquals(idPublicacionUs, guardado.getIdPublicacionUs());
        assertEquals(idVendedor, guardado.getIdVendedor());
        assertEquals("COMPRA", guardado.getTipoOperacion());
        assertEquals(200.00, guardado.getPrecio(), 0.001);
        assertEquals(20.00, guardado.getGananciaLibriFlow(), 0.001);
        assertEquals(180.00, guardado.getGananciaVendedor(), 0.001);
    }

    /**
     * Comprueba que una publicación de usuario que no se encuentra ACTIVA
     * no pueda volver a comprarse.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void createCompraUsuarioNoDisponible() {

        int idVendedor = crearUsuarioPrueba("Vendedor No Disponible");
        int idComprador = crearUsuarioPrueba("Comprador No Disponible");
        int idLibro = crearLibroPrueba("Libro No Disponible");

        int idPublicacionUs = crearPublicacionUsuarioPrueba(
                idVendedor,
                idLibro,
                "VENDIDO"
        );

        int idTransaccion = crearTransaccionPrueba(
                idComprador,
                180.00
        );

        DetalleTransaccion detalle = crearDetalleUsuario(
                idTransaccion,
                idPublicacionUs,
                idVendedor,
                "COMPRA",
                180.00,
                18.00,
                162.00
        );

        int resultado = dao.create(detalle);

        assertEquals(-1, resultado);
        assertEquals(
                0,
                contarDetallesPorTransaccion(idTransaccion)
        );
    }

    /**
     * Comprueba la creación de un detalle relacionado con una publicación
     * oficial de LibriFlow.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void createPublicacionLibriFlow() {

        int idComprador = crearUsuarioPrueba("Comprador LF");
        int idLibro = crearLibroPrueba("Libro Detalle LibriFlow");
        int idPublicacionLf = crearPublicacionLfPrueba(idLibro);

        int idTransaccion = crearTransaccionPrueba(
                idComprador,
                250.00
        );

        DetalleTransaccion detalle = crearDetalleLf(
                idTransaccion,
                idPublicacionLf,
                "COMPRA",
                250.00
        );

        int idDetalle = dao.create(detalle);

        assertTrue(idDetalle > 0);

        DetalleTransaccion guardado = dao.getById(idDetalle);

        assertNotNull(guardado);
        assertNull(guardado.getIdPublicacionUs());
        assertEquals(idPublicacionLf, guardado.getIdPublicacionLf());
        assertNull(guardado.getIdVendedor());
        assertEquals(250.00, guardado.getGananciaLibriFlow(), 0.001);
        assertEquals(0.00, guardado.getGananciaVendedor(), 0.001);
    }

    /**
     * Comprueba que puedan obtenerse todos los detalles registrados.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getAll() {

        int idComprador = crearUsuarioPrueba("Comprador Lista");

        int idLibro1 = crearLibroPrueba("Libro Detalle Lista 1");
        int idLibro2 = crearLibroPrueba("Libro Detalle Lista 2");

        int idPublicacion1 = crearPublicacionLfPrueba(idLibro1);
        int idPublicacion2 = crearPublicacionLfPrueba(idLibro2);

        int idTransaccion = crearTransaccionPrueba(
                idComprador,
                300.00
        );

        int id1 = dao.create(
                crearDetalleLf(
                        idTransaccion,
                        idPublicacion1,
                        "COMPRA",
                        140.00
                )
        );

        int id2 = dao.create(
                crearDetalleLf(
                        idTransaccion,
                        idPublicacion2,
                        "RENTA",
                        160.00
                )
        );

        assertTrue(id1 > 0);
        assertTrue(id2 > 0);

        List<DetalleTransaccion> detalles = dao.getAll();

        assertNotNull(detalles);

        assertTrue(
                detalles.stream()
                        .anyMatch(d -> d.getIdDetalle() == id1)
        );

        assertTrue(
                detalles.stream()
                        .anyMatch(d -> d.getIdDetalle() == id2)
        );
    }

    /**
     * Comprueba la consulta de un detalle mediante su identificador.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getById() {

        int idComprador = crearUsuarioPrueba("Comprador GetById");
        int idLibro = crearLibroPrueba("Libro Detalle GetById");
        int idPublicacion = crearPublicacionLfPrueba(idLibro);

        int idTransaccion = crearTransaccionPrueba(
                idComprador,
                130.00
        );

        int idDetalle = dao.create(
                crearDetalleLf(
                        idTransaccion,
                        idPublicacion,
                        "RENTA",
                        130.00
                )
        );

        DetalleTransaccion resultado = dao.getById(idDetalle);

        assertNotNull(resultado);
        assertEquals(idDetalle, resultado.getIdDetalle());
        assertEquals(idTransaccion, resultado.getIdTransaccion());
        assertEquals(idPublicacion, resultado.getIdPublicacionLf());
        assertEquals("RENTA", resultado.getTipoOperacion());
    }

    /**
     * Comprueba que un ID inexistente devuelva null.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getByIdInexistente() {

        assertNull(
                dao.getById(-999)
        );
    }

    /**
     * Comprueba la actualización CRUD de un detalle de transacción.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void update() {

        int idComprador = crearUsuarioPrueba("Comprador Update");
        int idLibro = crearLibroPrueba("Libro Detalle Update");
        int idPublicacion = crearPublicacionLfPrueba(idLibro);

        int idTransaccion = crearTransaccionPrueba(
                idComprador,
                300.00
        );

        int idDetalle = dao.create(
                crearDetalleLf(
                        idTransaccion,
                        idPublicacion,
                        "COMPRA",
                        300.00
                )
        );

        DetalleTransaccion detalle = dao.getById(idDetalle);

        assertNotNull(detalle);

        detalle.setTipoOperacion("RENTA");
        detalle.setPrecio(220.00);
        detalle.setGananciaLibriFlow(220.00);
        detalle.setGananciaVendedor(0.00);

        assertTrue(
                dao.update(detalle)
        );

        DetalleTransaccion resultado = dao.getById(idDetalle);

        assertNotNull(resultado);
        assertEquals("RENTA", resultado.getTipoOperacion());
        assertEquals(220.00, resultado.getPrecio(), 0.001);
        assertEquals(220.00, resultado.getGananciaLibriFlow(), 0.001);
        assertEquals(0.00, resultado.getGananciaVendedor(), 0.001);
    }

    /**
     * Comprueba que actualizar un detalle inexistente devuelva false.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void updateInexistente() {

        DetalleTransaccion detalle = new DetalleTransaccion();

        detalle.setIdDetalle(-999);
        detalle.setIdTransaccion(1);
        detalle.setTipoOperacion("COMPRA");
        detalle.setPrecio(100.00);
        detalle.setGananciaLibriFlow(10.00);
        detalle.setGananciaVendedor(90.00);

        assertFalse(
                dao.update(detalle)
        );
    }

    /**
     * Comprueba la eliminación de un detalle mediante su identificador.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void delete() {

        int idComprador = crearUsuarioPrueba("Comprador Delete");
        int idLibro = crearLibroPrueba("Libro Detalle Delete");
        int idPublicacion = crearPublicacionLfPrueba(idLibro);

        int idTransaccion = crearTransaccionPrueba(
                idComprador,
                170.00
        );

        int idDetalle = dao.create(
                crearDetalleLf(
                        idTransaccion,
                        idPublicacion,
                        "COMPRA",
                        170.00
                )
        );

        assertTrue(idDetalle > 0);
        assertTrue(dao.delete(idDetalle));
        assertNull(dao.getById(idDetalle));
    }

    /**
     * Comprueba que eliminar un detalle inexistente devuelva false.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void deleteInexistente() {

        assertFalse(
                dao.delete(-999)
        );
    }

    /**
     * Comprueba que la consulta de movimientos de un usuario encuentre
     * una compra real realizada sobre una publicación de usuario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getMovimientosByIdUsuario() {

        int idVendedor = crearUsuarioPrueba("Vendedor Movimiento");
        int idComprador = crearUsuarioPrueba("Comprador Movimiento");
        int idLibro = crearLibroPrueba("Libro Movimiento Usuario");

        int idPublicacion = crearPublicacionUsuarioPrueba(
                idVendedor,
                idLibro,
                "ACTIVO"
        );

        int idTransaccion = crearTransaccionPrueba(
                idComprador,
                210.00
        );

        int idDetalle = dao.create(
                crearDetalleUsuario(
                        idTransaccion,
                        idPublicacion,
                        idVendedor,
                        "COMPRA",
                        210.00,
                        21.00,
                        189.00
                )
        );

        assertTrue(idDetalle > 0);

        List<Movimiento> movimientosComprador =
                dao.getMovimientosByIdUsuario(idComprador);

        List<Movimiento> movimientosVendedor =
                dao.getMovimientosByIdUsuario(idVendedor);

        assertNotNull(movimientosComprador);
        assertNotNull(movimientosVendedor);
        assertFalse(movimientosComprador.isEmpty());
        assertFalse(movimientosVendedor.isEmpty());
    }

    /**
     * Comprueba la consulta de movimientos relacionados con una publicación
     * oficial de LibriFlow.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getMovimientosByIdUsuarioLibriFlow() {

        int idComprador = crearUsuarioPrueba("Comprador Movimiento LF");
        int idLibro = crearLibroPrueba("Libro Movimiento LF");
        int idPublicacion = crearPublicacionLfPrueba(idLibro);

        int idTransaccion = crearTransaccionPrueba(
                idComprador,
                190.00
        );

        int idDetalle = dao.create(
                crearDetalleLf(
                        idTransaccion,
                        idPublicacion,
                        "RENTA",
                        190.00
                )
        );

        assertTrue(idDetalle > 0);

        List<Movimiento> movimientos =
                dao.getMovimientosByIdUsuario(idComprador);

        assertNotNull(movimientos);
        assertFalse(movimientos.isEmpty());
    }

    /**
     * Comprueba que la consulta de ingresos globales encuentre movimientos
     * reales tanto de publicaciones de usuario como de LibriFlow.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getAllMovimientosIngresos() {

        int idVendedor = crearUsuarioPrueba("Vendedor Ingresos");
        int idComprador = crearUsuarioPrueba("Comprador Ingresos");

        int idLibroUs = crearLibroPrueba("Libro Ingreso Usuario");
        int idLibroLf = crearLibroPrueba("Libro Ingreso LibriFlow");

        int idPublicacionUs = crearPublicacionUsuarioPrueba(
                idVendedor,
                idLibroUs,
                "ACTIVO"
        );

        int idPublicacionLf =
                crearPublicacionLfPrueba(idLibroLf);

        int idTransaccion = crearTransaccionPrueba(
                idComprador,
                400.00
        );

        int idDetalleUs = dao.create(
                crearDetalleUsuario(
                        idTransaccion,
                        idPublicacionUs,
                        idVendedor,
                        "COMPRA",
                        180.00,
                        18.00,
                        162.00
                )
        );

        int idDetalleLf = dao.create(
                crearDetalleLf(
                        idTransaccion,
                        idPublicacionLf,
                        "COMPRA",
                        220.00
                )
        );

        assertTrue(idDetalleUs > 0);
        assertTrue(idDetalleLf > 0);

        List<Movimiento> ingresos =
                dao.getAllMovimientosIngresos();

        assertNotNull(ingresos);
        assertTrue(
                ingresos.size() >= 2,
                "Deben recuperarse los dos movimientos de ingreso creados"
        );
    }

    /**
     * Construye un detalle para una publicación perteneciente a un usuario.
     *
     * @param idTransaccion ID de la transacción principal.
     * @param idPublicacionUs ID de la publicación.
     * @param idVendedor ID del vendedor.
     * @param tipoOperacion Tipo de operación.
     * @param precio Precio del detalle.
     * @param gananciaLf Ganancia para LibriFlow.
     * @param gananciaVendedor Ganancia del vendedor.
     * @return Detalle preparado para almacenarse.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private DetalleTransaccion crearDetalleUsuario(
            int idTransaccion,
            int idPublicacionUs,
            int idVendedor,
            String tipoOperacion,
            double precio,
            double gananciaLf,
            double gananciaVendedor) {

        DetalleTransaccion detalle = new DetalleTransaccion();

        detalle.setIdTransaccion(idTransaccion);
        detalle.setIdPublicacionUs(idPublicacionUs);
        detalle.setIdPublicacionLf(null);
        detalle.setIdVendedor(idVendedor);
        detalle.setTipoOperacion(tipoOperacion);
        detalle.setPrecio(precio);
        detalle.setGananciaLibriFlow(gananciaLf);
        detalle.setGananciaVendedor(gananciaVendedor);

        return detalle;
    }

    /**
     * Construye un detalle correspondiente a una publicación oficial.
     *
     * @param idTransaccion ID de la transacción principal.
     * @param idPublicacionLf ID de la publicación de LibriFlow.
     * @param tipoOperacion Tipo de operación.
     * @param precio Precio del detalle.
     * @return Detalle preparado para almacenarse.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private DetalleTransaccion crearDetalleLf(
            int idTransaccion,
            int idPublicacionLf,
            String tipoOperacion,
            double precio) {

        DetalleTransaccion detalle = new DetalleTransaccion();

        detalle.setIdTransaccion(idTransaccion);
        detalle.setIdPublicacionUs(null);
        detalle.setIdPublicacionLf(idPublicacionLf);
        detalle.setIdVendedor(null);
        detalle.setTipoOperacion(tipoOperacion);
        detalle.setPrecio(precio);
        detalle.setGananciaLibriFlow(precio);
        detalle.setGananciaVendedor(0.00);

        return detalle;
    }

    /**
     * Crea un usuario dentro de la base temporal.
     *
     * @param nombre Nombre utilizado para identificar el registro de prueba.
     * @return Identificador generado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearUsuarioPrueba(String nombre) {

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

            ps.setString(1, nombre);
            ps.setString(2, "ApellidoP");
            ps.setString(3, "ApellidoM");
            ps.setString(
                    4,
                    "detalle_" + System.nanoTime() + "@test.com"
            );
            ps.setString(5, "7771234567");
            ps.setString(6, "ACTIVA");

            assertEquals(1, ps.executeUpdate());

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
     * Crea un libro dentro de la base temporal.
     *
     * @param titulo Título del libro.
     * @return Identificador generado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearLibroPrueba(String titulo) {

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
            ps.setString(2, "Autor Detalle Test");
            ps.setString(3, "Editorial Detalle Test");
            ps.setString(4, "Tecnología");

            assertEquals(1, ps.executeUpdate());

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
     * Crea una publicación perteneciente a un usuario.
     *
     * @param idUsuario ID del propietario.
     * @param idLibro ID del libro.
     * @param estado Estado inicial.
     * @return Identificador generado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearPublicacionUsuarioPrueba(
            int idUsuario,
            int idLibro,
            String estado) {

        String sql = """
                INSERT INTO publicacion_us(
                    id_usuario,
                    id_libro,
                    sinopsis,
                    estado,
                    precio
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
            ps.setString(3, "Publicación para DetalleTransaccionDaoTest");
            ps.setString(4, estado);
            ps.setDouble(5, 200.00);

            assertEquals(1, ps.executeUpdate());

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
     * Crea una publicación oficial de LibriFlow.
     *
     * @param idLibro ID del libro relacionado.
     * @return Identificador generado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearPublicacionLfPrueba(int idLibro) {

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

            ps.setInt(1, idLibro);
            ps.setString(2, "Publicación LF para DetalleTransaccionDaoTest");
            ps.setInt(3, 5);
            ps.setInt(4, 1);
            ps.setInt(5, 1);
            ps.setDouble(6, 250.00);
            ps.setString(7, "ACTIVO");

            assertEquals(1, ps.executeUpdate());

            try (ResultSet rs = ps.getGeneratedKeys()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudo crear la publicación de LibriFlow",
                    e
            );
        }
    }

    /**
     * Crea una transacción principal con fecha válida para las consultas
     * de movimientos.
     *
     * @param idComprador ID del usuario comprador.
     * @param total Total de la transacción.
     * @return Identificador generado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearTransaccionPrueba(
            int idComprador,
            double total) {

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

            ps.setInt(1, idComprador);
            ps.setDouble(2, total);
            ps.setDouble(3, 0.00);
            ps.setDouble(4, total);
            ps.setString(5, "COMPLETADA");

            assertEquals(1, ps.executeUpdate());

            try (ResultSet rs = ps.getGeneratedKeys()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudo crear la transacción de prueba",
                    e
            );
        }
    }

    /**
     * Obtiene el estado actual de una publicación de usuario.
     *
     * @param idPublicacion ID de la publicación.
     * @return Estado almacenado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private String obtenerEstadoPublicacionUsuario(
            int idPublicacion) {

        String sql = """
                SELECT estado
                FROM publicacion_us
                WHERE id_publicacion_us = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPublicacion);

            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                return rs.getString("estado");
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudo consultar el estado de la publicación",
                    e
            );
        }
    }

    /**
     * Cuenta los detalles asociados con una transacción.
     *
     * @param idTransaccion ID de la transacción.
     * @return Cantidad de detalles registrados.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int contarDetallesPorTransaccion(
            int idTransaccion) {

        String sql = """
                SELECT COUNT(*)
                FROM detalle_transaccion
                WHERE id_transaccion = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTransaccion);

            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudieron contar los detalles",
                    e
            );
        }
    }
}