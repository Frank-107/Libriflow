package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import mx.edu.utez.libriflow.model.Dao.ImagenDao;
import mx.edu.utez.libriflow.model.Dao.LibroDao;
import mx.edu.utez.libriflow.model.Dao.PublicacionAdministradorDao;
import mx.edu.utez.libriflow.model.Dao.RolDao;
import mx.edu.utez.libriflow.model.Imagen;
import mx.edu.utez.libriflow.model.Libro;
import mx.edu.utez.libriflow.model.PublicacionAdministrador;
import mx.edu.utez.libriflow.model.Usuario;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

/**
 * El servlet PublicarLibroAdminSv sirve para gestionar el alta de libros por parte
 * de un usuario con rol Administrador, validando la sesión, datos del formulario
 * y el almacenamiento de imágenes de la publicación.
 *
 * @author Irvin Abarca Arenas
 * @since 21/08/2026
 */

@WebServlet(
        name = "PublicarLibroAdminSv",
        value = "/publicar-libro-admin"
)
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1 MB
        maxFileSize = 1024 * 1024 * 5,        // 5 MB por archivo
        maxRequestSize = 1024 * 1024 * 20     // 20 MB por petición
)
public class PublicarLibroAdminSv extends HttpServlet {

    private final PublicacionAdministradorDao publicacionAdminDao = new PublicacionAdministradorDao();
    private final LibroDao libroDao = new LibroDao();
    private final ImagenDao imagenDao = new ImagenDao();
    private final RolDao rolDao = new RolDao(); // FIX: el rol vive en tabla ROL, no en Usuario

    // ============================================================
    // CONFIGURACIÓN DE SEGURIDAD
    // ============================================================
    // AJUSTAR: confirma el valor real con SELECT DISTINCT rol FROM rol;
    private static final String ROL_ADMIN = "ADMIN";
    private static final int MAX_TITULO = 150;
    private static final int MAX_AUTOR = 150;
    private static final int MAX_EDITORIAL = 150;
    private static final int MAX_GENERO = 100;
    private static final int SINOPSIS_MIN_PALABRAS = 100; // alineado con el flujo de usuario
    private static final int SINOPSIS_MAX_BYTES = 2900;
    private static final int MAX_CANTIDAD = 100000;
    private static final double MAX_PRECIO = 99999.99; // alineado con el límite del JSP de usuario
    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;
    private static final Set<String> EXTENSIONES_PERMITIDAS = Set.of("jpg", "jpeg", "png");
    private static final Set<String> MIME_PERMITIDOS = Set.of("image/jpeg", "image/png");

    /**
     * El método doGet sirve para verificar que la sesión pertenezca a un usuario
     * con rol de Administrador y mostrar el formulario de publicación de libros.
     *
     * @author Irvin Abarca Arenas
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP.
     * @param resp Objeto de respuesta HTTP.
     * @throws ServletException Si ocurre un error interno en el servlet.
     * @throws IOException Si ocurre un error de lectura o redirección.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/iniciar-sesion");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/iniciar-sesion");
            return;
        }

        String rol = rolDao.obtenerRol(usuario.getId());

        if (!ROL_ADMIN.equalsIgnoreCase(rol)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para acceder a esta sección.");
            return;
        }

        req.getRequestDispatcher("PublicarLibroAdministrador.jsp").forward(req, resp);
    }

    /**
     * El método doPost sirve para recibir y validar la información del libro,
     * crear el registro del libro, la publicación del administrador y guardar
     * las tres imágenes requeridas en el servidor.
     *
     * @author Irvin Abarca Arenas
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP con los datos del libro y archivos subidos.
     * @param resp Objeto de respuesta HTTP para manejo de vistas y redirecciones.
     * @throws ServletException Si ocurre un fallo en el despacho del formulario.
     * @throws IOException Si ocurre un error en la entrada/salida de datos o archivos.
     */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/iniciar-sesion");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/iniciar-sesion");
            return;
        }

        String rol = rolDao.obtenerRol(usuario.getId());

        if (!ROL_ADMIN.equalsIgnoreCase(rol)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para realizar esta acción.");
            return;
        }

        try {

            // ====================================================
            // OBTENER Y VALIDAR TEXTOS
            // ====================================================

            String titulo = limpiarTexto(req.getParameter("titulo"));
            String autor = limpiarTexto(req.getParameter("autor"));
            String editorial = limpiarTexto(req.getParameter("editorial"));
            String genero = limpiarTexto(req.getParameter("genero"));
            String sinopsis = limpiarTexto(req.getParameter("sinopsis"));

            validarTexto(titulo, "El título", 1, MAX_TITULO);
            validarTexto(autor, "El autor", 1, MAX_AUTOR);
            validarTexto(editorial, "La editorial", 1, MAX_EDITORIAL);
            validarTexto(genero, "El género", 1, MAX_GENERO);
            validarTexto(sinopsis, "La sinopsis", 1, Integer.MAX_VALUE); // longitud real se valida abajo

            // Mínimo de palabras, igual que en el flujo de usuario
            String[] palabras = sinopsis.split("\\s+");
            if (palabras.length < SINOPSIS_MIN_PALABRAS) {
                throw new IllegalArgumentException(
                        "La sinopsis debe tener al menos " + SINOPSIS_MIN_PALABRAS +
                                " palabras. Llevas " + palabras.length + "."
                );
            }

            // Máximo en bytes UTF-8, evita el error ORA-12899 de Oracle
            int sinopsisBytes = sinopsis.getBytes(StandardCharsets.UTF_8).length;
            if (sinopsisBytes > SINOPSIS_MAX_BYTES) {
                throw new IllegalArgumentException(
                        "La sinopsis es demasiado larga (" + sinopsisBytes +
                                " bytes). El máximo permitido es " + SINOPSIS_MAX_BYTES + " bytes."
                );
            }


            // ====================================================
            // PRECIO
            // ====================================================

            String paramPrecio = limpiarTexto(req.getParameter("precio"));
            double precio = 0.0;

            if (!paramPrecio.isEmpty()) {
                try {
                    precio = Double.parseDouble(paramPrecio);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("El precio debe ser un número válido.");
                }
            }

            if (Double.isNaN(precio) || Double.isInfinite(precio)) {
                throw new IllegalArgumentException("El precio no es válido.");
            }

            if (precio < 0) {
                throw new IllegalArgumentException("El precio no puede ser negativo.");
            }

            if (precio > MAX_PRECIO) {
                throw new IllegalArgumentException(
                        "El precio no puede superar $" + MAX_PRECIO + " MXN."
                );
            }


            // ====================================================
            // CANTIDAD
            // ====================================================

            String paramCantidad = limpiarTexto(req.getParameter("cantidad"));

            if (paramCantidad.isEmpty()) {
                throw new IllegalArgumentException("La cantidad es obligatoria.");
            }

            int cantidad;
            try {
                cantidad = Integer.parseInt(paramCantidad);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La cantidad debe ser un número entero.");
            }

            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
            }

            if (cantidad > MAX_CANTIDAD) {
                throw new IllegalArgumentException(
                        "La cantidad no puede superar " + MAX_CANTIDAD + " unidades."
                );
            }


            // ====================================================
            // VENTA / RENTA
            // ====================================================

            String paramVenta = req.getParameter("esVenta");
            int esVenta = "1".equals(paramVenta) ? 1 : 0;

            String paramRenta = req.getParameter("esRenta");
            int esRenta = "1".equals(paramRenta) ? 1 : 0;

            if (esVenta == 0 && esRenta == 0) {
                throw new IllegalArgumentException(
                        "La publicación debe permitir venta o renta."
                );
            }

            if (esVenta == 0) {
                precio = 0.0; // sin venta, no aplica precio de venta
            }



            Part imagen1 = req.getPart("imagen1");
            Part imagen2 = req.getPart("imagen2");
            Part imagen3 = req.getPart("imagen3");

            if (!esImagenValidaPresente(imagen1) ||
                    !esImagenValidaPresente(imagen2) ||
                    !esImagenValidaPresente(imagen3)) {
                throw new IllegalArgumentException(
                        "Debes cargar las 3 imágenes requeridas."
                );
            }

            String rutaImagen1 = guardarImagen(imagen1);
            String rutaImagen2 = guardarImagen(imagen2);
            String rutaImagen3 = guardarImagen(imagen3);


            // ====================================================
            // CREAR LIBRO
            // ====================================================

            Libro libro = new Libro(titulo, autor, editorial, genero);
            int idLibro = libroDao.create(libro);

            if (idLibro == -1) {
                throw new Exception("No se pudo guardar el libro principal.");
            }


            // ====================================================
            // CREAR PUBLICACIÓN
            // ====================================================

            PublicacionAdministrador publicacion = new PublicacionAdministrador();
            publicacion.setIdLibro(idLibro);
            publicacion.setPrecio(precio);
            publicacion.setCantidad(cantidad);
            publicacion.setEsVenta(esVenta);
            publicacion.setEsRenta(esRenta);
            publicacion.setSinopsis(sinopsis);

            int idPublicacion = publicacionAdminDao.create(publicacion);

            if (idPublicacion == -1) {
                throw new Exception("No se pudo guardar la publicación del administrador.");
            }


            // ====================================================
            // GUARDAR IMÁGENES ENLAZADAS
            // ====================================================

            Imagen objetoImagen1 = new Imagen();
            objetoImagen1.setIdPublicacionLibriflow(idPublicacion);
            objetoImagen1.setImagen(rutaImagen1);

            Imagen objetoImagen2 = new Imagen();
            objetoImagen2.setIdPublicacionLibriflow(idPublicacion);
            objetoImagen2.setImagen(rutaImagen2);

            Imagen objetoImagen3 = new Imagen();
            objetoImagen3.setIdPublicacionLibriflow(idPublicacion);
            objetoImagen3.setImagen(rutaImagen3);

            if (!imagenDao.createLf(objetoImagen1, 1) ||
                    !imagenDao.createLf(objetoImagen2, 2) ||
                    !imagenDao.createLf(objetoImagen3, 3)) {
                throw new RuntimeException("No se pudieron guardar las imágenes.");
            }


            // ====================================================
            // ÉXITO
            // ====================================================

            System.out.println("========== PUBLICACIÓN ADMIN EXITOSA ==========");
            System.out.println("ID Libro: " + idLibro);
            System.out.println("ID Pub Admin: " + idPublicacion);
            System.out.println("Título: " + titulo);
            System.out.println("Usuario administrador: " + usuario.getId() + " (" + rol + ")");
            System.out.println("===============================================");

            session.setAttribute("mensaje", "Libro publicado exitosamente.");

            resp.sendRedirect(req.getContextPath() + "/publicar-libro-admin");

        } catch (IllegalArgumentException e) {

            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("PublicarLibroAdministrador.jsp").forward(req, resp);

        } catch (Exception e) {

            e.printStackTrace();
            req.setAttribute("error", "Ocurrió un error al publicar el libro.");
            req.getRequestDispatcher("PublicarLibroAdministrador.jsp").forward(req, resp);
        }
    }

    /**
     * El método guardarImagen sirve para validar el formato, tamaño y extensión de una
     * imagen, comprobar que no sea un archivo corrupto y guardarla en el disco del servidor.
     *
     * @author Irvin Abarca Arenas
     * @since 21/08/2026
     *
     * @param imagen Parte del archivo recibido desde el formulario multipart.
     * @return Ruta relativa del archivo guardado en el directorio de subidas.
     * @throws IOException Si ocurre un problema al guardar el archivo en disco.
     */

    private String guardarImagen(Part imagen) throws IOException {

        if (imagen.getSize() <= 0) {
            throw new IllegalArgumentException("La imagen está vacía.");
        }

        if (imagen.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException(
                    "La imagen supera el tamaño máximo permitido de 5 MB."
            );
        }

        String contentType = imagen.getContentType();

        if (contentType == null) {
            throw new IllegalArgumentException("No se pudo determinar el tipo de imagen.");
        }

        contentType = contentType.toLowerCase().trim();

        if (!MIME_PERMITIDOS.contains(contentType)) {
            throw new IllegalArgumentException(
                    "Tipo de imagen no permitido. Solo se permiten JPG, JPEG y PNG."
            );
        }

        String nombreOriginal = imagen.getSubmittedFileName();

        if (nombreOriginal == null || nombreOriginal.trim().isEmpty()) {
            throw new IllegalArgumentException("La imagen no tiene un nombre válido.");
        }

        // Solo el nombre final, nunca confiar en rutas enviadas por el cliente
        nombreOriginal = new File(nombreOriginal).getName();

        int ultimoPunto = nombreOriginal.lastIndexOf('.');

        if (ultimoPunto <= 0 || ultimoPunto == nombreOriginal.length() - 1) {
            throw new IllegalArgumentException("La imagen debe tener una extensión válida.");
        }

        String extension = nombreOriginal.substring(ultimoPunto + 1).toLowerCase();

        if (!EXTENSIONES_PERMITIDAS.contains(extension)) {
            throw new IllegalArgumentException("Extensión de imagen no permitida.");
        }

        // Validación de contenido real: rechaza archivos falseados con extensión/MIME correctos
        BufferedImage bufferedImage = ImageIO.read(imagen.getInputStream());

        if (bufferedImage == null) {
            throw new IllegalArgumentException("El archivo enviado no es una imagen válida.");
        }

        String nombreUnico = UUID.randomUUID().toString().replace("-", "") + "." + extension;

        String uploadPath = getServletContext().getRealPath("")
                + File.separator + "uploads" + File.separator + "libros";

        File carpeta = new File(uploadPath);

        if (!carpeta.exists()) {
            if (!carpeta.mkdirs()) {
                throw new IOException("No se pudo crear el directorio de imágenes.");
            }
        }

        if (!carpeta.isDirectory()) {
            throw new IOException("La ruta de almacenamiento de imágenes no es válida.");
        }

        File archivoFinal = new File(carpeta, nombreUnico);
        imagen.write(archivoFinal.getAbsolutePath());

        return "uploads/libros/" + nombreUnico;
    }


    /**
     * El método esImagenValidaPresente sirve para determinar si una imagen fue
     * cargada correctamente en la solicitud y contiene un nombre válido.
     *
     * @author Irvin Abarca Arenas
     * @since 21/08/2026
     *
     * @param imagen Parte del archivo a evaluar.
     * @return true si la imagen está presente y no vacía; false en caso contrario.
     */
    private boolean esImagenValidaPresente(Part imagen) {
        return imagen != null
                && imagen.getSize() > 0
                && imagen.getSubmittedFileName() != null
                && !imagen.getSubmittedFileName().trim().isEmpty();
    }


    /**
     * El método limpiarTexto sirve para remover espacios en blanco sobrantes en
     * los extremos de un texto o devolver una cadena vacía en caso de ser nulo.
     *
     * @author Irvin Abarca Arenas
     * @since 21/08/2026
     *
     * @param texto Cadena de texto a limpiar.
     * @return Cadena formateada sin espacios sobrantes.
     */
    private String limpiarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }

    /**
     * El método validarTexto sirve para verificar que un campo de texto no sea nulo,
     * no esté vacío y cumpla con los límites de longitud mínima y máxima.
     *
     * @author Irvin Abarca Arenas
     * @since 21/08/2026
     *
     * @param texto Cadena a evaluar.
     * @param nombreCampo Nombre del campo utilizado para construir el mensaje de error.
     * @param minimo Cantidad mínima de caracteres permitidos.
     * @param maximo Cantidad máxima de caracteres permitidos.
     */
    private void validarTexto(String texto, String nombreCampo, int minimo, int maximo) {

        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " es obligatorio.");
        }

        int longitud = texto.trim().length();

        if (longitud < minimo) {
            throw new IllegalArgumentException(
                    nombreCampo + " debe tener al menos " + minimo + " caracteres."
            );
        }

        if (longitud > maximo) {
            throw new IllegalArgumentException(
                    nombreCampo + " no puede superar los " + maximo + " caracteres."
            );
        }
    }
}

