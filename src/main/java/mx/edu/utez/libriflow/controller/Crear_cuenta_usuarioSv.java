package mx.edu.utez.libriflow.controller;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.UsuarioDao;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;

@WebServlet(name = "Crear_cuenta_usuarioSv", value = "/Crear_cuenta_usuarioSv")
public class Crear_cuenta_usuarioSv extends HttpServlet {
UsuarioDao usuarioDao = new UsuarioDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nombre = req.getParameter("nombre");
        String apellidoPaterno = req.getParameter("apellidoPaterno");
        String apellidoMaterno = req.getParameter("apellidoMaterno");
        String correo = req.getParameter("correo");
        String contrasena = req.getParameter("contrasena");
        String contrasena2 = req.getParameter("contrasena2");
        // validaciones:

        if(!contrasena.equals(contrasena2)){
            req.setAttribute("error", "Las contraseñas no coinciden.");
            req.getRequestDispatcher("Crear_cuenta_usuario.jsp").forward(req, resp);
            return;
        }

        Usuario usuarionuevo = new Usuario(nombre, apellidoPaterno, apellidoMaterno, correo, contrasena);
        if(usuarioDao.create(usuarionuevo)){
            req.setAttribute("mensaje","cuenta creada con exito, ahora inicia sesion");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", "Error al crear la cuenta.");
            req.getRequestDispatcher("Crear_cuenta_usuario.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
