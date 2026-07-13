package mx.edu.utez.libriflow.controller.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import java.io.IOException;
@WebFilter("/*")
public class FiltroAutentificacion extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String ruta = req.getRequestURI();
        HttpSession session = req.getSession(false);

        boolean logeado = session != null && session.getAttribute("tipo_usuario")!=null;

        boolean publico =
                ruta.endsWith("Crear_cuenta_usuario.jsp") ||
                ruta.endsWith("index.jsp") ||
                ruta.endsWith("indexSV") ||
                ruta.endsWith("/Libriflow_war/") ||
                ruta.endsWith("/Iniciar_sesionSv") ||
                ruta.endsWith("/Crear_cuenta_usuarioSv")    ||
                ruta.contains("/assets/") ||
                ruta.endsWith("/") ||
                ruta.endsWith("/Validar_correo_CC.jsp") ||
                ruta.endsWith("/Validar_correo_CCSV") ||
                ruta.endsWith("Iniciar_sesion.jsp");

        if (publico || logeado){
        chain.doFilter(req,res);
        } else {
            res.sendRedirect(req.getContextPath() + "/Iniciar_sesion.jsp");
            System.out.println("Direccion no perimitida: "+ ruta);
            System.out.println("Usuario no autenticado, redirigiendo a la página de inicio de sesión.");
        }






    }
}
