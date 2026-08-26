package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Transaccion;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) encargada de gestionar la persistencia
 * y las operaciones CRUD relacionadas con las transacciones de LibriFlow.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
public class TransaccionDao {

    /**
     * Inserta un nuevo registro de transacción en la base de datos
     * y recupera la llave primaria generada.
     *
     * @param entidad Objeto {@link Transaccion} que contiene la información
     *                del comprador, montos y estado.
     * @return El identificador generado para la transacción, o {@code -1}
     *         si ocurre un error durante la inserción.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public int create(Transaccion entidad) {

        String sql = """
                INSERT INTO transaccion(
                    id_comprador,
                    subtotal,
                    costo_envio,
                    total,
                    estado
                )
                VALUES (?, ?, ?, ?, ?)
                """;

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
                throw new SQLException(
                        "No se pudo insertar la transacción."
                );
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new SQLException(
                    "No se pudo obtener el ID de la transacción."
            );

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Obtiene todas las transacciones registradas en la base de datos.
     *
     * La columna de fecha no se incluye en el modelo {@link Transaccion},
     * por lo que este método recupera únicamente los atributos definidos
     * actualmente en dicha clase.
     *
     * @return Lista con las transacciones encontradas. Si no existen registros,
     *         se devuelve una lista vacía.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<Transaccion> getAll() {

        List<Transaccion> transacciones =
                new ArrayList<>();

        String sql = """
                SELECT
                    id_transaccion,
                    id_comprador,
                    subtotal,
                    costo_envio,
                    total,
                    estado
                FROM transaccion
                ORDER BY id_transaccion
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                transacciones.add(
                        mapearTransaccion(rs)
                );
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return transacciones;
    }

    /**
     * Obtiene una transacción mediante su identificador único.
     *
     * @param id Identificador de la transacción que se desea consultar.
     * @return Objeto {@link Transaccion} encontrado o {@code null}
     *         si no existe un registro con dicho identificador.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public Transaccion getById(Integer id) {

        String sql = """
                SELECT
                    id_transaccion,
                    id_comprador,
                    subtotal,
                    costo_envio,
                    total,
                    estado
                FROM transaccion
                WHERE id_transaccion = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearTransaccion(rs);
                }
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Actualiza los datos principales de una transacción existente.
     *
     * La fecha original de la transacción se conserva, ya que no forma
     * parte del modelo {@link Transaccion} actual.
     *
     * @param entidad Objeto {@link Transaccion} con el identificador
     *                y los nuevos valores que se desean almacenar.
     * @return {@code true} si la transacción fue actualizada correctamente;
     *         {@code false} si no existe o si ocurre un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean update(Transaccion entidad) {

        String sql = """
                UPDATE transaccion
                SET id_comprador = ?,
                    subtotal = ?,
                    costo_envio = ?,
                    total = ?,
                    estado = ?
                WHERE id_transaccion = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entidad.getIdComprador());
            ps.setDouble(2, entidad.getSubtotal());
            ps.setDouble(3, entidad.getCostoEnvio());
            ps.setDouble(4, entidad.getTotal());
            ps.setString(5, entidad.getEstado());
            ps.setInt(6, entidad.getIdTransaccion());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una transacción utilizando su identificador único.
     *
     * Este método elimina únicamente el registro de la tabla TRANSACCION.
     * Las reglas de integridad referencial de la base de datos impedirán
     * eliminarla si existen registros dependientes que no permitan la operación.
     *
     * @param id Identificador de la transacción que se desea eliminar.
     * @return {@code true} si la transacción fue eliminada correctamente;
     *         {@code false} si no existe o si ocurre un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean delete(Integer id) {

        String sql = """
                DELETE FROM transaccion
                WHERE id_transaccion = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Convierte la fila actual de un {@link ResultSet}
     * en un objeto {@link Transaccion}.
     *
     * @param rs Resultado de la consulta posicionado en una fila válida.
     * @return Objeto {@link Transaccion} construido con los datos recuperados.
     * @throws SQLException Si ocurre un error al leer alguna columna.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private Transaccion mapearTransaccion(
            ResultSet rs) throws SQLException {

        Transaccion transaccion =
                new Transaccion();

        transaccion.setIdTransaccion(
                rs.getInt("id_transaccion")
        );

        transaccion.setIdComprador(
                rs.getInt("id_comprador")
        );

        transaccion.setSubtotal(
                rs.getDouble("subtotal")
        );

        transaccion.setCostoEnvio(
                rs.getDouble("costo_envio")
        );

        transaccion.setTotal(
                rs.getDouble("total")
        );

        transaccion.setEstado(
                rs.getString("estado")
        );

        return transaccion;
    }
}