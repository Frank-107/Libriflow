package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.Iniciar_sesionDao;

import java.io.IOException;
@WebServlet(name = "Iniciar_sesionSv", value = "/Iniciar_sesionSv")
public class Iniciar_sesionSV extends HttpServlet {
    Iniciar_sesionDao iniciarSesionDao = new Iniciar_sesionDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String correo = req.getParameter("correo");
            String contrasena = req.getParameter("contrasena");

            if (iniciarSesionDao.validarCredenciales(correo,contrasena)){
                HttpSession session = req.getSession(true);
                session.setAttribute("usuario", "usuario");
                session.setAttribute("correo", correo);
                System.out.println("se validaron las credenciales");
                resp.sendRedirect("Inicio.jsp");

            }else{
                req.setAttribute("error", "Usuario o contraseña incorrectos. Inténtalo de nuevo.");
                req.getRequestDispatcher("Iniciar_sesion.jsp").forward(req, resp);
            }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
