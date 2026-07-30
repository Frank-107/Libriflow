package mx.edu.utez.libriflow.model.Dao;
import mx.edu.utez.libriflow.model.PublicacionResumen;
import mx.edu.utez.libriflow.model.PublicacionUsuario;
import mx.edu.utez.libriflow.model.PublicacionUsuarioCompleta;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PublicacionUsuarioDao {


    public int create(PublicacionUsuario entidad){
        String sql = "INSERT INTO Publicacion_Us(id_usuario, id_libro, sinopsis, precio, estado) VALUES( ?, ?, ?, ?,?)";

        try(Connection con = SQLconnector.getConnection();
        PreparedStatement ps = con.prepareStatement(sql, new String[] {"id_publicacion_us"});){
            ps.setInt(1,entidad.getIdUsuario());
            ps.setInt(2,entidad.getIdLibro());
            ps.setString(3,entidad.getSinopsis());
            ps.setDouble(4,entidad.getPrecio());
            ps.setString(5,"PENDIENTE");

            int filasAfectadas = ps.executeUpdate();
            if(filasAfectadas == 0) throw new SQLException("No se pudo insertar la publicación del usuario.");

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            }
            throw new SQLException("No se pudo obtener el ID de la publicación del usuario insertada.");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }


    public List<PublicacionResumen> getResumenPublicacionesUs(String estado) {

        List<PublicacionResumen> lista = new ArrayList<>();

        String sql = "SELECT \n" +
                "    pu.id_publicacion_us,\n" +
                "    pu.id_usuario,\n" +
                "    pu.precio,\n" +
                "    l.titulo,\n" +
                "    l.autor,\n" +
                "    l.genero,\n" +
                "    i.imagen,\n" +
                "    u.nombre\n"+
                "   \n" +
                "FROM publicacion_us pu\n" +
                "JOIN libro l \n" +
                "    ON pu.id_libro = l.id_libro\n" +
                "JOIN imagen i \n" +
                "    ON pu.id_publicacion_us = i.id_publicacion_us\n" +
                "JOIN usuario u \n" +
                "    ON pu.id_usuario = u.id_usuario\n" +
                "WHERE i.tipo = 1\n" +
                "AND pu.estado = ?";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {

             ps.setString(1, estado);
             ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                PublicacionResumen resumen = new PublicacionResumen();

                resumen.setIdPublicacion(rs.getInt("id_publicacion_us"));
                resumen.setTitulo(rs.getString("titulo"));
                resumen.setIdPropietario(rs.getInt("id_usuario"));
                resumen.setAutor(rs.getString("autor"));
                resumen.setGenero(rs.getString("genero"));
                resumen.setNombrePropietario(rs.getString("nombre"));
                resumen.setPrecio(rs.getDouble("precio"));
                resumen.setImagenPrincipal(rs.getString("imagen"));
                resumen.setEsLibriFlow(false);

                lista.add(resumen);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public PublicacionUsuarioCompleta getPublicacionUsuarioCompleta(int id) {

        String sql = """
            SELECT
                pu.id_publicacion_us,
                pu.id_usuario,
                pu.id_libro,
                pu.fecha,
                pu.sinopsis,
                pu.precio,
                pu.estado,

                li.titulo,
                li.autor,
                li.editorial,
                li.genero,

                im.tipo,
                im.imagen

            FROM publicacion_us pu

            JOIN libro li
                ON pu.id_libro = li.id_libro

            JOIN imagen im
                ON im.id_publicacion_us = pu.id_publicacion_us

            WHERE pu.id_publicacion_us = ?
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                PublicacionUsuarioCompleta publicacion = null;

                while (rs.next()) {

                    if (publicacion == null) {

                        publicacion = new PublicacionUsuarioCompleta();

                        publicacion.setIdPublicacion(rs.getInt("id_publicacion_us"));
                        publicacion.setIdPropietario(rs.getInt("id_usuario"));
                        publicacion.setIdLibro(rs.getInt("id_libro"));

                        publicacion.setFecha(
                                rs.getTimestamp("fecha").toLocalDateTime()
                        );

                        publicacion.setTitulo(rs.getString("titulo"));
                        publicacion.setAutor(rs.getString("autor"));
                        publicacion.setEditorial(rs.getString("editorial"));
                        publicacion.setGenero(rs.getString("genero"));

                        publicacion.setSinopsis(rs.getString("sinopsis"));
                        publicacion.setPrecio(rs.getDouble("precio"));
                        publicacion.setEstado(rs.getString("estado"));
                    }

                    switch (rs.getInt("tipo")) {

                        case 1:
                            publicacion.setImagenPrincipal(
                                    rs.getString("imagen")
                            );
                            break;

                        case 2:
                            publicacion.setImagenReverso(
                                    rs.getString("imagen")
                            );
                            break;

                        case 3:
                            publicacion.setImagenInterior(
                                    rs.getString("imagen")
                            );
                            break;
                    }
                }

                return publicacion;
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<PublicacionResumen> getPublicacionesByArreglo(List<Integer> ids) {

        List<PublicacionResumen> lista = new ArrayList<>();

        if (ids == null || ids.isEmpty()) {
            return lista;
        }


        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));


        String sql = """
            SELECT 
                pu.id_publicacion_us,
                pu.id_usuario,
                pu.precio,
                l.titulo,
                l.autor,
                l.genero,
                i.imagen
            FROM publicacion_us pu
            JOIN libro l 
                ON pu.id_libro = l.id_libro
            JOIN imagen i 
                ON pu.id_publicacion_us = i.id_publicacion_us
            WHERE i.tipo = 1
            AND pu.id_publicacion_us IN (%s)
            """.formatted(placeholders);


        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {


            // Meter los IDs del carrito en los ?
            for (int i = 0; i < ids.size(); i++) {

                ps.setInt(i + 1, ids.get(i));

            }


            ResultSet rs = ps.executeQuery();


            while (rs.next()) {

                PublicacionResumen resumen = new PublicacionResumen();

                resumen.setIdPublicacion(
                        rs.getInt("id_publicacion_us")
                );

                resumen.setIdPropietario(
                        rs.getInt("id_usuario")
                );

                resumen.setTitulo(
                        rs.getString("titulo")
                );

                resumen.setAutor(
                        rs.getString("autor")
                );

                resumen.setGenero(
                        rs.getString("genero")
                );

                resumen.setPrecio(
                        rs.getDouble("precio")
                );

                resumen.setImagenPrincipal(
                        rs.getString("imagen")
                );

                resumen.setEsLibriFlow(false);


                lista.add(resumen);

            }


        } catch (SQLException e) {

            e.printStackTrace();

        }


        return lista;
    }
    public List<PublicacionResumen> getResumenPublicacionesPorUsuario(int idUsuario) {

        List<PublicacionResumen> lista = new ArrayList<>();

        String sql = """
            SELECT 
                pu.id_publicacion_us,
                pu.id_usuario,
                pu.precio,
                pu.estado,
                l.titulo,
                l.autor,
                l.genero,
                i.imagen
            FROM publicacion_us pu
            JOIN libro l
                ON pu.id_libro = l.id_libro
            JOIN imagen i
                ON pu.id_publicacion_us = i.id_publicacion_us
            WHERE i.tipo = 1
            AND pu.id_usuario = ?
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                PublicacionResumen resumen = new PublicacionResumen();

                resumen.setIdPublicacion(rs.getInt("id_publicacion_us"));
                resumen.setTitulo(rs.getString("titulo"));
                resumen.setIdPropietario(rs.getInt("id_usuario"));
                resumen.setAutor(rs.getString("autor"));
                resumen.setGenero(rs.getString("genero"));
                resumen.setPrecio(rs.getDouble("precio"));
                resumen.setImagenPrincipal(rs.getString("imagen"));
                resumen.setEstado(rs.getString("estado"));
                resumen.setEsLibriFlow(false);

                lista.add(resumen);
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean cambiarEstadoPublicacion(int idPublicacion, String estado) {

        String sql = """
            UPDATE publicacion_us
            SET estado = ?
            WHERE id_publicacion_us = ?
            """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idPublicacion);

            int filasActualizadas = ps.executeUpdate();

            return filasActualizadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }




    public List<PublicacionUsuario> getAll() {
        return List.of();
    }

    public PublicacionUsuario getById(Integer id) {
        return null;
    }

    public boolean update(PublicacionUsuario entidad) {
        return false;
    }

    public boolean delete(Integer id) {
        return false;
    }

    public int contarPublicacionesPorUsuario(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM publicacion_us WHERE id_usuario = ?";
        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<PublicacionResumen> buscarYFiltrarPublicacionesUs(String estado, String busqueda, String genero) {
        List<PublicacionResumen> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT 
                pu.id_publicacion_us,
                pu.id_usuario,
                pu.precio,
                l.titulo,
                l.autor,
                l.genero,
                i.imagen,
                u.nombre
            FROM publicacion_us pu
            JOIN libro l ON pu.id_libro = l.id_libro
            JOIN imagen i ON pu.id_publicacion_us = i.id_publicacion_us
            JOIN usuario u ON pu.id_usuario = u.id_usuario
            WHERE i.tipo = 1
            AND pu.estado = ?
        """);
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql.append(" AND (LOWER(l.titulo) LIKE ? OR LOWER(l.autor) LIKE ?)");
        }

        if (genero != null && !genero.trim().isEmpty() && !genero.equalsIgnoreCase("TODOS")) {
            sql.append(" AND LOWER(l.genero) = LOWER(?)");
        }try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int index = 1;
            ps.setString(index++, estado);

            if (busqueda != null && !busqueda.trim().isEmpty()) {
                String term = "%" + busqueda.trim().toLowerCase() + "%";
                ps.setString(index++, term);
                ps.setString(index++, term);
            }

            if (genero != null && !genero.trim().isEmpty() && !genero.equalsIgnoreCase("TODOS")) {
                ps.setString(index++, genero.trim());
            } try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PublicacionResumen resumen = new PublicacionResumen();
                    resumen.setIdPublicacion(rs.getInt("id_publicacion_us"));
                    resumen.setTitulo(rs.getString("titulo"));
                    resumen.setIdPropietario(rs.getInt("id_usuario"));
                    resumen.setAutor(rs.getString("autor"));
                    resumen.setGenero(rs.getString("genero"));
                    resumen.setNombrePropietario(rs.getString("nombre"));
                    resumen.setPrecio(rs.getDouble("precio"));
                    resumen.setImagenPrincipal(rs.getString("imagen"));
                    resumen.setEsLibriFlow(false);

                    lista.add(resumen);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
