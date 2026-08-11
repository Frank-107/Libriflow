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
        String sql = """
        INSERT INTO Detalle_Transaccion
        (ID_TRANSACCION,
         ID_PUBLICACION_US,
         ID_PUBLICACION_LF,
         ID_VENDEDOR,
         TIPO_OPERACION,
         PRECIO,
         GANANCIA_LIBRIFLOW,
         GANANCIA_VENDEDOR)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_DETALLE"}
             )) {

            ps.setInt(1, entidad.getIdTransaccion());

            if (entidad.getIdPublicacionUs() != null) {
                ps.setInt(2, entidad.getIdPublicacionUs());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }

            if (entidad.getIdPublicacionLf() != null) {
                ps.setInt(3, entidad.getIdPublicacionLf());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            if(entidad.getIdVendedor() != null) {
                ps.setInt(4, entidad.getIdVendedor());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            ps.setString(5, entidad.getTipoOperacion());
            ps.setDouble(6, entidad.getPrecio());
            ps.setDouble(7, entidad.getGananciaLibriFlow());
            ps.setDouble(8, entidad.getGananciaVendedor());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException("No se pudo insertar el detalle de la transacción.");
            }

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new SQLException("No se pudo obtener el ID del detalle insertado.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }


    public List<Movimiento> getMovimientosByIdUsuario(int idUsuario) {

        List<Movimiento> movimientos = new ArrayList<>();

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

            // Primera consulta
            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);
            ps.setInt(3, idUsuario);

            // Segunda consulta
            ps.setInt(4, idUsuario);
            ps.setInt(5, idUsuario);
            ps.setInt(6, idUsuario);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Movimiento movimiento = new Movimiento();

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

        } catch (SQLException e) {
            System.err.println("Error al obtener los movimientos del usuario:");
            e.printStackTrace();
        }

        return movimientos;
    }
}
