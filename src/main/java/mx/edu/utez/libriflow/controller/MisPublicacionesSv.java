package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionResumen;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet (name = "MisPublicacionesSv", value="/mis-publicaciones")
public class MisPublicacionesSv extends HttpServlet {
    PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        int id = usuario.getId();
        List<PublicacionResumen> lista = publicacionUsuarioDao.getResumenPublicacionesPorUsuario(id);
        req.setAttribute("publicaciones",lista);

        req.getRequestDispatcher("MisPublicaciones.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
