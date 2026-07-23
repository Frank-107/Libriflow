package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.CredencialDao;
import mx.edu.utez.libriflow.model.Dao.UsuarioDao;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;

@WebServlet(name = "IniciarSesionSv", value = "/iniciar-sesion")
public class IniciarSesionSv extends HttpServlet {

    private static final String CORREO_ADMIN = "20253ds094@utez.edu.mx";

    UsuarioDao usuarioDao = new UsuarioDao();
    CredencialDao credencialDao = new CredencialDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("IniciarSesion.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String correo = req.getParameter("correo");
        String contrasena = req.getParameter("contrasena");

        int id_usuario = usuarioDao.getIdUsuario(correo);

        if (id_usuario != -1) {

            if (credencialDao.validarContrasena(id_usuario, contrasena)) {

                HttpSession session = req.getSession(true);
                Usuario usuario = usuarioDao.obtenerUsuario(correo);

                session.setAttribute("usuario", usuario);

                if (correo.equalsIgnoreCase(CORREO_ADMIN)) {

                    session.setAttribute("tipo_usuario", "admin");
                    System.out.println("Se validaron las credenciales del administrador");
                    resp.sendRedirect("inicio-admin");

                } else {

                    session.setAttribute("tipo_usuario", "usuario");
                    System.out.println("Se validaron las credenciales del usuario");
                    resp.sendRedirect("inicio");

                }

                return;
            }
        }

        req.setAttribute("error", "Usuario o contraseña incorrectos. Inténtalo de nuevo.");
        req.getRequestDispatcher("IniciarSesion.jsp").forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    }
}
