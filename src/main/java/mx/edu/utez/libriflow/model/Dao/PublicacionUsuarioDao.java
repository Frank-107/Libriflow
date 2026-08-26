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

/**
 * Objeto de Acceso a Datos (DAO) encargado de gestionar las operaciones CRUD y consultas
 * de persistencia sobre las publicaciones de usuarios (`publicacion_us`) en la base de datos.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
public class PublicacionUsuarioDao {

    /**
     * Inserta una nueva publicación enviada por un usuario con estado por defecto 'PENDIENTE'.
     *
     * @param entidad Objeto {@link PublicacionUsuario} con la información de usuario, libro, sinopsis y precio.
     * @return El identificador entero (`id_publicacion_us`) generado por la base de datos, o `-1` si ocurre un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public int create(PublicacionUsuario entidad) {

        String sql = """
                INSERT INTO publicacion_us(
                    id_usuario,
                    id_libro,
                    sinopsis,
                    precio,
                    estado
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"id_publicacion_us"}
             )) {

            ps.setInt(1, entidad.getIdUsuario());
            ps.setInt(2, entidad.getIdLibro());
            ps.setString(3, entidad.getSinopsis());
            ps.setDouble(4, entidad.getPrecio());
            ps.setString(5, "PENDIENTE");

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException(
                        "No se pudo insertar la publicación del usuario."
                );
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new SQLException(
                    "No se pudo obtener el ID de la publicación insertada."
            );

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Elimina físicamente una publicación por su ID, eliminando en cascada sus imágenes y el libro asociado.
     * <p><strong>Nota:</strong> Solo permite borrar publicaciones en estado 'PENDIENTE' o 'RECHAZADO'.</p>
     *
     * @param idPublicacion Identificador único de la publicación a eliminar.
     * @return `true` si la eliminación en transacción fue exitosa; `false` en caso contrario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean deletePublicacionById(int idPublicacion) {

        Connection con = null;

        try {

            con = SQLconnector.getConnection();
            con.setAutoCommit(false);

            int idLibro;

            String sqlBuscar = """
                    SELECT id_libro
                    FROM publicacion_us
                    WHERE id_publicacion_us = ?
                    AND estado IN ('PENDIENTE', 'RECHAZADO')
                    """;

            try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {

                ps.setInt(1, idPublicacion);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }

                    idLibro = rs.getInt("id_libro");
                }
            }

            String sqlImagenes = """
                    DELETE FROM imagen
                    WHERE id_publicacion_us = ?
                    """;

            try (PreparedStatement ps = con.prepareStatement(sqlImagenes)) {
                ps.setInt(1, idPublicacion);
                ps.executeUpdate();
            }

            String sqlPublicacion = """
                    DELETE FROM publicacion_us
                    WHERE id_publicacion_us = ?
                    AND estado IN ('PENDIENTE', 'RECHAZADO')
                    """;

            try (PreparedStatement ps = con.prepareStatement(sqlPublicacion)) {

                ps.setInt(1, idPublicacion);

                int filas = ps.executeUpdate();

                if (filas == 0) {
                    con.rollback();
                    return false;
                }
            }

            String sqlLibro = """
                    DELETE FROM libro
                    WHERE id_libro = ?
                    """;

            try (PreparedStatement ps = con.prepareStatement(sqlLibro)) {
                ps.setInt(1, idLibro);
                ps.executeUpdate();
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
     * Consulta y devuelve las publicaciones de un usuario ordenadas por fecha de creación.
     *
     * @param idUsuario Identificador del usuario.
     * @param orden Criterio de ordenamiento ('antiguas' para ascendente; de lo contrario, descendente).
     * @return Lista de objetos {@link PublicacionResumen} con las publicaciones del usuario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<PublicacionResumen> getResumenPublicacionesPorUsuario(
            int idUsuario,
            String orden) {

        List<PublicacionResumen> lista =
                new ArrayList<>();

        String orderBy;

        if ("antiguas".equalsIgnoreCase(orden)) {
            orderBy = "pu.fecha ASC";
        } else {
            orderBy = "pu.fecha DESC";
        }

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
                ORDER BY %s
                """.formatted(orderBy);

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    PublicacionResumen resumen =
                            new PublicacionResumen();

                    resumen.setIdPublicacion(
                            rs.getInt("id_publicacion_us")
                    );

                    resumen.setTitulo(
                            rs.getString("titulo")
                    );

                    resumen.setIdPropietario(
                            rs.getInt("id_usuario")
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

                    resumen.setEstado(
                            rs.getString("estado")
                    );

                    resumen.setEsLibriFlow(false);

                    lista.add(resumen);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Consulta el detalle completo de una publicación de usuario incluyendo sus imágenes asociadas.
     *
     * @param id Identificador único de la publicación del usuario.
     * @return Objeto {@link PublicacionUsuarioCompleta} cargado con todos los campos, o `null` si no existe.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public PublicacionUsuarioCompleta getPublicacionUsuarioCompleta(
            int id) {

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

                PublicacionUsuarioCompleta publicacion =
                        null;

                while (rs.next()) {

                    if (publicacion == null) {

                        publicacion =
                                new PublicacionUsuarioCompleta();

                        publicacion.setIdPublicacion(
                                rs.getInt("id_publicacion_us")
                        );

                        publicacion.setIdPropietario(
                                rs.getInt("id_usuario")
                        );

                        publicacion.setIdLibro(
                                rs.getInt("id_libro")
                        );

                        if (rs.getTimestamp("fecha") != null) {
                            publicacion.setFecha(
                                    rs.getTimestamp("fecha")
                                            .toLocalDateTime()
                            );
                        }

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

                        default:
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

    /**
     * Obtiene el resumen de publicaciones activas correspondientes a una lista de identificadores.
     * Utilizado para cargar los ítems guardados en el carrito de compras.
     *
     * @param ids Lista de enteros con los identificadores de las publicaciones.
     * @return Lista de objetos {@link PublicacionResumen} cuyo estado sea 'ACTIVO'.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<PublicacionResumen> getPublicacionesByArreglo(
            List<Integer> ids) {

        List<PublicacionResumen> lista =
                new ArrayList<>();

        if (ids == null || ids.isEmpty()) {
            return lista;
        }

        String placeholders =
                String.join(
                        ",",
                        Collections.nCopies(
                                ids.size(),
                                "?"
                        )
                );

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
                AND pu.estado = 'ACTIVO'
                AND pu.id_publicacion_us IN (%s)
                """.formatted(placeholders);

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(
                        i + 1,
                        ids.get(i)
                );
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    PublicacionResumen resumen =
                            new PublicacionResumen();

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

                    resumen.setEstado(
                            rs.getString("estado")
                    );

                    resumen.setEsLibriFlow(false);

                    lista.add(resumen);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Consulta todas las publicaciones pertenecientes a un usuario en particular.
     *
     * @param idUsuario Identificador único del usuario.
     * @return Lista de objetos {@link PublicacionResumen} vinculados al usuario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<PublicacionResumen> getResumenPublicacionesPorUsuario(
            int idUsuario) {

        List<PublicacionResumen> lista =
                new ArrayList<>();

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

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    PublicacionResumen resumen =
                            new PublicacionResumen();

                    resumen.setIdPublicacion(
                            rs.getInt("id_publicacion_us")
                    );

                    resumen.setTitulo(
                            rs.getString("titulo")
                    );

                    resumen.setIdPropietario(
                            rs.getInt("id_usuario")
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

                    resumen.setEstado(
                            rs.getString("estado")
                    );

                    resumen.setEsLibriFlow(false);

                    lista.add(resumen);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Transiciona el estado de una publicación conforme a las reglas del flujo del sistema:
     * <ul>
     *     <li>PENDIENTE &rarr; ACTIVO</li>
     *     <li>PENDIENTE &rarr; RECHAZADO</li>
     *     <li>RECHAZADO &rarr; PENDIENTE</li>
     *     <li>ACTIVO &rarr; VENDIDO</li>
     * </ul>
     *
     * @param idPublicacion Identificador único de la publicación.
     * @param nuevoEstado Cadena con el nuevo estado a asignar.
     * @return `true` si el estado fue actualizado exitosamente; `false` en caso contrario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean cambiarEstadoPublicacion(
            int idPublicacion,
            String nuevoEstado) {

        if (nuevoEstado == null) {
            return false;
        }

        String estado =
                nuevoEstado.trim().toUpperCase();

        String sql;

        switch (estado) {

            case "ACTIVO":
                sql = """
                        UPDATE publicacion_us
                        SET estado = 'ACTIVO'
                        WHERE id_publicacion_us = ?
                        AND estado = 'PENDIENTE'
                        """;
                break;

            case "RECHAZADO":
                sql = """
                        UPDATE publicacion_us
                        SET estado = 'RECHAZADO'
                        WHERE id_publicacion_us = ?
                        AND estado = 'PENDIENTE'
                        """;
                break;

            case "PENDIENTE":
                sql = """
                        UPDATE publicacion_us
                        SET estado = 'PENDIENTE'
                        WHERE id_publicacion_us = ?
                        AND estado = 'RECHAZADO'
                        """;
                break;

            case "VENDIDO":
                sql = """
                        UPDATE publicacion_us
                        SET estado = 'VENDIDO'
                        WHERE id_publicacion_us = ?
                        AND estado = 'ACTIVO'
                        """;
                break;

            default:
                return false;
        }

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idPublicacion
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene todas las publicaciones de usuarios registradas en la base de datos.
     *
     * @return Lista de objetos {@link PublicacionUsuario}. Si no existen registros,
     *         se devuelve una lista vacía.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<PublicacionUsuario> getAll() {

        List<PublicacionUsuario> publicaciones = new ArrayList<>();

        String sql = """
                SELECT
                    id_publicacion_us,
                    id_usuario,
                    id_libro,
                    fecha,
                    estado,
                    precio,
                    sinopsis
                FROM publicacion_us
                ORDER BY id_publicacion_us
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                publicaciones.add(mapearPublicacionUsuario(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publicaciones;
    }

    /**
     * Busca una publicación de usuario utilizando su identificador único.
     *
     * @param id Identificador único de la publicación.
     * @return Objeto {@link PublicacionUsuario} encontrado o {@code null}
     *         si no existe un registro con el identificador recibido.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public PublicacionUsuario getById(Integer id) {

        String sql = """
                SELECT
                    id_publicacion_us,
                    id_usuario,
                    id_libro,
                    fecha,
                    estado,
                    precio,
                    sinopsis
                FROM publicacion_us
                WHERE id_publicacion_us = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPublicacionUsuario(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Actualiza los datos principales de una publicación de usuario.
     *
     * El identificador de usuario, libro, sinopsis y precio son actualizados.
     * Si el estado recibido es {@code null}, se conserva el estado actual.
     *
     * @param entidad Objeto {@link PublicacionUsuario} que contiene el identificador
     *                de la publicación y los datos que serán actualizados.
     * @return {@code true} si el registro fue actualizado correctamente;
     *         {@code false} si no existe o si ocurre un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean update(PublicacionUsuario entidad) {

        String sql = """
                UPDATE publicacion_us
                SET id_usuario = ?,
                    id_libro = ?,
                    sinopsis = ?,
                    precio = ?,
                    estado = NVL(?, estado)
                WHERE id_publicacion_us = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entidad.getIdUsuario());
            ps.setInt(2, entidad.getIdLibro());
            ps.setString(3, entidad.getSinopsis());
            ps.setDouble(4, entidad.getPrecio());
            ps.setString(5, entidad.getEstado());
            ps.setInt(6, entidad.getIdPublicacionUs());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una publicación de usuario y las imágenes relacionadas con ella.
     * La eliminación se realiza dentro de una transacción para evitar datos inconsistentes.
     *
     * @param id Identificador único de la publicación que será eliminada.
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
                    "DELETE FROM imagen WHERE id_publicacion_us = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            int filas;

            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM publicacion_us WHERE id_publicacion_us = ?")) {
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
     * {@link PublicacionUsuario}.
     *
     * @param rs Resultado de la consulta posicionado en una fila válida.
     * @return Objeto {@link PublicacionUsuario} construido con los datos de la fila.
     * @throws SQLException Si ocurre un error al leer las columnas del resultado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private PublicacionUsuario mapearPublicacionUsuario(ResultSet rs)
            throws SQLException {

        PublicacionUsuario publicacion = new PublicacionUsuario();

        publicacion.setIdPublicacionUs(
                rs.getInt("id_publicacion_us")
        );
        publicacion.setIdUsuario(
                rs.getInt("id_usuario")
        );
        publicacion.setIdLibro(
                rs.getInt("id_libro")
        );
        publicacion.setFechaPublicacion(
                rs.getString("fecha")
        );
        publicacion.setEstado(
                rs.getString("estado")
        );
        publicacion.setPrecio(
                rs.getDouble("precio")
        );
        publicacion.setSinopsis(
                rs.getString("sinopsis")
        );

        return publicacion;
    }

    /**
     * Contabiliza el número total de publicaciones en estado 'ACTIVO' pertenecientes a un usuario.
     *
     * @param idUsuario Identificador único del usuario.
     * @return La cantidad total entera de publicaciones activas.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public int contarPublicacionesPorUsuario(
            int idUsuario) {

        String sql = """
                SELECT COUNT(*)
                FROM publicacion_us
                WHERE id_usuario = ?
                AND estado = 'ACTIVO'
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idUsuario
            );

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

    /**
     * Busca y filtra las publicaciones de los usuarios según el estado, coincidencia por título/autor y género literario.
     *
     * @param estado Estado de la publicación requerido (ej. 'ACTIVO').
     * @param busqueda Cadena opcional para filtrar por coincidencias en el título o autor del libro.
     * @param genero Cadena opcional con el género literario a filtrar.
     * @return Lista de objetos {@link PublicacionResumen} que cumplen los criterios de búsqueda.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<PublicacionResumen> buscarYFiltrarPublicacionesUs(
            String estado,
            String busqueda,
            String genero) {

        List<PublicacionResumen> lista =
                new ArrayList<>();

        StringBuilder sql =
                new StringBuilder("""
                        SELECT
                            pu.id_publicacion_us,
                            pu.id_usuario,
                            pu.precio,
                            pu.estado,
                            l.titulo,
                            l.autor,
                            l.genero,
                            i.imagen,
                            u.nombre
                        FROM publicacion_us pu
                        JOIN libro l
                            ON pu.id_libro = l.id_libro
                        JOIN imagen i
                            ON pu.id_publicacion_us = i.id_publicacion_us
                        JOIN usuario u
                            ON pu.id_usuario = u.id_usuario
                        WHERE i.tipo = 1
                        AND pu.estado = ?
                        """);

        if (busqueda != null
                && !busqueda.trim().isEmpty()) {

            sql.append("""
                     AND (
                        LOWER(l.titulo) LIKE ?
                        OR LOWER(l.autor) LIKE ?
                     )
                    """);
        }

        if (genero != null
                && !genero.trim().isEmpty()
                && !genero.equalsIgnoreCase("TODOS")) {

            sql.append("""
                     AND LOWER(l.genero) = LOWER(?)
                    """);
        }

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(
                             sql.toString()
                     )) {

            int index = 1;

            ps.setString(
                    index++,
                    estado
            );

            if (busqueda != null
                    && !busqueda.trim().isEmpty()) {

                String term =
                        "%"
                                + busqueda.trim().toLowerCase()
                                + "%";

                ps.setString(
                        index++,
                        term
                );

                ps.setString(
                        index++,
                        term
                );
            }

            if (genero != null
                    && !genero.trim().isEmpty()
                    && !genero.equalsIgnoreCase("TODOS")) {

                ps.setString(
                        index,
                        genero.trim()
                );
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    PublicacionResumen resumen =
                            new PublicacionResumen();

                    resumen.setIdPublicacion(
                            rs.getInt("id_publicacion_us")
                    );

                    resumen.setTitulo(
                            rs.getString("titulo")
                    );

                    resumen.setIdPropietario(
                            rs.getInt("id_usuario")
                    );

                    resumen.setAutor(
                            rs.getString("autor")
                    );

                    resumen.setGenero(
                            rs.getString("genero")
                    );

                    resumen.setNombrePropietario(
                            rs.getString("nombre")
                    );

                    resumen.setPrecio(
                            rs.getDouble("precio")
                    );

                    resumen.setImagenPrincipal(
                            rs.getString("imagen")
                    );

                    resumen.setEstado(
                            rs.getString("estado")
                    );

                    resumen.setEsLibriFlow(false);

                    lista.add(resumen);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Modifica el estado de una publicación de usuario a 'RECHAZADO' (baja lógica).
     *
     * @param idPublicacionUs Identificador único de la publicación a actualizar.
     * @return `true` si el estado se cambió correctamente; `false` en caso contrario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean darDeBajaPublicacionUsuario(int idPublicacionUs) {
        String sql = "UPDATE publicacion_us SET estado = 'RECHAZADO' WHERE id_publicacion_us = ?";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPublicacionUs);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al dar de baja la publicación del usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza la información del libro y la publicación dentro de una transacción.
     * <p><strong>Nota:</strong> Si la publicación estaba en estado 'RECHAZADO', vuelve automáticamente a 'PENDIENTE'.</p>
     *
     * @param idPublicacion Identificador único de la publicación.
     * @param idUsuario Identificador del usuario propietario.
     * @param titulo Nuevo título del libro.
     * @param autor Nuevo autor del libro.
     * @param editorial Nueva editorial del libro.
     * @param genero Nuevo género del libro.
     * @param sinopsis Nueva sinopsis del libro.
     * @param precio Nuevo precio asignado.
     * @return `true` si la actualización fue exitosa; `false` si no se pudo actualizar o falló la transacción.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean actualizarPublicacionCompleta(
            int idPublicacion,
            int idUsuario,
            String titulo,
            String autor,
            String editorial,
            String genero,
            String sinopsis,
            double precio) {

        Connection con = null;

        try {

            con = SQLconnector.getConnection();
            con.setAutoCommit(false);

            int idLibro;
            String estadoActual;

            String sqlBuscar = """
                    SELECT id_libro, estado
                    FROM publicacion_us
                    WHERE id_publicacion_us = ?
                    AND id_usuario = ?
                    AND estado IN ('PENDIENTE', 'RECHAZADO')
                    """;

            try (PreparedStatement ps =
                         con.prepareStatement(sqlBuscar)) {

                ps.setInt(
                        1,
                        idPublicacion
                );

                ps.setInt(
                        2,
                        idUsuario
                );

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }

                    idLibro =
                            rs.getInt("id_libro");

                    estadoActual =
                            rs.getString("estado");
                }
            }

            String sqlLibro = """
                    UPDATE libro
                    SET titulo = ?,
                        autor = ?,
                        editorial = ?,
                        genero = ?
                    WHERE id_libro = ?
                    """;

            try (PreparedStatement ps =
                         con.prepareStatement(sqlLibro)) {

                ps.setString(
                        1,
                        titulo
                );

                ps.setString(
                        2,
                        autor
                );

                ps.setString(
                        3,
                        editorial
                );

                ps.setString(
                        4,
                        genero
                );

                ps.setInt(
                        5,
                        idLibro
                );

                if (ps.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }

            String nuevoEstado =
                    "RECHAZADO".equalsIgnoreCase(
                            estadoActual
                    )
                            ? "PENDIENTE"
                            : estadoActual;

            String sqlPublicacion = """
                    UPDATE publicacion_us
                    SET sinopsis = ?,
                        precio = ?,
                        estado = ?
                    WHERE id_publicacion_us = ?
                    AND id_usuario = ?
                    AND estado IN ('PENDIENTE', 'RECHAZADO')
                    """;

            try (PreparedStatement ps =
                         con.prepareStatement(
                                 sqlPublicacion
                         )) {

                ps.setString(
                        1,
                        sinopsis
                );

                ps.setDouble(
                        2,
                        precio
                );

                ps.setString(
                        3,
                        nuevoEstado
                );

                ps.setInt(
                        4,
                        idPublicacion
                );

                ps.setInt(
                        5,
                        idUsuario
                );

                if (ps.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
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
     * Elimina una publicación junto con sus imágenes y libro asociado cuando pertenezca al usuario que lo solicita.
     * <p><strong>Nota:</strong> Solamente pueden eliminarse publicaciones en estado 'PENDIENTE' o 'RECHAZADO'.</p>
     *
     * @param idPublicacion Identificador de la publicación a eliminar.
     * @param idUsuario Identificador del propietario de la publicación.
     * @return `true` si la eliminación fue exitosa; `false` si falló la operación o no se cumplieron las reglas.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean eliminarPublicacionPropietario(
            int idPublicacion,
            int idUsuario) {

        Connection con = null;

        try {

            con = SQLconnector.getConnection();
            con.setAutoCommit(false);

            int idLibro;

            String sqlBuscar = """
                    SELECT id_libro
                    FROM publicacion_us
                    WHERE id_publicacion_us = ?
                    AND id_usuario = ?
                    AND estado IN ('PENDIENTE', 'RECHAZADO')
                    """;

            try (PreparedStatement ps =
                         con.prepareStatement(
                                 sqlBuscar
                         )) {

                ps.setInt(
                        1,
                        idPublicacion
                );

                ps.setInt(
                        2,
                        idUsuario
                );

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }

                    idLibro =
                            rs.getInt("id_libro");
                }
            }

            String sqlImagenes = """
                    DELETE FROM imagen
                    WHERE id_publicacion_us = ?
                    """;

            try (PreparedStatement ps =
                         con.prepareStatement(
                                 sqlImagenes
                         )) {

                ps.setInt(
                        1,
                        idPublicacion
                );

                ps.executeUpdate();
            }

            String sqlPublicacion = """
                    DELETE FROM publicacion_us
                    WHERE id_publicacion_us = ?
                    AND id_usuario = ?
                    AND estado IN ('PENDIENTE', 'RECHAZADO')
                    """;

            try (PreparedStatement ps =
                         con.prepareStatement(
                                 sqlPublicacion
                         )) {

                ps.setInt(
                        1,
                        idPublicacion
                );

                ps.setInt(
                        2,
                        idUsuario
                );

                int filas = ps.executeUpdate();

                if (filas == 0) {
                    con.rollback();
                    return false;
                }
            }

            String sqlLibro = """
                    DELETE FROM libro
                    WHERE id_libro = ?
                    """;

            try (PreparedStatement ps =
                         con.prepareStatement(
                                 sqlLibro
                         )) {

                ps.setInt(
                        1,
                        idLibro
                );

                ps.executeUpdate();
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
}