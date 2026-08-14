package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.DetalleTransaccion;
import mx.edu.utez.libriflow.model.Movimiento;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleTransaccionDao {

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
                            java.sql.Types.INTEGER
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
                            java.sql.Types.INTEGER
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
                            java.sql.Types.INTEGER
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

        -- 2. PUBLICACIONES DE LIBRIFLOW (publicacion_lf)
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