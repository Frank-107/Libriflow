package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.DetalleTransaccionDao;
import mx.edu.utez.libriflow.model.Movimiento;

import java.io.IOException;
import java.util.List;
@WebServlet(name = "AdminIngresosSv", value = "/ingresos-admin")
public class AdminIngresosSv extends HttpServlet {
    DetalleTransaccionDao detalleTransaccionDao = new DetalleTransaccionDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Movimiento> ingresos = detalleTransaccionDao.getAllMovimientosIngresos();
        req.setAttribute("ingresos", ingresos);
        req.getRequestDispatcher("AdminIngresos.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
