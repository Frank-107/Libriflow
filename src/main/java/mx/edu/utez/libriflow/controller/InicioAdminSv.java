package mx.edu.utez.libriflow.controller;

import mx.edu.utez.libriflow.model.Dao.PublicacionAdministradorDao;
import mx.edu.utez.libriflow.model.PublicacionResumen;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "InicioAdminSv", value = "/inicio-admin")
public class InicioAdminSv extends HttpServlet {

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

        PublicacionAdministradorDao adminDao = new PublicacionAdministradorDao();
        List<PublicacionResumen> publicaciones = adminDao.getResumenCatalogo();

        req.setAttribute("publicaciones", publicaciones);
        req.getRequestDispatcher("InicioAdmin.jsp").forward(req, resp);
    }
}