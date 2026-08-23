package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Imagen;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * Esta clase se encarga de realizar las operaciones relacionadas con las
 * imágenes de las publicaciones dentro de la base de datos. Permite registrar
 * imágenes para publicaciones de usuarios y de LibriFlow, además de actualizar
 * las imágenes asociadas a una publicación de usuario.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 22/08/2026
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
     * @since 22/08/2026
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
     * @since 22/08/2026
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
     * @since 22/08/2026
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
}