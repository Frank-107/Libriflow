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
 * Objeto de Acceso a Datos (DAO) encargado de gestionar las operaciones CRUD
 * y consultas relacionadas con las publicaciones oficiales administradas
 * directamente por LibriFlow.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
public class PublicacionAdministradorDao {

    /**
     * Inserta una nueva publicación oficial realizada por el administrador
     * y recupera el identificador generado por la base de datos.
     *
     * @param entidad Objeto {@link PublicacionAdministrador} que contiene
     *                el libro, sinopsis, cantidad, modalidades y precio.
     * @return Identificador generado de la publicación o {@code -1}
     *         si ocurre un error durante la inserción.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public int create(PublicacionAdministrador entidad) {

        String sql = """
                INSERT INTO Publicacion_Lf(
                    ID_Libro,
                    Sinopsis,
                    Cantidad,
                    Es_venta,
                    Es_renta,
                    Precio
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_PUBLICACION_LF"}
             )) {

            ps.setInt(1, entidad.getIdLibro());
            ps.setString(2, entidad.getSinopsis());
            ps.setInt(3, entidad.getCantidad());
            ps.setInt(4, entidad.getEsVenta());
            ps.setInt(5, entidad.getEsRenta());
            ps.setDouble(6, entidad.getPrecio());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException("No se pudo insertar la publicación.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new SQLException(
                    "No se pudo obtener el ID de la publicación del administrador insertada."
            );

        } catch (SQLException e) {

            System.err.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Obtiene todas las publicaciones oficiales almacenadas en la base de datos.
     *
     * @return Lista de objetos {@link PublicacionAdministrador}. Si no existen
     *         publicaciones, se devuelve una lista vacía.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<PublicacionAdministrador> getAll() {

        List<PublicacionAdministrador> publicaciones = new ArrayList<>();

        String sql = """
                SELECT
                    id_publicacion_lf,
                    id_libro,
                    fecha_publicacion,
                    estado,
                    cantidad,
                    sinopsis,
                    es_venta,
                    es_renta,
                    precio
                FROM publicacion_lf
                ORDER BY id_publicacion_lf
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                publicaciones.add(mapearPublicacionAdministrador(rs));
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return publicaciones;
    }

    /**
     * Busca una publicación oficial por su identificador único.
     *
     * @param id Identificador de la publicación del administrador.
     * @return Objeto {@link PublicacionAdministrador} encontrado o {@code null}
     *         si no existe un registro con el identificador proporcionado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public PublicacionAdministrador getById(Integer id) {

        String sql = """
                SELECT
                    id_publicacion_lf,
                    id_libro,
                    fecha_publicacion,
                    estado,
                    cantidad,
                    sinopsis,
                    es_venta,
                    es_renta,
                    precio
                FROM publicacion_lf
                WHERE id_publicacion_lf = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearPublicacionAdministrador(rs);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }

    /**
     * Actualiza los datos modificables de una publicación oficial existente.
     *
     * La fecha de publicación se conserva porque corresponde al momento
     * original en que fue registrada la publicación.
     *
     * @param entidad Objeto {@link PublicacionAdministrador} con el identificador
     *                y los nuevos valores que serán almacenados.
     * @return {@code true} si la publicación fue actualizada correctamente;
     *         {@code false} si no existe o si ocurre un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean update(PublicacionAdministrador entidad) {

        String sql = """
                UPDATE publicacion_lf
                SET id_libro = ?,
                    estado = ?,
                    cantidad = ?,
                    sinopsis = ?,
                    es_venta = ?,
                    es_renta = ?,
                    precio = ?
                WHERE id_publicacion_lf = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entidad.getIdLibro());
            ps.setString(2, entidad.getEstado());
            ps.setInt(3, entidad.getCantidad());
            ps.setString(4, entidad.getSinopsis());
            ps.setInt(5, entidad.getEsVenta());
            ps.setInt(6, entidad.getEsRenta());
            ps.setDouble(7, entidad.getPrecio());
            ps.setInt(8, entidad.getIdPublicacionLf());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una publicación oficial y sus imágenes asociadas.
     *
     * La operación se realiza dentro de una transacción para evitar que
     * queden imágenes sin una publicación relacionada. El libro asociado
     * no se elimina, ya que representa una entidad independiente.
     *
     * @param id Identificador único de la publicación a eliminar.
     * @return {@code true} si la publicación fue eliminada correctamente;
     *         {@code false} si no existe o si ocurre un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean delete(Integer id) {

        Connection con = null;

        try {

            con = SQLconnector.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM imagen WHERE id_publicacion_lf = ?")) {

                ps.setInt(1, id);
                ps.executeUpdate();
            }

            int filas;

            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM publicacion_lf WHERE id_publicacion_lf = ?")) {

                ps.setInt(1, id);
                filas = ps.executeUpdate();
            }

            if (filas == 0) {
                con.rollback();
                return false;
            }

            con.commit();
            return true;

        } catch (SQLException e) {

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            e.printStackTrace();
            return false;

        } finally {

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Convierte la fila actual de un {@link ResultSet} en un objeto
     * {@link PublicacionAdministrador}.
     *
     * @param rs Resultado de la consulta posicionado en una fila válida.
     * @return Objeto {@link PublicacionAdministrador} construido con los datos
     *         recuperados de la base de datos.
     * @throws SQLException Si ocurre un error al leer las columnas del resultado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private PublicacionAdministrador mapearPublicacionAdministrador(ResultSet rs)
            throws SQLException {

        PublicacionAdministrador publicacion = new PublicacionAdministrador();

        publicacion.setIdPublicacionLf(
                rs.getInt("id_publicacion_lf")
        );

        publicacion.setIdLibro(
                rs.getInt("id_libro")
        );

        publicacion.setFechaPublicacion(
                rs.getString("fecha_publicacion")
        );

        publicacion.setEstado(
                rs.getString("estado")
        );

        publicacion.setCantidad(
                rs.getInt("cantidad")
        );

        publicacion.setSinopsis(
                rs.getString("sinopsis")
        );

        publicacion.setEsVenta(
                rs.getInt("es_venta")
        );

        publicacion.setEsRenta(
                rs.getInt("es_renta")
        );

        publicacion.setPrecio(
                rs.getDouble("precio")
        );

        return publicacion;
    }

    /**
     * Obtiene el listado simplificado de todas las publicaciones activas
     * del administrador para la vista de catálogo.
     *
     * @return Lista de objetos {@link PublicacionResumen} con los datos
     *         esenciales e imagen principal de cada publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<PublicacionResumen> getResumenCatalogo() {

        List<PublicacionResumen> lista = new ArrayList<>();

        String sql = """
                SELECT
                    plf.id_publicacion_lf,
                    plf.precio,
                    plf.cantidad,
                    l.titulo,
                    l.autor,
                    l.genero,
                    i.imagen
                FROM publicacion_lf plf
                JOIN libro l
                    ON plf.id_libro = l.id_libro
                JOIN imagen i
                    ON plf.id_publicacion_lf = i.id_publicacion_lf
                WHERE i.tipo = 1
                AND plf.estado = 'ACTIVO'
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                PublicacionResumen resumen = new PublicacionResumen();

                resumen.setIdPublicacion(
                        rs.getInt("id_publicacion_lf")
                );

                resumen.setTitulo(
                        rs.getString("titulo")
                );

                resumen.setIdPropietario(0);

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

                resumen.setEsLibriFlow(true);

                resumen.setCantidad(
                        rs.getInt("cantidad")
                );

                lista.add(resumen);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Consulta la información detallada e imágenes asociadas de una publicación
     * oficial utilizando su identificador.
     *
     * @param idPublicacionLf Identificador único de la publicación del administrador.
     * @return Objeto {@link PublicacionAdminCompleta} con los datos completos
     *         o {@code null} si la publicación no existe.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public PublicacionAdminCompleta getPublicacionAdminCompleta(
            int idPublicacionLf) {

        PublicacionAdminCompleta publicacion = null;

        String sql = """
                SELECT
                    plf.id_publicacion_lf,
                    plf.id_libro,
                    l.titulo,
                    l.autor,
                    l.editorial,
                    l.genero,
                    plf.sinopsis,
                    plf.precio,
                    plf.estado,
                    plf.cantidad,
                    plf.es_venta,
                    plf.es_renta,
                    plf.fecha_publicacion,
                    MAX(CASE WHEN i.tipo = 1 THEN i.imagen END) AS imagen_principal,
                    MAX(CASE WHEN i.tipo = 2 THEN i.imagen END) AS imagen_reverso,
                    MAX(CASE WHEN i.tipo = 3 THEN i.imagen END) AS imagen_interior
                FROM publicacion_lf plf
                JOIN libro l
                    ON plf.id_libro = l.id_libro
                LEFT JOIN imagen i
                    ON plf.id_publicacion_lf = i.id_publicacion_lf
                WHERE plf.id_publicacion_lf = ?
                GROUP BY
                    plf.id_publicacion_lf,
                    plf.id_libro,
                    l.titulo,
                    l.autor,
                    l.editorial,
                    l.genero,
                    plf.sinopsis,
                    plf.precio,
                    plf.estado,
                    plf.cantidad,
                    plf.es_venta,
                    plf.es_renta,
                    plf.fecha_publicacion
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPublicacionLf);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    publicacion = new PublicacionAdminCompleta();

                    publicacion.setIdPublicacionLf(
                            rs.getInt("id_publicacion_lf")
                    );

                    publicacion.setIdLibro(
                            rs.getInt("id_libro")
                    );

                    publicacion.setTitulo(
                            rs.getString("titulo")
                    );

                    publicacion.setAutor(
                            rs.getString("autor")
                    );

                    publicacion.setEditorial(
                            rs.getString("editorial")
                    );

                    publicacion.setGenero(
                            rs.getString("genero")
                    );

                    publicacion.setSinopsis(
                            rs.getString("sinopsis")
                    );

                    publicacion.setPrecio(
                            rs.getDouble("precio")
                    );

                    publicacion.setEstado(
                            rs.getString("estado")
                    );

                    publicacion.setCantidad(
                            rs.getInt("cantidad")
                    );

                    publicacion.setEsVenta(
                            rs.getInt("es_venta")
                    );

                    publicacion.setEsRenta(
                            rs.getInt("es_renta")
                    );

                    publicacion.setFechaPublicacion(
                            rs.getString("fecha_publicacion")
                    );

                    publicacion.setImagenPrincipal(
                            rs.getString("imagen_principal")
                    );

                    publicacion.setImagenReverso(
                            rs.getString("imagen_reverso")
                    );

                    publicacion.setImagenInterior(
                            rs.getString("imagen_interior")
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al obtener la publicación completa del administrador: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return publicacion;
    }

    /**
     * Reduce en una unidad el inventario disponible de una publicación
     * siempre que su cantidad actual sea mayor a cero.
     *
     * @param idPublicacion Identificador único de la publicación a actualizar.
     * @return {@code true} si se logró disminuir el inventario;
     *         {@code false} si no había existencias, no existe o ocurrió un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean disminuirInventario(int idPublicacion) {

        String sql = """
                UPDATE publicacion_lf
                SET cantidad = cantidad - 1
                WHERE id_publicacion_lf = ?
                AND cantidad > 0
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPublicacion);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cambia el estado de una publicación oficial a {@code INACTIVO}
     * como mecanismo de baja lógica.
     *
     * @param idPublicacionLf Identificador único de la publicación a inactivar.
     * @return {@code true} si el estado fue actualizado correctamente;
     *         {@code false} si la publicación no existe o ocurre un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean darDeBajaPublicacionAdmin(
            int idPublicacionLf) {

        String sql = """
                UPDATE publicacion_lf
                SET estado = 'INACTIVO'
                WHERE id_publicacion_lf = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPublicacionLf);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }
}