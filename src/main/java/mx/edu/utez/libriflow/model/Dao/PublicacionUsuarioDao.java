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
import java.util.List;

public class PublicacionUsuarioDao {


    public int create(PublicacionUsuario entidad){
        String sql = "INSERT INTO Publicacion_Us(id_usuario, id_libro, sinopsis, precio) VALUES( ?, ?, ?, ?)";

        try(Connection con = SQLconnector.getConnection();
        PreparedStatement ps = con.prepareStatement(sql, new String[] {"id_publicacion_us"});){
            ps.setInt(1,entidad.getIdUsuario());
            ps.setInt(2,entidad.getIdLibro());
            ps.setString(3,entidad.getSinopsis());
            ps.setDouble(4,entidad.getPrecio());

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


    public List<PublicacionResumen> getResumenCatalogo() {

        List<PublicacionResumen> lista = new ArrayList<>();

        String sql = "SELECT \n" +
                "    pu.id_publicacion_us,\n" +
                "    pu.id_usuario,\n" +
                "    pu.precio,\n" +
                "    l.titulo,\n" +
                "    l.autor,\n" +
                "    l.genero,\n" +
                "    i.imagen\n" +
                "   \n" +
                "FROM publicacion_us pu\n" +
                "JOIN libro l \n" +
                "    ON pu.id_libro = l.id_libro\n" +
                "JOIN imagen i \n" +
                "    ON pu.id_publicacion_us = i.id_publicacion_us\n" +
                "WHERE i.tipo = 1\n" +
                "AND pu.estado = 'ACTIVO'";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                PublicacionResumen resumen = new PublicacionResumen();

                resumen.setIdPublicacion(rs.getInt("id_publicacion_us"));
                resumen.setTitulo(rs.getString("titulo"));
                resumen.setIdPropietario(rs.getInt("id_usuario"));
                resumen.setAutor(rs.getString("autor"));
                resumen.setGenero(rs.getString("genero"));
                resumen.setPrecio(rs.getDouble("precio"));
                resumen.setImagenPrincipal(rs.getString("imagen"));
                resumen.setEsLibriFlow(false);

                lista.add(resumen);
            }

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
}
