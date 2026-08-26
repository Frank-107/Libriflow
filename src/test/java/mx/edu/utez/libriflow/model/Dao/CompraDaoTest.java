package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.CompraResumen;
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
 * Pruebas de integración para {@link CompraDao} utilizando una instancia
 * temporal de Oracle ejecutada mediante Docker y Testcontainers.
 *
 * Las pruebas crean usuarios, libros, publicaciones, transacciones y detalles
 * reales dentro del contenedor, evitando IDs quemados o datos preexistentes.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
class CompraDaoTest extends OracleTestBase {

    private CompraDao compraDao;

    /**
     * Inicializa el DAO antes de cada prueba.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @BeforeEach
    void setUp() {
        compraDao =
                new CompraDao();
    }

    /**
     * Comprueba que el historial recupere una compra realizada sobre una
     * publicación perteneciente a otro usuario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("Obtiene compra de publicación de usuario")
    void getResumenComprasPorUsuarioPublicacionUsuario() {

        int idVendedor =
                crearUsuarioPrueba(
                        "Vendedor Compra"
                );

        int idComprador =
                crearUsuarioPrueba(
                        "Comprador Compra"
                );

        int idLibro =
                crearLibroPrueba(
                        "Libro Compra Usuario"
                );

        int idPublicacion =
                crearPublicacionUsuarioPrueba(
                        idVendedor,
                        idLibro
                );

        int idTransaccion =
                crearTransaccionPrueba(
                        idComprador,
                        180.00
                );

        int idDetalle =
                crearDetalleUsuarioPrueba(
                        idTransaccion,
                        idPublicacion,
                        idVendedor,
                        "COMPRA",
                        180.00
                );

        List<CompraResumen> resultado =
                compraDao.getResumenComprasPorUsuario(
                        idComprador
                );

        assertNotNull(resultado);

        CompraResumen compra =
                resultado.stream()
                        .filter(
                                c ->
                                        c.getIdDetalle()
                                                == idDetalle
                        )
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new AssertionError(
                                                "Debe recuperarse la compra creada"
                                        )
                        );

        assertEquals(
                idTransaccion,
                compra.getIdTransaccion()
        );

        assertEquals(
                idPublicacion,
                compra.getIdPublicacion()
        );

        assertFalse(
                compra.isEsLibriFlow()
        );

        assertEquals(
                "Libro Compra Usuario",
                compra.getTitulo()
        );

        assertEquals(
                "Autor Compra Test",
                compra.getAutor()
        );

        assertEquals(
                180.00,
                compra.getPrecio(),
                0.001
        );

        assertEquals(
                "Vendedor Compra",
                compra.getNombreVendedor()
        );

        assertEquals(
                "COMPLETADA",
                compra.getEstadoTransaccion()
        );

        assertNotNull(
                compra.getFecha()
        );
    }

    /**
     * Comprueba que el historial también recupere una compra de una
     * publicación administrada directamente por LibriFlow.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("Obtiene compra de publicación LibriFlow")
    void getResumenComprasPorUsuarioPublicacionLf() {

        int idComprador =
                crearUsuarioPrueba(
                        "Comprador LibriFlow"
                );

        int idLibro =
                crearLibroPrueba(
                        "Libro Compra LibriFlow"
                );

        int idPublicacion =
                crearPublicacionLfPrueba(
                        idLibro
                );

        int idTransaccion =
                crearTransaccionPrueba(
                        idComprador,
                        250.00
                );

        int idDetalle =
                crearDetalleLfPrueba(
                        idTransaccion,
                        idPublicacion,
                        "COMPRA",
                        250.00
                );

        List<CompraResumen> resultado =
                compraDao.getResumenComprasPorUsuario(
                        idComprador
                );

        CompraResumen compra =
                resultado.stream()
                        .filter(
                                c ->
                                        c.getIdDetalle()
                                                == idDetalle
                        )
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new AssertionError(
                                                "Debe recuperarse la compra de LibriFlow"
                                        )
                        );

        assertTrue(
                compra.isEsLibriFlow()
        );

        assertEquals(
                idPublicacion,
                compra.getIdPublicacion()
        );

        assertEquals(
                "Libro Compra LibriFlow",
                compra.getTitulo()
        );

        assertEquals(
                "LibriFlow",
                compra.getNombreVendedor()
        );

        assertEquals(
                250.00,
                compra.getPrecio(),
                0.001
        );
    }

    /**
     * Comprueba que las operaciones de tipo RENTA no aparezcan dentro del
     * historial de compras.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("El historial ignora operaciones de renta")
    void getResumenComprasPorUsuarioIgnoraRentas() {

        int idComprador =
                crearUsuarioPrueba(
                        "Comprador Solo Renta"
                );

        int idLibro =
                crearLibroPrueba(
                        "Libro Solo Renta"
                );

        int idPublicacion =
                crearPublicacionLfPrueba(
                        idLibro
                );

        int idTransaccion =
                crearTransaccionPrueba(
                        idComprador,
                        120.00
                );

        crearDetalleLfPrueba(
                idTransaccion,
                idPublicacion,
                "RENTA",
                120.00
        );

        List<CompraResumen> resultado =
                compraDao.getResumenComprasPorUsuario(
                        idComprador
                );

        assertNotNull(resultado);
        assertTrue(
                resultado.isEmpty(),
                "Una renta no debe aparecer en el historial de compras"
        );
    }

    /**
     * Comprueba que un usuario sin compras reciba una lista vacía.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("Usuario sin compras obtiene lista vacía")
    void getResumenComprasPorUsuarioSinCompras() {

        int idComprador =
                crearUsuarioPrueba(
                        "Comprador Sin Compras"
                );

        List<CompraResumen> resultado =
                compraDao.getResumenComprasPorUsuario(
                        idComprador
                );

        assertNotNull(resultado);
        assertTrue(
                resultado.isEmpty()
        );
    }

    /**
     * Comprueba que contarVentasPorUsuario cuente las compras relacionadas
     * con un vendedor real.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("Cuenta ventas de un vendedor")
    void contarVentasPorUsuario() {

        int idVendedor =
                crearUsuarioPrueba(
                        "Vendedor Conteo"
                );

        int idComprador =
                crearUsuarioPrueba(
                        "Comprador Conteo"
                );

        int idLibro1 =
                crearLibroPrueba(
                        "Libro Venta 1"
                );

        int idLibro2 =
                crearLibroPrueba(
                        "Libro Venta 2"
                );

        int idPublicacion1 =
                crearPublicacionUsuarioPrueba(
                        idVendedor,
                        idLibro1
                );

        int idPublicacion2 =
                crearPublicacionUsuarioPrueba(
                        idVendedor,
                        idLibro2
                );

        int idTransaccion =
                crearTransaccionPrueba(
                        idComprador,
                        390.00
                );

        crearDetalleUsuarioPrueba(
                idTransaccion,
                idPublicacion1,
                idVendedor,
                "COMPRA",
                190.00
        );

        crearDetalleUsuarioPrueba(
                idTransaccion,
                idPublicacion2,
                idVendedor,
                "COMPRA",
                200.00
        );

        assertEquals(
                2,
                compraDao.contarVentasPorUsuario(
                        idVendedor
                )
        );
    }

    /**
     * Comprueba que las rentas de un vendedor no sean contabilizadas como ventas.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("El conteo de ventas ignora rentas")
    void contarVentasPorUsuarioIgnoraRentas() {

        int idVendedor =
                crearUsuarioPrueba(
                        "Vendedor Renta"
                );

        int idComprador =
                crearUsuarioPrueba(
                        "Comprador Renta"
                );

        int idLibro =
                crearLibroPrueba(
                        "Libro Renta Vendedor"
                );

        int idPublicacion =
                crearPublicacionUsuarioPrueba(
                        idVendedor,
                        idLibro
                );

        int idTransaccion =
                crearTransaccionPrueba(
                        idComprador,
                        100.00
                );

        crearDetalleUsuarioPrueba(
                idTransaccion,
                idPublicacion,
                idVendedor,
                "RENTA",
                100.00
        );

        assertEquals(
                0,
                compraDao.contarVentasPorUsuario(
                        idVendedor
                )
        );
    }

    /**
     * Comprueba que un usuario sin ventas obtenga un conteo igual a cero.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    @DisplayName("Usuario sin ventas obtiene cero")
    void contarVentasPorUsuarioSinVentas() {

        int idUsuario =
                crearUsuarioPrueba(
                        "Usuario Sin Ventas"
                );

        assertEquals(
                0,
                compraDao.contarVentasPorUsuario(
                        idUsuario
                )
        );
    }

    /**
     * Crea un usuario dentro de la base temporal.
     *
     * @param nombre Nombre utilizado en la prueba.
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
                    "compra_"
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
     * Crea un libro auxiliar.
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
                    "Autor Compra Test"
            );

            ps.setString(
                    3,
                    "Editorial Compra Test"
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
     * Crea una publicación perteneciente a un usuario.
     *
     * @param idUsuario Propietario de la publicación.
     * @param idLibro Libro relacionado.
     * @return Identificador generado.
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

            ps.setInt(
                    1,
                    idUsuario
            );

            ps.setInt(
                    2,
                    idLibro
            );

            ps.setString(
                    3,
                    "Publicación de usuario para CompraDaoTest"
            );

            ps.setString(
                    4,
                    "ACTIVO"
            );

            ps.setDouble(
                    5,
                    200.00
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
                    "No se pudo crear la publicación de usuario. ERROR SQL: "
                            + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Crea una publicación oficial de LibriFlow.
     *
     * @param idLibro Libro relacionado.
     * @return Identificador generado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearPublicacionLfPrueba(
            int idLibro) {

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
                    "Publicación LibriFlow para CompraDaoTest"
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
                    "No se pudo crear la publicación LibriFlow. ERROR SQL: "
                            + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Crea una transacción principal.
     *
     * @param idComprador Usuario comprador.
     * @param total Importe total.
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

            ps.setInt(
                    1,
                    idComprador
            );

            ps.setDouble(
                    2,
                    total
            );

            ps.setDouble(
                    3,
                    0.00
            );

            ps.setDouble(
                    4,
                    total
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
     * Crea un detalle de transacción asociado a una publicación de usuario.
     *
     * @param idTransaccion Transacción relacionada.
     * @param idPublicacion Publicación de usuario.
     * @param idVendedor Propietario de la publicación.
     * @param tipoOperacion COMPRA o RENTA.
     * @param precio Precio de la operación.
     * @return Identificador generado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearDetalleUsuarioPrueba(
            int idTransaccion,
            int idPublicacion,
            int idVendedor,
            String tipoOperacion,
            double precio) {

        String sql = """
                INSERT INTO detalle_transaccion(
                    id_transaccion,
                    id_publicacion_us,
                    id_vendedor,
                    tipo_operacion,
                    precio,
                    ganancia_libriflow,
                    ganancia_vendedor
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
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

            ps.setInt(
                    2,
                    idPublicacion
            );

            ps.setInt(
                    3,
                    idVendedor
            );

            ps.setString(
                    4,
                    tipoOperacion
            );

            ps.setDouble(
                    5,
                    precio
            );

            ps.setDouble(
                    6,
                    precio * 0.10
            );

            ps.setDouble(
                    7,
                    precio * 0.90
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
                    "No se pudo crear el detalle de usuario. ERROR SQL: "
                            + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Crea un detalle de transacción asociado a una publicación oficial.
     *
     * @param idTransaccion Transacción relacionada.
     * @param idPublicacion Publicación oficial.
     * @param tipoOperacion COMPRA o RENTA.
     * @param precio Precio de la operación.
     * @return Identificador generado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearDetalleLfPrueba(
            int idTransaccion,
            int idPublicacion,
            String tipoOperacion,
            double precio) {

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
                    precio
            );

            ps.setDouble(
                    5,
                    precio
            );

            ps.setDouble(
                    6,
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
                    "No se pudo crear el detalle LibriFlow. ERROR SQL: "
                            + e.getMessage(),
                    e
            );
        }
    }
}