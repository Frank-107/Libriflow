package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.UsuarioDao;

import java.io.IOException;

@WebServlet(name = "AdminBloquearUsuario", value = "/admin-bloquear-usuario")
public class AdminBloquearUsuario extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UsuarioDao usuarioDao = new UsuarioDao();

        int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));

        boolean bloqueado = usuarioDao.cambiarEstadoUsuario(
                idUsuario,
                "INACTIVA"
        );

        if (!bloqueado) {
            System.err.println("Error al bloquear usuario: " + idUsuario);
        } else {
            System.out.println("Usuario " + idUsuario + " bloqueado correctamente.");
        }

        resp.sendRedirect(
                "detalle-usuario-admin?idUsuario=" + idUsuario
        );
    }
}