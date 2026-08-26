package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.DetalleRenta;
import mx.edu.utez.libriflow.testconfig.OracleTestBase;
import mx.edu.utez.libriflow.utils.SQLconnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para {@link DetalleRentaDao} utilizando Oracle
 * ejecutado temporalmente mediante Docker y Testcontainers.
 *
 * Las pruebas crean sus propias dependencias de usuario, transacción y detalle
 * de transacción para verificar operaciones reales sobre la base temporal.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
class DetalleRentaDaoTest extends OracleTestBase {

    private DetalleRentaDao dao;

    /**
     * Inicializa el DAO antes de cada prueba.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @BeforeEach
    void setUp() {
        dao = new DetalleRentaDao();
    }

    /**
     * Comprueba la creación de un detalle de renta con una relación válida.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void create() {

        int idUsuario =
                crearUsuarioPrueba("Usuario Create");

        int idTransaccion =
                crearTransaccionPrueba(idUsuario);

        int idDetalleTransaccion =
                crearDetalleTransaccionPrueba(idTransaccion);

        DetalleRenta renta =
                crearEntidadRenta(
                        idDetalleTransaccion,
                        "PROGRAMADA",
                        0
                );

        int idRenta =
                dao.create(renta);

        assertTrue(
                idRenta > 0,
                "create debe devolver un ID válido"
        );

        DetalleRenta guardada =
                dao.getById(idRenta);

        assertNotNull(guardada);
        assertEquals(
                idDetalleTransaccion,
                guardada.getIdDetalleTransaccion()
        );
        assertEquals(
                "PROGRAMADA",
                guardada.getEstado()
        );
        assertEquals(
                0,
                guardada.getPenalizacion()
        );
    }

    /**
     * Comprueba que create conserve compatibilidad con el flujo actual de
     * LibriFlow, donde el ID del detalle de transacción se coloca en idDetalle.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void createCompatibleConFlujoActual() {

        int idUsuario =
                crearUsuarioPrueba("Usuario Compatibilidad");

        int idTransaccion =
                crearTransaccionPrueba(idUsuario);

        int idDetalleTransaccion =
                crearDetalleTransaccionPrueba(idTransaccion);

        DetalleRenta renta =
                new DetalleRenta();

        renta.setIdDetalle(
                idDetalleTransaccion
        );

        renta.setFechaInicio(
                Timestamp.valueOf(
                        LocalDateTime.now()
                )
        );

        renta.setFechaLimite(
                Timestamp.valueOf(
                        LocalDateTime.now()
                                .plusDays(7)
                )
        );

        renta.setEstado(
                "PROGRAMADA"
        );

        renta.setCodigo(
                generarCodigo()
        );

        renta.setPenalizacion(0);

        int idRenta =
                dao.create(renta);

        assertTrue(idRenta > 0);

        DetalleRenta guardada =
                dao.getById(idRenta);

        assertNotNull(guardada);

        assertEquals(
                idDetalleTransaccion,
                guardada.getIdDetalleTransaccion()
        );
    }

    /**
     * Comprueba que getAll incluya los detalles creados.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getAll() {

        int idRenta1 =
                crearRentaCompleta(
                        "Usuario Lista 1",
                        "PROGRAMADA",
                        0
                );

        int idRenta2 =
                crearRentaCompleta(
                        "Usuario Lista 2",
                        "ACTIVA",
                        0
                );

        List<DetalleRenta> lista =
                dao.getAll();

        assertNotNull(lista);

        assertTrue(
                lista.stream()
                        .anyMatch(
                                r ->
                                        r.getIdDetalle()
                                                == idRenta1
                        )
        );

        assertTrue(
                lista.stream()
                        .anyMatch(
                                r ->
                                        r.getIdDetalle()
                                                == idRenta2
                        )
        );
    }

    /**
     * Comprueba la consulta de un detalle por ID.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getById() {

        int idRenta =
                crearRentaCompleta(
                        "Usuario ById",
                        "ACTIVA",
                        0
                );

        DetalleRenta renta =
                dao.getById(idRenta);

        assertNotNull(renta);
        assertEquals(
                idRenta,
                renta.getIdDetalle()
        );
        assertEquals(
                "ACTIVA",
                renta.getEstado()
        );
    }

    /**
     * Comprueba que un ID inexistente produzca null.
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
     * Comprueba la actualización completa de un detalle de renta.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void update() {

        int idUsuario =
                crearUsuarioPrueba("Usuario Update");

        int idTransaccion =
                crearTransaccionPrueba(idUsuario);

        int idDetalleTransaccion =
                crearDetalleTransaccionPrueba(idTransaccion);

        DetalleRenta renta =
                crearEntidadRenta(
                        idDetalleTransaccion,
                        "PROGRAMADA",
                        0
                );

        int idRenta =
                dao.create(renta);

        assertTrue(idRenta > 0);

        DetalleRenta actualizada =
                dao.getById(idRenta);

        assertNotNull(actualizada);

        actualizada.setEstado(
                "ACTIVA"
        );

        actualizada.setPenalizacion(
                1
        );

        actualizada.setCodigo(
                generarCodigo()
        );

        actualizada.setFechaDevolucion(
                Timestamp.valueOf(
                        LocalDateTime.now()
                                .plusDays(4)
                )
        );

        boolean resultado =
                dao.update(actualizada);

        assertTrue(resultado);

        DetalleRenta guardada =
                dao.getById(idRenta);

        assertNotNull(guardada);
        assertEquals(
                "ACTIVA",
                guardada.getEstado()
        );
        assertEquals(
                1,
                guardada.getPenalizacion()
        );
        assertEquals(
                actualizada.getCodigo(),
                guardada.getCodigo()
        );
        assertNotNull(
                guardada.getFechaDevolucion()
        );
    }

    /**
     * Comprueba que update retorne false para un registro inexistente.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void updateInexistente() {

        DetalleRenta renta =
                new DetalleRenta();

        renta.setIdDetalle(-999);
        renta.setIdDetalleTransaccion(-999);
        renta.setFechaInicio(
                Timestamp.valueOf(
                        LocalDateTime.now()
                )
        );
        renta.setFechaLimite(
                Timestamp.valueOf(
                        LocalDateTime.now()
                                .plusDays(5)
                )
        );
        renta.setEstado("ACTIVA");
        renta.setCodigo("NOEXISTE");
        renta.setPenalizacion(0);

        assertFalse(
                dao.update(renta)
        );
    }

    /**
     * Comprueba la eliminación de un detalle de renta existente.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void delete() {

        int idRenta =
                crearRentaCompleta(
                        "Usuario Delete",
                        "PROGRAMADA",
                        0
                );

        assertTrue(
                dao.delete(idRenta)
        );

        assertNull(
                dao.getById(idRenta)
        );
    }

    /**
     * Comprueba que delete devuelva false cuando el ID no existe.
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
     * Comprueba que getRentasActivas recupere una renta ACTIVA sin penalización.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getRentasActivas() {

        int idRenta =
                crearRentaCompleta(
                        "Usuario Activa",
                        "ACTIVA",
                        0
                );

        List<DetalleRenta> lista =
                dao.getRentasActivas();

        assertNotNull(lista);

        assertTrue(
                lista.stream()
                        .anyMatch(
                                r ->
                                        r.getIdDetalle()
                                                == idRenta
                        )
        );
    }

    /**
     * Comprueba que getRentasRetrasadasActivas recupere una renta ACTIVA
     * con penalización nivel 1.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getRentasRetrasadasActivas() {

        int idRenta =
                crearRentaCompleta(
                        "Usuario Retrasada",
                        "ACTIVA",
                        1
                );

        List<DetalleRenta> lista =
                dao.getRentasRetrasadasActivas();

        assertNotNull(lista);

        assertTrue(
                lista.stream()
                        .anyMatch(
                                r ->
                                        r.getIdDetalle()
                                                == idRenta
                        )
        );
    }

    /**
     * Comprueba el cambio de penalización de una renta real.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void cambiarPenalizacion() {

        int idRenta =
                crearRentaCompleta(
                        "Usuario Penalizacion",
                        "ACTIVA",
                        0
                );

        assertTrue(
                dao.cambiarPenalizacion(
                        idRenta,
                        2
                )
        );

        DetalleRenta renta =
                dao.getById(idRenta);

        assertNotNull(renta);

        assertEquals(
                2,
                renta.getPenalizacion()
        );
    }

    /**
     * Comprueba que cambiarPenalizacion devuelva false para un ID inexistente.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void cambiarPenalizacionInexistente() {

        assertFalse(
                dao.cambiarPenalizacion(
                        -999,
                        2
                )
        );
    }

    /**
     * Comprueba que un usuario existente pueda ser suspendido.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void suspenderUsuario() {

        int idUsuario =
                crearUsuarioPrueba(
                        "Usuario Suspender"
                );

        Timestamp fechaDesbloqueo =
                Timestamp.valueOf(
                        LocalDateTime.now()
                                .plusDays(7)
                );

        assertTrue(
                dao.suspenderUsuario(
                        idUsuario,
                        fechaDesbloqueo
                )
        );

        assertEquals(
                "INACTIVA",
                obtenerEstadoUsuario(
                        idUsuario
                )
        );

        assertNotNull(
                obtenerFechaDesbloqueo(
                        idUsuario
                )
        );
    }

    /**
     * Comprueba que suspenderUsuario devuelva false para un usuario inexistente.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void suspenderUsuarioInexistente() {

        assertFalse(
                dao.suspenderUsuario(
                        -999,
                        Timestamp.valueOf(
                                LocalDateTime.now()
                                        .plusDays(7)
                        )
                )
        );
    }

    /**
     * Comprueba que se recupere el comprador relacionado con una renta.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getIdUsuarioByIdRenta() {

        int idUsuario =
                crearUsuarioPrueba(
                        "Usuario Obtener ID"
                );

        int idTransaccion =
                crearTransaccionPrueba(
                        idUsuario
                );

        int idDetalleTransaccion =
                crearDetalleTransaccionPrueba(
                        idTransaccion
                );

        DetalleRenta renta =
                crearEntidadRenta(
                        idDetalleTransaccion,
                        "ACTIVA",
                        0
                );

        int idRenta =
                dao.create(renta);

        assertTrue(idRenta > 0);

        assertEquals(
                idUsuario,
                dao.getIdUsuarioByIdRenta(
                        idRenta
                )
        );
    }

    /**
     * Comprueba que se devuelva -1 cuando la renta no existe.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getIdUsuarioByIdRentaInexistente() {

        assertEquals(
                -1,
                dao.getIdUsuarioByIdRenta(
                        -999
                )
        );
    }

    /**
     * Crea una renta completa con las relaciones necesarias.
     *
     * @param nombreUsuario Nombre descriptivo del usuario.
     * @param estado Estado de la renta.
     * @param penalizacion Penalización inicial.
     * @return Identificador generado de la renta.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearRentaCompleta(
            String nombreUsuario,
            String estado,
            int penalizacion) {

        int idUsuario =
                crearUsuarioPrueba(
                        nombreUsuario
                );

        int idTransaccion =
                crearTransaccionPrueba(
                        idUsuario
                );

        int idDetalleTransaccion =
                crearDetalleTransaccionPrueba(
                        idTransaccion
                );

        DetalleRenta renta =
                crearEntidadRenta(
                        idDetalleTransaccion,
                        estado,
                        penalizacion
                );

        int idRenta =
                dao.create(renta);

        assertTrue(
                idRenta > 0,
                "Debe crearse la renta auxiliar"
        );

        return idRenta;
    }

    /**
     * Construye una entidad DetalleRenta válida.
     *
     * @param idDetalleTransaccion Relación con DETALLE_TRANSACCION.
     * @param estado Estado inicial.
     * @param penalizacion Penalización inicial.
     * @return Entidad lista para almacenarse.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private DetalleRenta crearEntidadRenta(
            int idDetalleTransaccion,
            String estado,
            int penalizacion) {

        DetalleRenta renta =
                new DetalleRenta();

        renta.setIdDetalleTransaccion(
                idDetalleTransaccion
        );

        renta.setFechaInicio(
                Timestamp.valueOf(
                        LocalDateTime.now()
                                .minusDays(1)
                )
        );

        renta.setFechaLimite(
                Timestamp.valueOf(
                        LocalDateTime.now()
                                .plusDays(7)
                )
        );

        renta.setEstado(
                estado
        );

        renta.setCodigo(
                generarCodigo()
        );

        renta.setPenalizacion(
                penalizacion
        );

        return renta;
    }

    /**
     * Genera un código compatible con VARCHAR2(10).
     *
     * @return Código de diez caracteres.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private String generarCodigo() {

        return String.format(
                "DR%08d",
                Math.floorMod(
                        System.nanoTime(),
                        100000000L
                )
        );
    }

    /**
     * Crea un usuario para las relaciones de prueba.
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

            ps.setString(1, nombre);
            ps.setString(2, "ApellidoP");
            ps.setString(3, "ApellidoM");
            ps.setString(
                    4,
                    "detalle_renta_"
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
     * Crea una transacción relacionada con el comprador.
     *
     * @param idComprador Identificador del comprador.
     * @return Identificador generado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearTransaccionPrueba(
            int idComprador) {

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
                    idComprador
            );

            ps.setDouble(
                    2,
                    100.00
            );

            ps.setDouble(
                    3,
                    0.00
            );

            ps.setDouble(
                    4,
                    100.00
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
     * Crea el detalle de transacción que originará la renta.
     *
     * @param idTransaccion Identificador de la transacción.
     * @return Identificador generado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearDetalleTransaccionPrueba(
            int idTransaccion) {

        String sql = """
                INSERT INTO detalle_transaccion(
                    id_transaccion,
                    tipo_operacion,
                    precio,
                    ganancia_libriflow,
                    ganancia_vendedor
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_DETALLE"}
             )) {

            ps.setInt(
                    1,
                    idTransaccion
            );

            ps.setString(
                    2,
                    "RENTA"
            );

            ps.setDouble(
                    3,
                    100.00
            );

            ps.setDouble(
                    4,
                    100.00
            );

            ps.setDouble(
                    5,
                    0.00
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
                    "No se pudo crear el detalle de transacción. ERROR SQL: "
                            + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Consulta el estado de una cuenta de usuario.
     *
     * @param idUsuario Identificador del usuario.
     * @return Estado almacenado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private String obtenerEstadoUsuario(
            int idUsuario) {

        String sql = """
                SELECT estado_cuenta
                FROM usuario
                WHERE id_usuario = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idUsuario
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                assertTrue(rs.next());

                return rs.getString(
                        "ESTADO_CUENTA"
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo consultar el estado del usuario. ERROR SQL: "
                            + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Consulta la fecha de desbloqueo de un usuario.
     *
     * @param idUsuario Identificador del usuario.
     * @return Fecha almacenada o null.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private Timestamp obtenerFechaDesbloqueo(
            int idUsuario) {

        String sql = """
                SELECT fecha_desbloqueo
                FROM usuario
                WHERE id_usuario = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idUsuario
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                assertTrue(rs.next());

                return rs.getTimestamp(
                        "FECHA_DESBLOQUEO"
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo consultar la fecha de desbloqueo. ERROR SQL: "
                            + e.getMessage(),
                    e
            );
        }
    }
}