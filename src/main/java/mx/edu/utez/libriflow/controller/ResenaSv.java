package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mx.edu.utez.libriflow.model.Dao.ResenaDao;
import mx.edu.utez.libriflow.model.Resena;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;

@WebServlet(name = "ResenaSv", value = "/publicar-resena")
public class ResenaSv extends HttpServlet {

    private final ResenaDao resenaDao = new ResenaDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        String idPubStr = req.getParameter("idPublicacion");
        String comentario = req.getParameter("comentario");
        String calificacionStr = req.getParameter("calificacion");

        if (usuario == null) {
            resp.sendRedirect("iniciar-sesion");
            return;
        }

        try {
            int idPublicacion = Integer.parseInt(idPubStr);
            int calificacion = Integer.parseInt(calificacionStr);

            if (!resenaDao.usuarioHaCompradoORentado(usuario.getId(), idPublicacion)) {
                req.getSession().setAttribute("error", "Solo puedes reseñar libros que hayas comprado o rentado.");
                resp.sendRedirect("detalle-publicacion?id=" + idPublicacion);
                return;
            }

            if (comentario == null || comentario.trim().isEmpty() || calificacion < 1 || calificacion > 5) {
                req.getSession().setAttribute("error", "Por favor ingresa un comentario y una calificación válida.");
                resp.sendRedirect("detalle-publicacion?id=" + idPublicacion);
                return;
            }

            Resena resena = new Resena(usuario.getId(), idPublicacion, comentario.trim(), calificacion);
            boolean creada = resenaDao.create(resena);

            if (creada) {
                req.getSession().setAttribute("mensaje", "¡Reseña publicada con éxito!");
            } else {
                req.getSession().setAttribute("error", "No se pudo guardar tu reseña.");
            }

            resp.sendRedirect("detalle-publicacion?id=" + idPublicacion);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("inicio");
        }
    }
}