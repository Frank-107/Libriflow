package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.DetalleRenta;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleRentaDao {


    public int create(DetalleRenta entidad) {
        String sql = "INSERT INTO Detalle_Renta " +
                "(ID_DETALLE_TRANSACCION, FECHA_INICIO, FECHA_LIMITE, ESTADO, CODIGO) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_DETALLE"}
             )) {
            ps.setInt(1,entidad.getIdDetalle());
            ps.setTimestamp(2, entidad.getFechaInicio());
            ps.setTimestamp(3, entidad.getFechaLimite());
            ps.setString(4, entidad.getEstado());
            ps.setString(5, entidad.getCodigo());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException("No se pudo insertar el detalle de renta.");
            }

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new SQLException("No se pudo obtener el ID del detalle de renta.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public List<DetalleRenta> getRentasActivas() {
        String sql = "SELECT * FROM DETALLE_RENTA WHERE ESTADO = ? and penalizacion=?";

        List<DetalleRenta> rentas = new ArrayList<>();

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "ACTIVA");
            ps.setInt(2, 0);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DetalleRenta renta = new DetalleRenta();

                renta.setIdDetalle(rs.getInt("ID_DETALLE"));
                renta.setFechaInicio(rs.getTimestamp("FECHA_INICIO"));
                renta.setFechaLimite(rs.getTimestamp("FECHA_LIMITE"));
                renta.setFechaDevolucion(rs.getTimestamp("FECHA_DEVOLUCION"));
                renta.setEstado(rs.getString("ESTADO"));
                renta.setPenalizacion(rs.getInt("PENALIZACION"));

                rentas.add(renta);
            }

        } catch (SQLException e) {
            System.out.println("ERROR AL OBTENER RENTAS ACTIVAS");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return rentas;
    }

    public List<DetalleRenta> getRentasRetrasadasActivas() {
        String sql = """
            SELECT *
            FROM DETALLE_RENTA
            WHERE ESTADO = ?
            AND Penalizacion = ?
            """;

        List<DetalleRenta> rentas = new ArrayList<>();

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "ACTIVA");
            ps.setInt(2, 1);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DetalleRenta renta = new DetalleRenta();

                renta.setIdDetalle(rs.getInt("ID_DETALLE"));
                renta.setFechaInicio(rs.getTimestamp("FECHA_INICIO"));
                renta.setFechaLimite(rs.getTimestamp("FECHA_LIMITE"));
                renta.setFechaDevolucion(rs.getTimestamp("FECHA_DEVOLUCION"));
                renta.setEstado(rs.getString("ESTADO"));
                renta.setPenalizacion(rs.getInt("PENALIZACION"));

                rentas.add(renta);
            }

        } catch (SQLException e) {
            System.out.println("ERROR AL OBTENER RENTAS RETRASADAS");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return rentas;
    }

    public boolean cambiarPenalizacion(int idDetalle, int penalizacion) {
        String sql = "UPDATE DETALLE_RENTA SET PENALIZACION = ? WHERE ID_DETALLE = ?";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, penalizacion);
            ps.setInt(2, idDetalle);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("ERROR AL CAMBIAR PENALIZACION");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean suspenderUsuario(int idUsuario, Timestamp fechaDesbloqueo) {

        String sql = """
            UPDATE USUARIO
            SET ESTADO_CUENTA = 'INACTIVA',
                FECHA_DESBLOQUEO = ?
            WHERE ID_USUARIO = ?
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, fechaDesbloqueo);
            ps.setInt(2, idUsuario);

            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("ERROR AL BLOQUEAR USUARIO");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public int getIdUsuarioByIdRenta(int idDetalleRenta) {

        String sql = """
            SELECT t.ID_COMPRADOR
            FROM DETALLE_RENTA dr
            JOIN DETALLE_TRANSACCION dt
                ON dr.ID_DETALLE_TRANSACCION = dt.ID_DETALLE
            JOIN TRANSACCION t
                ON dt.ID_TRANSACCION = t.ID_TRANSACCION
            WHERE dr.ID_DETALLE = ?
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDetalleRenta);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("ID_COMPRADOR");
            }

            return -1;

        } catch (SQLException e) {
            System.out.println("ERROR AL OBTENER ID DEL USUARIO POR RENTA");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
}
