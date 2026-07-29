package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DireccionEnvioSv", value = "/direccion-envio")
public class DireccionEnvioSv extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("DireccionEnvio.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String destinatario = req.getParameter("destinatario");
        String calleNumero = req.getParameter("calleNumero");
        String colonia = req.getParameter("colonia");
        String codigoPostal = req.getParameter("codigoPostal");
        String municipio = req.getParameter("municipio");
        String estado = req.getParameter("estado");

        if (destinatario == null || destinatario.trim().isEmpty()) {
            req.setAttribute("error", "Por favor ingresa el nombre de quien recibe.");
            req.getRequestDispatcher("DireccionEnvio.jsp").forward(req, resp);
            return;
        }if (!destinatario.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s']+$")) {
            req.setAttribute("error", "El nombre de quien recibe no puede contener números ni caracteres especiales.");
            req.getRequestDispatcher("DireccionEnvio.jsp").forward(req, resp);
            return;
        } if (calleNumero == null || calleNumero.trim().isEmpty()) {
            req.setAttribute("error", "Ingresa la calle y número de entrega.");
            req.getRequestDispatcher("DireccionEnvio.jsp").forward(req, resp);
            return;
        } if (colonia == null || colonia.trim().isEmpty()) {
            req.setAttribute("error", "Por favor ingresa la colonia o barrio.");
            req.getRequestDispatcher("DireccionEnvio.jsp").forward(req, resp);
            return;
        }
        if (codigoPostal == null || !codigoPostal.matches("^\\d{5}$")) {
            req.setAttribute("error", "El Código Postal debe constar de exactamente 5 dígitos numéricos.");
            req.getRequestDispatcher("DireccionEnvio.jsp").forward(req, resp);
            return;
        }
        if (municipio == null || municipio.trim().isEmpty()) {
            req.setAttribute("error", "Por favor ingresa el municipio o alcaldía.");
            req.getRequestDispatcher("DireccionEnvio.jsp").forward(req, resp);
            return;
        }
        if (!municipio.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s']+$")) {
            req.setAttribute("error", "El municipio o alcaldía no puede contener números.");
            req.getRequestDispatcher("DireccionEnvio.jsp").forward(req, resp);
            return;
        }
        if (estado == null || estado.trim().isEmpty()) {
            req.setAttribute("error", "Debes seleccionar un Estado para calcular el costo de envío.");
            req.getRequestDispatcher("DireccionEnvio.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/validar-tarjeta");
    }
}