package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Usuario;
import mx.edu.utez.libriflow.utils.SQLconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Iniciar_sesionDao implements Dao<Object, Object> {

    public boolean validarCredenciales(String correo, String contrasena){
        String sql = "SELECT Id_Usuario FROM Usuario where correo_electronico=(?)";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int idUsuario = rs.getInt("Id_Usuario");
                String sql2 = "SELECT * FROM Credencial where id_usuario=(?) and contrasena=(STANDARD_HASH(?,'SHA256'))";
                try (PreparedStatement ps2 = con.prepareStatement(sql2)) {
                    ps2.setInt(1, idUsuario);
                    ps2.setString(2, contrasena);
                    ResultSet rs2 = ps2.executeQuery();
                    if(rs2.next()){
                        return true;
                    }else{
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
    }
        return false;
    }



    public Usuario obtenerUsuario(String correo){
        String sql = "SELECT * FROM Usuario where correo_electronico=(?)";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            Usuario usuario = new Usuario();
            if(rs.next()){
                usuario.setId(rs.getInt("Id_Usuario"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setTelefono(rs.getString("Telefono"));
                usuario.setApellidoPaterno(rs.getString("Apellido_Paterno"));
                usuario.setApellidoMaterno(rs.getString("Apellido_Materno"));
                usuario.setCorreo(rs.getString("Correo_Electronico"));
            }
            return usuario;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public boolean create(Object entidad) {
        return false;
    }

    @Override
    public java.util.List<Object> getAll() {
        return null;
    }


    @Override
    public Object getById(Object id) {
        return null;
    }

    @Override
    public boolean update(Object entidad) {
        return false;
    }

    @Override
    public boolean delete(Object id) {
        return false;
    }
}
