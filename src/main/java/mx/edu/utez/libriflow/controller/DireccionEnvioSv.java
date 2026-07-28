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
        String telefono = req.getParameter("telefono");
        if (destinatario == null || destinatario.trim().isEmpty()) {
            req.setAttribute("error", "Por favor ingresa el nombre de quien recibe.");
            req.getRequestDispatcher("DireccionEnvio.jsp").forward(req, resp);
            return;
        }

        if (calleNumero == null || calleNumero.trim().isEmpty()) {
            req.setAttribute("error", "Ingresa la calle y número de entrega.");
            req.getRequestDispatcher("DireccionEnvio.jsp").forward(req, resp);
            return;
        }
        if (codigoPostal == null || !codigoPostal.matches("^\\d{5}$")) {
            req.setAttribute("error", "El Código Postal debe constar de 5 dígitos numéricos.");
            req.getRequestDispatcher("DireccionEnvio.jsp").forward(req, resp);
            return;
        }

        if (estado == null || estado.trim().isEmpty()) {
            req.setAttribute("error", "Debes seleccionar un Estado para calcular el costo de envío.");
            req.getRequestDispatcher("DireccionEnvio.jsp").forward(req, resp);
            return;
        }
        if (telefono == null || !telefono.matches("^\\d{10}$")) {
            req.setAttribute("error", "El teléfono de contacto debe tener 10 dígitos numéricos.");
            req.getRequestDispatcher("DireccionEnvio.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/validar-tarjeta");
    }
}