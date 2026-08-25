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

/**
 * Controlador Servlet encargado de gestionar la consulta y cálculo de vigencia de las rentas del usuario.
 * Valida la sesión activa, recupera el historial de rentas personales y calcula dinámicamente
 * los días restantes de préstamo según la fecha límite establecida.
 *
 * @author Francisco
 * @since 24/08/2026
 */

@WebServlet(name = "MisRentasSv", value = "/mis-rentas")
public class MisRentasSv extends HttpServlet {

    RentaDao rentaDao = new RentaDao();

    /**
     * Procesa la petición HTTP GET para desplegar el panel de rentas activas e históricas del usuario.
     * Verifica la autenticación mediante sesión HTTP, consulta los registros mediante la capa DAO
     * y computa los días restantes hasta la fecha de vencimiento para las rentas vigentes.
     *
     * @param req Objeto HttpServletRequest que transporta la sesión y datos de la solicitud.
     * @param resp Objeto HttpServletResponse para gestionar la respuesta o redirección al Login.
     * @throws ServletException Si ocurre una falla en el procesamiento Servlet.
     * @throws IOException Si ocurre un error de lectura/escritura en la comunicación HTTP.
     *
     * @author Francisco
     * @since 24/08/2026
     */

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