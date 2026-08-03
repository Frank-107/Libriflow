package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionUsuarioCompleta;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "DetallePublicacionSv", value = "/detalle-publicacion")
public class DetallePublicacionSv extends HttpServlet {
    PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idPublicacion = Integer.parseInt(req.getParameter("idPublicacion"));
        PublicacionUsuarioCompleta publicacionUsuarioCompleta = publicacionUsuarioDao.getPublicacionUsuarioCompleta(idPublicacion);
        req.setAttribute("publicacion", publicacionUsuarioCompleta);
        req.getRequestDispatcher("/DetallePublicacion.jsp").forward(req, resp);

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idPublicacion = req.getParameter("idPublicacion");
        HttpSession session = req.getSession();
        ArrayList<Integer> carrito = (ArrayList<Integer>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        if(!carrito.contains(Integer.parseInt(idPublicacion))) {
            carrito.add(Integer.parseInt(idPublicacion));
        }
        session.setAttribute("carrito", carrito);
        resp.sendRedirect("carrito");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
