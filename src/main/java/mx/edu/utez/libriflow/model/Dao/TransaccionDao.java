package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Transaccion;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransaccionDao {
    public int create(Transaccion entidad) {
        String sql = "INSERT INTO Transaccion(id_comprador, subtotal, costo_envio, total, estado) VALUES(?, ?, ?, ?, ?)";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_TRANSACCION"}
             )) {

            ps.setInt(1, entidad.getIdComprador());
            ps.setDouble(2, entidad.getSubtotal());
            ps.setDouble(3, entidad.getCostoEnvio());
            ps.setDouble(4, entidad.getTotal());
            ps.setString(5, entidad.getEstado());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException("No se pudo insertar la transacción.");
            }

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new SQLException("No se pudo obtener el ID de la transacción.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }



}
