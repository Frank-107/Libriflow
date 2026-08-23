package mx.edu.utez.libriflow.controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
/**
 * El filtro FiltroNoCache intercepta todas las peticiones HTTP de la plataforma
 * LibriFlow para configurar los encabezados (headers) de respuesta de manera que
 * se deshabilite el almacenamiento en caché del navegador web.
 * Evita la sobreescritura o almacenamiento local de datos sensibles y asegura que,
 * al navegar por la aplicación o presionar el botón "atrás", la información mostrada
 * sea siempre en tiempo real.
 *
 * @author Anzures Visoso Monserrath
 * @since 23/08/2026
 */
@WebFilter("/*")
public class FiltroNoCache implements Filter {

    /**
     * Aplica los encabezados de respuesta de no-almacenamiento en caché
     * (Cache-Control, Pragma y Expires) a la respuesta HTTP y continúa con el flujo
     * normal de la solicitud a través de la cadena de filtros.
     *
     * @author Anzures Visoso Monserrath
     * @since 23/08/2026
     *
     * @param request Objeto de solicitud Servlet del cliente.
     * @param response Objeto de respuesta Servlet para el cliente.
     * @param chain Cadena de filtros para delegar la ejecución de la petición.
     * @throws IOException Si ocurre una falla de entrada/salida durante la ejecución.
     * @throws ServletException Si ocurre una anomalía en el procesamiento del contenedor web.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

      httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);

        chain.doFilter(request, response);
    }
}