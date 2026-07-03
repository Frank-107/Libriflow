package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
@WebServlet(name = "Iniciar_sesionSv", value = "/Iniciar_sesionSv")
public class Iniciar_sesionSV extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String correo = req.getParameter("correo");
            String contrasena = req.getParameter("contrasena");
        System.out.println("correo: " + correo);
        System.out.println("contrasena: " + contrasena);
            if (correo.equals("20253ds107@utez.edu.mx")&&contrasena.equals("1234")){
                HttpSession session = req.getSession(true);
                session.setAttribute("usuario", "usuario");
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
