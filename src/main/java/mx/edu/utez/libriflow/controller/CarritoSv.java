package mx.edu.utez.libriflow.controller;

import jakarta.mail.Session;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionResumen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CarritoSv", value = "/carrito")
public class CarritoSv extends HttpServlet {
    PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        List<Integer> idsPublicaciones =
                (List<Integer>) session.getAttribute("carrito");
        List<PublicacionResumen> publicaciones = new ArrayList<>();

        if(idsPublicaciones != null && !idsPublicaciones.isEmpty()) {

            publicaciones =
                    publicacionUsuarioDao.getPublicacionesByArreglo(idsPublicaciones);

        }


        req.setAttribute("publicaciones", publicaciones);

        req.getRequestDispatcher("Carrito.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);
        if (action.equals("comprar")){

            boolean tieneCompras = Boolean.parseBoolean(req.getParameter("contieneEnvio")); //validar si no, etnonces sera solo renta
            double subtotal = Double.parseDouble(req.getParameter("subtotal"));

            if(tieneCompras){

                //no hay envio, por ende es todo el costo
                session.setAttribute("total",subtotal);
                resp.sendRedirect("direccion-envio");
                return;


            }
            resp.sendRedirect("validar-tarjeta");





        }
        if (action.equals("eliminar")) {
            int idEliminar = Integer.parseInt(req.getParameter("idPublicacion"));
            List<Integer> ids = (List<Integer>) session.getAttribute("carrito");
            ids.remove(Integer.valueOf(idEliminar));
            session.setAttribute("carrito",ids);
            resp.sendRedirect("carrito");
        }
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
