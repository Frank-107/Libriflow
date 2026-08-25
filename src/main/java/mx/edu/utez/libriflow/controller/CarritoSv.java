package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionAdministradorDao;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.ItemCarritoAdmin;
import mx.edu.utez.libriflow.model.PublicacionAdminCompleta;
import mx.edu.utez.libriflow.model.PublicacionResumen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Controlador Servlet encargado de gestionar las operaciones del carrito de compras y rentas.
 * Permite consolidar items de usuarios e ítems administrados por LibriFlow, así como coordinar
 * el proceso de eliminación de elementos y redirección hacia el flujo de pago o envío.
 *
 * @author Monserrath Anzurez
 * @since 23/08/26
 */
@WebServlet(name = "CarritoSv", value = "/carrito")
public class CarritoSv extends HttpServlet {
    /** Objeto DAO para consultar las publicaciones realizadas por los usuarios. */
    private final PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
    /** Objeto DAO para consultar las publicaciones oficiales de la administración de LibriFlow. */
    private final PublicacionAdministradorDao adminDao = new PublicacionAdministradorDao();


    /**
     * Procesa las peticiones GET para construir la lista consolidada de publicaciones en el carrito.
     * Recupera tanto los ítems almacenados en el carrito de usuarios como los del carrito administrado,
     * modela las propiedades de precios o fechas de renta y despacha la información a la vista JSP.
     *
     * @param req Objeto HttpServletRequest que transporta la sesión del usuario y recibe los datos para la vista.
     * @param resp Objeto HttpServletResponse para despachar la petición a la vista correspondiente.
     * @throws ServletException Si ocurre un fallo en el reenvío de la vista por parte del Servlet.
     * @throws IOException Si ocurre un error de lectura o escritura durante la comunicación HTTP.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        List<PublicacionResumen> publicaciones = new ArrayList<>();

        // 1. Procesar publicaciones de usuarios (Carrito tradicional)
        List<Integer> idsPublicaciones = (List<Integer>) session.getAttribute("carrito");

        if (idsPublicaciones != null && !idsPublicaciones.isEmpty()) {
            List<PublicacionResumen> pubsUsuario = publicacionUsuarioDao.getPublicacionesByArreglo(idsPublicaciones);
            if (pubsUsuario != null) {
                publicaciones.addAll(pubsUsuario);
            }
        }

        // 2. Procesar publicaciones de administración (Carrito Admin)
        List<ItemCarritoAdmin> carritoAdmin = (List<ItemCarritoAdmin>) session.getAttribute("carritoAdmin");

        if (carritoAdmin != null && !carritoAdmin.isEmpty()) {
            for (ItemCarritoAdmin item : carritoAdmin) {
                PublicacionAdminCompleta adminPub = adminDao.getPublicacionAdminCompleta(item.getIdPublicacion());

                if (adminPub != null) {
                    PublicacionResumen resumenAdmin = new PublicacionResumen();
                    resumenAdmin.setIdPublicacion(adminPub.getIdPublicacionLf());
                    resumenAdmin.setTitulo(adminPub.getTitulo());
                    resumenAdmin.setAutor(adminPub.getAutor());
                    resumenAdmin.setGenero(adminPub.getGenero());
                    resumenAdmin.setImagenPrincipal(adminPub.getImagenPrincipal());
                    resumenAdmin.setEsLibriFlow(true);

                    // Asignar precio y fechas según el tipo de operación
                    if ("renta".equalsIgnoreCase(item.getTipoOperacion())) {
                        resumenAdmin.setPrecio(item.getPrecio());
                        resumenAdmin.setEsRentaSeleccionada(true);

                        // Asignación de timestamps provenientes del ItemCarritoAdmin
                        resumenAdmin.setFechaInicio(item.getFechaInicio());
                        resumenAdmin.setFechaFin(item.getFechaFin());
                    } else {
                        resumenAdmin.setPrecio(adminPub.getPrecio());
                        resumenAdmin.setEsRentaSeleccionada(false);
                    }

                    publicaciones.add(resumenAdmin);
                }
            }
        }

        // Guardar la lista construida en el request para enviarla a la vista JSP
        req.setAttribute("publicaciones", publicaciones);

        // Redirigir a la vista del carrito (ajusta la ruta según tu estructura)
        req.getRequestDispatcher("Carrito.jsp").forward(req, resp);
    }
    /**
     * Procesa las peticiones POST para ejecutar acciones dentro del carrito, como proceder al pago o eliminar ítems.
     * Evalúa si la compra requiere dirección de envío o redirige directamente a la verificación de pago,
     * o bien elimina publicaciones específicas de los arreglos almacenados en sesión.
     *
     * @param req Objeto HttpServletRequest que contiene la acción a realizar y los parámetros de los ítems.
     * @param resp Objeto HttpServletResponse utilizado para redirigir al usuario según la acción ejecutada.
     * @throws ServletException Si ocurre una falla interna en la ejecución del Servlet.
     * @throws IOException Si ocurre un error de E/S durante las redirecciones HTTP.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);

        if ("comprar".equals(action)) {
            session.setAttribute("puedeDireccion", true);

            boolean tieneCompras = Boolean.parseBoolean(req.getParameter("contieneEnvio"));
            double subtotal = Double.parseDouble(req.getParameter("subtotal"));

            session.setAttribute("subtotal", subtotal);

            if (tieneCompras) {
                session.setAttribute("puedePagar", true);
                resp.sendRedirect("direccion-envio");
                return;
            }

            session.setAttribute("envio", 0.0);
            session.setAttribute("puedePagar", true);
            resp.sendRedirect("validar-tarjeta");
            return;
        }

        if ("eliminar".equals(action)) {
            int idEliminar = Integer.parseInt(req.getParameter("idPublicacion"));
            boolean eliminadoDeUsuario = false;

            List<Integer> ids = (List<Integer>) session.getAttribute("carrito");

            if (ids != null && ids.contains(Integer.valueOf(idEliminar))) {
                ids.remove(Integer.valueOf(idEliminar));
                session.setAttribute("carrito", ids);
                eliminadoDeUsuario = true;
            }

            if (!eliminadoDeUsuario) {
                List<ItemCarritoAdmin> carritoAdmin = (List<ItemCarritoAdmin>) session.getAttribute("carritoAdmin");

                if (carritoAdmin != null) {
                    carritoAdmin.removeIf(item -> item.getIdPublicacion() == idEliminar);
                    session.setAttribute("carritoAdmin", carritoAdmin);
                }
            }

            resp.sendRedirect("carrito");
        }
    }
    /**
     * Maneja las peticiones de eliminación HTTP DELETE delegando el comportamiento a la clase superior.
     *
     * @param req Objeto HttpServletRequest de la petición.
     * @param resp Objeto HttpServletResponse de la respuesta.
     * @throws ServletException Si ocurre una falla en el Servlet.
     * @throws IOException Si ocurre un error de E/S.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
    /**
     * Maneja las peticiones de actualización HTTP PUT delegando el comportamiento a la clase superior.
     *
     * @param req Objeto HttpServletRequest de la petición.
     * @param resp Objeto HttpServletResponse de la respuesta.
     * @throws ServletException Si ocurre una falla en el Servlet.
     * @throws IOException Si ocurre un error de E/S.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }
}
