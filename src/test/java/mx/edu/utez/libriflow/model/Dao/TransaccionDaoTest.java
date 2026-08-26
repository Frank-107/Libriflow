package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Transaccion;
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
 * Pruebas de integración para {@link TransaccionDao} utilizando
 * una instancia temporal de Oracle ejecutada mediante Docker
 * y Testcontainers.
 *
 * Cada prueba genera sus propios datos para evitar dependencias
 * con registros preexistentes o con el orden de ejecución.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
class TransaccionDaoTest extends OracleTestBase {

    private TransaccionDao dao;

    /**
     * Inicializa el DAO antes de ejecutar cada prueba.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @BeforeEach
    void setUp() {
        dao = new TransaccionDao();
    }

    /**
     * Comprueba que pueda registrarse una nueva transacción.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void create() {

        int idComprador =
                crearUsuarioPrueba();

        Transaccion transaccion =
                crearEntidadTransaccion(
                        idComprador,
                        500.00,
                        50.00,
                        550.00,
                        "COMPLETADA"
                );

        int idTransaccion =
                dao.create(transaccion);

        assertTrue(
                idTransaccion > 0,
                "create debe devolver un ID válido"
        );

        Transaccion guardada =
                dao.getById(idTransaccion);

        assertNotNull(guardada);
        assertEquals(
                idComprador,
                guardada.getIdComprador()
        );
        assertEquals(
                500.00,
                guardada.getSubtotal(),
                0.001
        );
        assertEquals(
                50.00,
                guardada.getCostoEnvio(),
                0.001
        );
        assertEquals(
                550.00,
                guardada.getTotal(),
                0.001
        );
        assertEquals(
                "COMPLETADA",
                guardada.getEstado()
        );
    }

    /**
     * Comprueba que puedan obtenerse las transacciones registradas.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getAll() {

        int idComprador =
                crearUsuarioPrueba();

        int id1 = dao.create(
                crearEntidadTransaccion(
                        idComprador,
                        100.00,
                        20.00,
                        120.00,
                        "PENDIENTE"
                )
        );

        int id2 = dao.create(
                crearEntidadTransaccion(
                        idComprador,
                        200.00,
                        30.00,
                        230.00,
                        "COMPLETADA"
                )
        );

        assertTrue(id1 > 0);
        assertTrue(id2 > 0);

        List<Transaccion> transacciones =
                dao.getAll();

        assertNotNull(transacciones);

        assertTrue(
                transacciones.stream()
                        .anyMatch(
                                t ->
                                        t.getIdTransaccion()
                                                == id1
                        )
        );

        assertTrue(
                transacciones.stream()
                        .anyMatch(
                                t ->
                                        t.getIdTransaccion()
                                                == id2
                        )
        );
    }

    /**
     * Comprueba la consulta de una transacción mediante su ID.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getById() {

        int idComprador =
                crearUsuarioPrueba();

        int idTransaccion =
                dao.create(
                        crearEntidadTransaccion(
                                idComprador,
                                300.00,
                                40.00,
                                340.00,
                                "PENDIENTE"
                        )
                );

        Transaccion resultado =
                dao.getById(idTransaccion);

        assertNotNull(resultado);
        assertEquals(
                idTransaccion,
                resultado.getIdTransaccion()
        );
        assertEquals(
                idComprador,
                resultado.getIdComprador()
        );
        assertEquals(
                300.00,
                resultado.getSubtotal(),
                0.001
        );
        assertEquals(
                "PENDIENTE",
                resultado.getEstado()
        );
    }

    /**
     * Comprueba que un identificador inexistente devuelva null.
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
     * Comprueba la actualización de los datos principales
     * de una transacción existente.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void update() {

        int idComprador =
                crearUsuarioPrueba();

        int idTransaccion =
                dao.create(
                        crearEntidadTransaccion(
                                idComprador,
                                150.00,
                                25.00,
                                175.00,
                                "PENDIENTE"
                        )
                );

        Transaccion transaccion =
                dao.getById(idTransaccion);

        assertNotNull(transaccion);

        transaccion.setSubtotal(400.00);
        transaccion.setCostoEnvio(60.00);
        transaccion.setTotal(460.00);
        transaccion.setEstado("COMPLETADA");

        boolean actualizado =
                dao.update(transaccion);

        assertTrue(actualizado);

        Transaccion resultado =
                dao.getById(idTransaccion);

        assertNotNull(resultado);

        assertEquals(
                400.00,
                resultado.getSubtotal(),
                0.001
        );

        assertEquals(
                60.00,
                resultado.getCostoEnvio(),
                0.001
        );

        assertEquals(
                460.00,
                resultado.getTotal(),
                0.001
        );

        assertEquals(
                "COMPLETADA",
                resultado.getEstado()
        );
    }

    /**
     * Comprueba que actualizar una transacción inexistente devuelva false.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void updateInexistente() {

        int idComprador =
                crearUsuarioPrueba();

        Transaccion transaccion =
                crearEntidadTransaccion(
                        idComprador,
                        100.00,
                        10.00,
                        110.00,
                        "PENDIENTE"
                );

        transaccion.setIdTransaccion(-999);

        assertFalse(
                dao.update(transaccion)
        );
    }

    /**
     * Comprueba que una transacción pueda eliminarse por su ID.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void delete() {

        int idComprador =
                crearUsuarioPrueba();

        int idTransaccion =
                dao.create(
                        crearEntidadTransaccion(
                                idComprador,
                                250.00,
                                30.00,
                                280.00,
                                "PENDIENTE"
                        )
                );

        assertTrue(idTransaccion > 0);

        boolean eliminado =
                dao.delete(idTransaccion);

        assertTrue(eliminado);

        assertNull(
                dao.getById(idTransaccion)
        );
    }

    /**
     * Comprueba que eliminar una transacción inexistente devuelva false.
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
     * Construye una entidad de transacción válida para las pruebas.
     *
     * @param idComprador Identificador del usuario comprador.
     * @param subtotal Subtotal de la compra.
     * @param costoEnvio Costo de envío.
     * @param total Total de la compra.
     * @param estado Estado de la transacción.
     * @return Objeto Transaccion listo para almacenarse.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private Transaccion crearEntidadTransaccion(
            int idComprador,
            double subtotal,
            double costoEnvio,
            double total,
            String estado) {

        return new Transaccion(
                idComprador,
                subtotal,
                costoEnvio,
                total,
                estado
        );
    }

    /**
     * Inserta un usuario válido directamente en la base
     * de datos temporal utilizada para las pruebas.
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

            ps.setString(
                    1,
                    "Comprador Test"
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
                    "transaccion_"
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
                    "No se pudo crear el comprador de prueba",
                    e
            );
        }
    }
}