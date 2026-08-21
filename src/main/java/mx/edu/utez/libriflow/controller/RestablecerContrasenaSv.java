package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.UsuarioDao;
import mx.edu.utez.libriflow.utils.EmailSender;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.MessageFormat;

/**
 * El servlet RestablecerContrasenaSv sirve para gestionar las solicitudes para restablecer la contraseña
 * de los usuarios de LibriFlow mediante el envío de un código de 6 dígitos al correo del usuario.
 *
 * @author Irvin Abarca Arenas
 * @since 21/08/2026
 */
@WebServlet(name = "RestablecerContrasenaSv", value = "/restablecer-contrasena")
public class RestablecerContrasenaSv extends HttpServlet {

    UsuarioDao usuarioDao = new UsuarioDao();

    /**
     * El método doGet sirve para mostrar la vista del formulario
     * donde el usuario ingresará su correo para restablecer la contraseña.
     *
     * @author Irvin Abarca Arenas
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP.
     * @param resp Objeto de respuesta HTTP.
     */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("RestablecerContrasena.jsp").forward(req, resp);
    }


    /**
     * El método doPost sirve para validar el correo institucional UTEZ,
     * generar un código aleatorio de 6 dígitos, guardarlo en sesión y enviarlo
     * por correo electrónico.
     *
     * @author Irvin Abarca Arenas
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP que contiene el parámetro correo.
     * @param resp Objeto de respuesta HTTP para realizar redirecciones.
     */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String correo = req.getParameter("correo");

        if (correo == null || !correo.trim().toLowerCase().endsWith("@utez.edu.mx")) {
            req.setAttribute("error", "Solo se admiten correos institucionales (UTEZ).");
            req.getRequestDispatcher("RestablecerContrasena.jsp").forward(req, resp);
            return;
        }

        if (!usuarioDao.correoExistente(correo)) {
            resp.sendRedirect("ValidarTokenRC.jsp");
        } else {
            HttpSession session = req.getSession(true);
            session.setAttribute("correo", correo);
            SecureRandom random = new SecureRandom();
            String codigo = String.valueOf(100000 + random.nextInt(900000));
            session.setAttribute("codigoRC", codigo);


            //esto se va a quitar
            System.out.println("Código RC " + codigo + " guardado en sesión para " + correo);
            String plantillaHtml = """
                    <html>
                    <head><meta charset="UTF-8"></head>
                    <body style="margin:0; padding:0; background-color:#F6F1E9; font-family:Arial, Helvetica, sans-serif;">
                        <div style="max-width:600px; margin:40px auto; background:#FFFFFF; border-radius:12px; overflow:hidden; border:1px solid #D8CDBF;">
                            <div style="background:#6B4F3A; padding:25px; text-align:center;">
                                <h1 style="margin:0; color:#F6F1E9;">📚 LibriFlow</h1>
                            </div>
                            <div style="padding:35px; color:#4A3B31;">
                                <h2 style="margin-top:0;">Restablecer contraseña</h2>
                                <p>Recibimos una solicitud para restablecer la contraseña de tu cuenta.</p>
                                <p style="margin-top:30px;">Tu código de verificación es:</p>
                                <div style="text-align:center; margin:30px 0;">
                                    <span style="display:inline-block; padding:18px 35px; font-size:30px; font-weight:bold; letter-spacing:6px; color:#6B4F3A; background:#F3ECE3; border:2px dashed #B89B7A; border-radius:10px;">
                                        {0}
                                    </span>
                                </div>
                                <p>Si tú no solicitaste este cambio, ignora este mensaje.</p>
                            </div>
                            <div style="background:#EFE5D8; padding:18px; text-align:center; font-size:12px; color:#6F6257;">
                                © 2026 LibriFlow · Plataforma para la compra y renta de libros.
                            </div>
                        </div>
                    </body>
                    </html>
                    """;

            String cuerpoCorreo = MessageFormat.format(plantillaHtml, codigo);

            try {
                EmailSender.sendMail(correo, "Restablecer contraseña - LibriFlow", cuerpoCorreo);
            } catch (Exception e) {
                System.err.println(e);
                req.setAttribute("error", "No se pudo enviar el correo de recuperación.");
                req.getRequestDispatcher("RestablecerContrasena.jsp").forward(req, resp);
                return;
            }
            req.getRequestDispatcher("ValidarTokenRC.jsp").forward(req, resp);


        }
    }
}

