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
    public boolean correoExistente (String correo) {
        String sql = "SELECT * from Usuario where correo_electronico = ?";
        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql);) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false;


    }
    public Usuario obtenerUsuario(String correo){
        String sql = "SELECT * FROM Usuario where correo_electronico=(?)";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            Usuario usuario = new Usuario();
            if(rs.next()){
                usuario.setId(rs.getInt("Id_Usuario"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setTelefono(rs.getString("Telefono"));
                usuario.setApellidoPaterno(rs.getString("Apellido_Paterno"));
                usuario.setApellidoMaterno(rs.getString("Apellido_Materno"));
                usuario.setCorreo(rs.getString("Correo_Electronico"));
            }
            return usuario;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public int getIdUsuario(String correo) {
        String sql = "SELECT Id_Usuario FROM Usuario WHERE correo_electronico = ?";
        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("Id_Usuario");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return -1; // Retorna -1 si no se encuentra el usuario
    }


    public java.util.List<Usuario> getAll() {
        return null;
    }

    public Usuario getById(Integer id) {
        return null;
    }

    public boolean update(Usuario entidad) {
        String sql = "UPDATE Usuario SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, telefono = ? WHERE Id_Usuario = ?";
        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellidoPaterno());
            ps.setString(3, entidad.getApellidoMaterno());
            ps.setString(4, entidad.getTelefono());
            ps.setInt(5, entidad.getId());

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
