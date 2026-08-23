package mx.edu.utez.libriflow.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import mx.edu.utez.libriflow.model.Dao.ResenaDao;
import mx.edu.utez.libriflow.model.Resena;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;

/**
 * Controlador Servlet encargado de gestionar el envío y registro de reseñas de los libros.
 *
 * @author Irvin Abarca Arenas
 * @since 22/08/2026
 */
@WebServlet(name = "ResenaSv", value = "/resena")
public class ResenaSv extends HttpServlet {

    private final ResenaDao resenaDao = new ResenaDao();

    /**
     * Procesa la petición POST para crear una nueva reseña de un libro.
     * Valida que el usuario tenga sesión activa, que haya adquirido/rentado el libro
     * y que la calificación y comentario cumplan con los parámetros requeridos.
     *
     * @param req Objeto HttpServletRequest con la información de la solicitud y parámetros del formulario.
     * @param resp Objeto HttpServletResponse para redireccionar al usuario según el resultado.
     * @throws ServletException Si ocurre una falla en el Servlet.
     * @throws IOException Si ocurre un error de lectura/escritura durante el flujo HTTP.
     *
     * @author Irvin Abarca Arenas
     * @since 22/08/2026
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null)
                ? (Usuario) session.getAttribute("usuario")
                : null;


        if (usuario == null) {
            resp.sendRedirect("iniciar-sesion");
            return;
        }

        try {

            int idPublicacion = Integer.parseInt(req.getParameter("idPublicacion"));
            String comentario = req.getParameter("comentario");
            int calificacion = Integer.parseInt(req.getParameter("calificacion"));

            if (!resenaDao.usuarioHaCompradoORentado(usuario.getId(), idPublicacion)) {
                session.setAttribute("error", "Debes comprar o rentar el libro.");
                resp.sendRedirect("detalle-publicacion-superad?idPublicacion=" + idPublicacion);
                return;
            }

            if (comentario == null || comentario.trim().isEmpty()
                    || calificacion < 1 || calificacion > 5) {

                session.setAttribute("error", "Datos inválidos.");
                resp.sendRedirect("detalle-publicacion-superad?idPublicacion=" + idPublicacion);
                return;
            }
            Resena resena = new Resena(
                    usuario.getId(),
                    idPublicacion,
                    comentario.trim(),
                    calificacion
            );

            if (resenaDao.create(resena)) {
                session.setAttribute("mensaje", "¡Reseña publicada!");
            } else {
                session.setAttribute("error", "Error al guardar.");
            }

            resp.sendRedirect("detalle-publicacion-superad?idPublicacion=" + idPublicacion);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("inicio");
        }
    }
}