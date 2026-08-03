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

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String ruta = req.getRequestURI();
        HttpSession session = req.getSession(false);

        boolean logeado = session != null && session.getAttribute("tipo_usuario") != null;

        boolean rutaLogin =
                ruta.endsWith("IniciarSesion.jsp") ||
                        ruta.endsWith("/iniciar-sesion") ||
                        ruta.endsWith("CrearCuenta.jsp") ||
                        ruta.endsWith("/crear-cuenta-usuario") ||
                        ruta.endsWith("ValidarCorreoCC.jsp") ||
                        ruta.endsWith("/validar-correo-cc");

        boolean publico =
                ruta.endsWith("index.jsp") ||
                        ruta.endsWith("index") ||
                        ruta.endsWith("/Libriflow_war/") ||
                        ruta.contains("/assets/") ||
                        ruta.endsWith("/");

        if (logeado) {

            if (rutaLogin) {
                res.sendRedirect(req.getContextPath() + "/inicio");
            } else {
                chain.doFilter(req, res);
            }

        } else {

            if (rutaLogin || publico) {
                chain.doFilter(req, res);
            } else {

                System.out.println("Dirección no permitida: " + ruta);
                System.out.println("Usuario no autenticado, redirigiendo al inicio de sesión.");

                res.sendRedirect(req.getContextPath() + "/iniciar-sesion");
            }
        }
    }
}