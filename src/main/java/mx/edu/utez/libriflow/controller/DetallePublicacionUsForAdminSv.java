package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionUsuarioCompleta;
import mx.edu.utez.libriflow.model.Dao.ResenaDao;
import mx.edu.utez.libriflow.model.Resena;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DetallePublicacionUsForAdminSv", value = "/detalle-publicacion-us-admin")
public class DetallePublicacionUsForAdminSv extends HttpServlet {
    PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idPublicacion = Integer.parseInt(req.getParameter("idPublicacion"));
        PublicacionUsuarioCompleta publicacionUsuarioCompleta = publicacionUsuarioDao.getPublicacionUsuarioCompleta(idPublicacion);
        req.setAttribute("publicacion", publicacionUsuarioCompleta);

        ResenaDao resenaDao = new ResenaDao();
        List<Resena> resenas = resenaDao.getResenasByPublicacion(idPublicacion);
        req.setAttribute("resenas", resenas);

        req.getRequestDispatcher("/DetallePublicacionAdmin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idPublicacionStr = req.getParameter("idPublicacion");

        if (idPublicacionStr != null && !idPublicacionStr.isEmpty()) {
            int idPublicacion = Integer.parseInt(idPublicacionStr);

            boolean exito = publicacionUsuarioDao.darDeBajaPublicacionUsuario(idPublicacion);

            if (exito) {
                resp.sendRedirect("inicio-admin?exito=baja");
            } else {
                resp.sendRedirect("detalle-publicacion-us-admin?idPublicacion=" + idPublicacion + "&error=baja");
            }
        } else {
            resp.sendRedirect("inicio-admin");
        }
    }
}