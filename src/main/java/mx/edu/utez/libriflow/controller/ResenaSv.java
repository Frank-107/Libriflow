package mx.edu.utez.libriflow.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import mx.edu.utez.libriflow.model.Dao.ResenaDao;
import mx.edu.utez.libriflow.model.Resena;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;

@WebServlet(name = "ResenaSv", value = "/resena")
public class ResenaSv extends HttpServlet {

    private final ResenaDao resenaDao = new ResenaDao();

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
