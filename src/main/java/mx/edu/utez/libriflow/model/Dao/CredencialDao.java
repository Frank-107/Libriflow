package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Usuario;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CredencialDao {
    public boolean create(String contrasena, int id) {
        String sql = "INSERT INTO Credencial(id_usuario, contrasena) VALUES(?,?)";
        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, contrasena);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean validarContrasena(int id_usuario, String contrasena){
        String sql = "SELECT * from credencial where id_usuario=? and contrasena=STANDARD_HASH(?,'SHA256')";
        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_usuario);
            ps.setString(2, contrasena);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
    }}


    public java.util.List<Usuario> getAll() {
        return null;
    }

    public Usuario getById(Integer id) {
        return null;
    }

    public boolean updateCredencial(Usuario entidad) {
        String sql = "UPDATE Credencial SET contrasena = ? WHERE id_usuario = ?";
        try(Connection con = SQLconnector.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, entidad.getContrasenaHash());
            ps.setInt(2, entidad.getId());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Integer id) {
        return false;
    }
}
