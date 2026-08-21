package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.CompraResumen;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * La clase CompraDao proporciona los métodos para acceder y gestionar la información
 * relativa a las compras realizadas y ventas concretadas por los usuarios en la base de datos.
 *
 * @author Andres Gerardo Angelina Perez
 * @author Monserrath Anzures Visoso
 * @since 21/08/2026
 */
public class CompraDao {

    /**
     * Consulta el historial de compras realizadas por un usuario específico, consolidando
     * tanto las publicaciones de otros usuarios como las administradas directamente por LibriFlow.
     *
     * @param idUsuario Identificador único del usuario comprador.
     * @return Lista de objetos {@link CompraResumen} con el detalle visual y financiero de cada compra.
     */
    public List<CompraResumen> getResumenComprasPorUsuario(int idUsuario) {

        List<CompraResumen> lista = new ArrayList<>();

        String sql = """
            SELECT
                dt.id_detalle,
                dt.id_transaccion,

                COALESCE(
                    dt.id_publicacion_us,
                    dt.id_publicacion_lf
                ) AS id_publicacion,

                CASE
                    WHEN dt.id_publicacion_lf IS NOT NULL
                    THEN 1
                    ELSE 0
                END AS es_libriflow,

                dt.precio,
                t.fecha,
                t.estado AS estado_transaccion,

                COALESCE(
                    uv.nombre,
                    'LibriFlow'
                ) AS nombre_vendedor,

                COALESCE(
                    lus.titulo,
                    llf.titulo
                ) AS titulo,

                COALESCE(
                    lus.autor,
                    llf.autor
                ) AS autor,

                COALESCE(
                    ius.imagen,
                    ilf.imagen
                ) AS imagen

            FROM detalle_transaccion dt

            JOIN transaccion t
                ON dt.id_transaccion = t.id_transaccion

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
              AND dt.tipo_operacion = 'COMPRA'

            ORDER BY t.fecha DESC
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    CompraResumen compra = new CompraResumen();

                    compra.setIdDetalle(
                            rs.getInt("id_detalle")
                    );

                    compra.setIdTransaccion(
                            rs.getInt("id_transaccion")
                    );

                    compra.setIdPublicacion(
                            rs.getInt("id_publicacion")
                    );

                    compra.setEsLibriFlow(
                            rs.getInt("es_libriflow") == 1
                    );

                    compra.setTitulo(
                            rs.getString("titulo")
                    );

                    compra.setAutor(
                            rs.getString("autor")
                    );

                    compra.setImagenPrincipal(
                            rs.getString("imagen")
                    );

                    compra.setPrecio(
                            rs.getDouble("precio")
                    );

                    compra.setNombreVendedor(
                            rs.getString("nombre_vendedor")
                    );

                    compra.setEstadoTransaccion(
                            rs.getString("estado_transaccion")
                    );

                    if (rs.getTimestamp("fecha") != null) {
                        compra.setFecha(
                                rs.getTimestamp("fecha")
                                        .toLocalDateTime()
                        );
                    }

                    lista.add(compra);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene el número total de ventas completadas exitosamente por un usuario.
     *
     * @param idUsuario Identificador único del usuario vendedor.
     * @return Conteo total de transacciones de tipo 'COMPRA' asociadas al vendedor.
     */
    public int contarVentasPorUsuario(int idUsuario) {

        int total = 0;

        String sql = """
            SELECT COUNT(*)
            FROM DETALLE_TRANSACCION
            WHERE ID_VENDEDOR = ?
              AND TIPO_OPERACION = 'COMPRA'
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
                    "Error en contarVentasPorUsuario: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return total;
    }
}