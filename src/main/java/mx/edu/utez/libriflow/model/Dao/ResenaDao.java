package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Resena;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de Acceso a Datos (DAO) para la gestión de reseñas de publicaciones LibriFlow.
 * Proporciona métodos para registrar nuevas valoraciones, obtener el historial de reseñas
 * asociadas a una publicación y verificar si un usuario cuenta con los permisos de compra o renta
 * requeridos para publicar una opinión.
 *
 * @author Irvin
 * @since 25/08/2026
 */
public class ResenaDao {

    /**
     * Registra una nueva reseña en la base de datos para una publicación de LibriFlow.
     *
     * @param resena Objeto {@link Resena} con la información de la valoración (usuario, publicación, comentario y calificación).
     * @return {@code true} si el registro se insertó correctamente; {@code false} en caso de error o falla en la ejecución.
     */
    public boolean create(Resena resena) {
        String sql = "INSERT INTO RESENA (id_usuario, id_publicacion_lf, comentario, calificacion) VALUES (?, ?, ?, ?)";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, resena.getIdUsuario());
            ps.setInt(2, resena.getIdPublicacionLf());
            ps.setString(3, resena.getComentario());
            ps.setInt(4, resena.getCalificacion());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene el listado de reseñas asociadas a una publicación específica de LibriFlow.
     * Incluye el nombre completo del usuario autor y ordena los resultados de forma descendente por fecha.
     *
     * @param idPublicacionLf Identificador único de la publicación de LibriFlow.
     * @return Lista de objetos {@link Resena} registrados para la publicación solicitada.
     */
    public List<Resena> getResenasByPublicacion(int idPublicacionLf) {
        List<Resena> lista = new ArrayList<>();

        String sql = """
            SELECT r.*, 
                   u.nombre || ' ' || u.apellido_paterno AS nombre_completo
            FROM RESENA r
            JOIN USUARIO u ON r.id_usuario = u.id_usuario
            WHERE r.id_publicacion_lf = ?
            ORDER BY r.fecha DESC
        """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPublicacionLf);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Resena r = new Resena();

                r.setIdResena(rs.getInt("id_resena"));
                r.setIdUsuario(rs.getInt("id_usuario"));
                r.setIdPublicacionLf(rs.getInt("id_publicacion_lf"));
                r.setComentario(rs.getString("comentario"));
                r.setCalificacion(rs.getInt("calificacion"));
                r.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                r.setNombreUsuario(rs.getString("nombre_completo"));

                lista.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Verifica si un usuario ha realizado al menos una transacción (compra o renta)
     * asociada a una publicación específica de LibriFlow.
     *
     * @param idUsuario Identificador único del usuario.
     * @param idPublicacionLf Identificador único de la publicación en LibriFlow.
     * @return {@code true} si el usuario posee al menos una transacción registrada de dicha publicación; {@code false} de lo contrario.
     */
    public boolean usuarioHaCompradoORentado(int idUsuario, int idPublicacionLf) {

        String sql = """
            SELECT COUNT(*) 
            FROM TRANSACCION t
            JOIN DETALLE_TRANSACCION dt 
            ON t.id_transaccion = dt.id_transaccion
            WHERE t.id_comprador = ? 
            AND dt.id_publicacion_lf = ?
        """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idPublicacionLf);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}