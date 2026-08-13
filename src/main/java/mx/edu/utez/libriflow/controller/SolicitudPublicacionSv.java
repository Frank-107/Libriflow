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

@WebServlet(
        name = "SolicitudPublicacionAdminSv",
        value = "/solicitud-publicacion-admin"
)
public class SolicitudPublicacionSv extends HttpServlet {

    private final PublicacionUsuarioDao publicacionUsuarioDao =
            new PublicacionUsuarioDao();

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