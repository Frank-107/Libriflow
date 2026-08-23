package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionResumen;

import java.io.IOException;
import java.util.List;

/**
 * Controlador Servlet encargado de gestionar la revisión, aprobación y rechazo
 * de las solicitudes de publicación realizadas por los usuarios dentro del panel de administración.
 *
 * @author Andrés Gerardo Angelina Pérez
 * @since 22/08/2026
 */
@WebServlet(
        name = "SolicitudPublicacionAdminSv",
        value = "/solicitud-publicacion-admin"
)
public class SolicitudPublicacionSv extends HttpServlet {

    private final PublicacionUsuarioDao publicacionUsuarioDao =
            new PublicacionUsuarioDao();

    /**
     * Procesa las peticiones POST para actualizar el estado de una solicitud de publicación.
     * Permite al administrador aprobar ("ACTIVO") o rechazar ("RECHAZADO") una publicación según la acción seleccionada.
     *
     * @param req Objeto HttpServletRequest con los parámetros de la solicitud (idPublicacion y accion).
     * @param resp Objeto HttpServletResponse para redireccionar al listado de solicitudes.
     * @throws ServletException Si ocurre un error durante el manejo de la petición del Servlet.
     * @throws IOException Si ocurre un error de lectura/escritura durante el flujo HTTP.
     *
     * @author Andrés Gerardo Angelina Pérez
     * @since 22/08/2026
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        try {
            int idPublicacion =
                    Integer.parseInt(
                            req.getParameter("idPublicacion")
                    );

            String accion =
                    req.getParameter("accion");

            boolean actualizado;

            if ("aprobar".equalsIgnoreCase(accion)) {

                actualizado =
                        publicacionUsuarioDao.cambiarEstadoPublicacion(
                                idPublicacion,
                                "ACTIVO"
                        );

                if (actualizado) {
                    req.getSession().setAttribute(
                            "mensaje",
                            "Publicación aprobada correctamente."
                    );
                } else {
                    req.getSession().setAttribute(
                            "error",
                            "No se pudo aprobar la publicación."
                    );
                }

            } else if ("rechazar".equalsIgnoreCase(accion)) {

                actualizado =
                        publicacionUsuarioDao.cambiarEstadoPublicacion(
                                idPublicacion,
                                "RECHAZADO"
                        );

                if (actualizado) {
                    req.getSession().setAttribute(
                            "mensaje",
                            "Publicación rechazada correctamente."
                    );
                } else {
                    req.getSession().setAttribute(
                            "error",
                            "No se pudo rechazar la publicación."
                    );
                }

            } else {

                req.getSession().setAttribute(
                        "error",
                        "Acción no válida."
                );
            }

        } catch (NumberFormatException e) {

            req.getSession().setAttribute(
                    "error",
                    "El identificador de la publicación no es válido."
            );

        } catch (Exception e) {

            e.printStackTrace();

            req.getSession().setAttribute(
                    "error",
                    "Ocurrió un error al procesar la solicitud."
            );
        }

        resp.sendRedirect(
                req.getContextPath()
                        + "/solicitud-publicacion-admin"
        );
    }

    /**
     * Procesa las peticiones GET para obtener y mostrar la lista de publicaciones en estado 'PENDIENTE'.
     * Carga los datos necesarios y los despacha a la vista JSP de solicitudes de publicación.
     *
     * @param req Objeto HttpServletRequest utilizado para adjuntar la lista de publicaciones pendientes.
     * @param resp Objeto HttpServletResponse para redireccionar o despachar a la vista correspondiente.
     * @throws ServletException Si ocurre una falla en el Servlet al reenviar la vista.
     * @throws IOException Si ocurre un error de E/S.
     *
     * @author Andrés Gerardo Angelina Pérez
     * @since 22/08/2026
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<PublicacionResumen> lista =
                publicacionUsuarioDao.buscarYFiltrarPublicacionesUs(
                        "PENDIENTE",
                        null,
                        null
                );

        req.setAttribute(
                "publicaciones",
                lista
        );

        req.getRequestDispatcher(
                "/SolicitudPublicacion.jsp"
        ).forward(req, resp);
    }
}