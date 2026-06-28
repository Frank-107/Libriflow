package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Usuario;
import mx.edu.utez.libriflow.utils.CSV;

public class UsuarioDao implements Dao<Usuario, Integer> {
    @Override
    public boolean create(Usuario entidad) {
        try {
            String linea = entidad.toString();
            CSV.addToCSV(linea);
            return true;

        }catch (Exception e){
            System.err.println("Error al crear el usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public java.util.List<Usuario> getAll() {
        return null;
    }

    @Override
    public Usuario getById(Integer id) {
        return null;
    }

    @Override
    public boolean update(Usuario entidad) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
