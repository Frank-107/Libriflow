package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.DetalleRenta;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DetalleRentaDao {


    public int create(DetalleRenta entidad) {
        String sql = "INSERT INTO Detalle_Renta " +
                "(ID_DETALLE_TRANSACCION, FECHA_INICIO, FECHA_LIMITE, ESTADO) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_DETALLE"}
             )) {
            ps.setInt(1,entidad.getIdDetalle());
            ps.setTimestamp(2, entidad.getFechaInicio());
            ps.setTimestamp(3, entidad.getFechaLimite());
            ps.setString(4, entidad.getEstado());

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

}
