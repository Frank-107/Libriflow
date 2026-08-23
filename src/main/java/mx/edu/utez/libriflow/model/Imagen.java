package mx.edu.utez.libriflow.model;

/**
 * La clase Imagen representa la entidad de modelo encargada de la gestión
 * y vinculación de archivos de imagen o representaciones multimedia (cadenas Base64 o rutas)
 * asociadas a las publicaciones de los usuarios o del catálogo institucional LibriFlow.
 *
 * @author Fuentes Perez Francisco Emmanuel
 * @since 23/08/2026
 */
public class Imagen {

        private int idImagen;

        private int idPublicacionUs;
        private int idPublicacionLibriflow;

        private String imagen;


    /**
     * Constructor predeterminado de la clase Imagen.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     */
        public Imagen() {
        }

    /**
     * Constructor parametrizado para asociar una representación de imagen
     * a una publicación específica creada por un usuario.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param idPublicacionUsuario Identificador de la publicación del usuario.
     * @param imagen Cadena de texto que representa la imagen (Base64 o URL/Ruta).
     */
        public Imagen(int idPublicacionUsuario, String imagen) {
            this.idPublicacionUs = idPublicacionUsuario;
            this.imagen = imagen;
        }

    /**
     * Obtiene el identificador único del registro de la imagen.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Clave primaria numérica de la imagen.
     */
        public int getIdImagen() {
            return idImagen;
        }

    /**
     * Establece el identificador único del registro de la imagen.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param idImagen Clave primaria numérica a asignar.
     */
        public void setIdImagen(int idImagen) {
            this.idImagen = idImagen;
        }


    /**
     * Obtiene el identificador de la publicación de usuario vinculada.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Objeto Integer con el ID de la publicación de usuario.
     */
        public Integer getIdPublicacionUs() {
            return idPublicacionUs;
        }

    /**
     * Asocia la imagen a una publicación realizada por un usuario.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param idPublicacionUs Identificador de la publicación de usuario.
     */
        public void setIdPublicacionUs(int idPublicacionUs) {
            this.idPublicacionUs = idPublicacionUs;
        }

    /**
     * Obtiene el identificador de la publicación de LibriFlow vinculada.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Objeto Integer con el ID de la publicación de LibriFlow.
     */
        public Integer getIdPublicacionLibriflow() {
            return idPublicacionLibriflow;
        }

    /**
     * Asocia la imagen a una publicación oficial del catálogo LibriFlow.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param idPublicacionLibriflow Identificador de la publicación de LibriFlow.
     */
        public void setIdPublicacionLibriflow(int idPublicacionLibriflow) {
            this.idPublicacionLibriflow = idPublicacionLibriflow;
        }


    /**
     * Obtiene la cadena de texto con el contenido o dirección de la imagen.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Cadena que contiene los datos de la imagen (Base64, URL o ruta).
     */
        public String getImagen() {
            return imagen;
        }

    /**
     * Define el contenido o dirección de la imagen.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param imagen Cadena de texto con la información de la imagen.
     */
        public void setImagen(String imagen) {
            this.imagen = imagen;
        }


    }

