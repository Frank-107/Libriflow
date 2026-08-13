package mx.edu.utez.libriflow.controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class FiltroNoCache implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        boolean esRutaPublica = path.equals("/")
                || path.equals("/index.jsp")
                || path.equals("/iniciar-sesion")
                || path.equals("/iniciar-sesion.jsp")
                || path.startsWith("/assets/")
                || path.startsWith("/css/")
                || path.startsWith("/js/");

        HttpSession session = httpRequest.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("usuario") != null);

        if (loggedIn || esRutaPublica) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/iniciar-sesion");
        }
    }
}