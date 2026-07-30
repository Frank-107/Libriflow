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

@WebServlet(name = "ValidarTokenRCSv", value = "/validar-token-rc")

public class ValidarTokenRCSv extends HttpServlet {
    UsuarioDao usuarioDao = new UsuarioDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if(session!=null){
            String codigoBueno = session.getAttribute("codigoRC").toString();
            String codigoIngresado = req.getParameter("codigoIngresado");
            if (codigoBueno != null && codigoIngresado != null && codigoBueno.trim().equals(codigoIngresado.trim())){
                session.setAttribute("cambioDeContrasenaVerificado", true);
                resp.sendRedirect(req.getContextPath() + "/IngresarNuevaContrasena.jsp");
                return;

            }
        }
        req.setAttribute("error", "El código es incorrecto o tu sesión ha expirado.");
        req.getRequestDispatcher("ValidarTokenRC.jsp").forward(req, resp);

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }
}
