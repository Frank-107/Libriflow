package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controlador Servlet encargado de gestionar el acceso inicial y la redirección hacia la vista principal del sistema.
 * Sirve como punto de entrada de la aplicación al despachar la petición hacia la página `index.jsp`.
 *
 * @author Monserrath Anzurez
 * @since 23/08/26
 */
@WebServlet(name = "indexSv", value = "/index")
public class indexSv extends HttpServlet {

    /**
     * Procesa las peticiones GET para redirigir al usuario hacia la vista principal (`index.jsp`).
     *
     * @param req Objeto HttpServletRequest que representa la petición del cliente.
     * @param resp Objeto HttpServletResponse utilizado para realizar el reenvío a la vista.
     * @throws ServletException Si ocurre un error técnico en el reenvío del Servlet.
     * @throws IOException Si ocurre un error de lectura/escritura durante el procesamiento HTTP.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    /**
     * Maneja las peticiones HTTP POST delegando la respuesta al método por defecto de la clase superior.
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}