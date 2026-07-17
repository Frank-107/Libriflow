package mx.edu.utez.libriflow.model;

public class Imagen {

        private int idImagen;

        private int idPublicacionUs;
        private int idPublicacionLibriflow;

        private String imagen;


        public Imagen() {
        }

        public Imagen(int idPublicacionUsuario, String imagen) {
            this.idPublicacionUs = idPublicacionUsuario;
            this.imagen = imagen;
        }

        public int getIdImagen() {
            return idImagen;
        }

        public void setIdImagen(int idImagen) {
            this.idImagen = idImagen;
        }


        public Integer getIdPublicacionUs() {
            return idPublicacionUs;
        }

        public void setIdPublicacionUs(int idPublicacionUs) {
            this.idPublicacionUs = idPublicacionUs;
        }


        public Integer getIdPublicacionLibriflow() {
            return idPublicacionLibriflow;
        }

        public void setIdPublicacionLibriflow(int idPublicacionLibriflow) {
            this.idPublicacionLibriflow = idPublicacionLibriflow;
        }


        public String getImagen() {
            return imagen;
        }

        public void setImagen(String imagen) {
            this.imagen = imagen;
        }


    }

