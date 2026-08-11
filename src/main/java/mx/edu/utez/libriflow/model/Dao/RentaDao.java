package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.RentaResumen;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RentaDao {

    public List<RentaResumen> getResumenTodasLasRentas() {

        List<RentaResumen> lista = new ArrayList<>();

        String sql = """
            SELECT
                dr.id_detalle,
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
                ON dr.id_detalle = dt.id_detalle
            JOIN transaccion t
                ON dt.id_transaccion = t.id_transaccion
            JOIN usuario uc
                ON t.id_comprador = uc.id_usuario
            JOIN usuario uv
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
                renta.setIdTransaccion(rs.getInt("id_transaccion"));
                renta.setTitulo(rs.getString("titulo"));
                renta.setAutor(rs.getString("autor"));
                renta.setImagenPrincipal(rs.getString("imagen"));
                renta.setPrecio(rs.getDouble("precio"));
                renta.setNombreComprador(rs.getString("nombre_comprador"));
                renta.setNombreVendedor(rs.getString("nombre_vendedor"));

                if (rs.getDate("fecha_inicio") != null) {
                    renta.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                }
                if (rs.getDate("fecha_limite") != null) {
                    renta.setFechaLimite(rs.getDate("fecha_limite").toLocalDate());
                }
                if (rs.getDate("fecha_devolucion") != null) {
                    renta.setFechaDevolucion(rs.getDate("fecha_devolucion").toLocalDate());
                }

                renta.setEstado(rs.getString("estado"));

                lista.add(renta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<RentaResumen> getResumenRentasPorUsuario(int idUsuario) {

        List<RentaResumen> lista = new ArrayList<>();

        String sql = """
        SELECT
            dr.id_detalle,
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
            ON dr.id_detalle = dt.id_detalle
        JOIN transaccion t
            ON dt.id_transaccion = t.id_transaccion
        JOIN usuario uc
            ON t.id_comprador = uc.id_usuario
        JOIN usuario uv
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
                    renta.setIdTransaccion(rs.getInt("id_transaccion"));
                    renta.setTitulo(rs.getString("titulo"));
                    renta.setAutor(rs.getString("autor"));
                    renta.setImagenPrincipal(rs.getString("imagen"));
                    renta.setPrecio(rs.getDouble("precio"));
                    renta.setNombreComprador(rs.getString("nombre_comprador"));
                    renta.setNombreVendedor(rs.getString("nombre_vendedor"));

                    if (rs.getDate("fecha_inicio") != null) {
                        renta.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                    }
                    if (rs.getDate("fecha_limite") != null) {
                        renta.setFechaLimite(rs.getDate("fecha_limite").toLocalDate());
                    }
                    if (rs.getDate("fecha_devolucion") != null) {
                        renta.setFechaDevolucion(rs.getDate("fecha_devolucion").toLocalDate());
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

    public boolean marcarComoDevuelta(int idDetalle) {

        String sql = """
        UPDATE detalle_renta
        SET estado = 'DEVUELTA',
            fecha_devolucion = SYSDATE
        WHERE id_detalle = ?
        """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDetalle);

            int filasActualizadas = ps.executeUpdate();

            return filasActualizadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

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

            int filasActualizadas = ps.executeUpdate();

            return filasActualizadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public int contarRentasActivasPorUsuario(int idUsuario) {
        int total = 0;
        String sql = """
            SELECT COUNT(*)
            FROM DETALLE_RENTA dr
            JOIN DETALLE_TRANSACCION dt 
                ON dr.ID_DETALLE_TRANSACCION = dt.ID_DETALLE
            JOIN TRANSACCION t 
                ON dt.ID_TRANSACCION = t.ID_TRANSACCION
            WHERE t.ID_COMPRADOR = ?
              AND dr.ESTADO = 'ACTIVA'
              AND (dr.PENALIZACION = 0 OR dr.PENALIZACION IS NULL)
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
            System.out.println("Error en contarRentasActivasPorUsuario: " + e.getMessage());
            e.printStackTrace();
        }

        return total;
    }


    public int contarRetrasosPorUsuario(int idUsuario) {
        int total = 0;
        String sql = """
            SELECT COUNT(*)
            FROM DETALLE_RENTA dr
            JOIN DETALLE_TRANSACCION dt 
                ON dr.ID_DETALLE_TRANSACCION = dt.ID_DETALLE
            JOIN TRANSACCION t 
                ON dt.ID_TRANSACCION = t.ID_TRANSACCION
            WHERE t.ID_COMPRADOR = ?
              AND (dr.PENALIZACION = 1 OR dr.ESTADO = 'RETRASADO' OR (dr.ESTADO = 'ACTIVA' AND dr.FECHA_LIMITE < CURRENT_TIMESTAMP))
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
            System.out.println("Error en contarRetrasosPorUsuario: " + e.getMessage());
            e.printStackTrace();
        }

        return total;
    }
}