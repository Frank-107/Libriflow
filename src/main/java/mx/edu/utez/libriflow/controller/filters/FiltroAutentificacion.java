package mx.edu.utez.libriflow.controller.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//@WebFilter("/*")
public class FiltroAutentificacion extends HttpFilter {

    private static final String ROL_ADMIN = "ADMIN";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String ruta = req.getRequestURI();
        HttpSession session = req.getSession(false);

        String rol = (session != null) ? (String) session.getAttribute("tipo_usuario") : null;
        boolean logeado = rol != null;
        boolean esAdmin = ROL_ADMIN.equalsIgnoreCase(rol);

        boolean rutaLogin =
                ruta.endsWith("IniciarSesion.jsp") ||
                        ruta.endsWith("/iniciar-sesion") ||
                        ruta.endsWith("CrearCuenta.jsp") ||
                        ruta.endsWith("/crear-cuenta-usuario") ||
                        ruta.endsWith("ValidarCorreoCC.jsp") ||
                        ruta.endsWith("/validar-correo-cc")||
                        ruta.endsWith("RestablecerContrasena.jsp")||
                        ruta.endsWith("/restablecer-contrasena") ||
                        ruta.endsWith("ValidarTokenRC.jsp")||
                        ruta.endsWith("/validar-token-rc")||
                        ruta.endsWith("IngresarNuevaContrasena.jsp")||
                        ruta.endsWith("/ingresar-nueva-contrasena");

        boolean publico =
                ruta.endsWith("index.jsp") ||
                        ruta.endsWith("index") ||
                        ruta.endsWith("/Libriflow_war/") ||
                        ruta.contains("/assets/") ||
                        ruta.contains("/uploads/") ||
                        ruta.endsWith("/");

        // Nueva ruta permitida
        boolean rutaCerrarSesion =
                ruta.endsWith("/cerrar-sesion");

        boolean rutaAdmin = ruta.toLowerCase().contains("admin");

        // Usuario no autenticado
        if (!logeado) {
            if (rutaLogin || publico) {
                chain.doFilter(req, res);
            } else {
                System.out.println("Dirección no permitida: " + ruta);
                System.out.println("Usuario no autenticado, redirigiendo al inicio de sesión.");
                res.sendRedirect(req.getContextPath() + "/iniciar-sesion");
            }
            return;
        }

        // Usuario autenticado intentando entrar al login
        if (rutaLogin) {
            res.sendRedirect(req.getContextPath() + (esAdmin ? "/inicio-admin" : "/inicio"));
            return;
        }

        // Permitir siempre cerrar sesión
        if (rutaCerrarSesion) {
            chain.doFilter(req, res);
            return;
        }

        // El administrador solo puede acceder a sus rutas
        if (esAdmin && !rutaAdmin && !publico) {
            System.out.println("Admin intentó salir de su zona: " + ruta);
            res.sendRedirect(req.getContextPath() + "/inicio-admin");
            return;
        }

        // Un usuario normal no puede acceder a rutas de administrador
        if (!esAdmin && rutaAdmin) {
            System.out.println("Acceso denegado: usuario intentó acceder a zona admin -> " + ruta);
            res.sendRedirect(req.getContextPath() + "/inicio");
            return;
        }

        chain.doFilter(req, res);
    }
}