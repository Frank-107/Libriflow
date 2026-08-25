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
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionUsuarioCompleta;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Controlador Servlet encargado de la gestión para la edición y eliminación de publicaciones de usuarios.
 * Valida la autenticación, los permisos de propiedad sobre la publicación, el estado del registro,
 * la actualización de metadatos del libro y el procesamiento/almacenamiento de archivos de imagen.
 *
 * @author Monserrath Anzurez
 * @since 23/08/26
 */
@WebServlet(
        name = "EditarPublicacionSv",
        value = "/editar-publicacion"
)
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 20
)
public class EditarPublicacionSv extends HttpServlet {

    /** Objeto DAO para consultar y modificar la información de las publicaciones de usuarios. */
    private final PublicacionUsuarioDao publicacionDao =
            new PublicacionUsuarioDao();

    /** Objeto DAO para gestionar el almacenamiento y actualización de imágenes de publicaciones. */
    private final ImagenDao imagenDao =
            new ImagenDao();

    /**
     * Procesa las peticiones GET para cargar el formulario de edición de una publicación.
     * Valida la sesión de usuario, verifica la existencia de la publicación, comprueba que el usuario
     * sea el propietario legítimo y evalúa si el estado permite su modificación.
     *
     * @param req Objeto HttpServletRequest con el parámetro `idPublicacion` y la sesión del usuario.
     * @param resp Objeto HttpServletResponse para manejar las redirecciones o códigos de error HTTP.
     * @throws ServletException Si ocurre un error en el reenvío de la vista JSP.
     * @throws IOException Si ocurre un error de lectura/escritura durante el flujo de datos HTTP.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/iniciar-sesion");
            return;
        }

        Usuario usuario =
                (Usuario) session.getAttribute("usuario");

        try {
            int idPublicacion =
                    Integer.parseInt(
                            req.getParameter("idPublicacion")
                    );

            PublicacionUsuarioCompleta publicacion =
                    publicacionDao.getPublicacionUsuarioCompleta(
                            idPublicacion
                    );

            if (publicacion == null) {
                resp.sendRedirect(
                        req.getContextPath()
                                + "/mis-publicaciones-js"
                );
                return;
            }

            if (publicacion.getIdPropietario()
                    != usuario.getId()) {

                resp.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "No tienes permiso para editar esta publicación."
                );

                return;
            }

            if (!puedeModificar(publicacion.getEstado())) {

                session.setAttribute(
                        "error",
                        "Esta publicación ya no puede modificarse."
                );

                resp.sendRedirect(
                        req.getContextPath()
                                + "/detalle-publicacion?idPublicacion="
                                + idPublicacion
                );

                return;
            }

            req.setAttribute(
                    "publicacion",
                    publicacion
            );

            req.getRequestDispatcher(
                    "/EditarPublicacion.jsp"
            ).forward(req, resp);

        } catch (NumberFormatException e) {

            resp.sendRedirect(
                    req.getContextPath()
                            + "/mis-publicaciones-js"
            );
        }
    }

    /**
     * Procesa las peticiones POST para actualizar o eliminar una publicación de usuario.
     * Evalúa el parámetro de acción para dirigir la solicitud hacia la edición de campos e imágenes
     * o la eliminación física/lógica del registro.
     *
     * @param req Objeto HttpServletRequest con los parámetros del formulario multipart.
     * @param resp Objeto HttpServletResponse para enviar respuestas y redirecciones al usuario.
     * @throws ServletException Si ocurre una falla técnica en la ejecución del Servlet.
     * @throws IOException Si ocurre un error de E/S durante la recepción de la petición o procesamiento de archivos.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(
                    req.getContextPath()
                            + "/iniciar-sesion"
            );
            return;
        }

        Usuario usuario =
                (Usuario) session.getAttribute("usuario");

        try {

            int idPublicacion =
                    Integer.parseInt(
                            req.getParameter("idPublicacion")
                    );

            PublicacionUsuarioCompleta publicacion =
                    publicacionDao.getPublicacionUsuarioCompleta(
                            idPublicacion
                    );

            if (publicacion == null) {
                resp.sendRedirect(
                        req.getContextPath()
                                + "/mis-publicaciones-js"
                );
                return;
            }

            if (publicacion.getIdPropietario()
                    != usuario.getId()) {

                resp.sendError(
                        HttpServletResponse.SC_FORBIDDEN
                );

                return;
            }

            if (!puedeModificar(publicacion.getEstado())) {

                session.setAttribute(
                        "error",
                        "Esta publicación ya no puede editarse ni eliminarse."
                );

                resp.sendRedirect(
                        req.getContextPath()
                                + "/detalle-publicacion?idPublicacion="
                                + idPublicacion
                );

                return;
            }

            String accion =
                    req.getParameter("accion");

            if ("eliminar".equalsIgnoreCase(accion)) {

                eliminarPublicacion(
                        req,
                        resp,
                        session,
                        idPublicacion,
                        usuario.getId()
                );

                return;
            }

            editarPublicacion(
                    req,
                    resp,
                    session,
                    idPublicacion,
                    usuario.getId()
            );

        } catch (NumberFormatException e) {

            session.setAttribute(
                    "error",
                    "Publicación no válida."
            );

            resp.sendRedirect(
                    req.getContextPath()
                            + "/mis-publicaciones-js"
            );

        } catch (Exception e) {

            e.printStackTrace();

            session.setAttribute(
                    "error",
                    "Ocurrió un error al procesar la publicación."
            );

            resp.sendRedirect(
                    req.getContextPath()
                            + "/mis-publicaciones-js"
            );
        }
    }

    /**
     * Realiza la edición de los datos de la publicación e invoca la actualización de imágenes adjuntas.
     *
     * @param req Objeto HttpServletRequest con los datos textuales y binarios del formulario.
     * @param resp Objeto HttpServletResponse para controlar el flujo de respuesta.
     * @param session Objeto HttpSession utilizado para almacenar alertas de error o éxito.
     * @param idPublicacion Identificador único de la publicación a editar.
     * @param idUsuario Identificador único del usuario propietario.
     * @throws ServletException Si ocurre una falla en el Servlet.
     * @throws IOException Si ocurre un error al procesar la actualización o guardar imágenes.
     */
    private void editarPublicacion(
            HttpServletRequest req,
            HttpServletResponse resp,
            HttpSession session,
            int idPublicacion,
            int idUsuario
    ) throws ServletException, IOException {

        String titulo =
                req.getParameter("titulo");

        String autor =
                req.getParameter("autor");

        String editorial =
                req.getParameter("editorial");

        String genero =
                req.getParameter("genero");

        String sinopsis =
                req.getParameter("sinopsis");

        String precioTexto =
                req.getParameter("precio");

        if (estaVacio(titulo)
                || estaVacio(autor)
                || estaVacio(editorial)
                || estaVacio(genero)
                || estaVacio(sinopsis)
                || estaVacio(precioTexto)) {

            session.setAttribute(
                    "error",
                    "Completa todos los campos."
            );

            regresarEdicion(
                    req,
                    resp,
                    idPublicacion
            );

            return;
        }

        double precio;

        try {
            precio =
                    Double.parseDouble(
                            precioTexto
                    );
        } catch (NumberFormatException e) {

            session.setAttribute(
                    "error",
                    "El precio no es válido."
            );

            regresarEdicion(
                    req,
                    resp,
                    idPublicacion
            );

            return;
        }

        if (precio <= 0) {

            session.setAttribute(
                    "error",
                    "El precio debe ser mayor a 0."
            );

            regresarEdicion(
                    req,
                    resp,
                    idPublicacion
            );

            return;
        }

        boolean actualizado =
                publicacionDao.actualizarPublicacionCompleta(
                        idPublicacion,
                        idUsuario,
                        titulo.trim(),
                        autor.trim(),
                        editorial.trim(),
                        genero.trim(),
                        sinopsis.trim(),
                        precio
                );

        if (!actualizado) {

            session.setAttribute(
                    "error",
                    "No se pudieron guardar los cambios."
            );

            regresarEdicion(
                    req,
                    resp,
                    idPublicacion
            );

            return;
        }

        actualizarImagen(
                req.getPart("imagen1"),
                idPublicacion,
                1
        );

        actualizarImagen(
                req.getPart("imagen2"),
                idPublicacion,
                2
        );

        actualizarImagen(
                req.getPart("imagen3"),
                idPublicacion,
                3
        );

        session.setAttribute(
                "mensaje",
                "Publicación actualizada correctamente."
        );

        resp.sendRedirect(
                req.getContextPath()
                        + "/detalle-publicacion?idPublicacion="
                        + idPublicacion
        );
    }

    /**
     * Valida la extensión, guarda el archivo en el servidor y actualiza el registro de imagen en la base de datos.
     *
     * @param imagen Objeto Part recibido mediante la petición multipart.
     * @param idPublicacion Identificador único de la publicación asociada.
     * @param tipo Posición o índice ordinal de la imagen (1, 2 o 3).
     * @throws IOException Si el formato de la imagen no es compatible o falla su almacenamiento.
     */
    private void actualizarImagen(
            Part imagen,
            int idPublicacion,
            int tipo
    ) throws IOException {

        if (imagen == null
                || imagen.getSize() == 0
                || imagen.getSubmittedFileName() == null
                || imagen.getSubmittedFileName().isEmpty()) {

            return;
        }

        String contentType =
                imagen.getContentType();

        if (!"image/jpeg".equals(contentType)
                && !"image/png".equals(contentType)
                && !"image/webp".equals(contentType)) {

            throw new IOException(
                    "Formato de imagen no permitido."
            );
        }

        String rutaNueva =
                guardarImagen(imagen);

        boolean actualizada =
                imagenDao.actualizarImagenUs(
                        idPublicacion,
                        tipo,
                        rutaNueva
                );

        if (!actualizada) {
            throw new IOException(
                    "No se pudo actualizar una de las imágenes."
            );
        }
    }

    /**
     * Ejecuta el borrado de la publicación perteneciente al usuario propietario.
     *
     * @param req Objeto HttpServletRequest de la petición.
     * @param resp Objeto HttpServletResponse de la respuesta.
     * @param session Objeto HttpSession para asignar mensajes de notificación.
     * @param idPublicacion Identificador único de la publicación a eliminar.
     * @param idUsuario Identificador del propietario de la publicación.
     * @throws IOException Si ocurre un error al redireccionar.
     */
    private void eliminarPublicacion(
            HttpServletRequest req,
            HttpServletResponse resp,
            HttpSession session,
            int idPublicacion,
            int idUsuario
    ) throws IOException {

        boolean eliminada =
                publicacionDao.eliminarPublicacionPropietario(
                        idPublicacion,
                        idUsuario
                );

        if (eliminada) {

            session.setAttribute(
                    "mensaje",
                    "Publicación eliminada correctamente."
            );

            resp.sendRedirect(
                    req.getContextPath()
                            + "/mis-publicaciones-js"
            );

        } else {

            session.setAttribute(
                    "error",
                    "La publicación no puede eliminarse."
            );

            resp.sendRedirect(
                    req.getContextPath()
                            + "/detalle-publicacion?idPublicacion="
                            + idPublicacion
            );
        }
    }

    /**
     * Guarda el archivo físico enviado en la carpeta de destino dentro del servidor con un nombre único UUID.
     *
     * @param imagen Objeto Part recibido de la petición multipart.
     * @return Cadena con la ruta relativa del archivo guardado para su posterior persistencia en la base de datos.
     * @throws IOException Si ocurre una falla en el guardado físico dentro del sistema de archivos.
     */
    private String guardarImagen(Part imagen)
            throws IOException {

        String nombreOriginal =
                new File(
                        imagen.getSubmittedFileName()
                ).getName();

        String nombreUnico =
                UUID.randomUUID()
                        + "_"
                        + nombreOriginal;

        String uploadPath =
                getServletContext().getRealPath("")
                        + File.separator
                        + "uploads"
                        + File.separator
                        + "libros";

        File carpeta =
                new File(uploadPath);

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        imagen.write(
                uploadPath
                        + File.separator
                        + nombreUnico
        );

        return "uploads/libros/"
                + nombreUnico;
    }

    /**
     * Verifica si el estado actual de la publicación autoriza modificaciones por parte del usuario.
     *
     * @param estado Estado actual registrado para la publicación.
     * @return `true` si el estado es 'PENDIENTE' o 'RECHAZADO'; de lo contrario, `false`.
     */
    private boolean puedeModificar(String estado) {

        return "PENDIENTE".equalsIgnoreCase(estado)
                || "RECHAZADO".equalsIgnoreCase(estado);
    }

    /**
     * Evalúa si una cadena de texto es nula o se encuentra vacía de caracteres visibles.
     *
     * @param texto Cadena a evaluar.
     * @return `true` si es nula o vacía; de lo contrario, `false`.
     */
    private boolean estaVacio(String texto) {
        return texto == null
                || texto.trim().isEmpty();
    }

    /**
     * Redirige nuevamente al usuario a la vista de formulario de edición de la publicación.
     *
     * @param req Objeto HttpServletRequest con el contexto del Servlet.
     * @param resp Objeto HttpServletResponse para efectuar la redirección.
     * @param idPublicacion Identificador único de la publicación a volver a cargar.
     * @throws IOException Si ocurre un fallo en la redirección HTTP.
     */
    private void regresarEdicion(
            HttpServletRequest req,
            HttpServletResponse resp,
            int idPublicacion
    ) throws IOException {

        resp.sendRedirect(
                req.getContextPath()
                        + "/editar-publicacion?idPublicacion="
                        + idPublicacion
        );
    }
}