package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionAdministradorDao;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionResumen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Controlador Servlet encargado de gestionar la vista principal de inicio (catálogo de publicaciones).
 * Procesa la búsqueda dinámica, filtrado por género, unificación de publicaciones de usuarios y
 * administradores, y control de acceso por sesión.
 *
 * @author Francisco
 * @since 24/08/2026
 */

@WebServlet(name = "InicioSv", value = "/inicio")
public class InicioSv extends HttpServlet {

    private final PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
    private final PublicacionAdministradorDao publicacionAdministradorDao = new PublicacionAdministradorDao();

    /**
     * Procesa las peticiones HTTP GET para cargar el catálogo principal.
     * Valida la sesión activa del usuario, sanitiza los parámetros de búsqueda y filtro por género,
     * consulta las publicaciones activas tanto de usuarios como de administradores, aplica los filtros
     * dinámicos y redirige hacia la vista `Inicio.jsp`.
     *
     * @param req Objeto HttpServletRequest con los parámetros de búsqueda 'q' y filtro 'genero'.
     * @param resp Objeto HttpServletResponse para enviar la respuesta o realizar redirecciones.
     * @throws ServletException Si ocurre una falla de procesamiento en el Servlet.
     * @throws IOException Si ocurre un error de entrada/salida durante la redirección o despacho.
     *
     * @author Francisco
     * @since 24/08/2026
     */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        // 1. Validar sesión
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String busqueda = req.getParameter("q");
        String genero = req.getParameter("genero");

        String qClean = (busqueda != null) ? busqueda.trim() : "";
        String generoClean = (genero != null) ? genero.trim() : "";

        // Validaciones previas
        if (qClean.length() > 100) {
            req.setAttribute("error", "El término de búsqueda es demasiado largo (máximo 100 caracteres).");
            req.setAttribute("publicaciones", new ArrayList<PublicacionResumen>());
            req.setAttribute("paramBusqueda", "");
            req.setAttribute("paramGenero", generoClean);
            req.getRequestDispatcher("Inicio.jsp").forward(req, resp);
            return;
        }

        if (generoClean.length() > 50 || qClean.contains("<") || qClean.contains(">")) {
            req.setAttribute("error", "Búsqueda o filtro no válido.");
            req.setAttribute("publicaciones", new ArrayList<PublicacionResumen>());
            req.setAttribute("paramBusqueda", "");
            req.setAttribute("paramGenero", "");
            req.getRequestDispatcher("Inicio.jsp").forward(req, resp);
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
            System.err.println("Error en InicioSv: " + e.getMessage());
        }

        req.setAttribute("publicaciones", catalogo);
        req.setAttribute("paramBusqueda", qClean);
        req.setAttribute("paramGenero", generoClean);

        req.getRequestDispatcher("Inicio.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}