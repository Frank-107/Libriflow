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

/**
 * Este servlet se encarga de gestionar las publicaciones propias de un usuario.
 * Permite visualizar el historial de publicaciones con opciones de ordenamiento,
 * así como la eliminación (cancelación) de las mismas mediante peticiones asíncronas.
 *
 * @author Alejandro Mena Pereyda
 * @since 23/08/2026
 */
@WebServlet(name = "MisPublicacionesSvJS", value = "/mis-publicaciones-js")
public class MisPublicacionesSvJS extends HttpServlet {

    private final PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
    private final Gson gson = new Gson();

    /**
     * Maneja las peticiones GET para obtener y mostrar la lista de publicaciones
     * creadas por el usuario en sesión. Permite ordenar los resultados por fecha
     * (más recientes o más antiguas) y redirige a la vista correspondiente.
     *
     * @param req Contiene la solicitud HTTP con los datos de la sesión y el parámetro de ordenamiento.
     * @param resp Permite generar la respuesta HTTP y redirigir hacia la vista JSP.
     * @throws ServletException Si ocurre un problema al procesar la solicitud o despachar a la vista.
     * @throws IOException Si ocurre un problema durante el manejo de la solicitud o respuesta HTTP.
     *
     * @author Alejandro Mena Pereyda
     * @since 23/08/2026
     */
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

    /**
     * Método auxiliar para centralizar la configuración y envío de respuestas
     * en formato JSON hacia el cliente.
     *
     * @param response El objeto HttpServletResponse donde se escribirá la respuesta.
     * @param statusCode El código de estado HTTP a devolver (ej. 200, 400, 500).
     * @param responseData El objeto o mapa de datos que será convertido a JSON.
     * @throws IOException Si ocurre un error de lectura/escritura al enviar la respuesta.
     *
     * @author Alejandro Mena Pereyda
     * @since 23/08/2026
     */
    private void sendJsonResponse(HttpServletResponse response, int statusCode, Object responseData) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(responseData));
        out.flush();
    }

    /**
     * Procesa las peticiones HTTP DELETE enviadas de forma asíncrona para
     * cancelar (eliminar) una publicación específica del usuario. Devuelve
     * un mensaje en formato JSON indicando el éxito o el motivo del error.
     *
     * @param request Contiene la solicitud HTTP con el parámetro "idPublicacion" a eliminar.
     * @param response Permite generar la respuesta HTTP en formato JSON.
     * @throws ServletException Si ocurre un error interno durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un problema al construir y enviar la respuesta.
     *
     * @author Alejandro Mena Pereyda
     * @since 23/08/2026
     */
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