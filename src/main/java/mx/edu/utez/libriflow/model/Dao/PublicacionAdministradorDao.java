package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.PublicacionAdminCompleta;
import mx.edu.utez.libriflow.model.PublicacionAdministrador;
import mx.edu.utez.libriflow.model.PublicacionResumen;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de Acceso a Datos (DAO) encargado de gestionar las operaciones CRUD y consultas
 * de persistencia sobre las publicaciones oficiales del administrador (`Publicacion_Lf`) en la base de datos.
 *
 * @author Monserrath Anzurez
 * @since 23/08/26
 */
public class PublicacionAdministradorDao {

    /**
     * Inserta una nueva publicación oficial realizada por el administrador y retorna su clave primaria.
     *
     * @param entidad Objeto {@link PublicacionAdministrador} con los datos de sinopsis, stock, modalidad y precio.
     * @return El identificador entero (`ID_PUBLICACION_LF`) generado por la base de datos, o `-1` si ocurre un error.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
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

    /**
     * Obtiene el listado simplificado de todas las publicaciones activas del administrador para la vista de catálogo.
     *
     * @return Lista de objetos {@link PublicacionResumen} con los datos esenciales e imagen principal de cada publicación.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    public List<PublicacionResumen> getResumenCatalogo() {

        List<PublicacionResumen> lista = new ArrayList<>();

        String sql = "SELECT \n" +
                "    plf.id_publicacion_lf,\n" +
                "    plf.precio,\n" +
                "    plf.cantidad,\n" +
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
                resumen.setCantidad(rs.getInt("cantidad"));

                lista.add(resumen);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Consulta la información detallada e imágenes asociadas de una publicación de administración por su identificador.
     *
     * @param idPublicacionLf Identificador único de la publicación del administrador.
     * @return Objeto {@link PublicacionAdminCompleta} cargado con los metadatos e imágenes, o `null` si no se encuentra.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    public PublicacionAdminCompleta getPublicacionAdminCompleta(int idPublicacionLf) {
        PublicacionAdminCompleta publicacion = null;

        String sql = "SELECT " +
                "    plf.id_publicacion_lf, " +
                "    plf.id_libro, " +
                "    l.titulo, " +
                "    l.autor, " +
                "    l.editorial, " +
                "    l.genero, " +
                "    plf.sinopsis, " +
                "    plf.precio, " +
                "    plf.estado, " +
                "    plf.cantidad, " +
                "    plf.es_venta, " +
                "    plf.es_renta, " +
                "    plf.fecha_publicacion, " +
                "    MAX(CASE WHEN i.tipo = 1 THEN i.imagen END) AS imagen_principal, " +
                "    MAX(CASE WHEN i.tipo = 2 THEN i.imagen END) AS imagen_reverso, " +
                "    MAX(CASE WHEN i.tipo = 3 THEN i.imagen END) AS imagen_interior " +
                "FROM publicacion_lf plf " +
                "JOIN libro l ON plf.id_libro = l.id_libro " +
                "LEFT JOIN imagen i ON plf.id_publicacion_lf = i.id_publicacion_lf " +
                "WHERE plf.id_publicacion_lf = ? " +
                "GROUP BY plf.id_publicacion_lf, plf.id_libro, l.titulo, l.autor, l.editorial, " +
                "         l.genero, plf.sinopsis, plf.precio, plf.estado, plf.cantidad, " +
                "         plf.es_venta, plf.es_renta, plf.fecha_publicacion";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPublicacionLf);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    publicacion = new PublicacionAdminCompleta();

                    publicacion.setIdPublicacionLf(rs.getInt("id_publicacion_lf"));
                    publicacion.setIdLibro(rs.getInt("id_libro"));
                    publicacion.setTitulo(rs.getString("titulo"));
                    publicacion.setAutor(rs.getString("autor"));
                    publicacion.setEditorial(rs.getString("editorial"));
                    publicacion.setGenero(rs.getString("genero"));
                    publicacion.setSinopsis(rs.getString("sinopsis"));
                    publicacion.setPrecio(rs.getDouble("precio"));
                    publicacion.setEstado(rs.getString("estado"));
                    publicacion.setCantidad(rs.getInt("cantidad"));
                    publicacion.setEsVenta(rs.getInt("es_venta"));
                    publicacion.setEsRenta(rs.getInt("es_renta"));
                    publicacion.setFechaPublicacion(rs.getString("fecha_publicacion"));
                    publicacion.setImagenPrincipal(rs.getString("imagen_principal"));
                    publicacion.setImagenReverso(rs.getString("imagen_reverso"));
                    publicacion.setImagenInterior(rs.getString("imagen_interior"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener la publicación completa del administrador: " + e.getMessage());
            e.printStackTrace();
        }

        return publicacion;
    }

    /**
     * Reduce en una unidad el stock/inventario disponible de una publicación si la cantidad actual es mayor a 0.
     *
     * @param idPublicacion Identificador único de la publicación a actualizar.
     * @return `true` si se logró decrementar el inventario; `false` en caso contrario o si no había existencias.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    public boolean disminuirInventario(int idPublicacion) {
        String sql = "UPDATE PUBLICACION_LF " +
                "SET CANTIDAD = CANTIDAD - 1 " +
                "WHERE ID_PUBLICACION_LF = ? AND CANTIDAD > 0";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPublicacion);

            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cambia el estado de una publicación del administrador a 'INACTIVO' (baja lógica).
     *
     * @param idPublicacionLf Identificador único de la publicación a inactivar.
     * @return `true` si el estado fue actualizado correctamente; `false` si ocurre algún error.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    public boolean darDeBajaPublicacionAdmin(int idPublicacionLf) {
        String sql = "UPDATE publicacion_lf SET estado = 'INACTIVO' WHERE id_publicacion_lf = ?";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPublicacionLf);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}