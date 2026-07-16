package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Imagen;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImagenDao {

    public boolean createUs(Imagen entidad) {
        String sql = "Insert into imagen(id_publicacion_us, imagen) values(?,?)";
        try(Connection con = SQLconnector.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);){
            ps.setInt(1, entidad.getIdPublicacionUs());
            ps.setString(2, entidad.getImagen());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas>0;
        }
         catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }



    }

}
