package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Imagen;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImagenDao {

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