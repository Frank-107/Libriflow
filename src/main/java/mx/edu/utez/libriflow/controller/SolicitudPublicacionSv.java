package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionResumen;

import java.io.IOException;
import java.util.List;

@WebServlet (name = "SolicitudPublicacionAdminSv", value = "/solicitud-publicacion-admin")
public class SolicitudPublicacionSv extends HttpServlet {
    PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idPublicacion = Integer.parseInt(req.getParameter("idPublicacion"));
        publicacionUsuarioDao.cambiarEstadoPublicacion(idPublicacion,"ACTIVO");
        resp.sendRedirect("solicitud-publicacion-admin");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<PublicacionResumen> lista = publicacionUsuarioDao.getResumenPublicacionesUs("PENDIENTE");
        req.setAttribute("publicaciones", lista);
        req.getRequestDispatcher("SolicitudPublicacion.jsp").forward(req,resp);
    }
}
