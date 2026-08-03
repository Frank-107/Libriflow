package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RolDao {

    public boolean create(int id) {

        String sql = "INSERT INTO Rol(id_usuario, rol) VALUES(?, 'USUARIO')";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String obtenerRol(int idUsuario) {

        String sql = "SELECT ROL FROM ROL WHERE ID_USUARIO = ?";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("ROL");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "USUARIO";
    }
}