package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Imagen;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Esta clase se encarga de realizar las operaciones relacionadas con las
 * imágenes de las publicaciones dentro de la base de datos. Permite registrar,
 * consultar, actualizar y eliminar imágenes asociadas a publicaciones de
 * usuarios y publicaciones oficiales de LibriFlow.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
public class ImagenDao {

    /**
     *
     * Este método se encarga de registrar una imagen relacionada con una
     * publicación realizada por un usuario. Almacena el identificador de la
     * publicación, la ruta de la imagen y el tipo de imagen correspondiente.
     *
     * @param entidad Es el objeto Imagen que contiene la información de la
     *                imagen que se desea registrar.
     * @param tipo Es el tipo de imagen que se desea asociar a la publicación.
     * @return true si la imagen fue registrada correctamente o false si
     *         ocurrió un error durante el registro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean createUs(Imagen entidad, int tipo) {

        String sql = """
                INSERT INTO imagen(id_publicacion_us, imagen, tipo)
                VALUES (?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entidad.getIdPublicacionUs());
            ps.setString(2, entidad.getImagen());
            ps.setInt(3, tipo);

            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * Este método se encarga de registrar una imagen relacionada con una
     * publicación administrada directamente por LibriFlow. Almacena el
     * identificador de la publicación, la ruta de la imagen y el tipo
     * correspondiente.
     *
     * @param entidad Es el objeto Imagen que contiene la información de la
     *                imagen que se desea registrar.
     * @param tipo Es el tipo de imagen que se desea asociar a la publicación.
     * @return true si la imagen fue registrada correctamente o false si
     *         ocurrió un error durante el registro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean createLf(Imagen entidad, int tipo) {

        String sql = """
                INSERT INTO imagen(id_publicacion_lf, imagen, tipo)
                VALUES (?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entidad.getIdPublicacionLibriflow());
            ps.setString(2, entidad.getImagen());
            ps.setInt(3, tipo);

            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * Este método se encarga de consultar todas las imágenes registradas
     * en la base de datos.
     *
     * @return Lista de objetos Imagen encontrados. Si no existen registros,
     *         se devuelve una lista vacía.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<Imagen> getAll() {

        List<Imagen> imagenes = new ArrayList<>();

        String sql = """
                SELECT
                    id_imagen,
                    id_publicacion_us,
                    id_publicacion_lf,
                    imagen
                FROM imagen
                ORDER BY id_imagen
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                imagenes.add(mapearImagen(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return imagenes;
    }

    /**
     *
     * Este método se encarga de buscar una imagen mediante su identificador
     * único dentro de la base de datos.
     *
     * @param id Es el identificador único de la imagen que se desea consultar.
     * @return El objeto Imagen encontrado o null si no existe un registro
     *         con el identificador proporcionado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public Imagen getById(Integer id) {

        String sql = """
                SELECT
                    id_imagen,
                    id_publicacion_us,
                    id_publicacion_lf,
                    imagen
                FROM imagen
                WHERE id_imagen = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearImagen(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * Este método se encarga de actualizar la ruta o contenido de una imagen
     * existente utilizando su identificador único. Las relaciones con la
     * publicación y el tipo de imagen se conservan sin modificaciones.
     *
     * @param entidad Es el objeto Imagen que contiene el identificador de la
     *                imagen y la nueva ruta o contenido que se desea almacenar.
     * @return true si la imagen fue actualizada correctamente o false si
     *         no existe el registro o ocurrió un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean update(Imagen entidad) {

        String sql = """
                UPDATE imagen
                SET imagen = ?
                WHERE id_imagen = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entidad.getImagen());
            ps.setInt(2, entidad.getIdImagen());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * Este método se encarga de eliminar una imagen de la base de datos
     * utilizando su identificador único.
     *
     * @param id Es el identificador de la imagen que se desea eliminar.
     * @return true si la imagen fue eliminada correctamente o false si
     *         no existe el registro o ocurrió un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean delete(Integer id) {

        String sql = """
                DELETE FROM imagen
                WHERE id_imagen = ?
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
     *
     * Este método se encarga de actualizar una imagen perteneciente a una
     * publicación realizada por un usuario. Localiza la imagen mediante el
     * identificador de la publicación y su tipo, y reemplaza la ruta almacenada
     * por la nueva ruta proporcionada.
     *
     * @param idPublicacion Es el identificador de la publicación cuya imagen
     *                      se desea actualizar.
     * @param tipo Es el tipo de imagen que se desea modificar.
     * @param rutaImagen Es la nueva ruta de la imagen que se desea almacenar.
     * @return true si la imagen fue actualizada correctamente o false si
     *         no se realizó la modificación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean actualizarImagenUs(
            int idPublicacion,
            int tipo,
            String rutaImagen) {

        String sql = """
                UPDATE imagen
                SET imagen = ?
                WHERE id_publicacion_us = ?
                AND tipo = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rutaImagen);
            ps.setInt(2, idPublicacion);
            ps.setInt(3, tipo);

            int filasActualizadas = ps.executeUpdate();

            return filasActualizadas > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * Este método convierte la fila actual de un ResultSet en un objeto Imagen.
     * Cuando una de las llaves foráneas es nula en la base de datos, su valor
     * queda representado como 0 dentro del modelo porque los atributos son int.
     *
     * @param rs Es el resultado de la consulta posicionado en una fila válida.
     * @return El objeto Imagen construido con los valores recuperados.
     * @throws SQLException Si ocurre un error al leer las columnas.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private Imagen mapearImagen(ResultSet rs) throws SQLException {

        Imagen imagen = new Imagen();

        imagen.setIdImagen(
                rs.getInt("id_imagen")
        );

        imagen.setIdPublicacionUs(
                rs.getInt("id_publicacion_us")
        );

        imagen.setIdPublicacionLibriflow(
                rs.getInt("id_publicacion_lf")
        );

        imagen.setImagen(
                rs.getString("imagen")
        );

        return imagen;
    }
}