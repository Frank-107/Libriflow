package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.*;
import mx.edu.utez.libriflow.model.Dao.*;
import mx.edu.utez.libriflow.utils.EmailSender;

import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.UUID;
/**
 * Servlet encargado de la validación de tarjetas bancarias y del procesamiento
 * de compras y rentas de libros dentro de la plataforma LibriFlow.
 */
@WebServlet(name = "ValidarTarjetaSv", value = "/validar-tarjeta")
public class ValidarTarjetaSv extends HttpServlet {
    /**
     * Maneja las peticiones HTTP GET para la pantalla de validación de tarjeta.
     * Verifica la sesión del usuario y calcula el monto total del pago.
     *
     * @param req  Objeto {@link HttpServletRequest} con la petición del cliente.
     * @param resp Objeto {@link HttpServletResponse} para enviar la respuesta.
     * @throws ServletException Si ocurre un error en la redirección o despacho del servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("puedePagar") == null) {
            resp.sendRedirect(req.getContextPath() + "/inicio");
            return;
        }

        double envio =
                (Double) session.getAttribute("envio");

        double subtotal =
                (Double) session.getAttribute("subtotal");

        double total =
                subtotal + envio;

        req.setAttribute("total", total);

        session.setAttribute("total", total);
        session.setAttribute("subtotal", subtotal);
        session.setAttribute("envio", envio);

        req.getRequestDispatcher("/ValidarTarjeta.jsp")
                .forward(req, resp);
    }
    /**
     * Maneja las peticiones HTTP POST para validar los datos bancarios e iniciar el procesamiento de compra.
     * Realiza validaciones de titular, número de tarjeta mediante el algoritmo de Luhn, fecha de expiración y CVV.
     *
     * @param req  Objeto {@link HttpServletRequest} con la petición del cliente.
     * @param resp Objeto {@link HttpServletResponse} para enviar la respuesta.
     * @throws ServletException Si ocurre un error en la redirección o despacho del servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session =
                req.getSession(false);

        if (session == null
                || session.getAttribute("puedePagar") == null) {

            resp.sendRedirect(
                    req.getContextPath() + "/inicio"
            );

            return;
        }

        String titular =
                req.getParameter("titular");

        String numeroTarjeta =
                req.getParameter("numeroTarjeta");

        String fechaVencimiento =
                req.getParameter("fechaVencimiento");

        String cvv =
                req.getParameter("cvv");

        double precio;

        try {

            precio =
                    Double.parseDouble(
                            req.getParameter("precio")
                    );

        } catch (NumberFormatException e) {

            req.setAttribute(
                    "error",
                    "El monto de la compra no es válido."
            );

            req.getRequestDispatcher(
                    "/ValidarTarjeta.jsp"
            ).forward(req, resp);

            return;
        }

        req.setAttribute("total", precio);
        req.setAttribute("titular", titular);
        req.setAttribute("numeroTarjeta", numeroTarjeta);
        req.setAttribute("fechaVencimiento", fechaVencimiento);

        if (titular == null
                || titular.trim().isEmpty()
                || !titular.matches(
                "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"
        )) {

            req.setAttribute(
                    "error",
                    "Ingresa un nombre de titular válido."
            );

            req.getRequestDispatcher(
                    "/ValidarTarjeta.jsp"
            ).forward(req, resp);

            return;
        }

        if (numeroTarjeta == null
                || !numeroTarjeta.matches("^\\d{13,19}$")) {

            req.setAttribute(
                    "error",
                    "El número de tarjeta debe contener entre 13 y 19 dígitos."
            );

            req.getRequestDispatcher(
                    "/ValidarTarjeta.jsp"
            ).forward(req, resp);

            return;
        }

        if (!validarLuhn(numeroTarjeta)) {

            req.setAttribute(
                    "error",
                    "El número de tarjeta ingresado no es válido."
            );

            req.getRequestDispatcher(
                    "/ValidarTarjeta.jsp"
            ).forward(req, resp);

            return;
        }

        if (fechaVencimiento == null
                || !fechaVencimiento.matches(
                "^(0[1-9]|1[0-2])/(\\d{2})$"
        )) {

            req.setAttribute(
                    "error",
                    "Formato de fecha inválido. Usa MM/AA (Ej. 08/28)."
            );

            req.getRequestDispatcher(
                    "/ValidarTarjeta.jsp"
            ).forward(req, resp);

            return;
        }

        try {

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("MM/yy");

            YearMonth vencimiento =
                    YearMonth.parse(
                            fechaVencimiento,
                            formatter
                    );

            YearMonth mesActual =
                    YearMonth.now();

            if (vencimiento.isBefore(mesActual)) {

                req.setAttribute(
                        "error",
                        "La tarjeta ingresada está vencida."
                );

                req.getRequestDispatcher(
                        "/ValidarTarjeta.jsp"
                ).forward(req, resp);

                return;
            }

        } catch (DateTimeParseException e) {

            req.setAttribute(
                    "error",
                    "Fecha de vencimiento no válida."
            );

            req.getRequestDispatcher(
                    "/ValidarTarjeta.jsp"
            ).forward(req, resp);

            return;
        }

        if (cvv == null
                || !cvv.matches("^\\d{3,4}$")) {

            req.setAttribute(
                    "error",
                    "El código CVV debe tener 3 o 4 dígitos numéricos."
            );

            req.getRequestDispatcher(
                    "/ValidarTarjeta.jsp"
            ).forward(req, resp);

            return;
        }

        try {

            procesarCompra(req);

            session.setAttribute(
                    "pagoRealizado",
                    true
            );

            session.removeAttribute(
                    "puedePagar"
            );

            session.removeAttribute(
                    "puedeDireccion"
            );

            resp.sendRedirect(
                    req.getContextPath()
                            + "/PagoExitoso.jsp"
            );

        } catch (Exception e) {

            System.err.println(
                    "Error al procesar la compra: "
                            + e.getMessage()
            );

            e.printStackTrace();

            req.setAttribute(
                    "error",
                    "Ocurrió un error al procesar el pago."
            );

            req.getRequestDispatcher(
                    "/ValidarTarjeta.jsp"
            ).forward(req, resp);
        }
    }

    /**
     * Valida el número de tarjeta utilizando el algoritmo de Luhn.
     *
     * @param numero Número de la tarjeta a validar.
     * @return {@code true} si el número es válido, {@code false} en caso contrario.
     */
    private boolean validarLuhn(String numero) {

        int suma = 0;
        boolean alternar = false;

        for (int i = numero.length() - 1;
             i >= 0;
             i--) {

            int n =
                    Integer.parseInt(
                            numero.substring(
                                    i,
                                    i + 1
                            )
                    );

            if (alternar) {

                n *= 2;

                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }

            suma += n;
            alternar = !alternar;
        }

        return suma % 10 == 0;
    }
    /**
     * Ejecuta el flujo principal de persistencia de la transacción,
     * descomponiendo los elementos del carrito en ventas o rentas,
     * actualizando inventarios y enviando notificaciones por correo.
     *
     * @param req Objeto {@link HttpServletRequest} con la información de sesión requerida.
     */
    private void procesarCompra(HttpServletRequest req) {

        PublicacionUsuarioDao publicacionUsuarioDao =
                new PublicacionUsuarioDao();

        TransaccionDao transaccionDao =
                new TransaccionDao();

        DetalleTransaccionDao detalleTransaccionDao =
                new DetalleTransaccionDao();

        PublicacionAdministradorDao publicacionAdminDao =
                new PublicacionAdministradorDao();

        HttpSession session =
                req.getSession(false);

        ArrayList<Integer> carritoPubUsuario =
                (ArrayList<Integer>)
                        session.getAttribute("carrito");

        ArrayList<ItemCarritoAdmin> carritoAdmin =
                (ArrayList<ItemCarritoAdmin>)
                        session.getAttribute("carritoAdmin");

        Usuario usuario =
                (Usuario)
                        session.getAttribute("usuario");

        int idUsuario =
                usuario.getId();

        Transaccion transaccion =
                new Transaccion();

        transaccion.setIdComprador(
                idUsuario
        );

        transaccion.setTotal(
                Double.parseDouble(
                        session.getAttribute("total")
                                .toString()
                )
        );

        transaccion.setSubtotal(
                Double.parseDouble(
                        session.getAttribute("subtotal")
                                .toString()
                )
        );

        transaccion.setCostoEnvio(
                Double.parseDouble(
                        session.getAttribute("envio")
                                .toString()
                )
        );

        transaccion.setEstado(
                "PAGADO"
        );

        StringBuilder librosComprados =
                new StringBuilder();

        StringBuilder librosRentados =
                new StringBuilder();

        String guiaSeguimiento =
                "";

        int idTransaccion =
                transaccionDao.create(
                        transaccion
                );

        if (idTransaccion == -1) {

            throw new RuntimeException(
                    "No se pudo crear la transacción."
            );
        }

        System.out.println(
                "Transacción creada con id: "
                        + idTransaccion
        );

        /*
         * =================================
         * PUBLICACIONES DE USUARIOS
         * =================================
         */

        if (carritoPubUsuario != null
                && !carritoPubUsuario.isEmpty()) {

            guiaSeguimiento =
                    generarGuiaSeguimiento();

            UsuarioDao usuarioDao =
                    new UsuarioDao();

            for (Integer idPublicacionUs
                    : carritoPubUsuario) {

                PublicacionUsuarioCompleta publicacionUsuario =
                        publicacionUsuarioDao
                                .getPublicacionUsuarioCompleta(
                                        idPublicacionUs
                                );

                if (publicacionUsuario == null) {

                    throw new RuntimeException(
                            "No se encontró la publicación con id: "
                                    + idPublicacionUs
                    );
                }

                /*
                 * Comprobación adicional.
                 *
                 * El DAO de detalle también protege la compra,
                 * pero aquí evitamos continuar si la publicación
                 * claramente ya no está disponible.
                 */
                if (!"ACTIVO".equalsIgnoreCase(
                        publicacionUsuario.getEstado()
                )) {

                    throw new RuntimeException(
                            "La publicación "
                                    + idPublicacionUs
                                    + " ya no está disponible para compra."
                    );
                }

                DetalleTransaccion detalleTransaccion =
                        new DetalleTransaccion();

                detalleTransaccion.setIdTransaccion(
                        idTransaccion
                );

                detalleTransaccion.setIdPublicacionUs(
                        publicacionUsuario
                                .getIdPublicacion()
                );

                detalleTransaccion.setIdVendedor(
                        publicacionUsuario
                                .getIdPropietario()
                );

                detalleTransaccion.setTipoOperacion(
                        "COMPRA"
                );

                detalleTransaccion.setPrecio(
                        publicacionUsuario.getPrecio()
                );

                detalleTransaccion.setGananciaLibriFlow(
                        publicacionUsuario.getPrecio()
                                * 0.15
                );

                detalleTransaccion.setGananciaVendedor(
                        publicacionUsuario.getPrecio()
                                * 0.85
                );

                librosComprados.append(
                        "<p><strong>Libro:</strong> "
                                + publicacionUsuario.getTitulo()
                                + "</p>"
                                + "<p><strong>Precio:</strong> $"
                                + String.format(
                                "%.2f",
                                publicacionUsuario.getPrecio()
                        )
                                + "</p><hr>"
                );

                int idDetalleTransaccion =
                        detalleTransaccionDao.create(
                                detalleTransaccion
                        );

                if (idDetalleTransaccion == -1) {

                    throw new RuntimeException(
                            "No se pudo crear el detalle de la transacción "
                                    + "para la publicación de usuario con id: "
                                    + idPublicacionUs
                    );
                }

                System.out.println(
                        "Detalle transacción creado con id: "
                                + idDetalleTransaccion
                );

                /*
                 * IMPORTANTE:
                 *
                 * Ya NO hacemos aquí:
                 *
                 * publicacionUsuarioDao.cambiarEstadoPublicacion(
                 *      idPublicacionUs,
                 *      "VENDIDO"
                 * );
                 *
                 * DetalleTransaccionDao.create()
                 * ya realiza ACTIVO -> VENDIDO.
                 */

                try {

                    Usuario vendedor =
                            usuarioDao
                                    .getDuenoPublicacionById(
                                            publicacionUsuario
                                                    .getIdPublicacion()
                                    );

                    if (vendedor != null) {

                        String correoVenta =
                                correoVentaRealizada(
                                        vendedor.getNombre(),
                                        publicacionUsuario
                                                .getTitulo(),
                                        detalleTransaccion
                                                .getGananciaVendedor()
                                );

                        EmailSender.sendMail(
                                vendedor.getCorreo(),
                                "¡Buenas noticias! Tu libro ha sido vendido - LibriFlow",
                                correoVenta
                        );
                    }

                } catch (Exception e) {

                    System.out.println(
                            "===================================="
                    );

                    System.out.println(
                            "ERROR AL ENVIAR CORREO AL VENDEDOR"
                    );

                    System.out.println(
                            "===================================="
                    );

                    System.out.println(
                            e.getMessage()
                    );

                    e.printStackTrace();

                    /*
                     * No detenemos la compra solamente
                     * porque falle el correo.
                     */
                    req.setAttribute(
                            "errorCorreo",
                            "No se pudo enviar uno de los correos de confirmación."
                    );
                }
            }
        }

        /*
         * =================================
         * PUBLICACIONES LIBRIFLOW
         * =================================
         */

        if (carritoAdmin != null
                && !carritoAdmin.isEmpty()) {

            DetalleRentaDao detalleRentaDao =
                    new DetalleRentaDao();

            for (ItemCarritoAdmin item
                    : carritoAdmin) {

                PublicacionAdministradorDao
                        publicacionAdministradorDao =
                        new PublicacionAdministradorDao();

                String tipoOperacion =
                        item.getTipoOperacion()
                                .toUpperCase();

                tipoOperacion =
                        tipoOperacion.equals("VENTA")
                                ? "COMPRA"
                                : tipoOperacion;

                DetalleTransaccion detalleTransaccion =
                        new DetalleTransaccion();

                detalleTransaccion.setIdTransaccion(
                        idTransaccion
                );

                detalleTransaccion.setIdPublicacionLf(
                        item.getIdPublicacion()
                );

                detalleTransaccion.setTipoOperacion(
                        tipoOperacion
                );

                detalleTransaccion.setPrecio(
                        item.getPrecio()
                );

                detalleTransaccion.setGananciaVendedor(
                        0.0
                );

                detalleTransaccion.setGananciaLibriFlow(
                        item.getPrecio()
                );

                int idDetalleTransaccion =
                        detalleTransaccionDao.create(
                                detalleTransaccion
                        );

                if (idDetalleTransaccion == -1) {

                    throw new RuntimeException(
                            "No se pudo crear el detalle de la transacción "
                                    + "para la publicación LibriFlow con id: "
                                    + item.getIdPublicacion()
                    );
                }

                System.out.println(
                        "Detalle transacción creado con id: "
                                + idDetalleTransaccion
                );

                if (!publicacionAdminDao
                        .disminuirInventario(
                                item.getIdPublicacion()
                        )) {

                    throw new RuntimeException(
                            "No se pudo disminuir el inventario "
                                    + "de la publicación con id: "
                                    + item.getIdPublicacion()
                    );
                }

                if ("RENTA".equals(
                        tipoOperacion
                )) {

                    DetalleRenta renta =
                            new DetalleRenta();

                    renta.setIdDetalle(
                            idDetalleTransaccion
                    );

                    renta.setFechaInicio(
                            item.getFechaInicio()
                    );

                    renta.setFechaLimite(
                            item.getFechaFin()
                    );

                    renta.setEstado(
                            "PROGRAMADA"
                    );

                    String codigoRenta =
                            generarCodigoRenta();

                    renta.setCodigo(
                            codigoRenta
                    );

                    int idDetalleRenta =
                            detalleRentaDao.create(
                                    renta
                            );

                    if (idDetalleRenta == -1) {

                        throw new RuntimeException(
                                "No se pudo crear el detalle de renta."
                        );
                    }

                    PublicacionAdminCompleta publicacion =
                            publicacionAdministradorDao
                                    .getPublicacionAdminCompleta(
                                            item.getIdPublicacion()
                                    );

                    librosRentados.append(
                            "<p><strong>Libro:</strong> "
                                    + publicacion.getTitulo()
                                    + "</p>"
                                    + "<p><strong>Código de renta:</strong> "
                                    + codigoRenta
                                    + "</p>"
                                    + "<p><strong>Precio:</strong> $"
                                    + String.format(
                                    "%.2f",
                                    item.getPrecio()
                            )
                                    + "</p>"
                                    + "<p><strong>Fecha de inicio de renta:</strong> "
                                    + item.getFechaInicio()
                                    + "</p>"
                                    + "<p><strong>Fecha límite de renta:</strong> "
                                    + item.getFechaFin()
                                    + "</p><hr>"
                    );

                    System.out.println(
                            "Detalle renta creado con id: "
                                    + idDetalleRenta
                    );

                } else {

                    if (guiaSeguimiento.isEmpty()) {
                        guiaSeguimiento =
                                generarGuiaSeguimiento();
                    }

                    PublicacionAdminCompleta publicacion =
                            publicacionAdministradorDao
                                    .getPublicacionAdminCompleta(
                                            item.getIdPublicacion()
                                    );

                    librosComprados.append(
                            "<p><strong>Libro (LibriFlow):</strong> "
                                    + publicacion.getTitulo()
                                    + "</p>"
                                    + "<p><strong>Precio:</strong> $"
                                    + String.format(
                                    "%.2f",
                                    item.getPrecio()
                            )
                                    + "</p><hr>"
                    );
                }
            }
        }

        /*
         * =================================
         * CORREO RESUMEN DEL COMPRADOR
         * =================================
         */

        try {

            String cuerpoCorreoResumen =
                    getcuerpoResumenCompra(
                            usuario.getNombre(),
                            transaccion.getTotal(),
                            librosComprados.toString(),
                            librosRentados.toString(),
                            guiaSeguimiento
                    );

            EmailSender.sendMail(
                    usuario.getCorreo(),
                    "Resumen de tu compra en LibriFlow",
                    cuerpoCorreoResumen
            );

        } catch (Exception e) {

            System.out.println(
                    "Error al mandar el resumen de compra."
            );

            e.printStackTrace();

            /*
             * Tampoco cancelamos una compra ya realizada
             * únicamente porque falle el correo.
             */
            req.setAttribute(
                    "errorCorreo",
                    "La compra fue procesada, pero no se pudo enviar el correo de confirmación."
            );
        }

        System.out.println(
                "Todo se insertó correctamente."
        );

        /*
         * La compra terminó correctamente.
         * Limpiamos los carritos de la sesión.
         */
        session.removeAttribute(
                "carritoAdmin"
        );

        session.removeAttribute(
                "carrito"
        );
    }
    /**
     * Construye la estructura HTML inicial para el cuerpo de los correos electrónicos.
     *
     * @return Cadena con el HTML del encabezado del correo.
     */
    private String encabezado() {

        return """
                <div style="max-width:650px;margin:auto;background:#F4EFEA;
                            border-radius:18px;overflow:hidden;
                            font-family:Arial,sans-serif;color:#4A4641;
                            border:1px solid #E5DDD3;">

                    <div style="background:#5B564F;padding:28px;text-align:center;">
                        <h2 style="color:white;margin-top:12px;">
                            📚 LibriFlow
                        </h2>
                    </div>

                    <div style="padding:35px;">
                """;
    }
    /**
     * Genera la sección de saludo personalizado para el destinatario en el correo.
     *
     * @param nombre Nombre del destinatario.
     * @return Cadena con el HTML correspondiente al saludo.
     */
    private String saludo(String nombre) {

        return """
                <h2 style="margin-top:0;">
                    ¡Gracias por tu compra!
                </h2>

                <p style="font-size:15px;line-height:1.7;">
                    Hola <strong>"""
                + nombre
                + """
                    </strong>,
                    hemos procesado correctamente tu pedido.
                    A continuación encontrarás toda la información.
                </p>
                """;
    }

    /**
     * Genera el resumen del pago en formato HTML.
     *
     * @param total Monto total del pago.
     * @return Cadena con el HTML correspondiente al resumen.
     */
    private String resumen(double total) {

        return """
                <div style="
                    background:#F8F5F2;
                    border-radius:12px;
                    padding:18px;
                    margin:25px 0;">

                    <h3 style="margin-top:0;">
                        Resumen del pago
                    </h3>

                    <p>
                        Hemos recibido correctamente el pago correspondiente a tu pedido.
                    </p>

                    <p>
                        <strong>Total pagado:</strong> $"""
                + total
                + """
                    </p>

                    <p style="margin-bottom:0;">
                        El cargo fue procesado correctamente utilizando el método de pago registrado.
                    </p>

                </div>
                """;
    }
    /**
     * Construye la sección HTML de confirmación de envío y lista de libros comprados.
     *
     * @param codigoSeguimiento Guía de rastreo asignada al paquete.
     * @param libros            Detalle formateado en HTML de los libros comprados.
     * @return Cadena HTML con el bloque de compra, o una cadena vacía si no hay libros comprados.
     */
    private String bloqueCompra(
            String codigoSeguimiento,
            String libros) {

        if (libros == null
                || libros.isEmpty()) {

            return "";
        }

        return """
                <div style="
                    background:#F8F5F2;
                    border-radius:12px;
                    padding:18px;
                    margin:25px 0;">

                    <h3 style="margin-top:0;">
                        📦 Compra confirmada
                    </h3>

                    <p>
                        Tu pedido será preparado y enviado en los próximos días.
                    </p>

                    <p>
                        <strong>Código de seguimiento:</strong><br>
                        """
                + codigoSeguimiento
                + """
                    </p>

                    <p>
                        <strong>Tu pedido de libro(s) incluye:</strong><br><br>
                        """
                + libros
                + """
                    </p>

                </div>
                """;
    }

    /**
     * Construye la sección HTML de confirmación de renta y lista de libros rentados.
     *
     * @param libros Detalle formateado en HTML de los libros rentados.
     * @return Cadena HTML con el bloque de renta, o una cadena vacía si no hay libros rentados.
     */
    private String bloqueRenta(
            String libros) {

        if (libros == null
                || libros.isEmpty()) {

            return "";
        }

        return """
                <div style="
                    background:#F8F5F2;
                    border-radius:12px;
                    padding:18px;
                    margin:25px 0;">

                    <h3 style="margin-top:0;">
                        📚 Renta confirmada
                    </h3>

                    <p>
                        Tus libros ya se encuentran reservados para ti.
                        Para recogerlos, acude a la <strong>Biblioteca de la UTEZ</strong>
                        y presenta el siguiente código de retiro.
                    </p>

                    <p>
                        <strong>Tus libro(s) rentados son:</strong><br><br>
                        """
                + libros
                + """
                    </p>

                </div>
                """;
    }
    /**
     * Genera el cierre y pie de página institucional del correo electrónico.
     *
     * @return Cadena HTML con la despedida e información de derechos de autor.
     */
    private String despedida() {

        return """
                    <p style="
                        margin-top:35px;
                        line-height:1.7;">

                        Si tienes alguna duda puedes responder este
                        correo o comunicarte con nuestro equipo.

                    </p>

                    <p>
                        ¡Gracias por confiar en LibriFlow!
                    </p>

                    </div>

                    <div style="
                        background:#5B564F;
                        color:white;
                        text-align:center;
                        padding:20px;
                        font-size:13px;">

                        © LibriFlow · Todos los derechos reservados.

                    </div>

                </div>
                """;
    }
    /**
     * Construye el correo de notificación dirigido a un vendedor cuando uno de sus libros es vendido.
     *
     * @param nombre      Nombre del vendedor.
     * @param tituloLibro Título de la obra vendida.
     * @param ganancia    Monto a acreditar correspondiente al vendedor.
     * @return Cadena HTML con la notificación de venta realizada.
     */
    private String correoVentaRealizada(
            String nombre,
            String tituloLibro,
            double ganancia) {

        return """
                <div style="max-width:650px;margin:auto;background:#F4EFEA;
                            border-radius:18px;overflow:hidden;
                            font-family:Arial,sans-serif;color:#4A4641;
                            border:1px solid #E5DDD3;">

                    <div style="background:#5B564F;padding:28px;text-align:center;">
                        <h2 style="color:white;margin:12px 0 0 0;">
                            📚 LibriFlow
                        </h2>
                    </div>

                    <div style="padding:35px;">

                        <h2 style="margin-top:0;">
                            ¡Tu libro encontró un nuevo lector!
                        </h2>

                        <p style="font-size:15px;line-height:1.8;">
                            Hola <strong>"""
                + nombre
                + """
                            </strong>,
                            nos alegra informarte que uno de los libros que publicaste
                            en LibriFlow acaba de ser vendido.
                        </p>

                        <div style="
                            background:#F8F5F2;
                            border-radius:12px;
                            padding:18px;
                            margin:25px 0;">

                            <h3 style="margin-top:0;">
                                Detalles de la venta
                            </h3>

                            <p>
                                <strong>Libro:</strong><br>
                                """
                + tituloLibro
                + """
                            </p>

                            <p>
                                <strong>Monto acreditado:</strong><br>
                                $"""
                + String.format("%.2f", ganancia)
                + """
                            </p>

                        </div>

                        <p style="line-height:1.8;">
                            Gracias por confiar en LibriFlow para compartir tus libros.
                            Esperamos verte muy pronto realizando más ventas.
                        </p>

                    </div>

                    <div style="
                        background:#5B564F;
                        color:white;
                        text-align:center;
                        padding:20px;
                        font-size:13px;">

                        Este es un correo automático de LibriFlow.
                        No es necesario responder este mensaje.

                    </div>

                </div>
                """;
    }
    /**
     * Compone la estructura completa del correo de resumen de compra unificando encabezado, saludo, resumen y bloques correspondientes.
     *
     * @return Cadena HTML completa del correo de confirmación.
     */
    private String getcuerpoResumenCompra(
            String nombre,
            double total,
            String librosComprados,
            String librosRentados,
            String codigoSeguimientoCompra) {

        return encabezado()
                + saludo(nombre)
                + resumen(total)
                + bloqueCompra(
                codigoSeguimientoCompra,
                librosComprados
        )
                + bloqueRenta(
                librosRentados
        )
                + despedida();
    }
    /**
     * Genera un código único para la identificación de la renta de libros.
     *
     * @return Cadena con el código de renta generado.
     */
    private String generarCodigoRenta() {

        return UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
    /**
     * Genera un código único para la guía de seguimiento de envío.
     *
     * @return Cadena con el código de seguimiento generado.
     */
    private String generarGuiaSeguimiento() {

        long numero =
                (long) (
                        Math.random()
                                * 9000000000L
                )
                        + 1000000000L;

        return String.valueOf(numero);
    }
}