package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.RentaDao;
import mx.edu.utez.libriflow.model.RentaResumen;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(name = "MisRentasAdminSv", value = "/mis-rentas-admin")
public class MisRentasAdminSv extends HttpServlet {

    RentaDao rentaDao = new RentaDao();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<RentaResumen> lista = rentaDao.getResumenTodasLasRentas();
        req.setAttribute("rentas", lista);

        req.getRequestDispatcher("MisRentasAdmin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int idDetalle = Integer.parseInt(req.getParameter("idDetalle"));
        String accion = req.getParameter("accion");

        boolean actualizado = false;

        if ("ENTREGAR".equals(accion)) {

            actualizado = rentaDao.marcarComoEntregada(idDetalle);

        } else if ("DEVOLVER".equals(accion)) {

            actualizado = rentaDao.marcarComoFinalizada(idDetalle);
        }

        resp.sendRedirect(req.getContextPath() + "/mis-rentas-admin");
    }
}