package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.RentaResumen;
import mx.edu.utez.libriflow.testconfig.OracleTestBase;
import mx.edu.utez.libriflow.utils.SQLconnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para {@link RentaDao} utilizando una instancia
 * temporal de Oracle ejecutada mediante Docker y Testcontainers.
 *
 * Cada prueba crea las relaciones necesarias desde cero para evitar IDs
 * quemados, registros preexistentes y dependencias entre pruebas.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
class RentaDaoTest extends OracleTestBase {

    private RentaDao dao;

    /**
     * Inicializa el DAO antes de ejecutar cada prueba.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @BeforeEach
    void setUp() {
        dao = new RentaDao();
    }

    /**
     * Comprueba que el resumen global recupere una renta real registrada
     * dentro de la base de datos temporal.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getResumenTodasLasRentas() {

        int idComprador = crearUsuarioPrueba("Comprador Global");

        crearRentaLibriFlowPrueba(
                idComprador,
                "PROGRAMADA",
                0,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );

        List<RentaResumen> lista =
                dao.getResumenTodasLasRentas();

        assertNotNull(lista);
        assertFalse(
                lista.isEmpty(),
                "El resumen global debe contener la renta creada"
        );
    }

    /**
     * Comprueba que el resumen por usuario recupere las rentas
     * correspondientes al comprador indicado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getResumenRentasPorUsuario() {

        int idComprador = crearUsuarioPrueba("Comprador Resumen");

        crearRentaLibriFlowPrueba(
                idComprador,
                "ACTIVA",
                0,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(6)
        );

        List<RentaResumen> lista =
                dao.getResumenRentasPorUsuario(idComprador);

        assertNotNull(lista);
        assertFalse(
                lista.isEmpty(),
                "El usuario debe tener al menos una renta"
        );
    }

    /**
     * Comprueba que un usuario sin rentas obtenga una lista vacía.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getResumenRentasPorUsuarioSinRegistros() {

        int idUsuario =
                crearUsuarioPrueba("Usuario Sin Rentas");

        List<RentaResumen> lista =
                dao.getResumenRentasPorUsuario(idUsuario);

        assertNotNull(lista);
        assertTrue(lista.isEmpty());
    }

    /**
     * Comprueba que una renta PROGRAMADA cuya fecha de inicio ya llegó
     * pueda marcarse como ACTIVA.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void marcarComoEntregada() {

        int idComprador =
                crearUsuarioPrueba("Comprador Entrega");

        int idDetalleRenta =
                crearRentaLibriFlowPrueba(
                        idComprador,
                        "PROGRAMADA",
                        0,
                        LocalDate.now().minusDays(1),
                        LocalDate.now().plusDays(7)
                );

        boolean resultado =
                dao.marcarComoEntregada(idDetalleRenta);

        assertTrue(resultado);

        assertEquals(
                "ACTIVA",
                obtenerEstadoRenta(idDetalleRenta)
        );
    }

    /**
     * Comprueba que una renta PROGRAMADA con fecha futura todavía
     * no pueda marcarse como entregada.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void marcarComoEntregadaAntesDeFecha() {

        int idComprador =
                crearUsuarioPrueba("Comprador Entrega Futura");

        int idDetalleRenta =
                crearRentaLibriFlowPrueba(
                        idComprador,
                        "PROGRAMADA",
                        0,
                        LocalDate.now().plusDays(3),
                        LocalDate.now().plusDays(10)
                );

        boolean resultado =
                dao.marcarComoEntregada(idDetalleRenta);

        assertFalse(resultado);

        assertEquals(
                "PROGRAMADA",
                obtenerEstadoRenta(idDetalleRenta)
        );
    }

    /**
     * Comprueba que una renta ACTIVA pueda finalizarse y que se registre
     * su fecha de devolución.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void marcarComoFinalizada() {

        int idComprador =
                crearUsuarioPrueba("Comprador Finalizar");

        int idDetalleRenta =
                crearRentaLibriFlowPrueba(
                        idComprador,
                        "ACTIVA",
                        0,
                        LocalDate.now().minusDays(3),
                        LocalDate.now().plusDays(4)
                );

        boolean resultado =
                dao.marcarComoFinalizada(idDetalleRenta);

        assertTrue(resultado);

        assertEquals(
                "FINALIZADA",
                obtenerEstadoRenta(idDetalleRenta)
        );

        assertTrue(
                tieneFechaDevolucion(idDetalleRenta),
                "La fecha de devolución debe registrarse al finalizar"
        );
    }

    /**
     * Comprueba que una renta que no se encuentra ACTIVA
     * no pueda finalizarse.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void marcarComoFinalizadaEstadoInvalido() {

        int idComprador =
                crearUsuarioPrueba("Comprador Finalizar Invalida");

        int idDetalleRenta =
                crearRentaLibriFlowPrueba(
                        idComprador,
                        "PROGRAMADA",
                        0,
                        LocalDate.now(),
                        LocalDate.now().plusDays(7)
                );

        assertFalse(
                dao.marcarComoFinalizada(idDetalleRenta)
        );

        assertEquals(
                "PROGRAMADA",
                obtenerEstadoRenta(idDetalleRenta)
        );
    }

    /**
     * Comprueba el cambio directo de estado de una renta existente.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void cambiarEstadoRenta() {

        int idComprador =
                crearUsuarioPrueba("Comprador Cambio Estado");

        int idDetalleRenta =
                crearRentaLibriFlowPrueba(
                        idComprador,
                        "PROGRAMADA",
                        0,
                        LocalDate.now(),
                        LocalDate.now().plusDays(7)
                );

        boolean resultado =
                dao.cambiarEstadoRenta(
                        idDetalleRenta,
                        "CANCELADA"
                );

        assertTrue(resultado);

        assertEquals(
                "CANCELADA",
                obtenerEstadoRenta(idDetalleRenta)
        );
    }

    /**
     * Comprueba que cambiar el estado de una renta inexistente devuelva false.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void cambiarEstadoRentaInexistente() {

        assertFalse(
                dao.cambiarEstadoRenta(
                        -999,
                        "CANCELADA"
                )
        );
    }

    /**
     * Comprueba que se cuenten únicamente las rentas ACTIVA
     * sin penalización del usuario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void contarRentasActivasPorUsuario() {

        int idComprador =
                crearUsuarioPrueba("Comprador Rentas Activas");

        crearRentaLibriFlowPrueba(
                idComprador,
                "ACTIVA",
                0,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(5)
        );

        crearRentaLibriFlowPrueba(
                idComprador,
                "ACTIVA",
                1,
                LocalDate.now().minusDays(5),
                LocalDate.now().minusDays(1)
        );

        crearRentaLibriFlowPrueba(
                idComprador,
                "PROGRAMADA",
                0,
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(8)
        );

        assertEquals(
                1,
                dao.contarRentasActivasPorUsuario(idComprador)
        );
    }

    /**
     * Comprueba que se cuenten como retrasos las rentas ACTIVA
     * con penalización 1 o 2.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void contarRetrasosPorUsuario() {

        int idComprador =
                crearUsuarioPrueba("Comprador Retrasos");

        crearRentaLibriFlowPrueba(
                idComprador,
                "ACTIVA",
                1,
                LocalDate.now().minusDays(10),
                LocalDate.now().minusDays(3)
        );

        crearRentaLibriFlowPrueba(
                idComprador,
                "ACTIVA",
                2,
                LocalDate.now().minusDays(15),
                LocalDate.now().minusDays(7)
        );

        crearRentaLibriFlowPrueba(
                idComprador,
                "ACTIVA",
                0,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(5)
        );

        assertEquals(
                2,
                dao.contarRetrasosPorUsuario(idComprador)
        );
    }

    /**
     * Crea un usuario válido para las relaciones de prueba.
     *
     * @param nombre Nombre descriptivo del usuario.
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
                    "renta_" + System.nanoTime() + "@test.com"
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
     * Crea una renta completa de LibriFlow junto con todas sus relaciones:
     * libro, publicación, transacción y detalle de transacción.
     *
     * @param idComprador Identificador del comprador.
     * @param estado Estado inicial de la renta.
     * @param penalizacion Nivel de penalización.
     * @param fechaInicio Fecha de inicio.
     * @param fechaLimite Fecha límite.
     * @return Identificador generado en detalle_renta.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearRentaLibriFlowPrueba(
            int idComprador,
            String estado,
            int penalizacion,
            LocalDate fechaInicio,
            LocalDate fechaLimite) {

        int idLibro =
                crearLibroPrueba();

        int idPublicacionLf =
                crearPublicacionLfPrueba(idLibro);

        int idTransaccion =
                crearTransaccionPrueba(idComprador);

        int idDetalleTransaccion =
                crearDetalleTransaccionLfPrueba(
                        idTransaccion,
                        idPublicacionLf
                );

        return crearDetalleRentaPrueba(
                idDetalleTransaccion,
                estado,
                penalizacion,
                fechaInicio,
                fechaLimite
        );
    }

    /**
     * Crea un libro auxiliar.
     *
     * @return Identificador generado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearLibroPrueba() {

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
                    "Libro Renta " + System.nanoTime()
            );
            ps.setString(2, "Autor Renta");
            ps.setString(3, "Editorial Renta");
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
     * Crea una publicación oficial de LibriFlow.
     *
     * @param idLibro Identificador del libro.
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
                    estado,
                    cantidad,
                    es_venta,
                    es_renta,
                    precio
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_PUBLICACION_LF"}
             )) {

            ps.setInt(1, idLibro);
            ps.setString(2, "Publicación para prueba de RentaDao");
            ps.setString(3, "ACTIVO");
            ps.setInt(4, 5);
            ps.setInt(5, 1);
            ps.setInt(6, 1);
            ps.setDouble(7, 200.00);

            assertEquals(1, ps.executeUpdate());

            try (ResultSet rs = ps.getGeneratedKeys()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudo crear la publicación LF de prueba",
                    e
            );
        }
    }

    /**
     * Crea la transacción principal de la renta.
     *
     * @param idComprador Identificador del comprador.
     * @return Identificador generado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearTransaccionPrueba(int idComprador) {

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
            ps.setDouble(2, 200.00);
            ps.setDouble(3, 0.00);
            ps.setDouble(4, 200.00);
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
     * Crea el detalle de transacción correspondiente a la renta.
     *
     * @param idTransaccion Identificador de la transacción.
     * @param idPublicacionLf Identificador de la publicación.
     * @return Identificador generado del detalle.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearDetalleTransaccionLfPrueba(
            int idTransaccion,
            int idPublicacionLf) {

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
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_DETALLE"}
             )) {

            ps.setInt(1, idTransaccion);
            ps.setInt(2, idPublicacionLf);
            ps.setString(3, "RENTA");
            ps.setDouble(4, 200.00);
            ps.setDouble(5, 200.00);
            ps.setDouble(6, 0.00);

            assertEquals(1, ps.executeUpdate());

            try (ResultSet rs = ps.getGeneratedKeys()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudo crear el detalle de transacción",
                    e
            );
        }
    }

    /**
     * Crea el registro de detalle_renta que será utilizado por RentaDao.
     *
     * @param idDetalleTransaccion Identificador del detalle de transacción.
     * @param estado Estado inicial.
     * @param penalizacion Penalización registrada.
     * @param fechaInicio Fecha de inicio.
     * @param fechaLimite Fecha límite.
     * @return Identificador generado de la renta.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearDetalleRentaPrueba(
            int idDetalleTransaccion,
            String estado,
            int penalizacion,
            LocalDate fechaInicio,
            LocalDate fechaLimite) {

        String sql = """
                INSERT INTO detalle_renta(
                    id_detalle_transaccion,
                    fecha_inicio,
                    fecha_limite,
                    estado,
                    penalizacion,
                    codigo
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_DETALLE"}
             )) {

            ps.setInt(1, idDetalleTransaccion);
            ps.setDate(2, Date.valueOf(fechaInicio));
            ps.setDate(3, Date.valueOf(fechaLimite));
            ps.setString(4, estado);
            ps.setInt(5, penalizacion);
            ps.setString(
                    6,
                    "REN-" + System.nanoTime()
            );

            assertEquals(1, ps.executeUpdate());

            try (ResultSet rs = ps.getGeneratedKeys()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudo crear el detalle de renta",
                    e
            );
        }
    }

    /**
     * Consulta el estado actual de una renta.
     *
     * @param idDetalle Identificador de la renta.
     * @return Estado almacenado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private String obtenerEstadoRenta(int idDetalle) {

        String sql = """
                SELECT estado
                FROM detalle_renta
                WHERE id_detalle = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDetalle);

            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                return rs.getString("estado");
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudo consultar el estado de la renta",
                    e
            );
        }
    }

    /**
     * Comprueba si una renta ya tiene fecha de devolución.
     *
     * @param idDetalle Identificador de la renta.
     * @return true si la fecha de devolución no es nula.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private boolean tieneFechaDevolucion(int idDetalle) {

        String sql = """
                SELECT fecha_devolucion
                FROM detalle_renta
                WHERE id_detalle = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDetalle);

            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                return rs.getDate("fecha_devolucion") != null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudo consultar la fecha de devolución",
                    e
            );
        }
    }
}