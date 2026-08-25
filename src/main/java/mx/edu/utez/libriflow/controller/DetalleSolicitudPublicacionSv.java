package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionUsuarioCompleta;


import java.io.IOException;

/**
 *
 * Este servlet se encarga de mostrar el detalle de una solicitud de publicación
 * desde la vista del administrador. Verifica que exista una sesión activa y que
 * la publicación solicitada se encuentre en estado pendiente antes de mostrar
 * su información.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 22/08/2026
 */
@WebServlet(
        name = "DetalleSolicitudPublicacionSv",
        value = "/detalle-solicitud-publicacion-admin"
)
public class DetalleSolicitudPublicacionSv extends HttpServlet {

    /**
     *
     * DAO utilizado para consultar la información completa de las publicaciones
     * realizadas por los usuarios.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private final PublicacionUsuarioDao publicacionDao =
            new PublicacionUsuarioDao();

    /**
     *
     * Este método se encarga de obtener y mostrar la información de una solicitud
     * de publicación pendiente. Primero verifica que exista una sesión activa,
     * obtiene la publicación mediante su identificador y valida que exista y que
     * su estado sea PENDIENTE. Si las validaciones son correctas, envía la
     * información a la vista DetalleSolicitudPublicacion.jsp.
     *
     * @param req Contiene la solicitud HTTP y el identificador de la publicación
     *            que se desea consultar.
     * @param resp Permite generar la respuesta HTTP, realizar redirecciones y
     *             enviar la información hacia la vista correspondiente.
     *
     * @throws ServletException Si ocurre un problema al procesar o enviar la
     *                          solicitud hacia la vista.
     * @throws IOException Si ocurre un problema durante el manejo de la solicitud
     *                     o respuesta HTTP.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(
                    req.getContextPath() + "/iniciar-sesion"
            );
            return;
        }

        try {
            int idPublicacion =
                    Integer.parseInt(
                            req.getParameter("idPublicacion")
                    );

            PublicacionUsuarioCompleta publicacion =
                    publicacionDao
                            .getPublicacionUsuarioCompleta(idPublicacion);

            if (publicacion == null) {
                resp.sendRedirect(
                        req.getContextPath()
                                + "/solicitud-publicacion-admin"
                );
                return;
            }

            if (!"PENDIENTE".equalsIgnoreCase(publicacion.getEstado())) {
                resp.sendRedirect(
                        req.getContextPath()
                                + "/solicitud-publicacion-admin"
                );
                return;
            }

            req.setAttribute(
                    "publicacion",
                    publicacion
            );

            req.getRequestDispatcher(
                    "/DetalleSolicitudPublicacion.jsp"
            ).forward(req, resp);

        } catch (NumberFormatException e) {

            resp.sendRedirect(
                    req.getContextPath()
                            + "/solicitud-publicacion-admin"
            );
        }
    }
}