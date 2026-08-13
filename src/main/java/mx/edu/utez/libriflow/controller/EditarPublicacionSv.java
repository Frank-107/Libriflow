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

    private final PublicacionUsuarioDao publicacionDao =
            new PublicacionUsuarioDao();

    private final ImagenDao imagenDao =
            new ImagenDao();

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
                    publicacionDao
                            .getPublicacionUsuarioCompleta(idPublicacion);

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
                    publicacionDao
                            .getPublicacionUsuarioCompleta(idPublicacion);

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
                publicacionDao
                        .actualizarPublicacionCompleta(
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

    private void eliminarPublicacion(
            HttpServletRequest req,
            HttpServletResponse resp,
            HttpSession session,
            int idPublicacion,
            int idUsuario
    ) throws IOException {

        boolean eliminada =
                publicacionDao
                        .eliminarPublicacionPropietario(
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
                    "No se pudo eliminar la publicación."
            );

            regresarEdicion(
                    req,
                    resp,
                    idPublicacion
            );
        }
    }

    private String guardarImagen(
            Part imagen
    ) throws IOException {

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

    private boolean estaVacio(
            String texto
    ) {

        return texto == null
                || texto.trim().isEmpty();
    }

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