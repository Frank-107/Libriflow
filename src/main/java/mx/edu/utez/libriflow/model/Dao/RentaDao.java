package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.RentaResumen;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de Acceso a Datos (DAO) para la gestión de rentas de libros en el sistema LibriFlow.
 * Proporciona métodos para consultar el historial de rentas globales o por usuario,
 * así como para actualizar estados operativos de entrega, devolución y penalizaciones.
 *
 * @author Andres
 * @since 24/08/2026
 */
public class RentaDao {

    /**
     * Recupera el listado completo y detallado de todas las rentas registradas en la base de datos.
     * Consolida información de transacciones, compradores, vendedores y datos bibliográficos del libro.
     *
     * @return Lista de objetos {@link RentaResumen} con el resumen global de rentas.
     */
    public List<RentaResumen> getResumenTodasLasRentas() {

        List<RentaResumen> lista = new ArrayList<>();

        String sql = """
            SELECT
                dr.id_detalle,
                dr.codigo,
                NVL(dr.penalizacion, 0) AS penalizacion,
                dr.fecha_inicio,
                dr.fecha_limite,
                dr.fecha_devolucion,
                dr.estado,
                dt.id_transaccion,
                dt.precio,
                uc.nombre AS nombre_comprador,
                uv.nombre AS nombre_vendedor,
                COALESCE(lus.titulo, llf.titulo) AS titulo,
                COALESCE(lus.autor, llf.autor) AS autor,
                COALESCE(ius.imagen, ilf.imagen) AS imagen
            FROM detalle_renta dr
            JOIN detalle_transaccion dt
                ON dr.id_detalle_transaccion = dt.id_detalle
            JOIN transaccion t
                ON dt.id_transaccion = t.id_transaccion
            JOIN usuario uc
                ON t.id_comprador = uc.id_usuario
            LEFT JOIN usuario uv
                ON dt.id_vendedor = uv.id_usuario
            LEFT JOIN publicacion_us pus
                ON dt.id_publicacion_us = pus.id_publicacion_us
            LEFT JOIN libro lus
                ON pus.id_libro = lus.id_libro
            LEFT JOIN imagen ius
                ON ius.id_publicacion_us = pus.id_publicacion_us
                AND ius.tipo = 1
            LEFT JOIN publicacion_lf plf
                ON dt.id_publicacion_lf = plf.id_publicacion_lf
            LEFT JOIN libro llf
                ON plf.id_libro = llf.id_libro
            LEFT JOIN imagen ilf
                ON ilf.id_publicacion_lf = plf.id_publicacion_lf
                AND ilf.tipo = 1
            ORDER BY dr.fecha_inicio DESC
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                RentaResumen renta = new RentaResumen();

                renta.setIdDetalle(rs.getInt("id_detalle"));
                renta.setCodigo(rs.getString("codigo"));
                renta.setPenalizacion(rs.getInt("penalizacion"));
                renta.setIdTransaccion(rs.getInt("id_transaccion"));
                renta.setTitulo(rs.getString("titulo"));
                renta.setAutor(rs.getString("autor"));
                renta.setImagenPrincipal(rs.getString("imagen"));
                renta.setPrecio(rs.getDouble("precio"));
                renta.setNombreComprador(rs.getString("nombre_comprador"));
                renta.setNombreVendedor(rs.getString("nombre_vendedor"));

                if (rs.getDate("fecha_inicio") != null) {
                    renta.setFechaInicio(
                            rs.getDate("fecha_inicio").toLocalDate()
                    );
                }

                if (rs.getDate("fecha_limite") != null) {
                    renta.setFechaLimite(
                            rs.getDate("fecha_limite").toLocalDate()
                    );
                }

                if (rs.getDate("fecha_devolucion") != null) {
                    renta.setFechaDevolucion(
                            rs.getDate("fecha_devolucion").toLocalDate()
                    );
                }

                renta.setEstado(rs.getString("estado"));

                lista.add(renta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Consulta y obtiene la lista de rentas realizadas por un comprador específico.
     *
     * @param idUsuario Identificador único del usuario comprador.
     * @return Lista de objetos {@link RentaResumen} asociados al usuario.
     */
    public List<RentaResumen> getResumenRentasPorUsuario(int idUsuario) {

        List<RentaResumen> lista = new ArrayList<>();

        String sql = """
            SELECT
                dr.id_detalle,
                dr.codigo,
                NVL(dr.penalizacion, 0) AS penalizacion,
                dr.fecha_inicio,
                dr.fecha_limite,
                dr.fecha_devolucion,
                dr.estado,
                dt.id_transaccion,
                dt.precio,
                uc.nombre AS nombre_comprador,
                uv.nombre AS nombre_vendedor,
                COALESCE(lus.titulo, llf.titulo) AS titulo,
                COALESCE(lus.autor, llf.autor) AS autor,
                COALESCE(ius.imagen, ilf.imagen) AS imagen
            FROM detalle_renta dr
            JOIN detalle_transaccion dt
                ON dr.id_detalle_transaccion = dt.id_detalle
            JOIN transaccion t
                ON dt.id_transaccion = t.id_transaccion
            JOIN usuario uc
                ON t.id_comprador = uc.id_usuario
            LEFT JOIN usuario uv
                ON dt.id_vendedor = uv.id_usuario
            LEFT JOIN publicacion_us pus
                ON dt.id_publicacion_us = pus.id_publicacion_us
            LEFT JOIN libro lus
                ON pus.id_libro = lus.id_libro
            LEFT JOIN imagen ius
                ON ius.id_publicacion_us = pus.id_publicacion_us
                AND ius.tipo = 1
            LEFT JOIN publicacion_lf plf
                ON dt.id_publicacion_lf = plf.id_publicacion_lf
            LEFT JOIN libro llf
                ON plf.id_libro = llf.id_libro
            LEFT JOIN imagen ilf
                ON ilf.id_publicacion_lf = plf.id_publicacion_lf
                AND ilf.tipo = 1
            WHERE t.id_comprador = ?
            ORDER BY dr.fecha_inicio DESC
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    RentaResumen renta = new RentaResumen();

                    renta.setIdDetalle(rs.getInt("id_detalle"));
                    renta.setCodigo(rs.getString("codigo"));
                    renta.setPenalizacion(rs.getInt("penalizacion"));
                    renta.setIdTransaccion(rs.getInt("id_transaccion"));
                    renta.setTitulo(rs.getString("titulo"));
                    renta.setAutor(rs.getString("autor"));
                    renta.setImagenPrincipal(rs.getString("imagen"));
                    renta.setPrecio(rs.getDouble("precio"));
                    renta.setNombreComprador(rs.getString("nombre_comprador"));
                    renta.setNombreVendedor(rs.getString("nombre_vendedor"));

                    if (rs.getDate("fecha_inicio") != null) {
                        renta.setFechaInicio(
                                rs.getDate("fecha_inicio").toLocalDate()
                        );
                    }

                    if (rs.getDate("fecha_limite") != null) {
                        renta.setFechaLimite(
                                rs.getDate("fecha_limite").toLocalDate()
                        );
                    }

                    if (rs.getDate("fecha_devolucion") != null) {
                        renta.setFechaDevolucion(
                                rs.getDate("fecha_devolucion").toLocalDate()
                        );
                    }

                    renta.setEstado(rs.getString("estado"));

                    lista.add(renta);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Cambia el estado de una renta a 'ACTIVA' cuando la entrega se realiza con éxito,
     * siempre y cuando la renta se encuentre 'PROGRAMADA' y la fecha actual sea igual o posterior a la fecha de inicio.
     *
     * @param idDetalle Identificador del detalle de la renta.
     * @return {@code true} si la actualización fue exitosa; {@code false} en caso contrario.
     */
    public boolean marcarComoEntregada(int idDetalle) {

        String sql = """
            UPDATE detalle_renta
            SET estado = 'ACTIVA'
            WHERE id_detalle = ?
              AND estado = 'PROGRAMADA'
              AND fecha_inicio <= SYSDATE
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDetalle);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Marca una renta como 'FINALIZADA' y establece la fecha de devolución al momento actual,
     * siempre que la renta se encuentre en estado 'ACTIVA'.
     *
     * @param idDetalle Identificador del detalle de la renta.
     * @return {@code true} si se registró la devolución correctamente; {@code false} en caso contrario.
     */
    public boolean marcarComoFinalizada(int idDetalle) {

        String sql = """
            UPDATE detalle_renta
            SET estado = 'FINALIZADA',
                fecha_devolucion = SYSDATE
            WHERE id_detalle = ?
              AND estado = 'ACTIVA'
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDetalle);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza arbitrariamente el estado de un registro en la tabla de detalle de renta.
     *
     * @param idDetalle Identificador del detalle de la renta.
     * @param estado Nuevo estado que se asignará al registro.
     * @return {@code true} si se actualizó el registro; {@code false} en caso de error.
     */
    public boolean cambiarEstadoRenta(int idDetalle, String estado) {

        String sql = """
            UPDATE detalle_renta
            SET estado = ?
            WHERE id_detalle = ?
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idDetalle);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cuenta la cantidad de rentas vigentes ('ACTIVA') que posee un usuario comprador
     * y que no cuentan con ninguna penalización registrada.
     *
     * @param idUsuario Identificador único del usuario comprador.
     * @return Número total de rentas activas sin penalización.
     */
    public int contarRentasActivasPorUsuario(int idUsuario) {

        int total = 0;

        String sql = """
            SELECT COUNT(*)
            FROM detalle_renta dr
            JOIN detalle_transaccion dt
                ON dr.id_detalle_transaccion = dt.id_detalle
            JOIN transaccion t
                ON dt.id_transaccion = t.id_transaccion
            WHERE t.id_comprador = ?
              AND dr.estado = 'ACTIVA'
              AND (dr.penalizacion = 0 OR dr.penalizacion IS NULL)
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error en contarRentasActivasPorUsuario: "
                            + e.getMessage()
            );
            e.printStackTrace();
        }

        return total;
    }

    /**
     * Cuenta cuántas rentas activas presenta un usuario con nivel de penalización por retraso (1 o 2).
     *
     * @param idUsuario Identificador único del usuario comprador.
     * @return Número total de rentas activas con atraso/penalización.
     */
    public int contarRetrasosPorUsuario(int idUsuario) {

        int total = 0;

        String sql = """
            SELECT COUNT(*)
            FROM detalle_renta dr
            JOIN detalle_transaccion dt
                ON dr.id_detalle_transaccion = dt.id_detalle
            JOIN transaccion t
                ON dt.id_transaccion = t.id_transaccion
            WHERE t.id_comprador = ?
              AND dr.estado = 'ACTIVA'
              AND dr.penalizacion IN (1, 2)
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error en contarRetrasosPorUsuario: "
                            + e.getMessage()
            );
            e.printStackTrace();
        }

        return total;
    }
}