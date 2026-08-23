package mx.edu.utez.libriflow.controller;

import com.google.gson.Gson;
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
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ActualizarPerfilSvJS", value = "/actualizar-perfil-js")
public class ActualizarPerfilSvJS extends HttpServlet {

    private final PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
    private final CompraDao compraDao = new CompraDao();
    private final RentaDao rentaDao = new RentaDao();
    private final Gson gson = new Gson();

    private static final int MAX_TEXTO_CORTO = 50;
    private static final int MAX_PASSWORD = 100;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        cargarContadores(req, usuarioSesion.getId());

        req.getRequestDispatcher("ActualizarPerfilJS.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        PrintWriter out = resp.getWriter();
        Map<String, Object> respuesta = new HashMap<>();
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            respuesta.put("status", "error");
            respuesta.put("message", "Sesión expirada. Por favor inicie sesión de nuevo.");
            out.print(gson.toJson(respuesta));
            out.flush();
            return;
        }

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        String nombre = req.getParameter("nombre");
        String apellidoPaterno = req.getParameter("apellidoPaterno");
        String apellidoMaterno = req.getParameter("apellidoMaterno");
        String telefono = req.getParameter("telefono");

        String nuevaContrasena = req.getParameter("nueva_contrasena");
        String confirmarContrasena = req.getParameter("confirmar_contrasena");

        if (nombre == null || nombre.trim().isEmpty() ||
                apellidoPaterno == null || apellidoPaterno.trim().isEmpty() ||
                apellidoMaterno == null || apellidoMaterno.trim().isEmpty()) {

            respuesta.put("status", "error");
            respuesta.put("message", "Todos los campos de texto personales son obligatorios.");
            out.print(gson.toJson(respuesta));
            out.flush();
            return;
        }

        if (nombre.trim().length() > MAX_TEXTO_CORTO ||
                apellidoPaterno.trim().length() > MAX_TEXTO_CORTO ||
                apellidoMaterno.trim().length() > MAX_TEXTO_CORTO) {

            respuesta.put("status", "error");
            respuesta.put("message", "Los nombres y apellidos no pueden exceder los 50 caracteres.");
            out.print(gson.toJson(respuesta));
            out.flush();
            return;
        }

        if (telefono == null || !telefono.trim().matches("\\d{10}")) {
            respuesta.put("status", "error");
            respuesta.put("message", "Formato de teléfono inválido (deben ser 10 dígitos numéricos).");
            out.print(gson.toJson(respuesta));
            out.flush();
            return;
        }

        boolean cambioPasswordExitoso = false;
        String nuevoHashPassword = null;

        if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {

            if (!nuevaContrasena.equals(confirmarContrasena)) {
                respuesta.put("status", "error");
                respuesta.put("message", "Las contraseñas no coinciden.");
                out.print(gson.toJson(respuesta));
                out.flush();
                return;
            }

            if (nuevaContrasena.trim().length() < 8 || nuevaContrasena.trim().length() > MAX_PASSWORD) {
                respuesta.put("status", "error");
                respuesta.put("message", "La contraseña debe tener entre 8 y 100 caracteres.");
                out.print(gson.toJson(respuesta));
                out.flush();
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
                    respuesta.put("status", "error");
                    respuesta.put("message", "Ocurrió un problema al guardar tu nueva contraseña.");
                    out.print(gson.toJson(respuesta));
                    out.flush();
                    return;
                }
                cambioPasswordExitoso = true;

            } catch (Exception e) {
                respuesta.put("status", "error");
                respuesta.put("message", "Error interno al procesar la contraseña.");
                out.print(gson.toJson(respuesta));
                out.flush();
                return;
            }
        }

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

            respuesta.put("status", "success");
            respuesta.put("message", "¡Tu perfil se ha actualizado con éxito!");
            respuesta.put("nombre", usuarioSesion.getNombre());
        } else {
            respuesta.put("status", "error");
            respuesta.put("message", "Ocurrió un error inesperado al actualizar tus datos personales.");
        }

        out.print(gson.toJson(respuesta));
        out.flush();
    }

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