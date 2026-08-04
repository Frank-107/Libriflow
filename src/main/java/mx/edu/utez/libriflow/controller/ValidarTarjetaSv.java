package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.*;
import mx.edu.utez.libriflow.model.Dao.DetalleTransaccionDao;
import mx.edu.utez.libriflow.model.Dao.PublicacionAdministradorDao;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.Dao.TransaccionDao;

import java.io.IOException;
import java.sql.ClientInfoStatus;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

@WebServlet(name = "ValidarTarjetaSv", value = "/validar-tarjeta")
public class ValidarTarjetaSv extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if(session.getAttribute("puedePagar")==null){
            resp.sendRedirect("inicio");
            return;
        }
        double envio = (Double) session.getAttribute("envio");
        double total;
        double subtotal = (Double) session.getAttribute("subtotal");
        total= subtotal+envio;
        req.setAttribute("total", total);
        session.setAttribute("total", total);
        session.setAttribute("subtotal", subtotal);
        session.setAttribute("envio", envio);
        req.getRequestDispatcher("/ValidarTarjeta.jsp").forward(req, resp);

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if(session.getAttribute("puedePagar")==null){
            resp.sendRedirect("inicio");
            return;
        }

        String titular = req.getParameter("titular");
        String numeroTarjeta = req.getParameter("numeroTarjeta");
        String fechaVencimiento = req.getParameter("fechaVencimiento");
        String cvv = req.getParameter("cvv");
        double precio = Double.parseDouble(req.getParameter("precio"));
        req.setAttribute("total",precio);


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


        // logica para procesar el pago y mandar los correos correspondientes y registros a la base de datos

        try {
            procesarCompra(req);

            session.setAttribute("pagoRealizado", true);
            session.removeAttribute("puedePagar");
            session.removeAttribute("puedeDireccion");

            resp.sendRedirect(req.getContextPath() + "/PagoExitoso.jsp");

        } catch (Exception e) {
            req.setAttribute("error", "Ocurrió un error al procesar el pago.");
            System.err.println(e.getMessage());
            req.getRequestDispatcher("/ValidarTarjeta.jsp").forward(req, resp);
        }

    }

    private void procesarCompra(HttpServletRequest req){
        PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
        TransaccionDao transaccionDao = new TransaccionDao();
        DetalleTransaccionDao detalleTransaccionDao = new DetalleTransaccionDao();
        PublicacionAdministradorDao publicacionAdminDao = new PublicacionAdministradorDao();
        HttpSession session = req.getSession(false);

        ArrayList<Integer> carritoPubUsuario = (ArrayList<Integer>) session.getAttribute("carrito");
        ArrayList<ItemCarritoAdmin> carritoAdmin = (ArrayList<ItemCarritoAdmin>) session.getAttribute("carritoAdmin");
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        int idUsuario = usuario.getId();
        Transaccion transaccion = new Transaccion();
        transaccion.setIdComprador(idUsuario);
        transaccion.setTotal(Double.parseDouble(session.getAttribute("total").toString()));
        transaccion.setSubtotal(Double.parseDouble(session.getAttribute("subtotal").toString()));
        transaccion.setCostoEnvio(Double.parseDouble(session.getAttribute("envio").toString()));
        transaccion.setEstado("PAGADO");

        int idTransaccion = transaccionDao.create(transaccion);
        if(idTransaccion==-1){
            throw new RuntimeException("No se pudo crear la transacción");
        }

        if(carritoPubUsuario!=null){
        for(Integer idPublicaion_us :carritoPubUsuario) {
            PublicacionUsuarioCompleta publicacionUsuario = publicacionUsuarioDao.getPublicacionUsuarioCompleta(idPublicaion_us);
            DetalleTransaccion detalleTransaccion = new DetalleTransaccion();
            detalleTransaccion.setIdTransaccion(idTransaccion);
            detalleTransaccion.setIdPublicacionUs(publicacionUsuario.getIdPublicacion());
            detalleTransaccion.setIdVendedor(publicacionUsuario.getIdPropietario());
            detalleTransaccion.setTipoOperacion("COMPRA");
            detalleTransaccion.setPrecio(publicacionUsuario.getPrecio());
            detalleTransaccion.setGananciaLibriFlow(publicacionUsuario.getPrecio() * 0.15);
            detalleTransaccion.setGananciaVendedor(publicacionUsuario.getPrecio() * 0.85);
            detalleTransaccionDao.create(detalleTransaccion);
            publicacionUsuarioDao.cambiarEstadoPublicacion(idPublicaion_us, "VENDIDO");
            //mandar correo al vendedor con su transaccion
        }
        }
        if(carritoAdmin!=null){
        for(ItemCarritoAdmin item : carritoAdmin){
            String tipoOperacion = item.getTipoOperacion().toUpperCase();
            tipoOperacion = tipoOperacion.equals("VENTA") ? "COMPRA" : tipoOperacion;
            DetalleTransaccion detalleTransaccion = new DetalleTransaccion();
            detalleTransaccion.setIdTransaccion(idTransaccion);
            detalleTransaccion.setIdPublicacionLf(item.getIdPublicacion());
            detalleTransaccion.setTipoOperacion(tipoOperacion);
            detalleTransaccion.setPrecio(item.getPrecio());
            detalleTransaccion.setGananciaVendedor(0.0);
            detalleTransaccion.setGananciaLibriFlow(item.getPrecio());

            detalleTransaccionDao.create(detalleTransaccion);
            if(!publicacionAdminDao.disminuirInventario(item.getIdPublicacion()) ){
                throw new RuntimeException("No se pudo disminuir el inventario de la publicación con id: " + item.getIdPublicacion());
            }

            if(tipoOperacion.equals("RENTA")){
            }
        }}


        System.out.println("todo bien, el id genereado es:" + idTransaccion);

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