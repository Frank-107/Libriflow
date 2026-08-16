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

@WebServlet(name = "DetalleInformativoSv", value = "/detalle-informativo")
public class DetalleInformativoSv extends HttpServlet {

    private final PublicacionUsuarioDao publicacionUsuarioDao =
            new PublicacionUsuarioDao();

    private final PublicacionAdministradorDao publicacionAdministradorDao =
            new PublicacionAdministradorDao();

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