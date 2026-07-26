package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.PublicacionAdministrador;
import mx.edu.utez.libriflow.model.PublicacionResumen;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PublicacionAdministradorDao {
    public int create(PublicacionAdministrador entidad){
        String sql = "Insert into Publicacion_Lf (ID_Libro, Sinopsis, Cantidad, Es_venta, Es_renta, Precio) values(?, ?, ?, ?, ?, ?)";
        try(Connection con = SQLconnector.getConnection();
            PreparedStatement ps = con.prepareStatement(sql, new String[] {"ID_PUBLICACION_LF"});){

            ps.setInt(1, entidad.getIdLibro());
            ps.setString(2, entidad.getSinopsis());
            ps.setInt(3, entidad.getCantidad());
            ps.setInt(4, entidad.getEsVenta());
            ps.setInt(5, entidad.getEsRenta());
            ps.setDouble(6, entidad.getPrecio());

            int filasAfectadas = ps.executeUpdate();
            if(filasAfectadas == 0) throw new SQLException("No se pudo insertar la publicación.");

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            }
            throw new SQLException("No se pudo obtener el ID de la publicación del administrador insertada.");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public List<PublicacionResumen> getResumenCatalogo() {

        List<PublicacionResumen> lista = new ArrayList<>();

        String sql = "SELECT \n" +
                "    plf.id_publicacion_lf,\n" +
                "    plf.precio,\n" +
                "    l.titulo,\n" +
                "    l.autor,\n" +
                "    l.genero,\n" +
                "    i.imagen\n" +
                "   \n" +
                "FROM publicacion_lf plf\n" +
                "JOIN libro l \n" +
                "    ON plf.id_libro = l.id_libro\n" +
                "JOIN imagen i \n" +
                "    ON plf.id_publicacion_lf = i.id_publicacion_lf\n" +
                "WHERE i.tipo = 1\n" +
                "AND plf.estado = 'ACTIVO'";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                PublicacionResumen resumen = new PublicacionResumen();

                resumen.setIdPublicacion(rs.getInt("id_publicacion_lf"));
                resumen.setTitulo(rs.getString("titulo"));
                resumen.setIdPropietario(0);
                resumen.setAutor(rs.getString("autor"));
                resumen.setGenero(rs.getString("genero"));
                resumen.setPrecio(rs.getDouble("precio"));
                resumen.setImagenPrincipal(rs.getString("imagen"));
                resumen.setEsLibriFlow(true);

                lista.add(resumen);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}