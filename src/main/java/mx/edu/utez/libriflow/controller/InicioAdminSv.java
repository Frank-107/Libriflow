package mx.edu.utez.libriflow.controller;

import mx.edu.utez.libriflow.model.Dao.PublicacionAdministradorDao;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionResumen;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controlador Servlet encargado de gestionar la vista principal del panel de administración.
 * Realiza la verificación de la sesión y del rol de Administrador, consulta y unifica las publicaciones
 * tanto de usuarios como institucionales, aplica filtros por búsqueda de texto y género, y desordena
 * aleatoriamente el catálogo cuando no existen filtros activos.
 *
 * @author Monserrath Anzurez
 * @since 23/08/26
 */
@WebServlet(name = "InicioAdminSv", value = "/inicio-admin")
public class InicioAdminSv extends HttpServlet {

    /** Objeto DAO para consultar las publicaciones activas realizadas por los usuarios. */
    private final PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();

    /** Objeto DAO para recuperar los resúmenes del catálogo de publicaciones oficiales de LibriFlow. */
    private final PublicacionAdministradorDao publicacionAdministradorDao = new PublicacionAdministradorDao();

    /**
     * Procesa las peticiones GET para construir y mostrar el catálogo unificado para el administrador.
     * Valida que exista una sesión activa con el rol 'ADMIN', recopila y filtra las publicaciones
     * de administración y de usuarios según los parámetros de consulta recibidos, y despacha
     * los resultados a la vista `InicioAdmin.jsp`.
     *
     * @param req Objeto HttpServletRequest que transporta la sesión del usuario y los parámetros de búsqueda (`q` y `genero`).
     * @param resp Objeto HttpServletResponse para manejar la redirección de seguridad o el despacho de la vista.
     * @throws ServletException Si ocurre un fallo interno en la ejecución del Servlet al reenviar la vista.
     * @throws IOException Si ocurre una falla de E/S durante la comunicación HTTP.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.sendRedirect("iniciar-sesion");
            return;
        }

        String tipoUsuario = (String) session.getAttribute("tipo_usuario");
        if (!"ADMIN".equals(tipoUsuario)) {
            resp.sendRedirect("inicio");
            return;
        }

        String busqueda = req.getParameter("q");
        String genero = req.getParameter("genero");

        List<PublicacionResumen> publicacionesUs = publicacionUsuarioDao.buscarYFiltrarPublicacionesUs("ACTIVO", busqueda, genero);
        List<PublicacionResumen> publicacionesAdmin = publicacionAdministradorDao.getResumenCatalogo();
        List<PublicacionResumen> catalogo = new ArrayList<>();

        if (publicacionesAdmin != null) {
            for (PublicacionResumen adminPub : publicacionesAdmin) {
                boolean cumpleBusqueda = true;
                boolean cumpleGenero = true;

                if (busqueda != null && !busqueda.trim().isEmpty()) {
                    String q = busqueda.trim().toLowerCase();
                    String titulo = adminPub.getTitulo() != null ? adminPub.getTitulo().toLowerCase() : "";
                    String autor = adminPub.getAutor() != null ? adminPub.getAutor().toLowerCase() : "";
                    cumpleBusqueda = titulo.contains(q) || autor.contains(q);
                }

                if (genero != null && !genero.trim().isEmpty() && !genero.equalsIgnoreCase("TODOS")) {
                    String g = adminPub.getGenero() != null ? adminPub.getGenero().toLowerCase() : "";
                    cumpleGenero = g.equalsIgnoreCase(genero.trim());
                }
                if (cumpleBusqueda && cumpleGenero) {
                    catalogo.add(adminPub);
                }
            }
        }
        if (publicacionesUs != null) {
            catalogo.addAll(publicacionesUs);
        }
        if ((busqueda == null || busqueda.trim().isEmpty()) && (genero == null || genero.trim().isEmpty())) {
            Collections.shuffle(catalogo);
        }

        req.setAttribute("publicaciones", catalogo);
        req.setAttribute("paramBusqueda", busqueda);
        req.setAttribute("paramGenero", genero);

        req.getRequestDispatcher("InicioAdmin.jsp").forward(req, resp);
    }
}