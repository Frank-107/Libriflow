package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Libro;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de Acceso a Datos (DAO) encargado de gestionar las operaciones de persistencia
 * en la base de datos para la entidad {@link Libro}.
 *
 * Permite registrar, consultar, actualizar y eliminar libros almacenados
 * en la base de datos de LibriFlow.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
public class LibroDao {

    /**
     * Inserta un nuevo registro de libro en la base de datos y recupera
     * la clave primaria generada automáticamente.
     *
     * @param entidad Objeto {@link Libro} que contiene la información
     *                del título, autor, editorial y género.
     * @return El identificador generado del libro, o {@code -1}
     *         si ocurre un error durante la inserción.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public int create(Libro entidad) {

        String sql = "INSERT INTO Libro(titulo, autor, editorial, genero) VALUES(?, ?, ?, ?)";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_LIBRO"}
             );) {

            ps.setString(1, entidad.getTitulo());
            ps.setString(2, entidad.getAutor());
            ps.setString(3, entidad.getEditorial());
            ps.setString(4, entidad.getGenero());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException("No se pudo insertar el libro.");
            }

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new SQLException("No se pudo obtener el ID del libro insertado.");

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * Recupera todos los libros registrados en la base de datos.
     *
     * Cada registro encontrado es convertido en un objeto {@link Libro}
     * y agregado a una lista.
     *
     * @return Lista con todos los libros registrados. Si no existen libros,
     *         se devuelve una lista vacía.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public List<Libro> getAll() {

        List<Libro> libros = new ArrayList<>();

        String sql =
                "SELECT ID_LIBRO, TITULO, AUTOR, EDITORIAL, GENERO FROM LIBRO";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Libro libro = new Libro();

                libro.setIdLibro(
                        rs.getInt("ID_LIBRO")
                );

                libro.setTitulo(
                        rs.getString("TITULO")
                );

                libro.setAutor(
                        rs.getString("AUTOR")
                );

                libro.setEditorial(
                        rs.getString("EDITORIAL")
                );

                libro.setGenero(
                        rs.getString("GENERO")
                );

                libros.add(libro);
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return libros;
    }


    /**
     * Busca un libro registrado en la base de datos utilizando su
     * identificador único.
     *
     * @param id Identificador único del libro que se desea consultar.
     * @return Objeto {@link Libro} correspondiente al identificador recibido,
     *         o {@code null} si el libro no existe o ocurre un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public Libro getById(Integer id) {

        String sql =
                "SELECT ID_LIBRO, TITULO, AUTOR, EDITORIAL, GENERO " +
                        "FROM LIBRO WHERE ID_LIBRO = ?";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Libro libro = new Libro();

                    libro.setIdLibro(
                            rs.getInt("ID_LIBRO")
                    );

                    libro.setTitulo(
                            rs.getString("TITULO")
                    );

                    libro.setAutor(
                            rs.getString("AUTOR")
                    );

                    libro.setEditorial(
                            rs.getString("EDITORIAL")
                    );

                    libro.setGenero(
                            rs.getString("GENERO")
                    );

                    return libro;
                }
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Actualiza la información de un libro existente en la base de datos.
     *
     * Los datos modificados son el título, autor, editorial y género.
     * El libro es localizado mediante su identificador único.
     *
     * @param entidad Objeto {@link Libro} que contiene el identificador
     *                del libro y los nuevos datos que serán almacenados.
     * @return {@code true} si el libro fue actualizado correctamente;
     *         {@code false} si no se encontró el registro o ocurrió un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean update(Libro entidad) {

        String sql =
                "UPDATE LIBRO " +
                        "SET TITULO = ?, AUTOR = ?, EDITORIAL = ?, GENERO = ? " +
                        "WHERE ID_LIBRO = ?";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(
                    1,
                    entidad.getTitulo()
            );

            ps.setString(
                    2,
                    entidad.getAutor()
            );

            ps.setString(
                    3,
                    entidad.getEditorial()
            );

            ps.setString(
                    4,
                    entidad.getGenero()
            );

            ps.setInt(
                    5,
                    entidad.getIdLibro()
            );

            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Elimina un libro de la base de datos utilizando su identificador único.
     *
     * Si el libro está relacionado con otros registros mediante llaves foráneas,
     * la base de datos puede impedir su eliminación y el método devolverá
     * {@code false}.
     *
     * @param id Identificador único del libro que se desea eliminar.
     * @return {@code true} si el libro fue eliminado correctamente;
     *         {@code false} si no existe o si ocurre un error.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    public boolean delete(Integer id) {

        String sql =
                "DELETE FROM LIBRO WHERE ID_LIBRO = ?";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}