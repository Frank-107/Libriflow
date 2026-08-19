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
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@WebServlet(name = "PublicarLibroUsuarioSv", value = "/publicar-libro-usuario")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,      // 5MB por archivo
        maxRequestSize = 1024 * 1024 * 20   // 20MB en total
)
public class PublicarLibroUsuarioSv extends HttpServlet {
    private final PublicacionUsuarioDao publicacionDao = new PublicacionUsuarioDao();
    private final LibroDao libroDao = new LibroDao();
    private final ImagenDao imagenDao = new ImagenDao();

    // --- CONSTANTES DE VALIDACIÓN (deben coincidir con la BD y el JSP) ---
    private static final double PRECIO_MAXIMO = 99999.99;
    private static final int SINOPSIS_MIN_PALABRAS = 100;
    private static final int SINOPSIS_MAX_BYTES = 2900; // margen bajo el límite real de Oracle: VARCHAR2(3000 BYTE)
    private static final int TITULO_MAX_LEN = 150;
    private static final int AUTOR_MAX_LEN = 100;
    private static final int EDITORIAL_MAX_LEN = 100;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("PublicarLibroUsuario.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // 1. VALIDACIÓN DE SESIÓN (CRÍTICO)
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                resp.sendRedirect("login.jsp");
                return;
            }
            Usuario usuario = (Usuario) session.getAttribute("usuario");

            // 2. RECUPERAR PARÁMETROS
            String titulo = req.getParameter("titulo");
            String autor = req.getParameter("autor");
            String editorial = req.getParameter("editorial");
            String genero = req.getParameter("genero");
            String sinopsis = req.getParameter("sinopsis");
            String precioStr = req.getParameter("precio");

            // 3. VALIDACIÓN DE CAMPOS NULOS O VACÍOS
            if (esNuloOVacio(titulo) || esNuloOVacio(autor) ||
                    esNuloOVacio(editorial) || esNuloOVacio(genero) || esNuloOVacio(sinopsis)) {
                throw new Exception("Todos los campos de texto son obligatorios.");
            }

            titulo = titulo.trim();
            autor = autor.trim();
            editorial = editorial.trim();
            genero = genero.trim();
            sinopsis = sinopsis.trim();

            // 3.1 VALIDACIÓN DE LONGITUD DE CAMPOS DE TEXTO
            if (titulo.length() > TITULO_MAX_LEN) {
                throw new Exception("El título no puede superar " + TITULO_MAX_LEN + " caracteres.");
            }
            if (autor.length() > AUTOR_MAX_LEN) {
                throw new Exception("El autor no puede superar " + AUTOR_MAX_LEN + " caracteres.");
            }
            if (editorial.length() > EDITORIAL_MAX_LEN) {
                throw new Exception("La editorial no puede superar " + EDITORIAL_MAX_LEN + " caracteres.");
            }

            // 4. VALIDACIÓN DE PRECIO
            double precio;
            try {
                precio = Double.parseDouble(precioStr.trim());
            } catch (NumberFormatException e) {
                throw new Exception("El precio ingresado no tiene un formato válido.");
            }

            if (precio <= 0) {
                throw new Exception("El precio del libro debe ser mayor a $0 MXN.");
            }
            if (precio > PRECIO_MAXIMO) {
                throw new Exception("El precio no puede superar $" + PRECIO_MAXIMO + " MXN.");
            }

            // 5. VALIDACIÓN DE SINOPSIS — mínimo de palabras
            String[] palabras = sinopsis.split("\\s+");
            if (palabras.length < SINOPSIS_MIN_PALABRAS) {
                throw new Exception("La sinopsis debe tener al menos " + SINOPSIS_MIN_PALABRAS +
                        " palabras. Llevas " + palabras.length + ".");
            }

            // 5.1 VALIDACIÓN DE SINOPSIS — máximo de bytes (evita el error ORA-12899 de Oracle)
            int sinopsisBytes = sinopsis.getBytes(StandardCharsets.UTF_8).length;
            if (sinopsisBytes > SINOPSIS_MAX_BYTES) {
                throw new Exception("La sinopsis es demasiado larga (" + sinopsisBytes +
                        " bytes). El máximo permitido es " + SINOPSIS_MAX_BYTES +
                        " bytes. Reduce el texto o los caracteres especiales (acentos, ñ).");
            }

            // 6. RECUPERAR Y VALIDAR IMÁGENES
            Part imagen1 = req.getPart("imagen1");
            Part imagen2 = req.getPart("imagen2");
            Part imagen3 = req.getPart("imagen3");

            if (!esImagenValida(imagen1) || !esImagenValida(imagen2) || !esImagenValida(imagen3)) {
                throw new Exception("Debes cargar 3 archivos de imagen válidos (JPG, JPEG, PNG o WEBP).");
            }

            // 7. GUARDAR ARCHIVOS FÍSICOS
            String rutaImagen1 = guardarImagen(imagen1);
            String rutaImagen2 = guardarImagen(imagen2);
            String rutaImagen3 = guardarImagen(imagen3);

            if (rutaImagen1 == null || rutaImagen2 == null || rutaImagen3 == null) {
                throw new Exception("Hubo un error al procesar las imágenes en el servidor.");
            }

            // 8. TRANSACCIÓN A BASE DE DATOS
            Libro libro = new Libro(titulo, autor, editorial, genero);
            int idLibro = libroDao.create(libro);

            if (idLibro == -1) {
                throw new Exception("No se pudo guardar la información del libro.");
            }

            PublicacionUsuario publicacion = new PublicacionUsuario();
            publicacion.setIdUsuario(usuario.getId());
            publicacion.setIdLibro(idLibro);
            publicacion.setPrecio(precio);
            publicacion.setSinopsis(sinopsis);

            int idPublicacion = publicacionDao.create(publicacion);

            if (idPublicacion == -1) {
                throw new Exception("No se pudo registrar la publicación, inténtalo nuevamente.");
            }

            Imagen objetoImagen1 = new Imagen(idPublicacion, rutaImagen1);
            Imagen objetoImagen2 = new Imagen(idPublicacion, rutaImagen2);
            Imagen objetoImagen3 = new Imagen(idPublicacion, rutaImagen3);

            if (!imagenDao.createUs(objetoImagen1, 1) ||
                    !imagenDao.createUs(objetoImagen2, 2) ||
                    !imagenDao.createUs(objetoImagen3, 3)) {
                throw new Exception("Se publicó el libro, pero hubo un error al enlazar las imágenes en la base de datos.");
            }

            // 9. ÉXITO
            resp.sendRedirect("publicar-libro-usuario?exito=true");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error",
                    e.getMessage() != null ? e.getMessage() : "Error interno del servidor. No se pudo publicar.");
            req.getRequestDispatcher("PublicarLibroUsuario.jsp").forward(req, resp);
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private boolean esNuloOVacio(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean esImagenValida(Part part) {
        if (part == null || part.getSize() == 0 || part.getSubmittedFileName() == null) {
            return false;
        }

        String contentType = part.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return false;
        }

        String nombre = part.getSubmittedFileName().toLowerCase();
        return nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") ||
                nombre.endsWith(".png") || nombre.endsWith(".webp");
    }

    private String guardarImagen(Part imagen) throws IOException {
        String nombreOriginal = imagen.getSubmittedFileName();

        // Extraemos solo la extensión; descartamos el nombre original para
        // evitar path traversal, colisiones y caracteres inválidos.
        String extension = "";
        int puntoIdx = nombreOriginal.lastIndexOf('.');
        if (puntoIdx >= 0) {
            extension = nombreOriginal.substring(puntoIdx)
                    .toLowerCase()
                    .replaceAll("[^a-z0-9.]", "");
        }

        String nombreUnico = System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;

        String uploadPath = getServletContext().getRealPath("")
                + File.separator + "uploads" + File.separator + "libros";
        File carpeta = new File(uploadPath);

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        imagen.write(uploadPath + File.separator + nombreUnico);
        return "uploads/libros/" + nombreUnico;
    }
}