package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "InicioSesionAdminSv", value = "/inicio-admin")
public class InicioSesionAdminSv extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Verificar que exista una sesión
        if (session == null) {
            resp.sendRedirect("iniciar-sesion");
            return;
        }

        // Verificar que sea administrador
        String tipoUsuario = (String) session.getAttribute("tipo_usuario");

        if (!"ADMIN".equals(tipoUsuario)) {
            resp.sendRedirect("inicio");
            return;
        }

        // Mostrar la página del administrador
        req.getRequestDispatcher("InicioAdmin.jsp").forward(req, resp);
    }
}