package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Usuario;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.*;


public class UsuarioDao {
    public int create(Usuario entidad) {
        String sql = "INSERT INTO Usuario(nombre, apellido_paterno, apellido_materno, correo_electronico, telefono, estado_cuenta) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_USUARIO"}
             );) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellidoPaterno());
            ps.setString(3, entidad.getApellidoMaterno());
            ps.setString(4, entidad.getCorreo());
            ps.setString(5, entidad.getTelefono());
            ps.setString(6, "ACTIVA"); // Estado de la cuenta por defecto

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) throw new SQLException("No se pudo insertar el usuario.");
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("No se pudo obtener el ID del usuario insertado.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }


    public java.util.List<Usuario> getAll() {
        return null;
    }

    public Usuario getById(Integer id) {
        return null;
    }

    public boolean update(Usuario entidad) {
        return false;
    }

    public boolean delete(Integer id) {
        return false;
    }
}
