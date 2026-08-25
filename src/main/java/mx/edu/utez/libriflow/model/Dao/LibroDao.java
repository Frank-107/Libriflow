package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Libro;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.*;

/**
 * Objeto de Acceso a Datos (DAO) encargado de gestionar las operaciones de persistencia
 * en la base de datos para la entidad {@link Libro}.
 *
 * @author Monserrath Anzurez
 * @since 23/08/26
 */
public class LibroDao {

    /**
     * Inserta un nuevo registro de libro en la base de datos y recupera la clave primaria generada.
     *
     * @param entidad Objeto {@link Libro} que contiene la información del título, autor, editorial y género.
     * @return El identificador entero (`ID_LIBRO`) generado por la base de datos, o `-1` si ocurre un error durante la inserción.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
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
     * Recupera la lista completa de libros registrados en la base de datos.
     *
     * @return Una lista de objetos {@link Libro}, o `null` si aún no está implementado.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    public java.util.List<Libro> getAll() {
        return null;
    }


    /**
     * Busca y obtiene un libro por su identificador único.
     *
     * @param id Identificador entero del libro a consultar.
     * @return El objeto {@link Libro} correspondiente, o `null` si no se encuentra o no está implementado.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    public Libro getById(Integer id) {
        return null;
    }


    /**
     * Actualiza la información de un libro existente en la base de datos.
     *
     * @param entidad Objeto {@link Libro} con los datos actualizados.
     * @return `true` si la actualización fue exitosa; `false` en caso contrario o si no está implementado.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    public boolean update(Libro entidad) {
        return false;
    }


    /**
     * Elimina un libro de la base de datos a partir de su identificador.
     *
     * @param id Identificador entero del libro a eliminar.
     * @return `true` si se eliminó correctamente; `false` en caso contrario o si no está implementado.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    public boolean delete(Integer id) {
        return false;
    }
}