package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.CompraDao;
import mx.edu.utez.libriflow.model.Dao.CredencialDao;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.Dao.RentaDao;
import mx.edu.utez.libriflow.model.Dao.UsuarioDao;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * El servlet ActualizarPerfilSv permite a los usuarios clientes gestionar y actualizar
 * sus datos personales (nombre, apellidos, teléfono), modificar su contraseña de forma
 * segura y consultar los indicadores de actividad de su cuenta.
 *
 * @author Francisco Emmanuel Fuentes Perez
 * @author Alejandro Mena Pereyda
 * @author Monserrath Anzures Visoso
 * @since 21/08/2026
 */
@WebServlet(name = "ActualizarPerfilSv", value = "/actualizar-perfil")
public class ActualizarPerfilSv extends HttpServlet {

    private final PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
    private final CompraDao compraDao = new CompraDao();
    private final RentaDao rentaDao = new RentaDao();

    // Límites de seguridad para evitar desbordamientos de datos en MySQL
    private static final int MAX_TEXTO_CORTO = 50; // Para Nombre y Apellidos
    private static final int MAX_PASSWORD = 100;

    /**
     * El método doGet verifica la sesión del usuario, invoca la carga de contadores de
     * estadísticas personales y canaliza la solicitud hacia la vista del perfil.
     *
     * @author Francisco Emmanuel Fuentes Perez
     * @author Alejandro Mena Pereyda
     * @author Monserrath Anzures Visoso
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP.
     * @param resp Objeto de respuesta HTTP.
     * @throws ServletException Si ocurre un error en la navegación hacia el JSP.
     * @throws IOException Si se genera un fallo en la entrada/salida o redirección.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        // 1. Redirección si la sesión no existe o expiró
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        cargarContadores(req, usuarioSesion.getId());

        req.getRequestDispatcher("ActualizarPerfil.jsp").forward(req, resp);
    }

    /**
     * El método doPost procesa la actualización de datos de perfil y cambio de contraseña,
     * evaluando límites de longitud de campos, expresiones regulares de teléfono y
     * cifrado SHA-256 para contraseñas.
     *
     * @author Francisco Emmanuel Fuentes Perez
     * @author Alejandro Mena Pereyda
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP que contiene los datos del formulario de perfil.
     * @param resp Objeto de respuesta HTTP para responder al cliente.
     * @throws ServletException Si ocurre un fallo en el reenvío de la solicitud.
     * @throws IOException Si ocurre un error de transmisión de datos.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);

        // 1. Validar existencia de sesión activa
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        String nombre = req.getParameter("nombre");
        String apellidoPaterno = req.getParameter("apellidoPaterno");
        String apellidoMaterno = req.getParameter("apellidoMaterno");
        String telefono = req.getParameter("telefono");

        String nuevaContrasena = req.getParameter("nueva_contrasena");
        String confirmarContrasena = req.getParameter("confirmar_contrasena");

        // 2. Validar campos de texto obligatorios
        if (nombre == null || nombre.trim().isEmpty() ||
                apellidoPaterno == null || apellidoPaterno.trim().isEmpty() ||
                apellidoMaterno == null || apellidoMaterno.trim().isEmpty()) {

            req.setAttribute("error", "Todos los campos de texto personales son obligatorios.");
            cargarContadores(req, usuarioSesion.getId());
            req.getRequestDispatcher("ActualizarPerfil.jsp").forward(req, resp);
            return;
        }

        // 3. VALIDACIÓN DE LONGITUD MÁXIMA (Protección contra textos gigantes)
        if (nombre.trim().length() > MAX_TEXTO_CORTO ||
                apellidoPaterno.trim().length() > MAX_TEXTO_CORTO ||
                apellidoMaterno.trim().length() > MAX_TEXTO_CORTO) {

            req.setAttribute("error", "Los nombres y apellidos no pueden exceder los " + MAX_TEXTO_CORTO + " caracteres.");
            cargarContadores(req, usuarioSesion.getId());
            req.getRequestDispatcher("ActualizarPerfil.jsp").forward(req, resp);
            return;
        }

        // 4. Validar teléfono (exactamente 10 dígitos)
        if (telefono == null || !telefono.trim().matches("\\d{10}")) {
            req.setAttribute("error", "Formato de teléfono inválido (deben ser 10 dígitos numéricos).");
            cargarContadores(req, usuarioSesion.getId());
            req.getRequestDispatcher("ActualizarPerfil.jsp").forward(req, resp);
            return;
        }

        boolean cambioPasswordExitoso = false;
        String nuevoHashPassword = null;

        // 5. Validar y procesar cambio de contraseña
        if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {

            if (!nuevaContrasena.equals(confirmarContrasena)) {
                req.setAttribute("error", "Las contraseñas no coinciden.");
                cargarContadores(req, usuarioSesion.getId());
                req.getRequestDispatcher("ActualizarPerfil.jsp").forward(req, resp);
                return;
            }

            // Validar rango de caracteres de la contraseña
            if (nuevaContrasena.trim().length() < 8 || nuevaContrasena.trim().length() > MAX_PASSWORD) {
                req.setAttribute("error", "La contraseña debe tener entre 8 y " + MAX_PASSWORD + " caracteres.");
                cargarContadores(req, usuarioSesion.getId());
                req.getRequestDispatcher("ActualizarPerfil.jsp").forward(req, resp);
                return;
            }

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(nuevaContrasena.trim().getBytes(StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                for (byte b : hash) {
                    sb.append(String.format("%02X", b));
                }

                nuevoHashPassword = sb.toString();

                Usuario usuarioTempPass = new Usuario();
                usuarioTempPass.setId(usuarioSesion.getId());
                usuarioTempPass.setContrasenaHash(nuevoHashPassword);

                CredencialDao credencialDao = new CredencialDao();
                if (!credencialDao.updateCredencial(usuarioTempPass)) {
                    req.setAttribute("error", "Ocurrió un problema al guardar tu nueva contraseña. Inténtalo más tarde.");
                    cargarContadores(req, usuarioSesion.getId());
                    req.getRequestDispatcher("ActualizarPerfil.jsp").forward(req, resp);
                    return;
                }
                cambioPasswordExitoso = true;

            } catch (Exception e) {
                req.setAttribute("error", "Error interno al procesar la contraseña.");
                cargarContadores(req, usuarioSesion.getId());
                req.getRequestDispatcher("ActualizarPerfil.jsp").forward(req, resp);
                return;
            }
        }

        // 6. Actualizar datos personales en BD
        Usuario usuarioAActualizar = new Usuario();
        usuarioAActualizar.setId(usuarioSesion.getId());
        usuarioAActualizar.setNombre(nombre.trim());
        usuarioAActualizar.setApellidoPaterno(apellidoPaterno.trim());
        usuarioAActualizar.setApellidoMaterno(apellidoMaterno.trim());
        usuarioAActualizar.setTelefono(telefono.trim());

        UsuarioDao usuarioDao = new UsuarioDao();
        boolean datosActualizados = usuarioDao.update(usuarioAActualizar);

        if (datosActualizados) {
            usuarioSesion.setNombre(nombre.trim());
            usuarioSesion.setApellidoPaterno(apellidoPaterno.trim());
            usuarioSesion.setApellidoMaterno(apellidoMaterno.trim());
            usuarioSesion.setTelefono(telefono.trim());

            if (cambioPasswordExitoso) {
                usuarioSesion.setContrasenaHash(nuevoHashPassword);
            }

            session.setAttribute("usuario", usuarioSesion);
            req.setAttribute("exito", "¡Tu perfil se ha actualizado con éxito!");
        } else {
            req.setAttribute("error", "Ocurrió un error inesperado al actualizar tus datos personales.");
        }

        cargarContadores(req, usuarioSesion.getId());
        req.getRequestDispatcher("ActualizarPerfil.jsp").forward(req, resp);
    }
    /**
     * El método cargarContadores consulta las métricas de publicaciones, ventas, rentas
     * activas y retrasos de un usuario y las adjunta a la solicitud HTTP.
     *
     * @author Monserrath Anzures Visoso
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP donde se registrarán los contadores.
     * @param idUsuario Identificador único del usuario consultado.
     */
    private void cargarContadores(HttpServletRequest req, int idUsuario) {
        int totalPublicaciones = publicacionUsuarioDao.contarPublicacionesPorUsuario(idUsuario);
        int totalVendidos = compraDao.contarVentasPorUsuario(idUsuario);
        int totalEnRenta = rentaDao.contarRentasActivasPorUsuario(idUsuario);
        int totalRetrasos = rentaDao.contarRetrasosPorUsuario(idUsuario);

        req.setAttribute("totalPublicaciones", totalPublicaciones);
        req.setAttribute("totalVendidos", totalVendidos);
        req.setAttribute("totalEnRenta", totalEnRenta);
        req.setAttribute("totalRetrasos", totalRetrasos);
    }
}