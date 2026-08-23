package mx.edu.utez.libriflow.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionAdministradorDao;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionResumen;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Este servlet se encarga de cargar y mostrar el catálogo principal de publicaciones
 * (tanto de usuarios como de administradores). También gestiona las búsquedas por
 * texto y los filtros por género, respondiendo de manera tradicional (JSP) o
 * de forma asíncrona mediante JSON.
 *
 * @author Alejandro Mena Pereyda
 * @since 23/08/2026
 */
@WebServlet(name = "InicioSvJS", value = "/inicio-js")
public class InicioSvJS extends HttpServlet {

    private final PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
    private final PublicacionAdministradorDao publicacionAdministradorDao = new PublicacionAdministradorDao();
    private final Gson gson = new Gson();

    /**
     * Procesa las peticiones GET para obtener el catálogo de publicaciones.
     * Verifica la sesión del usuario, procesa los parámetros de búsqueda y filtros,
     * realiza validaciones de seguridad (longitud y caracteres extraños) y obtiene
     * la lista combinada de publicaciones. Si la petición es AJAX, devuelve un
     * objeto JSON; en caso contrario, redirige a la vista InicioJS.jsp.
     *
     * @param req Contiene la solicitud HTTP, incluyendo la sesión, los parámetros
     *            de búsqueda ("q") y el filtro de género ("genero").
     * @param resp Permite generar la respuesta HTTP, ya sea redirigiendo a una vista
     *             o enviando datos en formato JSON.
     * @throws ServletException Si ocurre un problema interno al despachar hacia la vista.
     * @throws IOException Si ocurre un problema durante la lectura/escritura de la respuesta HTTP.
     *
     * @author Alejandro Mena Pereyda
     * @since 23/08/2026
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        String busqueda = req.getParameter("q");
        String genero = req.getParameter("genero");

        String qClean = (busqueda != null) ? busqueda.trim() : "";
        String generoClean = (genero != null) ? genero.trim() : "";

        if (qClean.length() > 100) {
            req.setAttribute("error", "El término de búsqueda es demasiado largo (máximo 100 caracteres).");
            req.setAttribute("publicaciones", new ArrayList<PublicacionResumen>());
            req.setAttribute("paramBusqueda", "");
            req.setAttribute("paramGenero", generoClean);
            req.getRequestDispatcher("InicioJS.jsp").forward(req, resp);
            return;
        }

        if (generoClean.length() > 50 || qClean.contains("<") || qClean.contains(">")) {
            req.setAttribute("error", "Búsqueda o filtro no válido.");
            req.setAttribute("publicaciones", new ArrayList<PublicacionResumen>());
            req.setAttribute("paramBusqueda", "");
            req.setAttribute("paramGenero", "");
            req.getRequestDispatcher("InicioJS.jsp").forward(req, resp);
            return;
        }

        List<PublicacionResumen> catalogo = new ArrayList<>();

        try {
            List<PublicacionResumen> publicacionesUs = publicacionUsuarioDao.buscarYFiltrarPublicacionesUs("ACTIVO", qClean, generoClean);
            List<PublicacionResumen> publicacionesAdmin = publicacionAdministradorDao.getResumenCatalogo();

            if (publicacionesAdmin != null) {
                for (PublicacionResumen adminPub : publicacionesAdmin) {
                    if (adminPub == null) continue;

                    boolean cumpleBusqueda = true;
                    boolean cumpleGenero = true;

                    if (!qClean.isEmpty()) {
                        String qLower = qClean.toLowerCase();
                        String titulo = adminPub.getTitulo() != null ? adminPub.getTitulo().toLowerCase() : "";
                        String autor = adminPub.getAutor() != null ? adminPub.getAutor().toLowerCase() : "";
                        cumpleBusqueda = titulo.contains(qLower) || autor.contains(qLower);
                    }

                    if (!generoClean.isEmpty() && !generoClean.equalsIgnoreCase("TODOS")) {
                        String gLower = adminPub.getGenero() != null ? adminPub.getGenero().toLowerCase() : "";
                        cumpleGenero = gLower.equalsIgnoreCase(generoClean);
                    }

                    if (cumpleBusqueda && cumpleGenero) {
                        catalogo.add(adminPub);
                    }
                }
            }

            if (publicacionesUs != null) {
                catalogo.addAll(publicacionesUs);
            }

            if (qClean.isEmpty() && (generoClean.isEmpty() || generoClean.equalsIgnoreCase("TODOS"))) {
                Collections.shuffle(catalogo);
            }

        } catch (Exception e) {
            req.setAttribute("error", "Ocurrió un error al cargar el catálogo de publicaciones.");
            System.err.println("Error en InicioSvJS: " + e.getMessage());
        }

        String esAjax = req.getParameter("ajax");

        if ("true".equals(esAjax)) {
            resp.setContentType("application/json");
            Map<String, Object> respuestaJson = new HashMap<>();
            respuestaJson.put("publicaciones", catalogo);
            respuestaJson.put("paramBusqueda", qClean);
            respuestaJson.put("paramGenero", generoClean);
            respuestaJson.put("idUsuarioActual", usuario.getId());

            PrintWriter out = resp.getWriter();
            out.print(gson.toJson(respuestaJson));
            out.flush();
        } else {
            req.setAttribute("publicaciones", catalogo);
            req.setAttribute("paramBusqueda", qClean);
            req.setAttribute("paramGenero", generoClean);
            req.getRequestDispatcher("InicioJS.jsp").forward(req, resp);
        }
    }

    /**
     * Procesa las peticiones POST delegándolas directamente al método doGet,
     * permitiendo que el servlet maneje ambos tipos de peticiones (GET y POST)
     * con la misma lógica de negocio para obtener el catálogo.
     *
     * @param req Contiene la solicitud HTTP enviada por el cliente.
     * @param resp Permite generar la respuesta HTTP correspondiente.
     * @throws ServletException Si ocurre un problema durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida al despachar la respuesta.
     *
     * @author Alejandro Mena Pereyda
     * @since 23/08/2026
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}