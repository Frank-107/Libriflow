package mx.edu.utez.libriflow.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionResumen;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "MisPublicacionesSvJS", value = "/mis-publicaciones-js")
public class MisPublicacionesSvJS extends HttpServlet {

    private final PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession sesion = req.getSession(false);

        if (sesion != null && sesion.getAttribute("usuario") != null) {

            Usuario usuario = (Usuario) sesion.getAttribute("usuario");

            int id = usuario.getId();

            String orden = req.getParameter("orden");

            if (orden == null ||
                    (!orden.equalsIgnoreCase("recientes")
                            && !orden.equalsIgnoreCase("antiguas"))) {

                orden = "recientes";
            }

            List<PublicacionResumen> lista =
                    publicacionUsuarioDao.getResumenPublicacionesPorUsuario(id, orden);

            req.setAttribute("publicaciones", lista);
            req.setAttribute("ordenActual", orden);

            req.getRequestDispatcher("MisPublicacionesJS.jsp")
                    .forward(req, resp);

        } else {

            resp.sendRedirect("login.jsp");
        }
    }

    private void sendJsonResponse(HttpServletResponse response, int statusCode, Object responseData) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(responseData));
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            String idParam = request.getParameter("idPublicacion");

            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);
                publicacionUsuarioDao.deletePublicacionById(id);

                jsonResponse.put("status", "success");
                jsonResponse.put("message", "La publicación se canceló correctamente.");
                sendJsonResponse(response, HttpServletResponse.SC_OK, jsonResponse);
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "ID de publicación no proporcionado.");
                sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, jsonResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "No se pudo cancelar la publicación. Inténtalo más tarde.");
            sendJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, jsonResponse);
        }
    }
}