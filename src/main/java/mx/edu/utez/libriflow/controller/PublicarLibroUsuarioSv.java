package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import mx.edu.utez.libriflow.model.Dao.LibroDao;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.Libro;
import mx.edu.utez.libriflow.model.Publicacion;
import mx.edu.utez.libriflow.model.PublicacionUsuario;

import java.io.File;
import java.io.IOException;

@WebServlet(name = "PublicarLibroUsuarioSv", value = "/publicar-libro-usuario")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 5,   // 5 MB
        maxRequestSize = 1024 * 1024 * 20 // 20 MB (3 imágenes)
)
public class PublicarLibroUsuarioSv extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("PublicarLibroUsuario.jsp")
                .forward(req, resp);
    }


    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        try {

            String titulo = req.getParameter("titulo");
            String autor = req.getParameter("autor");
            String editorial = req.getParameter("editorial");
            String genero = req.getParameter("genero");
            String sinopsis = req.getParameter("sinopsis");
            double precio = Double.parseDouble(req.getParameter("precio"));


            Part imagen1 = req.getPart("imagen1");
            Part imagen2 = req.getPart("imagen2");
            Part imagen3 = req.getPart("imagen3");


            String rutaImagen1 = guardarImagen(imagen1);
            String rutaImagen2 = guardarImagen(imagen2);
            String rutaImagen3 = guardarImagen(imagen3);


            LibroDao libroDao = new LibroDao();
            PublicacionUsuarioDao publicacionDao = new PublicacionUsuarioDao();
            // 1. Crear libro
            Libro libro = new Libro(
                    titulo,
                    autor,
                    editorial,
                    genero
            );


            int idLibro = libroDao.create(libro);


            if(idLibro == -1){
                throw new Exception("No se pudo guardar el libro");
            }



            // 2. Crear publicación


            publicacion.setIdLibro(idLibro);

            publicacion.setFechaPublicacion(
                    LocalDate.now().toString()
            );

            publicacion.setEstado("ACTIVO");
            publicacion.setTipoServicio("RENTA");
            publicacion.setPrecio(precio);



            int idPublicacion = publicacionDao.create(publicacion);



            if(idPublicacion == -1){
                throw new Exception("No se pudo guardar la publicación");
            }




            // 3. Guardar imágenes

            imagenDao.create(idPublicacion, rutaImagen1);
            imagenDao.create(idPublicacion, rutaImagen2);
            imagenDao.create(idPublicacion, rutaImagen3);



            resp.sendRedirect("publicar-libro-usuario");



        } catch(Exception e){

            e.printStackTrace();

            req.setAttribute(
                    "error",
                    "No se pudo publicar el libro"
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


        // Carpeta física donde se guardan
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