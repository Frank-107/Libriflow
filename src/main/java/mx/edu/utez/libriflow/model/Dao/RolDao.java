package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Objeto de Acceso a Datos (DAO) para la gestión de roles de usuario en el sistema LibriFlow.
 * Proporciona métodos para asignar el rol por defecto a nuevos usuarios registrados
 * y para consultar el rol actual asignado a un usuario.
 *
 * @author Andres
 * @since 25/08/2026
 */
public class RolDao {

    /**
     * Asigna e inserta el rol por defecto {@code 'USUARIO'} a un usuario recién registrado en la base de datos.
     *
     * @param id Identificador único del usuario al que se le asignará el rol.
     * @return {@code true} si la inserción fue exitosa; {@code false} en caso de error o fallo en la sentencia SQL.
     */
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

    /**
     * Consulta el rol asignado a un usuario específico mediante su identificador.
     * En caso de no encontrar un registro asignado o surgir un error en la base de datos,
     * retorna el rol predeterminado {@code 'USUARIO'}.
     *
     * @param idUsuario Identificador único del usuario a consultar.
     * @return Cadena con el nombre del rol (ej. 'ADMIN', 'USUARIO').
     */
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