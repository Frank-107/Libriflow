package mx.edu.utez.libriflow.controller;

import jakarta.mail.Session;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.UsuarioDao;

import java.io.IOException;

@WebServlet(name = "IngresarNuevaContrasenaSv", value = "/ingresar-nueva-contrasena")
public class IngresarNuevaContrasenaSv extends HttpServlet {

    UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession(false);
        String nuevaContrasena=req.getParameter("nuevaContrasena");
        String correo=(String) session.getAttribute("correo");

        if((boolean)session.getAttribute("cambioDeContrasenaVerificado")){
            if (usuarioDao.actualizarContrasena(correo, nuevaContrasena)){
                session.setAttribute("mensaje", "Contrasena actualizada correctamente");
                session.removeAttribute("correo");
                session.removeAttribute("cambioDeContrasenaVerificado");
                resp.sendRedirect("index.jsp");
            }else {
                req.setAttribute("error","Falta de verificacion");
                req.getRequestDispatcher("IngresarNuevaContrasena.jsp").forward(req,resp);
            }


        }


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}

