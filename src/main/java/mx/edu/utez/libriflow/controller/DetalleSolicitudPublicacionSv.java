package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionUsuarioCompleta;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;

@WebServlet(
        name = "DetalleSolicitudPublicacionSv",
        value = "/detalle-solicitud-publicacion-admin"
)
public class DetalleSolicitudPublicacionSv extends HttpServlet {

    private final PublicacionUsuarioDao publicacionDao =
            new PublicacionUsuarioDao();

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