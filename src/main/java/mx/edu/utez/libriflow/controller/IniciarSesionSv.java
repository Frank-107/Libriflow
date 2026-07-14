package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.IniciarSesionDao;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
@WebServlet(name = "IniciarSesionSv", value = "/iniciar-sesion")
public class IniciarSesionSv extends HttpServlet {
    IniciarSesionDao iniciarSesionDao = new IniciarSesionDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("IniciarSesion.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String correo = req.getParameter("correo");
            String contrasena = req.getParameter("contrasena");

            if (iniciarSesionDao.validarCredenciales(correo,contrasena)){
                HttpSession session = req.getSession(true);
                Usuario usuario = iniciarSesionDao.obtenerUsuario(correo);
                session.setAttribute("tipo_usuario", "usuario");
                session.setAttribute("usuario",usuario);
                System.out.println("se validaron las credenciales");
                resp.sendRedirect("Inicio.jsp");

            }else{
                req.setAttribute("error", "Usuario o contraseña incorrectos. Inténtalo de nuevo.");
                req.getRequestDispatcher("IniciarSesion.jsp").forward(req, resp);
            }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
