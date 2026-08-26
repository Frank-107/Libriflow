package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Resena;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de Acceso a Datos (DAO) para la gestión de reseñas de publicaciones LibriFlow.
 * Proporciona operaciones CRUD y consultas relacionadas con las reseñas realizadas
 * por los usuarios sobre publicaciones oficiales de LibriFlow.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
public class ResenaDao {

    /**
     * Registra una nueva reseña en la base de datos para una publicación de LibriFlow.
     *
     * @param resena Objeto {@link Resena} con la información de la valoración.
     * @return {@code true} si el registro se insertó correctamente;
     *         {@code false} en caso contrario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean create(Resena resena) {

        String sql = """
                INSERT INTO resena(
                    id_usuario,
                    id_publicacion_lf,
                    comentario,
                    calificacion
                )
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    resena.getIdUsuario()
            );

            ps.setInt(
                    2,
                    resena.getIdPublicacionLf()
            );

            ps.setString(
                    3,
                    resena.getComentario()
            );

            ps.setInt(
                    4,
                    resena.getCalificacion()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL CREAR RESEÑA"
            );
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene todas las reseñas registradas en la base de datos junto con
     * el nombre del usuario que realizó cada valoración.
     *
     * @return Lista con todas las reseñas registradas.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<Resena> getAll() {

        List<Resena> lista =
                new ArrayList<>();

        String sql = """
                SELECT
                    r.id_resena,
                    r.id_usuario,
                    r.id_publicacion_lf,
                    r.comentario,
                    r.calificacion,
                    r.fecha,
                    u.nombre || ' ' || u.apellido_paterno AS nombre_completo
                FROM resena r
                JOIN usuario u
                    ON r.id_usuario = u.id_usuario
                ORDER BY r.id_resena
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(
                        mapearResena(rs)
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL OBTENER TODAS LAS RESEÑAS"
            );
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene una reseña mediante su identificador.
     *
     * @param id Identificador único de la reseña.
     * @return La reseña encontrada o {@code null} si no existe.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public Resena getById(Integer id) {

        String sql = """
                SELECT
                    r.id_resena,
                    r.id_usuario,
                    r.id_publicacion_lf,
                    r.comentario,
                    r.calificacion,
                    r.fecha,
                    u.nombre || ' ' || u.apellido_paterno AS nombre_completo
                FROM resena r
                JOIN usuario u
                    ON r.id_usuario = u.id_usuario
                WHERE r.id_resena = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    id
            );

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearResena(rs);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL OBTENER RESEÑA POR ID"
            );
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Actualiza la información editable de una reseña existente.
     * Se conservan el usuario, la publicación y la fecha original.
     *
     * @param resena Reseña con el identificador y los nuevos valores.
     * @return {@code true} si la reseña fue actualizada;
     *         {@code false} si no existe o ocurre un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean update(Resena resena) {

        String sql = """
                UPDATE resena
                SET comentario = ?,
                    calificacion = ?
                WHERE id_resena = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(
                    1,
                    resena.getComentario()
            );

            ps.setInt(
                    2,
                    resena.getCalificacion()
            );

            ps.setInt(
                    3,
                    resena.getIdResena()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL ACTUALIZAR RESEÑA"
            );
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una reseña mediante su identificador.
     *
     * @param id Identificador único de la reseña.
     * @return {@code true} si se eliminó correctamente;
     *         {@code false} si no existe o ocurre un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean delete(Integer id) {

        String sql = """
                DELETE FROM resena
                WHERE id_resena = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    id
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL ELIMINAR RESEÑA"
            );
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene el listado de reseñas asociadas a una publicación específica de LibriFlow.
     * Incluye el nombre completo del usuario autor y ordena los resultados de forma
     * descendente por fecha.
     *
     * @param idPublicacionLf Identificador único de la publicación de LibriFlow.
     * @return Lista de objetos {@link Resena} registrados para la publicación solicitada.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<Resena> getResenasByPublicacion(
            int idPublicacionLf) {

        List<Resena> lista =
                new ArrayList<>();

        String sql = """
                SELECT
                    r.id_resena,
                    r.id_usuario,
                    r.id_publicacion_lf,
                    r.comentario,
                    r.calificacion,
                    r.fecha,
                    u.nombre || ' ' || u.apellido_paterno AS nombre_completo
                FROM resena r
                JOIN usuario u
                    ON r.id_usuario = u.id_usuario
                WHERE r.id_publicacion_lf = ?
                ORDER BY r.fecha DESC
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idPublicacionLf
            );

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(
                            mapearResena(rs)
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL OBTENER RESEÑAS POR PUBLICACIÓN"
            );
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Verifica si un usuario ha realizado al menos una transacción de compra
     * o renta asociada a una publicación específica de LibriFlow.
     *
     * @param idUsuario Identificador único del usuario.
     * @param idPublicacionLf Identificador único de la publicación en LibriFlow.
     * @return {@code true} si el usuario tiene una operación registrada para
     *         esa publicación; {@code false} de lo contrario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean usuarioHaCompradoORentado(
            int idUsuario,
            int idPublicacionLf) {

        String sql = """
                SELECT COUNT(*)
                FROM transaccion t
                JOIN detalle_transaccion dt
                    ON t.id_transaccion = dt.id_transaccion
                WHERE t.id_comprador = ?
                  AND dt.id_publicacion_lf = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idUsuario
            );

            ps.setInt(
                    2,
                    idPublicacionLf
            );

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "ERROR AL VERIFICAR COMPRA O RENTA"
            );
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Convierte la fila actual del ResultSet en un objeto Resena.
     *
     * @param rs Resultado de consulta posicionado en una fila válida.
     * @return Objeto Resena construido con los datos de la fila.
     * @throws SQLException Si ocurre un error al leer los datos.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private Resena mapearResena(
            ResultSet rs) throws SQLException {

        Resena resena =
                new Resena();

        resena.setIdResena(
                rs.getInt("id_resena")
        );

        resena.setIdUsuario(
                rs.getInt("id_usuario")
        );

        resena.setIdPublicacionLf(
                rs.getInt("id_publicacion_lf")
        );

        resena.setComentario(
                rs.getString("comentario")
        );

        resena.setCalificacion(
                rs.getInt("calificacion")
        );

        Timestamp fecha =
                rs.getTimestamp("fecha");

        if (fecha != null) {
            resena.setFecha(
                    fecha.toLocalDateTime()
            );
        }

        resena.setNombreUsuario(
                rs.getString("nombre_completo")
        );

        return resena;
    }
}