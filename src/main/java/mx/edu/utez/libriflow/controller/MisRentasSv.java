package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.RentaDao;
import mx.edu.utez.libriflow.model.RentaResumen;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@WebServlet(name = "MisRentasSv", value = "/mis-rentas")
public class MisRentasSv extends HttpServlet {

    RentaDao rentaDao = new RentaDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession sesion = req.getSession(false);

        if (sesion == null || sesion.getAttribute("usuario") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        int idUsuario = usuario.getId();

        List<RentaResumen> lista = rentaDao.getResumenRentasPorUsuario(idUsuario);

        if (lista != null) {
            LocalDate hoy = LocalDate.now();

            for (RentaResumen renta : lista) {
                if ("ACTIVO".equalsIgnoreCase(renta.getEstado()) || "ACTIVA".equalsIgnoreCase(renta.getEstado())) {
                    if (renta.getFechaLimite() != null) {
                        try {
                            // Se convierte a String para parsear de forma segura sin importar el tipo original (Date o String)
                            LocalDate fechaLimite = LocalDate.parse(renta.getFechaLimite().toString());
                            long dias = ChronoUnit.DAYS.between(hoy, fechaLimite);
                            renta.setDiasRestantes((int) dias);
                        } catch (Exception e) {
                            renta.setDiasRestantes(-1); // Indica un error en el cálculo de días restantes
                        }
                    }
                }
            }
        }

        req.setAttribute("rentas", lista);
        req.getRequestDispatcher("MisRentas.jsp").forward(req, resp);
    }
}