package mx.edu.utez.libriflow.model;

import java.time.LocalDateTime;

/**
 * Clase de modelo (Entity/DTO) que representa una reseña o valoración realizada por un usuario
 * sobre una publicación en la plataforma LibriFlow.
 * Almacena la calificación numérica, el comentario explicativo, la fecha de creación y los identificadores
 * de referencia necesarios.
 *
 * @author Irvin
 * @since 24/08/2026
 */
public class Resena {

    /** Identificador único de la reseña. */
    private int idResena;

    /** Identificador del usuario que realiza la reseña. */
    private int idUsuario;

    /** Identificador de la publicación asociada en LibriFlow. */
    private int idPublicacionLf;

    /** Comentario descriptivo u opinión escrita por el usuario. */
    private String comentario;

    /** Calificación numérica otorgada (ej. escala de 1 a 5 estrellas). */
    private int calificacion;

    /** Fecha y hora exacta en la que se registró la reseña. */
    private LocalDateTime fecha;

    /** Nombre completo o alias del usuario autor de la reseña. */
    private String nombreUsuario;

    /**
     * Constructor predeterminado por defecto.
     */
    public Resena() {}

    /**
     * Constructor con parámetros para la creación de una nueva reseña.
     *
     * @param idUsuario Identificador del usuario que emite la valoración.
     * @param idPublicacionLf Identificador de la publicación valorada.
     * @param comentario Texto con la opinión o reseña del usuario.
     * @param calificacion Valor entero que asigna la puntuación otorgada.
     */
    public Resena(int idUsuario, int idPublicacionLf, String comentario, int calificacion) {
        this.idUsuario = idUsuario;
        this.idPublicacionLf = idPublicacionLf;
        this.comentario = comentario;
        this.calificacion = calificacion;
    }

    /**
     * Obtiene el identificador de la reseña.
     *
     * @return Entero con el ID de la reseña.
     */
    public int getIdResena() { return idResena; }

    /**
     * Establece el identificador de la reseña.
     *
     * @param idResena Nuevo identificador numérico de la reseña.
     */
    public void setIdResena(int idResena) { this.idResena = idResena; }

    /**
     * Obtiene el ID del usuario autor de la reseña.
     *
     * @return Entero con el ID del usuario.
     */
    public int getIdUsuario() { return idUsuario; }

    /**
     * Establece el ID del usuario autor de la reseña.
     *
     * @param idUsuario Identificador del usuario.
     */
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    /**
     * Obtiene el ID de la publicación valorada.
     *
     * @return Entero con el ID de la publicación.
     */
    public int getIdPublicacionLf() { return idPublicacionLf; }

    /**
     * Establece el ID de la publicación valorada.
     *
     * @param idPublicacionLf Identificador de la publicación en LibriFlow.
     */
    public void setIdPublicacionLf(int idPublicacionLf) { this.idPublicacionLf = idPublicacionLf; }

    /**
     * Obtiene el comentario escrito en la reseña.
     *
     * @return Cadena con la opinión expresada.
     */
    public String getComentario() { return comentario; }

    /**
     * Establece el comentario descriptivo de la reseña.
     *
     * @param comentario Texto con la opinión emitida.
     */
    public void setComentario(String comentario) { this.comentario = comentario; }

    /**
     * Obtiene la calificación numérica otorgada.
     *
     * @return Entero con la puntuación de la reseña.
     */
    public int getCalificacion() { return calificacion; }

    /**
     * Establece la calificación numérica de la reseña.
     *
     * @param calificacion Valor entero correspondiente a las estrellas o puntos asignados.
     */
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }

    /**
     * Obtiene la fecha y hora de emisión de la reseña.
     *
     * @return Objeto {@link LocalDateTime} con la marca de tiempo.
     */
    public LocalDateTime getFecha() { return fecha; }

    /**
     * Establece la fecha y hora de registro de la reseña.
     *
     * @param fecha Objeto {@link LocalDateTime} con la fecha.
     */
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    /**
     * Obtiene el nombre o alias del usuario autor.
     *
     * @return Cadena con el nombre del usuario.
     */
    public String getNombreUsuario() { return nombreUsuario; }

    /**
     * Establece el nombre del usuario autor de la reseña.
     *
     * @param nombreUsuario Nombre o alias del usuario.
     */
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
}