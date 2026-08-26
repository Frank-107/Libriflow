package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.DetalleTransaccion;
import mx.edu.utez.libriflow.model.Movimiento;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Esta clase se encarga de realizar las operaciones relacionadas con los
 * detalles de las transacciones dentro de la base de datos. Permite registrar,
 * consultar, actualizar y eliminar detalles, además de obtener los movimientos
 * de usuarios y los ingresos generados dentro de LibriFlow.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
public class DetalleTransaccionDao {

    /**
     *
     * Este método se encarga de registrar un nuevo detalle de transacción en
     * la base de datos. Cuando la operación corresponde a la compra de una
     * publicación de usuario, primero cambia su estado a VENDIDO para evitar
     * que pueda ser comprada nuevamente. Después registra la información de
     * la transacción y devuelve el identificador generado.
     *
     * @param entidad Es el objeto DetalleTransaccion que contiene la información
     *                que se desea registrar.
     * @return El identificador generado para el detalle de la transacción o -1
     *         si ocurre un error durante el registro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public int create(DetalleTransaccion entidad) {

        Connection con = null;

        try {
            con = SQLconnector.getConnection();
            con.setAutoCommit(false);

            /*
             * Si es una COMPRA de una publicación de usuario,
             * primero reservamos/marcamos la publicación como VENDIDO.
             *
             * Solamente se puede realizar si actualmente está ACTIVO.
             *
             * Esto también evita que dos usuarios puedan comprar
             * la misma publicación.
             */
            if (entidad.getIdPublicacionUs() != null
                    && "COMPRA".equalsIgnoreCase(entidad.getTipoOperacion())) {

                String sqlVender = """
                        UPDATE publicacion_us
                        SET estado = 'VENDIDO'
                        WHERE id_publicacion_us = ?
                        AND estado = 'ACTIVO'
                        """;

                try (PreparedStatement ps = con.prepareStatement(sqlVender)) {

                    ps.setInt(
                            1,
                            entidad.getIdPublicacionUs()
                    );

                    int filasActualizadas =
                            ps.executeUpdate();

                    if (filasActualizadas == 0) {

                        con.rollback();

                        System.err.println(
                                "La publicación "
                                        + entidad.getIdPublicacionUs()
                                        + " ya no está disponible para compra."
                        );

                        return -1;
                    }
                }
            }

            String sql = """
                    INSERT INTO detalle_transaccion
                    (
                        id_transaccion,
                        id_publicacion_us,
                        id_publicacion_lf,
                        id_vendedor,
                        tipo_operacion,
                        precio,
                        ganancia_libriflow,
                        ganancia_vendedor
                    )
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            int idDetalle;

            try (PreparedStatement ps = con.prepareStatement(
                    sql,
                    new String[]{"ID_DETALLE"}
            )) {

                ps.setInt(
                        1,
                        entidad.getIdTransaccion()
                );

                if (entidad.getIdPublicacionUs() != null) {

                    ps.setInt(
                            2,
                            entidad.getIdPublicacionUs()
                    );

                } else {

                    ps.setNull(
                            2,
                            Types.INTEGER
                    );
                }

                if (entidad.getIdPublicacionLf() != null) {

                    ps.setInt(
                            3,
                            entidad.getIdPublicacionLf()
                    );

                } else {

                    ps.setNull(
                            3,
                            Types.INTEGER
                    );
                }

                if (entidad.getIdVendedor() != null) {

                    ps.setInt(
                            4,
                            entidad.getIdVendedor()
                    );

                } else {

                    ps.setNull(
                            4,
                            Types.INTEGER
                    );
                }

                ps.setString(
                        5,
                        entidad.getTipoOperacion()
                );

                ps.setDouble(
                        6,
                        entidad.getPrecio()
                );

                ps.setDouble(
                        7,
                        entidad.getGananciaLibriFlow()
                );

                ps.setDouble(
                        8,
                        entidad.getGananciaVendedor()
                );

                int filasAfectadas =
                        ps.executeUpdate();

                if (filasAfectadas == 0) {

                    con.rollback();

                    throw new SQLException(
                            "No se pudo insertar el detalle de la transacción."
                    );
                }

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (!rs.next()) {

                        con.rollback();

                        throw new SQLException(
                                "No se pudo obtener el ID del detalle insertado."
                        );
                    }

                    idDetalle =
                            rs.getInt(1);
                }
            }

            con.commit();

            return idDetalle;

        } catch (SQLException e) {

            if (con != null) {

                try {
                    con.rollback();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            System.err.println(
                    "Error al crear detalle de transacción: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return -1;

        } finally {

            if (con != null) {

                try {
                    con.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Obtiene todos los detalles de transacción registrados.
     *
     * @return Lista de objetos {@link DetalleTransaccion}. Si no existen
     *         registros, se devuelve una lista vacía.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<DetalleTransaccion> getAll() {

        List<DetalleTransaccion> detalles = new ArrayList<>();

        String sql = """
                SELECT
                    id_detalle,
                    id_transaccion,
                    id_publicacion_us,
                    id_publicacion_lf,
                    id_vendedor,
                    tipo_operacion,
                    precio,
                    ganancia_libriflow,
                    ganancia_vendedor
                FROM detalle_transaccion
                ORDER BY id_detalle
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                detalles.add(mapearDetalleTransaccion(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener los detalles de transacción:");
            e.printStackTrace();
        }

        return detalles;
    }

    /**
     * Obtiene un detalle de transacción mediante su identificador único.
     *
     * @param id Identificador del detalle que se desea consultar.
     * @return Objeto {@link DetalleTransaccion} encontrado o {@code null}
     *         si no existe el registro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public DetalleTransaccion getById(Integer id) {

        String sql = """
                SELECT
                    id_detalle,
                    id_transaccion,
                    id_publicacion_us,
                    id_publicacion_lf,
                    id_vendedor,
                    tipo_operacion,
                    precio,
                    ganancia_libriflow,
                    ganancia_vendedor
                FROM detalle_transaccion
                WHERE id_detalle = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearDetalleTransaccion(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el detalle de transacción:");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Actualiza los datos de un detalle de transacción existente.
     *
     * Este método realiza solamente la actualización CRUD del registro.
     * No ejecuta nuevamente la lógica de venta utilizada por {@link #create(DetalleTransaccion)}.
     *
     * @param entidad Objeto con el ID del detalle y los nuevos valores.
     * @return {@code true} si se actualizó correctamente o {@code false}
     *         si el registro no existe o ocurre un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean update(DetalleTransaccion entidad) {

        String sql = """
                UPDATE detalle_transaccion
                SET id_transaccion = ?,
                    id_publicacion_us = ?,
                    id_publicacion_lf = ?,
                    id_vendedor = ?,
                    tipo_operacion = ?,
                    precio = ?,
                    ganancia_libriflow = ?,
                    ganancia_vendedor = ?
                WHERE id_detalle = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entidad.getIdTransaccion());
            setIntegerNullable(ps, 2, entidad.getIdPublicacionUs());
            setIntegerNullable(ps, 3, entidad.getIdPublicacionLf());
            setIntegerNullable(ps, 4, entidad.getIdVendedor());
            ps.setString(5, entidad.getTipoOperacion());
            ps.setDouble(6, entidad.getPrecio());
            ps.setDouble(7, entidad.getGananciaLibriFlow());
            ps.setDouble(8, entidad.getGananciaVendedor());
            ps.setInt(9, entidad.getIdDetalle());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar el detalle de transacción:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un detalle de transacción mediante su identificador.
     *
     * @param id Identificador del detalle que se desea eliminar.
     * @return {@code true} si se eliminó correctamente o {@code false}
     *         si no existe o la integridad referencial impide la operación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean delete(Integer id) {

        String sql = """
                DELETE FROM detalle_transaccion
                WHERE id_detalle = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar el detalle de transacción:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Convierte la fila actual de un {@link ResultSet} en un objeto
     * {@link DetalleTransaccion}, conservando los valores nulos de las
     * relaciones opcionales.
     *
     * @param rs Resultado de la consulta posicionado en una fila válida.
     * @return Objeto construido con los valores de la fila.
     * @throws SQLException Si ocurre un error al leer los datos.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private DetalleTransaccion mapearDetalleTransaccion(ResultSet rs)
            throws SQLException {

        DetalleTransaccion detalle = new DetalleTransaccion();

        detalle.setIdDetalle(rs.getInt("id_detalle"));
        detalle.setIdTransaccion(rs.getInt("id_transaccion"));

        int idPublicacionUs = rs.getInt("id_publicacion_us");
        detalle.setIdPublicacionUs(
                rs.wasNull() ? null : idPublicacionUs
        );

        int idPublicacionLf = rs.getInt("id_publicacion_lf");
        detalle.setIdPublicacionLf(
                rs.wasNull() ? null : idPublicacionLf
        );

        int idVendedor = rs.getInt("id_vendedor");
        detalle.setIdVendedor(
                rs.wasNull() ? null : idVendedor
        );

        detalle.setTipoOperacion(
                rs.getString("tipo_operacion")
        );

        detalle.setPrecio(
                rs.getDouble("precio")
        );

        detalle.setGananciaLibriFlow(
                rs.getDouble("ganancia_libriflow")
        );

        detalle.setGananciaVendedor(
                rs.getDouble("ganancia_vendedor")
        );

        return detalle;
    }

    /**
     * Coloca un entero en un PreparedStatement y conserva {@code null}
     * cuando la relación opcional no tiene valor.
     *
     * @param ps PreparedStatement que recibirá el parámetro.
     * @param indice Posición del parámetro.
     * @param valor Valor entero o null.
     * @throws SQLException Si ocurre un error al establecer el parámetro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private void setIntegerNullable(
            PreparedStatement ps,
            int indice,
            Integer valor) throws SQLException {

        if (valor == null) {
            ps.setNull(indice, Types.INTEGER);
        } else {
            ps.setInt(indice, valor);
        }
    }

    /**
     *
     * Este método se encarga de obtener todos los movimientos relacionados
     * con un usuario. Consulta las compras, ventas y rentas realizadas con
     * publicaciones de usuarios y publicaciones de LibriFlow. Los movimientos
     * encontrados se ordenan desde el más reciente hasta el más antiguo.
     *
     * @param idUsuario Es el identificador del usuario del cual se desean
     *                  consultar los movimientos.
     * @return Una lista con los movimientos relacionados con el usuario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<Movimiento> getMovimientosByIdUsuario(int idUsuario) {

        List<Movimiento> movimientos =
                new ArrayList<>();

        String sql = """
                SELECT
                    CASE
                        WHEN dt.tipo_operacion = 'RENTA' THEN 'RENTA'
                        WHEN t.id_comprador = ? THEN 'COMPRA'
                        ELSE 'VENTA'
                    END AS tipo_movimiento,
                    t.fecha,
                    l.titulo,
                    dt.precio,
                    0 AS es_libriflow
                FROM detalle_transaccion dt
                JOIN transaccion t
                    ON dt.id_transaccion = t.id_transaccion
                JOIN publicacion_us pu
                    ON dt.id_publicacion_us = pu.id_publicacion_us
                JOIN libro l
                    ON pu.id_libro = l.id_libro
                WHERE t.id_comprador = ?
                   OR dt.id_vendedor = ?

                UNION ALL

                SELECT
                    CASE
                        WHEN dt.tipo_operacion = 'RENTA' THEN 'RENTA'
                        WHEN t.id_comprador = ? THEN 'COMPRA'
                        ELSE 'VENTA'
                    END AS tipo_movimiento,
                    t.fecha,
                    l.titulo,
                    dt.precio,
                    1 AS es_libriflow
                FROM detalle_transaccion dt
                JOIN transaccion t
                    ON dt.id_transaccion = t.id_transaccion
                JOIN publicacion_lf pl
                    ON dt.id_publicacion_lf = pl.id_publicacion_lf
                JOIN libro l
                    ON pl.id_libro = l.id_libro
                WHERE t.id_comprador = ?
                   OR dt.id_vendedor = ?

                ORDER BY fecha DESC
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);
            ps.setInt(3, idUsuario);

            ps.setInt(4, idUsuario);
            ps.setInt(5, idUsuario);
            ps.setInt(6, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Movimiento movimiento =
                            new Movimiento();

                    movimiento.setTipoMovimiento(
                            rs.getString("tipo_movimiento")
                    );

                    movimiento.setFecha(
                            rs.getTimestamp("fecha")
                    );

                    movimiento.setTitulo(
                            rs.getString("titulo")
                    );

                    movimiento.setPrecio(
                            rs.getDouble("precio")
                    );

                    movimiento.setEsLibriFlow(
                            rs.getInt("es_libriflow") == 1
                    );

                    movimientos.add(movimiento);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al obtener los movimientos del usuario:"
            );

            e.printStackTrace();
        }

        return movimientos;
    }

    /**
     *
     * Este método se encarga de obtener todos los movimientos utilizados
     * para consultar los ingresos generados dentro de LibriFlow. Incluye
     * movimientos relacionados con publicaciones de usuarios y publicaciones
     * administradas directamente por LibriFlow, obteniendo el comprador,
     * la fecha, el libro, el precio y la ganancia correspondiente.
     *
     * @return Una lista con todos los movimientos relacionados con los
     *         ingresos registrados en el sistema.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<Movimiento> getAllMovimientosIngresos() {

        List<Movimiento> movimientos = new ArrayList<>();

        String sql = """
        SELECT
            u.nombre AS comprador,
            t.fecha,
            l.titulo AS titulo,
            dt.precio AS total,
            dt.ganancia_libriflow AS ganancia_libriflow,
            0 AS es_libriflow
        FROM detalle_transaccion dt
        JOIN transaccion t
            ON dt.id_transaccion = t.id_transaccion
        JOIN usuario u
            ON t.id_comprador = u.id_usuario
        JOIN publicacion_us pu
            ON dt.id_publicacion_us = pu.id_publicacion_us
        JOIN libro l
            ON pu.id_libro = l.id_libro
        WHERE dt.id_publicacion_us IS NOT NULL

        UNION ALL

        SELECT
            u.nombre AS comprador,
            t.fecha,
            l.titulo AS titulo,
            dt.precio AS total,
            dt.precio AS ganancia_libriflow,
            1 AS es_libriflow
        FROM detalle_transaccion dt
        JOIN transaccion t
            ON dt.id_transaccion = t.id_transaccion
        JOIN usuario u
            ON t.id_comprador = u.id_usuario
        JOIN publicacion_lf pl
            ON dt.id_publicacion_lf = pl.id_publicacion_lf
        JOIN libro l
            ON pl.id_libro = l.id_libro
        WHERE dt.id_publicacion_lf IS NOT NULL

        ORDER BY fecha DESC
        """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Movimiento movimiento = new Movimiento();

                movimiento.setComprador(
                        rs.getString("comprador")
                );

                movimiento.setFecha(
                        rs.getTimestamp("fecha")
                );

                movimiento.setTitulo(
                        rs.getString("titulo")
                );

                movimiento.setPrecio(
                        rs.getDouble("total")
                );

                movimiento.setGanaciaLibriflow(
                        rs.getDouble("ganancia_libriflow")
                );

                // Convierte el 1 a true y el 0 a false
                movimiento.setEsLibriFlow(
                        rs.getInt("es_libriflow") == 1
                );

                movimientos.add(movimiento);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al obtener los ingresos globales:"
            );

            e.printStackTrace();
        }

        return movimientos;
    }
}