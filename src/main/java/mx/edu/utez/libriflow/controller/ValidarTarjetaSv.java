package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@WebServlet(name = "ValidarTarjetaSv", value = "/validar-tarjeta")
public class ValidarTarjetaSv extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        req.setAttribute("total",session.getAttribute("total"));
        req.getRequestDispatcher("/ValidarTarjeta.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String titular = req.getParameter("titular");
        String numeroTarjeta = req.getParameter("numeroTarjeta");
        String fechaVencimiento = req.getParameter("fechaVencimiento");
        String cvv = req.getParameter("cvv");

        req.setAttribute("titular", titular);
        req.setAttribute("numeroTarjeta", numeroTarjeta);
        req.setAttribute("fechaVencimiento", fechaVencimiento);

        if (titular == null || titular.trim().isEmpty() || !titular.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            req.setAttribute("error", "Ingresa un nombre de titular válido.");
            req.getRequestDispatcher("/ValidarTarjeta.jsp").forward(req, resp);
            return;
        }

        if (numeroTarjeta == null || !numeroTarjeta.matches("^\\d{13,19}$")) {
            req.setAttribute("error", "El número de tarjeta debe contener entre 13 y 19 dígitos.");
            req.getRequestDispatcher("/ValidarTarjeta.jsp").forward(req, resp);
            return;
        }

        if (!validarLuhn(numeroTarjeta)) {
            req.setAttribute("error", "El número de tarjeta ingresado no es válido.");
            req.getRequestDispatcher("/ValidarTarjeta.jsp").forward(req, resp);
            return;}

        if (fechaVencimiento == null || !fechaVencimiento.matches("^(0[1-9]|1[0-2])/(\\d{2})$")) {
            req.setAttribute("error", "Formato de fecha inválido. Usa MM/AA (Ej. 08/28).");
            req.getRequestDispatcher("/ValidarTarjeta.jsp").forward(req, resp);
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth vencimiento = YearMonth.parse(fechaVencimiento, formatter);
            YearMonth mesActual = YearMonth.now();

            if (vencimiento.isBefore(mesActual)) {
                req.setAttribute("error", "La tarjeta ingresada está vencida");
                req.getRequestDispatcher("/ValidarTarjeta.jsp").forward(req, resp);
                return;
            }
        } catch (DateTimeParseException e) {
            req.setAttribute("error", "Fecha de vencimiento no valida.");
            req.getRequestDispatcher("/ValidarTarjeta.jsp").forward(req, resp);
            return;
        }

        if (cvv == null || !cvv.matches("^\\d{3,4}$")) {
            req.setAttribute("error", "El código CVV debe tener 3 o 4 dígitos numéricos");
            req.getRequestDispatcher("/ValidarTarjeta.jsp").forward(req, resp);
            return;}
        resp.sendRedirect(req.getContextPath() + "/PagoExitoso.jsp");
    }
    private boolean validarLuhn(String numero) {
        int suma = 0;
        boolean alternar = false;
        for (int i = numero.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(numero.substring(i, i + 1));
            if (alternar) {
                n*=2;
                if (n >9) {
                    n=(n % 10)+ 1;
                }
            }
            suma+= n;
            alternar = !alternar;}
        return (suma % 10 == 0);
    }
}