package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Transaccion;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase DAO (Data Access Object) encargada de gestionar la persistencia y
 * operaciones de acceso a datos relacionadas con las transacciones en la base de datos.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 */
public class TransaccionDao {

    /**
     * Inserta un nuevo registro de transacción en la base de datos y recupera la llave primaria generada.
     *
     * @param entidad Objeto {@link Transaccion} que contiene la información de la compra (comprador, montos y estado).
     * @return El identificador único (ID) generado para la transacción, o {@code -1} si ocurre un error en la inserción.
     * @author Francisco Emmanuel Fuentes Pérez
     */
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