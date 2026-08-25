package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Usuario;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.*;

/**
 * Clase DAO (Data Access Object) encargada de gestionar la persistencia y
 * consultas de la entidad Usuario en la base de datos.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 */
public class UsuarioDao {

    /**
     * Inserta un nuevo registro de usuario en la base de datos con el estado de cuenta "ACTIVA" por defecto.
     *
     * @param entidad Objeto {@link Usuario} que contiene los datos del usuario a registrar.
     * @return El identificador único (ID) generado para el usuario, o {@code -1} si ocurre un error.
     * @author Francisco Emmanuel Fuentes Pérez
     */
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

    /**
     * Verifica si un correo electrónico ya se encuentra registrado en la base de datos.
     *
     * @param correo Dirección de correo electrónico a consultar.
     * @return {@code true} si el correo existe; {@code false} en caso contrario.
     * @author Francisco Emmanuel Fuentes Pérez
     */
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

    /**
     * Obtiene la información completa de un usuario a partir de su correo electrónico.
     *
     * @param correo Dirección de correo electrónico del usuario.
     * @return Objeto {@link Usuario} poblado con los datos correspondientes, o {@code null} si ocurre una excepción.
     * @author Francisco Emmanuel Fuentes Pérez
     */
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

    /**
     * Consulta únicamente el identificador (ID) de un usuario buscando por su correo electrónico.
     *
     * @param correo Dirección de correo electrónico del usuario.
     * @return El identificador del usuario, o {@code -1} si no se encuentra o hay un error.
     * @author Francisco Emmanuel Fuentes Pérez
     */
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

    /**
     * Obtiene una lista con todos los usuarios registrados en el sistema.
     *
     * @return Lista de objetos {@link Usuario}.
     * @author Francisco Emmanuel Fuentes Pérez
     */
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

    /**
     * Busca y obtiene la información de un usuario según su identificador único (ID).
     *
     * @param id Identificador numérico del usuario.
     * @return Objeto {@link Usuario} poblado, o {@code null} en caso de error.
     * @author Francisco Emmanuel Fuentes Pérez
     */
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

    /**
     * Actualiza la información personal de un usuario (nombre, apellidos y teléfono).
     *
     * @param entidad Objeto {@link Usuario} que contiene los datos actualizados y el ID del usuario.
     * @return {@code true} si la actualización fue exitosa; {@code false} en caso contrario.
     * @author Francisco Emmanuel Fuentes Pérez
     */
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

    /**
     * Actualiza la contraseña asociada a un usuario en la tabla de credenciales aplicando un hash SHA-256.
     *
     * @param correo          Correo electrónico del usuario al que se le cambiará la contraseña.
     * @param nuevaContrasena Nueva contraseña en texto plano para ser cifrada.
     * @return {@code true} si la contraseña fue modificada correctamente; {@code false} en caso de error.
     * @author Francisco Emmanuel Fuentes Pérez
     */
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

    /**
     * Recupera los datos básicos del usuario propietario de una publicación específica.
     *
     * @param idPublicacion Identificador único de la publicación de usuario.
     * @return Objeto {@link Usuario} con ID, nombre y correo; o {@code null} si no existe o hay un error.
     * @author Francisco Emmanuel Fuentes Pérez
     */
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

    /**
     * Cambia el estado de la cuenta de un usuario a "ACTIVA" y reinicia la fecha de desbloqueo a NULL.
     *
     * @param idUsuario Identificador único del usuario.
     * @return {@code true} si se actualizó el estado correctamente; {@code false} en caso contrario.
     * @author Francisco Emmanuel Fuentes Pérez
     */
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

    /**
     * Modifica el estado de la cuenta de un usuario por el estado proporcionado.
     *
     * @param idUsuario Identificador único del usuario.
     * @param estado    Nuevo estado a asignar a la cuenta.
     * @return {@code true} si el estado fue actualizado; {@code false} en caso contrario.
     * @author Francisco Emmanuel Fuentes Pérez
     */
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

    /**
     * Elimina un usuario según su identificador único (Método no implementado).
     *
     * @param id Identificador numérico del usuario.
     * @return {@code false} por defecto.
     * @author Francisco Emmanuel Fuentes Pérez
     */
    public boolean delete(Integer id) {
        return false;
    }
}