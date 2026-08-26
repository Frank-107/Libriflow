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
     * Para conservar compatibilidad con el flujo actual de LibriFlow, primero
     * utiliza idDetalleTransaccion y, si no fue establecido, utiliza idDetalle
     * como identificador del detalle de transacción.
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

        String sql = """
                INSERT INTO detalle_renta(
                    id_detalle_transaccion,
                    fecha_inicio,
                    fecha_limite,
                    estado,
                    codigo,
                    penalizacion
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        int idDetalleTransaccion =
                entidad.getIdDetalleTransaccion() > 0
                        ? entidad.getIdDetalleTransaccion()
                        : entidad.getIdDetalle();

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_DETALLE"}
             )) {

            ps.setInt(1, idDetalleTransaccion);
            ps.setTimestamp(2, entidad.getFechaInicio());
            ps.setTimestamp(3, entidad.getFechaLimite());
            ps.setString(4, entidad.getEstado());
            ps.setString(5, entidad.getCodigo());
            ps.setInt(6, entidad.getPenalizacion());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException(
                        "No se pudo insertar el detalle de renta."
                );
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new SQLException(
                    "No se pudo obtener el ID del detalle de renta."
            );

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Obtiene todos los detalles de renta registrados.
     *
     * @return Lista con todos los detalles de renta almacenados.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<DetalleRenta> getAll() {

        List<DetalleRenta> rentas =
                new ArrayList<>();

        String sql = """
                SELECT
                    id_detalle,
                    id_detalle_transaccion,
                    fecha_inicio,
                    fecha_limite,
                    fecha_devolucion,
                    estado,
                    codigo,
                    penalizacion
                FROM detalle_renta
                ORDER BY id_detalle
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rentas.add(mapearDetalleRenta(rs));
            }

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL OBTENER TODOS LOS DETALLES DE RENTA"
            );
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return rentas;
    }

    /**
     * Obtiene un detalle de renta por su identificador.
     *
     * @param id Identificador del detalle de renta.
     * @return El detalle encontrado o null si no existe.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public DetalleRenta getById(Integer id) {

        String sql = """
                SELECT
                    id_detalle,
                    id_detalle_transaccion,
                    fecha_inicio,
                    fecha_limite,
                    fecha_devolucion,
                    estado,
                    codigo,
                    penalizacion
                FROM detalle_renta
                WHERE id_detalle = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearDetalleRenta(rs);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL OBTENER DETALLE DE RENTA POR ID"
            );
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Actualiza la información de un detalle de renta existente.
     *
     * @param entidad Detalle de renta con los nuevos valores.
     * @return true si el registro fue actualizado o false si no existe.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean update(DetalleRenta entidad) {

        String sql = """
                UPDATE detalle_renta
                SET id_detalle_transaccion = ?,
                    fecha_inicio = ?,
                    fecha_limite = ?,
                    fecha_devolucion = ?,
                    estado = ?,
                    codigo = ?,
                    penalizacion = ?
                WHERE id_detalle = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    entidad.getIdDetalleTransaccion()
            );

            ps.setTimestamp(
                    2,
                    entidad.getFechaInicio()
            );

            ps.setTimestamp(
                    3,
                    entidad.getFechaLimite()
            );

            if (entidad.getFechaDevolucion() == null) {
                ps.setNull(
                        4,
                        Types.TIMESTAMP
                );
            } else {
                ps.setTimestamp(
                        4,
                        entidad.getFechaDevolucion()
                );
            }

            ps.setString(
                    5,
                    entidad.getEstado()
            );

            ps.setString(
                    6,
                    entidad.getCodigo()
            );

            ps.setInt(
                    7,
                    entidad.getPenalizacion()
            );

            ps.setInt(
                    8,
                    entidad.getIdDetalle()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL ACTUALIZAR DETALLE DE RENTA"
            );
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un detalle de renta por su identificador.
     *
     * @param id Identificador del detalle a eliminar.
     * @return true si fue eliminado o false si no existe.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean delete(Integer id) {

        String sql = """
                DELETE FROM detalle_renta
                WHERE id_detalle = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL ELIMINAR DETALLE DE RENTA"
            );
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
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

        String sql = """
                SELECT *
                FROM detalle_renta
                WHERE estado = ?
                  AND penalizacion = ?
                """;

        List<DetalleRenta> rentas =
                new ArrayList<>();

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(
                    1,
                    "ACTIVA"
            );

            ps.setInt(
                    2,
                    0
            );

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    rentas.add(
                            mapearDetalleRenta(rs)
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL OBTENER RENTAS ACTIVAS"
            );
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
                FROM detalle_renta
                WHERE estado = ?
                  AND penalizacion = ?
                """;

        List<DetalleRenta> rentas =
                new ArrayList<>();

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(
                    1,
                    "ACTIVA"
            );

            ps.setInt(
                    2,
                    1
            );

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    rentas.add(
                            mapearDetalleRenta(rs)
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL OBTENER RENTAS RETRASADAS"
            );
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
    public boolean cambiarPenalizacion(
            int idDetalle,
            int penalizacion) {

        String sql = """
                UPDATE detalle_renta
                SET penalizacion = ?
                WHERE id_detalle = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    penalizacion
            );

            ps.setInt(
                    2,
                    idDetalle
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL CAMBIAR PENALIZACION"
            );
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
    public boolean suspenderUsuario(
            int idUsuario,
            Timestamp fechaDesbloqueo) {

        String sql = """
                UPDATE usuario
                SET estado_cuenta = 'INACTIVA',
                    fecha_desbloqueo = ?
                WHERE id_usuario = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(
                    1,
                    fechaDesbloqueo
            );

            ps.setInt(
                    2,
                    idUsuario
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL BLOQUEAR USUARIO"
            );
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
    public int getIdUsuarioByIdRenta(
            int idDetalleRenta) {

        String sql = """
                SELECT t.id_comprador
                FROM detalle_renta dr
                JOIN detalle_transaccion dt
                    ON dr.id_detalle_transaccion = dt.id_detalle
                JOIN transaccion t
                    ON dt.id_transaccion = t.id_transaccion
                WHERE dr.id_detalle = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idDetalleRenta
            );

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(
                            "ID_COMPRADOR"
                    );
                }
            }

            return -1;

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL OBTENER ID DEL USUARIO POR RENTA"
            );
            System.out.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Convierte una fila del ResultSet en un objeto DetalleRenta.
     *
     * @param rs Resultado de consulta posicionado en una fila válida.
     * @return Objeto DetalleRenta construido con los valores de la fila.
     * @throws SQLException Si ocurre un error al leer los datos.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private DetalleRenta mapearDetalleRenta(
            ResultSet rs) throws SQLException {

        DetalleRenta renta =
                new DetalleRenta();

        renta.setIdDetalle(
                rs.getInt("ID_DETALLE")
        );

        renta.setIdDetalleTransaccion(
                rs.getInt("ID_DETALLE_TRANSACCION")
        );

        renta.setFechaInicio(
                rs.getTimestamp("FECHA_INICIO")
        );

        renta.setFechaLimite(
                rs.getTimestamp("FECHA_LIMITE")
        );

        renta.setFechaDevolucion(
                rs.getTimestamp("FECHA_DEVOLUCION")
        );

        renta.setEstado(
                rs.getString("ESTADO")
        );

        renta.setCodigo(
                rs.getString("CODIGO")
        );

        renta.setPenalizacion(
                rs.getInt("PENALIZACION")
        );

        return renta;
    }
}