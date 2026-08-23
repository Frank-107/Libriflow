package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.DetalleRenta;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Esta clase se encarga de realizar las operaciones relacionadas con los
 * detalles de las rentas dentro de la base de datos. Permite registrar rentas,
 * consultar rentas activas o retrasadas, modificar penalizaciones, suspender
 * usuarios y obtener al usuario relacionado con una renta.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 22/08/2026
 */
public class DetalleRentaDao {


    /**
     *
     * Este método se encarga de registrar un nuevo detalle de renta en la
     * base de datos. Almacena la transacción relacionada, las fechas de inicio
     * y límite, el estado y el código de la renta. Si el registro se realiza
     * correctamente, obtiene y devuelve el identificador generado.
     *
     * @param entidad Es el objeto DetalleRenta que contiene la información
     *                que se desea registrar.
     * @return El identificador generado para el detalle de renta o -1 si
     *         ocurre un error durante el registro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public int create(DetalleRenta entidad) {
        String sql = "INSERT INTO Detalle_Renta " +
                "(ID_DETALLE_TRANSACCION, FECHA_INICIO, FECHA_LIMITE, ESTADO, CODIGO) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_DETALLE"}
             )) {
            ps.setInt(1,entidad.getIdDetalle());
            ps.setTimestamp(2, entidad.getFechaInicio());
            ps.setTimestamp(3, entidad.getFechaLimite());
            ps.setString(4, entidad.getEstado());
            ps.setString(5, entidad.getCodigo());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException("No se pudo insertar el detalle de renta.");
            }

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new SQLException("No se pudo obtener el ID del detalle de renta.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     *
     * Este método se encarga de consultar las rentas que se encuentran activas
     * y que no presentan ninguna penalización. Obtiene la información de cada
     * renta encontrada y la almacena en una lista.
     *
     * @return Una lista con las rentas activas que no tienen penalización.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public List<DetalleRenta> getRentasActivas() {
        String sql = "SELECT * FROM DETALLE_RENTA WHERE ESTADO = ? and penalizacion=?";

        List<DetalleRenta> rentas = new ArrayList<>();

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "ACTIVA");
            ps.setInt(2, 0);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DetalleRenta renta = new DetalleRenta();

                renta.setIdDetalle(rs.getInt("ID_DETALLE"));
                renta.setFechaInicio(rs.getTimestamp("FECHA_INICIO"));
                renta.setFechaLimite(rs.getTimestamp("FECHA_LIMITE"));
                renta.setFechaDevolucion(rs.getTimestamp("FECHA_DEVOLUCION"));
                renta.setEstado(rs.getString("ESTADO"));
                renta.setPenalizacion(rs.getInt("PENALIZACION"));

                rentas.add(renta);
            }

        } catch (SQLException e) {
            System.out.println("ERROR AL OBTENER RENTAS ACTIVAS");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return rentas;
    }

    /**
     *
     * Este método se encarga de consultar las rentas que continúan activas
     * pero que presentan una penalización por retraso. La información obtenida
     * de cada renta se almacena en una lista.
     *
     * @return Una lista con las rentas activas que presentan retraso.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public List<DetalleRenta> getRentasRetrasadasActivas() {
        String sql = """
            SELECT *
            FROM DETALLE_RENTA
            WHERE ESTADO = ?
            AND Penalizacion = ?
            """;

        List<DetalleRenta> rentas = new ArrayList<>();

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "ACTIVA");
            ps.setInt(2, 1);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DetalleRenta renta = new DetalleRenta();

                renta.setIdDetalle(rs.getInt("ID_DETALLE"));
                renta.setFechaInicio(rs.getTimestamp("FECHA_INICIO"));
                renta.setFechaLimite(rs.getTimestamp("FECHA_LIMITE"));
                renta.setFechaDevolucion(rs.getTimestamp("FECHA_DEVOLUCION"));
                renta.setEstado(rs.getString("ESTADO"));
                renta.setPenalizacion(rs.getInt("PENALIZACION"));

                rentas.add(renta);
            }

        } catch (SQLException e) {
            System.out.println("ERROR AL OBTENER RENTAS RETRASADAS");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return rentas;
    }

    /**
     *
     * Este método se encarga de modificar el nivel de penalización de una
     * renta específica mediante su identificador.
     *
     * @param idDetalle Es el identificador del detalle de renta que se desea
     *                  actualizar.
     * @param penalizacion Es el nuevo valor de penalización que se desea asignar.
     * @return true si la penalización fue actualizada correctamente o false
     *         si no se realizó la modificación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public boolean cambiarPenalizacion(int idDetalle, int penalizacion) {
        String sql = "UPDATE DETALLE_RENTA SET PENALIZACION = ? WHERE ID_DETALLE = ?";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, penalizacion);
            ps.setInt(2, idDetalle);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("ERROR AL CAMBIAR PENALIZACION");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * Este método se encarga de suspender temporalmente la cuenta de un usuario.
     * Cambia el estado de la cuenta a INACTIVA y registra la fecha en la que
     * podrá ser desbloqueada nuevamente.
     *
     * @param idUsuario Es el identificador del usuario que se desea suspender.
     * @param fechaDesbloqueo Es la fecha en la que finalizará la suspensión
     *                        del usuario.
     * @return true si el usuario fue suspendido correctamente o false si
     *         ocurrió un error durante la actualización.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public boolean suspenderUsuario(int idUsuario, Timestamp fechaDesbloqueo) {

        String sql = """
            UPDATE USUARIO
            SET ESTADO_CUENTA = 'INACTIVA',
                FECHA_DESBLOQUEO = ?
            WHERE ID_USUARIO = ?
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, fechaDesbloqueo);
            ps.setInt(2, idUsuario);

            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("ERROR AL BLOQUEAR USUARIO");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    /**
     *
     * Este método se encarga de obtener el identificador del usuario que
     * realizó una renta. Para encontrarlo relaciona el detalle de renta con
     * el detalle de la transacción y la transacción correspondiente.
     *
     * @param idDetalleRenta Es el identificador del detalle de renta del cual
     *                       se desea obtener al usuario.
     * @return El identificador del usuario relacionado con la renta o -1 si
     *         no se encuentra el usuario o ocurre un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public int getIdUsuarioByIdRenta(int idDetalleRenta) {

        String sql = """
            SELECT t.ID_COMPRADOR
            FROM DETALLE_RENTA dr
            JOIN DETALLE_TRANSACCION dt
                ON dr.ID_DETALLE_TRANSACCION = dt.ID_DETALLE
            JOIN TRANSACCION t
                ON dt.ID_TRANSACCION = t.ID_TRANSACCION
            WHERE dr.ID_DETALLE = ?
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDetalleRenta);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("ID_COMPRADOR");
            }

            return -1;

        } catch (SQLException e) {
            System.out.println("ERROR AL OBTENER ID DEL USUARIO POR RENTA");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
}