package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Usuario;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class UsuarioDao implements Dao<Usuario, Integer> {
    @Override
    public boolean create(Usuario entidad) {
        String sql = "INSERT INTO Usuario(nombre, apellido_paterno, apellido_materno, correo_electronico, telefono, estado_cuenta) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellidoPaterno());
            ps.setString(3, entidad.getApellidoMaterno());
            ps.setString(4, entidad.getCorreo());
            ps.setString(5, entidad.getTelefono());
            ps.setString(6, "activo"); // Estado de la cuenta por defecto

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public java.util.List<Usuario> getAll() {
        return null;
    }

    @Override
    public Usuario getById(Integer id) {
        return null;
    }

    @Override
    public boolean update(Usuario entidad) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
