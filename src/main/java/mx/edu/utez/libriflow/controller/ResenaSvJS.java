package mx.edu.utez.libriflow.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.ResenaDao;
import mx.edu.utez.libriflow.model.Resena;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ResenaSvJS", value = "/resena-js")
public class ResenaSvJS extends HttpServlet {

    private final ResenaDao resenaDao = new ResenaDao();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        PrintWriter out = resp.getWriter();
        Map<String, Object> respuesta = new HashMap<>();

        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null)
                ? (Usuario) session.getAttribute("usuario")
                : null;

        if (usuario == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            respuesta.put("status", "error");
            respuesta.put("message", "Sesión expirada. Por favor inicie sesión de nuevo.");
            out.print(gson.toJson(respuesta));
            out.flush();
            return;
        }

        try {
            int idPublicacion = Integer.parseInt(req.getParameter("idPublicacion"));
            String comentario = req.getParameter("comentario");
            int calificacion = Integer.parseInt(req.getParameter("calificacion"));

            if (!resenaDao.usuarioHaCompradoORentado(usuario.getId(), idPublicacion)) {
                respuesta.put("status", "error");
                respuesta.put("message", "Debes comprar o rentar el libro para dejar una reseña.");
                out.print(gson.toJson(respuesta));
                out.flush();
                return;
            }

            if (comentario == null || comentario.trim().isEmpty() || calificacion < 1 || calificacion > 5) {
                respuesta.put("status", "error");
                respuesta.put("message", "Datos inválidos. Por favor verifica tu comentario y calificación.");
                out.print(gson.toJson(respuesta));
                out.flush();
                return;
            }

            Resena resena = new Resena(
                    usuario.getId(),
                    idPublicacion,
                    comentario.trim(),
                    calificacion
            );

            if (resenaDao.create(resena)) {
                respuesta.put("status", "success");
                respuesta.put("message", "¡Reseña publicada!");
                respuesta.put("nombreUsuario", usuario.getNombre());
                respuesta.put("calificacion", calificacion);
                respuesta.put("comentario", comentario.trim());
            } else {
                respuesta.put("status", "error");
                respuesta.put("message", "Error al guardar en la base de datos.");
            }

        } catch (Exception e) {
            respuesta.put("status", "error");
            respuesta.put("message", "Error interno al procesar la reseña.");
        }

        out.print(gson.toJson(respuesta));
        out.flush();
    }
}