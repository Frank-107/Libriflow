package mx.edu.utez.libriflow.model;

public class Libro {

    private int idLibro;

    private String titulo;
    private String autor;
    private String editorial;
    private String genero;


    // Constructor vacío
    public Libro() {
    }


    // Constructor sin ID (para crear un libro nuevo)
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


    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }


    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }


    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }


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