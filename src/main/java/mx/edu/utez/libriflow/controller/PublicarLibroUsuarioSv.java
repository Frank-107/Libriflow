package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mx.edu.utez.libriflow.model.Dao.ImagenDao;
import mx.edu.utez.libriflow.model.Dao.LibroDao;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.Imagen;
import mx.edu.utez.libriflow.model.Libro;
import mx.edu.utez.libriflow.model.PublicacionUsuario;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.File;
import java.io.IOException;

@WebServlet(name = "PublicarLibroUsuarioSv", value = "/publicar-libro-usuario")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 20
)
public class PublicarLibroUsuarioSv extends HttpServlet {
    private final PublicacionUsuarioDao publicacionDao = new PublicacionUsuarioDao();
    private final LibroDao libroDao = new LibroDao();
    private final ImagenDao imagenDao = new ImagenDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("PublicarLibroUsuario.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            String titulo = req.getParameter("titulo");
            String autor = req.getParameter("autor");
            String editorial = req.getParameter("editorial");
            String genero = req.getParameter("genero");
            String sinopsis = req.getParameter("sinopsis");
            String precioStr = req.getParameter("precio");

            double precio = 0;
            try {
                if (precioStr != null) {
                    precio = Double.parseDouble(precioStr);
                }
            } catch (NumberFormatException e) {
                precio = 0;
            }

            if (precio <= 0) {
                throw new Exception("El precio del libro debe ser mayor a $0 MXN.");
            }

            double gananciaUsuario = Math.round((precio * 0.85) * 100.0) / 100.0;

            Part imagen1 = req.getPart("imagen1");
            Part imagen2 = req.getPart("imagen2");
            Part imagen3 = req.getPart("imagen3");

            String[] palabras = (sinopsis != null) ? sinopsis.trim().split(" ") : new String[0];
            int totalPalabras = 0;

            for (String palabra : palabras) {
                if (!palabra.trim().isEmpty()) {
                    totalPalabras++;
                }
            }

            if (totalPalabras < 100) {
                throw new Exception("La sinopsis debe tener al menos 100 palabras. Llevas " + totalPalabras + ".");
            }

            if (imagen1 == null || imagen1.getSize() == 0 ||
                    imagen2 == null || imagen2.getSize() == 0 ||
                    imagen3 == null || imagen3.getSize() == 0) {
                throw new Exception("Debes cargar las 3 imágenes requeridas del libro.");
            }

            String rutaImagen1 = guardarImagen(imagen1);
            String rutaImagen2 = guardarImagen(imagen2);
            String rutaImagen3 = guardarImagen(imagen3);

            Libro libro = new Libro(
                    titulo,
                    autor,
                    editorial,
                    genero
            );

            int idLibro = libroDao.create(libro);

            if(idLibro == -1){
                throw new Exception("No se pudo guardar el libro.");
            }

            Usuario usuario = (Usuario) req.getSession(false).getAttribute("usuario");
            int idUsuario = usuario.getId();

            PublicacionUsuario publicacion = new PublicacionUsuario();
            publicacion.setIdUsuario(idUsuario);
            publicacion.setIdLibro(idLibro);
            publicacion.setPrecio(precio);
            publicacion.setSinopsis(sinopsis);

            int idPublicacion = publicacionDao.create(publicacion);

            if(idPublicacion == -1){
                throw new Exception("No se pudo guardar la publicación, inténtalo nuevamente.");
            }

            Imagen objetoImagen1 = new Imagen(idPublicacion, rutaImagen1);
            Imagen objetoImagen2 = new Imagen(idPublicacion, rutaImagen2);
            Imagen objetoImagen3 = new Imagen(idPublicacion, rutaImagen3);

            if(
                    !imagenDao.createUs(objetoImagen1, 1) ||
                            !imagenDao.createUs(objetoImagen2, 2) ||
                            !imagenDao.createUs(objetoImagen3, 3)
            ){
                throw new RuntimeException("No se pudieron guardar las imágenes.");
            }

            req.getSession(false).setAttribute("mensaje", "Listo, Entrega tu libro en la librería para completar la publicación.");
            resp.sendRedirect("publicar-libro-usuario?exito=true");

        } catch(Exception e){
            e.printStackTrace();

            req.setAttribute(
                    "error",
                    e.getMessage() != null ? e.getMessage() : "No se pudo publicar el libro."
            );

            req.getRequestDispatcher(
                    "PublicarLibroUsuario.jsp"
            ).forward(req, resp);
        }
    }

    private String guardarImagen(Part imagen) throws IOException {
        if(imagen == null || imagen.getSubmittedFileName() == null
                || imagen.getSubmittedFileName().isEmpty()){
            return null;
        }

        String nombreOriginal = imagen.getSubmittedFileName();
        String nombreUnico = System.currentTimeMillis()
                + "_"
                + nombreOriginal;

        String uploadPath = getServletContext().getRealPath("")
                + File.separator
                + "uploads"
                + File.separator
                + "libros";

        File carpeta = new File(uploadPath);

        if(!carpeta.exists()){
            carpeta.mkdirs();
        }

        imagen.write(
                uploadPath + File.separator + nombreUnico
        );

        return "uploads/libros/" + nombreUnico;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    }
}