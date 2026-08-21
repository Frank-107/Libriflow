package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Usuario;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * La clase CredencialDao se encarga de la persistencia y gestión de las
 * credenciales de acceso (contraseñas cifradas) de los usuarios en la base de datos.
 *
 * @author Francisco Emmanuel Fuentes Perez
 * @since 21/08/2026
 */
public class CredencialDao {

    /**
     * El método create sirve para registrar una nueva credencial de acceso en la BD.
     * Requiere el identificador del usuario y la contraseña cifrada.
     *
     * @author Francisco Emmanuel Fuentes Perez
     * @since 21/08/2026
     *
     * @param contrasena - Tipo: String, Contraseña cifrada que se asignará al usuario
     * @param id - Tipo: int, Identificador único del usuario
     * @return boolean - true si se insertó o false si no
     */
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

    /**
     * El método validarContrasena sirve para verificar si las credenciales coinciden en la BD.
     * Realiza la comparación con un hash SHA-256 sobre el registro del usuario.
     *
     * @author Francisco Emmanuel Fuentes Perez
     * @since 21/08/2026
     *
     * @param id_usuario - Tipo: int, Identificador único del usuario
     * @param contrasena - Tipo: String, Contraseña en texto plano a verificar
     * @return boolean - true si la contraseña es válida o false si no
     */
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

    /**
     * El método getAll sirve para obtener el listado completo de usuarios con credenciales en la BD.
     *
     * @author Francisco Emmanuel Fuentes Perez
     * @since 21/08/2026
     *
     * @return List - Lista con objetos Usuario o null si no se implementa
     */
    public java.util.List<Usuario> getAll() {
        return null;
    }

    /**
     * El método getById sirve para obtener las credenciales de un usuario mediante su identificador.
     *
     * @author Francisco Emmanuel Fuentes Perez
     * @since 21/08/2026
     *
     * @param id - Tipo: Integer, Identificador del usuario a consultar
     * @return Usuario - Objeto Usuario encontrado o null si no existe
     */
    public Usuario getById(Integer id) {
        return null;
    }

    /**
     * El método updateCredencial sirve para actualizar el hash de la contraseña de un usuario en la BD.
     *
     * @author Alejandro Mena Pereyda
     * @since 21/08/2026
     *
     * @param entidad - Tipo: Usuario, Entidad que contiene el id y el nuevo hash de contraseña
     * @return boolean - true si se actualizó correctamente o false si no
     */
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

    /**
     * El método delete sirve para eliminar el registro de credencial asociado a un identificador en la BD.
     *
     * @author Francisco Emmanuel Fuentes Perez
     * @since 21/08/2026
     *
     * @param id - Tipo: Integer, Identificador de la credencial a eliminar
     * @return boolean - true si se eliminó o false si no
     */
    public boolean delete(Integer id) {
        return false;
    }
}
