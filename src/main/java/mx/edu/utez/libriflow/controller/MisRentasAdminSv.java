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

    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            "ACTIVA", "DEVUELTA", "ATRASADA", "MUY ATRASADA", "CANCELADA"
    );

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<RentaResumen> lista = rentaDao.getResumenTodasLasRentas();
        req.setAttribute("rentas", lista);

        req.getRequestDispatcher("MisRentasAdmin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int idDetalle = Integer.parseInt(req.getParameter("idDetalle"));
        String nuevoEstado = req.getParameter("estado");

        if (!ESTADOS_VALIDOS.contains(nuevoEstado)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Estado no válido");
            return;
        }

        rentaDao.cambiarEstadoRenta(idDetalle, nuevoEstado);
        resp.sendRedirect(req.getContextPath() + "/mis-rentas-admin");
    }
}