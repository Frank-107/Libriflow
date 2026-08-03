package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mx.edu.utez.libriflow.model.Dao.ImagenDao;
import mx.edu.utez.libriflow.model.Dao.LibroDao;
import mx.edu.utez.libriflow.model.Dao.PublicacionAdministradorDao;
import mx.edu.utez.libriflow.model.Imagen;
import mx.edu.utez.libriflow.model.Libro;
import mx.edu.utez.libriflow.model.PublicacionAdministrador;

import java.io.File;
import java.io.IOException;

@WebServlet(name = "PublicarLibroAdminSv", value = "/publicar-libro-admin")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 5,   // 5 MB
        maxRequestSize = 1024 * 1024 * 20 // 20 MB (3 imágenes)
)
public class PublicarLibroAdminSv extends HttpServlet {

    private final PublicacionAdministradorDao publicacionAdminDao = new PublicacionAdministradorDao();
    private final LibroDao libroDao = new LibroDao();
    private final ImagenDao imagenDao = new ImagenDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("PublicarLibroAdministrador.jsp").forward(req, resp);
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

            String paramPrecio = req.getParameter("precio");
            double precio = 0.0;
            if (paramPrecio != null && !paramPrecio.trim().isEmpty()) {
                precio = Double.parseDouble(paramPrecio);
            }

            int cantidad = Integer.parseInt(req.getParameter("cantidad"));

            String paramVenta = req.getParameter("esVenta");
            int esVenta = (paramVenta != null && paramVenta.equals("1")) ? 1 : 0;

            String paramRenta = req.getParameter("esRenta");
            int esRenta = (paramRenta != null && paramRenta.equals("1")) ? 1 : 0;

            Part imagen1 = req.getPart("imagen1");
            Part imagen2 = req.getPart("imagen2");
            Part imagen3 = req.getPart("imagen3");

            String rutaImagen1 = guardarImagen(imagen1);
            String rutaImagen2 = guardarImagen(imagen2);
            String rutaImagen3 = guardarImagen(imagen3);

            Libro libro = new Libro(titulo, autor, editorial, genero);
            int idLibro = libroDao.create(libro);

            if(idLibro == -1){
                throw new Exception("No se pudo guardar el libro principal");
            }

            PublicacionAdministrador publicacion = new PublicacionAdministrador();
            publicacion.setIdLibro(idLibro);
            publicacion.setPrecio(precio);
            publicacion.setCantidad(cantidad);
            publicacion.setEsVenta(esVenta);
            publicacion.setEsRenta(esRenta);
            publicacion.setSinopsis(sinopsis);

            int idPublicacion = publicacionAdminDao.create(publicacion);

            if(idPublicacion == -1){
                throw new Exception("No se pudo guardar la publicación del administrador.");
            }

            Imagen objetoImagen1 = new Imagen();
            objetoImagen1.setIdPublicacionLibriflow(idPublicacion);
            objetoImagen1.setImagen(rutaImagen1);

            Imagen objetoImagen2 = new Imagen();
            objetoImagen2.setIdPublicacionLibriflow(idPublicacion);
            objetoImagen2.setImagen(rutaImagen2);

            Imagen objetoImagen3 = new Imagen();
            objetoImagen3.setIdPublicacionLibriflow(idPublicacion);
            objetoImagen3.setImagen(rutaImagen3);

            if(
                    !imagenDao.createLf(objetoImagen1, 1) ||
                            !imagenDao.createLf(objetoImagen2, 2) ||
                            !imagenDao.createLf(objetoImagen3, 3)
            ){
                throw new RuntimeException("No se pudieron enlazar las imágenes a la publicación");
            }
            /*Comprobacion en terminal*/
            System.out.println("========== PUBLICACIÓN ADMIN EXITOSA ==========");
            System.out.println("ID Libro: " + idLibro);
            System.out.println("ID Pub Admin: " + idPublicacion);
            System.out.println("Título: " + titulo);
            System.out.println("===============================================");
            /*---------------------------------------------------------------------*/

            req.getSession(false).setAttribute("mensaje", "Libro publicado exitosamente");
            resp.sendRedirect("publicar-libro-admin");

        } catch(Exception e){
            e.printStackTrace();
            System.err.println(e.getMessage());

            req.setAttribute("error", "Error al publicar: " + e.getMessage());
            req.getRequestDispatcher("PublicarLibroAdministrador.jsp").forward(req, resp);
        }
    }

    private String guardarImagen(Part imagen) throws IOException {
        if(imagen == null || imagen.getSubmittedFileName() == null || imagen.getSubmittedFileName().trim().isEmpty()){
            return null;
        }

        String nombreOriginal = imagen.getSubmittedFileName();
        String nombreUnico = System.currentTimeMillis() + "_" + nombreOriginal;

        String uploadPath = getServletContext().getRealPath("")
                + File.separator + "uploads" + File.separator + "libros";

        File carpeta = new File(uploadPath);
        if(!carpeta.exists()){
            carpeta.mkdirs();
        }

        imagen.write(uploadPath + File.separator + nombreUnico);
        return "uploads/libros/" + nombreUnico;
    }
}