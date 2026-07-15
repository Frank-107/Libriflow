package mx.edu.utez.libriflow.model.Dao;
import mx.edu.utez.libriflow.model.PublicacionUsuario;

import java.util.List;

public class PublicacionUsuarioDao implements Dao<PublicacionUsuario, Integer> {

    @Override
    public boolean create(PublicacionUsuario entidad) {
        return false;
    }

    @Override
    public List<PublicacionUsuario> getAll() {
        return List.of();
    }

    @Override
    public PublicacionUsuario getById(Integer id) {
        return null;
    }

    @Override
    public boolean update(PublicacionUsuario entidad) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
