package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Libro;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.*;

public class LibroDao {

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


    public java.util.List<Libro> getAll() {
        return null;
    }


    public Libro getById(Integer id) {
        return null;
    }


    public boolean update(Libro entidad) {
        return false;
    }


    public boolean delete(Integer id) {
        return false;
    }
}