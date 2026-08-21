package mx.edu.utez.libriflow.controller;

import jakarta.mail.Session;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.UsuarioDao;

import java.io.IOException;

/**
 * El servlet IngresarNuevaContrasenaSv sirve para procesar la actualización
 * de la contraseña del usuario, validando la longitud mínima del texto y
 * actualizando el registro en la base de datos mediante el DAO correspondiente.
 *
 * @author Irvin Abarca Arenas
 * @since 21/08/2026
 */

@WebServlet(name = "IngresarNuevaContrasenaSv", value = "/ingresar-nueva-contrasena")
public class IngresarNuevaContrasenaSv extends HttpServlet {

    UsuarioDao usuarioDao = new UsuarioDao();

    /**
     * El método doGet sirve para atender peticiones de tipo GET a este servlet.
     *
     * @author Irvin Abarca Arenas
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP.
     * @param resp Objeto de respuesta HTTP.
     * @throws ServletException Si ocurre un error interno del servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
     */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    /**
     * El método doPost sirve para recibir la nueva contraseña enviada desde el formulario,
     * verificar que tenga al menos 8 caracteres y que el estado de verificación en sesión sea válido,
     * para finalmente llamar a {@link UsuarioDao#actualizarContrasena(String, String)} y redirigir al index.
     *
     * @author Irvin Abarca Arenas
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP que contiene el parámetro nuevaContrasena y la sesión activa.
     * @param resp Objeto de respuesta HTTP para gestionar los reenvíos de vista o redirecciones.
     * @throws ServletException Si ocurre un fallo en el reenvío de la solicitud.
     * @throws IOException Si ocurre un error al procesar la redirección HTTP.
     */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession(false);
        String nuevaContrasena=req.getParameter("nuevaContrasena");
        String correo=(String) session.getAttribute("correo");

        if(nuevaContrasena.length() < 8){
            req.setAttribute("error", "La contraseña debe tener al menos 8 caracteres.");
            req.getRequestDispatcher("IngresarNuevaContrasena.jsp").forward(req, resp);
            return;
        }


        if((boolean)session.getAttribute("cambioDeContrasenaVerificado")){
            if (usuarioDao.actualizarContrasena(correo, nuevaContrasena)){
                session.setAttribute("mensaje", "Contrasena actualizada correctamente");
                session.removeAttribute("correo");
                session.removeAttribute("cambioDeContrasenaVerificado");
                resp.sendRedirect("index.jsp");
            }else {
                req.setAttribute("error","Falta de verificacion");
                req.getRequestDispatcher("IngresarNuevaContrasena.jsp").forward(req,resp);
            }


        }


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}

