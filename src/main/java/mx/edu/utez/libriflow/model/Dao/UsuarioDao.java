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
                usuario.setEstado(rs.getString("Estado_Cuenta"));
                usuario.setFechaDesbloqueo(rs.getTimestamp("Fecha_Desbloqueo"));
                usuario.setFechaCreacion(rs.getTimestamp("Fecha_Creacion"));
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

        return -1;
    }
    public java.util.List<Usuario> getAll() {
        java.util.List<Usuario> lista = new java.util.ArrayList<>();
        String sql = "SELECT Id_Usuario, Nombre, Apellido_Paterno, Apellido_Materno, Correo_Electronico, Telefono, estado_cuenta FROM Usuario";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("Id_Usuario"));
                u.setNombre(rs.getString("Nombre"));
                u.setApellidoPaterno(rs.getString("Apellido_Paterno"));
                u.setApellidoMaterno(rs.getString("Apellido_Materno"));
                u.setCorreo(rs.getString("Correo_Electronico"));
                u.setTelefono(rs.getString("Telefono"));

                u.setEstado(rs.getString("estado_cuenta"));

                lista.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Usuario getById(Integer id) {
        String sql = "SELECT * FROM Usuario where Id_Usuario=(?)";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Usuario usuario = new Usuario();
            if(rs.next()){
                usuario.setId(rs.getInt("Id_Usuario"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setTelefono(rs.getString("Telefono"));
                usuario.setApellidoPaterno(rs.getString("Apellido_Paterno"));
                usuario.setApellidoMaterno(rs.getString("Apellido_Materno"));
                usuario.setCorreo(rs.getString("Correo_Electronico"));
                usuario.setEstado(rs.getString("Estado_Cuenta"));
                usuario.setFechaDesbloqueo(rs.getTimestamp("Fecha_Desbloqueo"));
                usuario.setFechaCreacion(rs.getTimestamp("Fecha_Creacion"));
            }
            return usuario;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
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

    public boolean actualizarContrasena(String correo, String nuevaContrasena) {

        String sql = "UPDATE credencial " +
                "SET contrasena = STANDARD_HASH(?, 'SHA256') " +
                "WHERE id_usuario = (SELECT id_usuario FROM usuario WHERE correo_electronico = ?)";
        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevaContrasena);
            ps.setString(2, correo);

            int filas = ps.executeUpdate();
            return (filas == 1);

        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public Usuario getDuenoPublicacionById(int idPublicacion) {
        String sql = """
            SELECT u.ID_USUARIO,
                   u.NOMBRE,
                   u.CORREO_ELECTRONICO
            FROM PUBLICACION_US pu, USUARIO u
            WHERE pu.ID_USUARIO = u.ID_USUARIO
              AND pu.ID_PUBLICACION_US = ?
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPublicacion);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("ID_USUARIO"));
                usuario.setNombre(rs.getString("NOMBRE"));
                usuario.setCorreo(rs.getString("CORREO_ELECTRONICO"));

                return usuario;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
    public boolean activarUsuario(int idUsuario) {

        String sql = """
            UPDATE USUARIO
            SET ESTADO_CUENTA = 'ACTIVA',
                FECHA_DESBLOQUEO = NULL
            WHERE ID_USUARIO = ?
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("ERROR AL ACTIVAR USUARIO");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean cambiarEstadoUsuario(int idUsuario, String estado) {

        String sql = "UPDATE usuario SET estado_cuenta = ? WHERE id_usuario = ?";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println("Error al cambiar el estado del usuario:");
            e.printStackTrace();
            return false;
        }
    }



    public boolean delete(Integer id) {
        return false;
    }
}
