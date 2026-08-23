package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionAdministradorDao;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;

/**
 *
 * Este servlet se encarga de mostrar la información detallada de una publicación.
 * Permite consultar publicaciones pertenecientes a usuarios o a LibriFlow y enviar
 * la información correspondiente a la vista de detalle.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 22/08/2026
 */
@WebServlet(name = "DetalleInformativoSv", value = "/detalle-informativo")
public class DetalleInformativoSv extends HttpServlet {

    /**
     *
     * DAO utilizado para consultar la información de las publicaciones realizadas
     * por los usuarios.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private final PublicacionUsuarioDao publicacionUsuarioDao =
            new PublicacionUsuarioDao();

    /**
     *
     * DAO utilizado para consultar la información de las publicaciones
     * administradas directamente por LibriFlow.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private final PublicacionAdministradorDao publicacionAdministradorDao =
            new PublicacionAdministradorDao();

    /**
     *
     * Este método se encarga de obtener y mostrar la información detallada de una
     * publicación seleccionada por el usuario. Primero verifica que exista una sesión
     * activa, valida los parámetros recibidos y determina si la publicación pertenece
     * a un usuario o a LibriFlow. Finalmente envía la información obtenida a la vista
     * DetalleInformativo.jsp.
     *
     * @param req Contiene la solicitud HTTP y los parámetros enviados para consultar
     *            la publicación.
     * @param resp Permite generar la respuesta HTTP y realizar las redirecciones
     *             necesarias.
     *
     * @throws ServletException Si ocurre un problema al procesar o enviar la solicitud
     *                          hacia la vista.
     * @throws IOException Si ocurre un problema durante el manejo de la solicitud
     *                     o respuesta HTTP.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession sesion = req.getSession(false);

        if (sesion == null || sesion.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/iniciar-sesion");
            return;
        }

        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        String idParametro = req.getParameter("idPublicacion");
        String tipo = req.getParameter("tipo");
        String origen = req.getParameter("origen");

        if (idParametro == null || tipo == null) {
            resp.sendRedirect(req.getContextPath() + "/inicio");
            return;
        }

        int idPublicacion;

        try {
            idPublicacion = Integer.parseInt(idParametro);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/inicio");
            return;
        }

        if (!"usuario".equalsIgnoreCase(tipo)
                && !"libriflow".equalsIgnoreCase(tipo)) {

            resp.sendRedirect(req.getContextPath() + "/inicio");
            return;
        }

        if (!"carrito".equalsIgnoreCase(origen)
                && !"compras".equalsIgnoreCase(origen)) {

            origen = "inicio";
        }

        Object publicacion;
        boolean esLibriFlow;

        if ("libriflow".equalsIgnoreCase(tipo)) {

            publicacion =
                    publicacionAdministradorDao
                            .getPublicacionAdminCompleta(idPublicacion);

            esLibriFlow = true;

        } else {

            publicacion =
                    publicacionUsuarioDao
                            .getPublicacionUsuarioCompleta(idPublicacion);

            esLibriFlow = false;
        }

        if (publicacion == null) {

            if ("carrito".equalsIgnoreCase(origen)) {
                resp.sendRedirect(req.getContextPath() + "/carrito");
            } else if ("compras".equalsIgnoreCase(origen)) {
                resp.sendRedirect(req.getContextPath() + "/mis-compras");
            } else {
                resp.sendRedirect(req.getContextPath() + "/inicio");
            }

            return;
        }

        req.setAttribute("publicacion", publicacion);
        req.setAttribute("esLibriFlow", esLibriFlow);
        req.setAttribute("origen", origen);
        req.setAttribute("usuarioActual", usuario);

        req.getRequestDispatcher("/DetalleInformativo.jsp")
                .forward(req, resp);
    }
}