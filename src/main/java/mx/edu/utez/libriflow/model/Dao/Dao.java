package mx.edu.utez.libriflow.model.Dao;
import java.util.List;

/**
 * La interfaz genérica Dao define el contrato estándar de operaciones
 * fundamentadas en el patrón CRUD (Create, Read, Update, Delete) para la
 * capa de acceso a datos dentro de la plataforma LibriFlow.
 *
 * @param <T> Tipo del modelo de entidad administrado por el DAO.
 * @param <K> Tipo de dato del identificador o clave primaria de la entidad.
 *
 * @author Fuentes Perez Francisco Emmanuel
 * @since 23/08/2026
 */
public interface Dao<T, K> {

    /**
     * Inserta un nuevo registro de la entidad en la base de datos.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param entidad Objeto con la información de la entidad a registrar.
     * @return {@code true} si el registro se creó exitosamente; {@code false} en caso contrario.
     */
    boolean create(T entidad);

    /**
     * Consulta y obtiene la lista completa de entidades registradas en la base de datos.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Objeto {@link List} que contiene todos los registros existentes.
     */
    List<T> getAll();

    /**
     * Recupera una entidad específica a través de su clave primaria.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param id Clave primaria o identificador de la entidad a buscar.
     * @return Objeto de tipo {@code T} correspondiente al identificador suministrado,
     *         o {@code null} si no se encuentra.
     */
    T getById(K id);

    /**
     * Actualiza la información de un registro existente en la base de datos.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param entidad Objeto con los datos modificados de la entidad.
     * @return {@code true} si la actualización fue exitosa; {@code false} en caso contrario.
     */
    boolean update(T entidad);

    /**
     * Remueve o elimina un registro de la base de datos por medio de su identificador único.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param id Clave primaria o identificador único del registro a eliminar.
     * @return {@code true} si la eliminación se realizó con éxito; {@code false} en caso contrario.
     */
    boolean delete(K id);
}