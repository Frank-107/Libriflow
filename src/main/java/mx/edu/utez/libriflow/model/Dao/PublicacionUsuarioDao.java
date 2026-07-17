package mx.edu.utez.libriflow.model.Dao;
import mx.edu.utez.libriflow.model.PublicacionUsuario;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
