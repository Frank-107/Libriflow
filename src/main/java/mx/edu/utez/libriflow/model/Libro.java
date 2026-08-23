package mx.edu.utez.libriflow.model;

/**
 *
 * Esta clase representa la información de un libro dentro del sistema.
 * Permite almacenar datos como el identificador, título, autor, editorial
 * y género del libro.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 22/08/2026
 */
public class Libro {

    /**
     *
     * Identificador único del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private int idLibro;

    /**
     *
     * Título correspondiente al libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String titulo;

    /**
     *
     * Nombre del autor del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String autor;

    /**
     *
     * Editorial encargada de la publicación del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String editorial;

    /**
     *
     * Género al que pertenece el libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String genero;


    // Constructor vacío
    /**
     *
     * Este constructor permite crear un objeto Libro sin proporcionar
     * valores iniciales.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public Libro() {
    }


    // Constructor sin ID (para crear un libro nuevo)
    /**
     *
     * Este constructor permite crear un nuevo libro proporcionando su título,
     * autor, editorial y género, sin necesidad de establecer un identificador.
     *
     * @param titulo Es el título del libro.
     * @param autor Es el autor del libro.
     * @param editorial Es la editorial del libro.
     * @param genero Es el género al que pertenece el libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public Libro(String titulo,
                 String autor,
                 String editorial,
                 String genero) {

        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.genero = genero;
    }


    // Constructor completo
    /**
     *
     * Este constructor permite crear un libro proporcionando toda su
     * información, incluyendo su identificador, título, autor, editorial
     * y género.
     *
     * @param idLibro Es el identificador del libro.
     * @param titulo Es el título del libro.
     * @param autor Es el autor del libro.
     * @param editorial Es la editorial del libro.
     * @param genero Es el género al que pertenece el libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public Libro(int idLibro,
                 String titulo,
                 String autor,
                 String editorial,
                 String genero) {

        this.idLibro = idLibro;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.genero = genero;
    }


    /**
     *
     * Este método obtiene el identificador del libro.
     *
     * @return El identificador del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public int getIdLibro() {
        return idLibro;
    }

    /**
     *
     * Este método permite establecer el identificador del libro.
     *
     * @param idLibro Es el identificador que se desea asignar al libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }


    /**
     *
     * Este método obtiene el título del libro.
     *
     * @return El título del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     *
     * Este método permite establecer el título del libro.
     *
     * @param titulo Es el título que se desea asignar al libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    /**
     *
     * Este método obtiene el nombre del autor del libro.
     *
     * @return El autor del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getAutor() {
        return autor;
    }

    /**
     *
     * Este método permite establecer el autor del libro.
     *
     * @param autor Es el nombre del autor que se desea asignar al libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }


    /**
     *
     * Este método obtiene la editorial del libro.
     *
     * @return La editorial del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getEditorial() {
        return editorial;
    }

    /**
     *
     * Este método permite establecer la editorial del libro.
     *
     * @param editorial Es la editorial que se desea asignar al libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }


    /**
     *
     * Este método obtiene el género al que pertenece el libro.
     *
     * @return El género del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getGenero() {
        return genero;
    }

    /**
     *
     * Este método permite establecer el género del libro.
     *
     * @param genero Es el género que se desea asignar al libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }


    /**
     *
     * Este método genera una representación en texto de la información
     * almacenada en el objeto Libro.
     *
     * @return Una cadena de texto con los datos del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    @Override
    public String toString() {
        return "Libro{" +
                "idLibro=" + idLibro +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", editorial='" + editorial + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
}