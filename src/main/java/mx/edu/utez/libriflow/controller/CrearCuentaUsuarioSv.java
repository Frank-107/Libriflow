package mx.edu.utez.libriflow.controller;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.UsuarioDao;
import mx.edu.utez.libriflow.model.Usuario;
import mx.edu.utez.libriflow.utils.EmailSender;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.MessageFormat;

@WebServlet(name = "CrearCuentaUsuarioSv", value = "/crear-cuenta-usuario")
public class CrearCuentaUsuarioSv extends HttpServlet {
    UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("CrearCuenta.jsp").forward(req, resp);

    }

        @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String nombre = req.getParameter("nombre");
        String apellidoPaterno = req.getParameter("apellidoPaterno");
        String apellidoMaterno = req.getParameter("apellidoMaterno");
        String correo = req.getParameter("correo");
        String correo2 = req.getParameter("correo2");
        String contrasena = req.getParameter("contrasena");
        String contrasena2 = req.getParameter("contrasena2");
        String telefono = req.getParameter("telefono");


        if (!correo.equals(correo2)) {
            req.setAttribute("error", "Los correos no coinciden.");
            req.getRequestDispatcher("CrearCuenta.jsp").forward(req, resp);
            return;
        }
        if (!correo.endsWith("@utez.edu.mx")) {
            req.setAttribute("error", "Solo se admiten correos institucionales (UTEZ).");
            req.getRequestDispatcher("CrearCuenta.jsp").forward(req, resp);
            return;
        }

        if (!contrasena.equals(contrasena2)) {
            req.setAttribute("error", "Las contraseñas no coinciden.");
            req.getRequestDispatcher("CrearCuenta.jsp").forward(req, resp);
            return;
        }
        if(contrasena.length() < 8){
            req.setAttribute("error", "La contraseña debe tener al menos 8 caracteres.");
            req.getRequestDispatcher("CrearCuenta.jsp").forward(req, resp);
            return;
        }

        if (!telefono.matches("\\d{10}")) {
            req.setAttribute("error", "Formato de teléfono inválido.");
            req.getRequestDispatcher("CrearCuenta.jsp").forward(req, resp);
            return;
        }
        if (usuarioDao.correoExistente(correo)){
            req.setAttribute("error", "Ya tienes una cuenta con nosotros, intententa iniciando sesión");
            req.getRequestDispatcher("CrearCuenta.jsp").forward(req, resp);
            return;
        }

            String contrasenaHash = null;
        //proceso de hash
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hash = md.digest(contrasena.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();

            for (byte b : hash) {
                sb.append(String.format("%02X", b));
            }

            contrasenaHash = sb.toString();
        }catch (Exception e) {
            req.setAttribute("error", "Error al procesar la contraseña.");
            req.getRequestDispatcher("CrearCuenta.jsp").forward(req, resp);
            return;
        }

        Usuario usuarioPendiente = new Usuario(
                nombre,
                apellidoPaterno,
                apellidoMaterno,
                correo,
                telefono,
                contrasenaHash
        );
            HttpSession session = req.getSession(true);
            session.setAttribute("usuarioPendiente", usuarioPendiente);
            //Mandar correo para verificar
            String plantillaHtml = """
                    <html>
                    <head>
                        <meta charset="UTF-8">
                    </head>
                    <body style="margin:0; padding:0; background-color:#F6F1E9; font-family:Arial, Helvetica, sans-serif;">
                    
                        <div style="max-width:600px; margin:40px auto; background:#FFFFFF; border-radius:12px; overflow:hidden; border:1px solid #D8CDBF;">
                    
                            <div style="background:#6B4F3A; padding:25px; text-align:center;">
                                <h1 style="margin:0; color:#F6F1E9;">📚 LibriFlow</h1>
                            </div>
                    
                            <div style="padding:35px; color:#4A3B31;">
                    
                                <h2 style="margin-top:0;">¡Hola, {0}!</h2>
                    
                                <p>
                                    Gracias por registrarte en <strong>LibriFlow</strong>.
                                    Para proteger tu cuenta y confirmar que el correo electrónico te pertenece,
                                    es necesario verificar tu dirección de correo.
                                </p>
                    
                                <p style="margin-top:30px;">
                                    Tu código de verificación es:
                                </p>
                    
                                <div style="text-align:center; margin:30px 0;">
                                    <span style="
                                        display:inline-block;
                                        padding:18px 35px;
                                        font-size:30px;
                                        font-weight:bold;
                                        letter-spacing:6px;
                                        color:#6B4F3A;
                                        background:#F3ECE3;
                                        border:2px dashed #B89B7A;
                                        border-radius:10px;">
                                        {1}
                                    </span>
                                </div>
                    
                                <p>
                                    Ingresa este código en la página de verificación para completar el proceso de creación de tu cuenta.
                                </p>
                    
                                <hr style="border:none; border-top:1px solid #DDD; margin:35px 0;">
                    
                                <p style="font-size:13px; color:#777777;">
                                    Si no solicitaste crear una cuenta en LibriFlow, puedes ignorar este mensaje.
                                    No se realizará ningún registro sin completar la verificación.
                                </p>
                    
                            </div>
                    
                            <div style="background:#EFE5D8; padding:18px; text-align:center; font-size:12px; color:#6F6257;">
                                © 2026 LibriFlow · Plataforma para la compra y renta de libros.
                            </div>
                    
                        </div>
                    
                    </body>
                    </html>
                    """;
            SecureRandom random = new SecureRandom();
            int codigoInt = 100000 + random.nextInt(900000);
            String codigo = String.valueOf(codigoInt);
            session.setAttribute("codigoVerificacion", codigo);
            System.out.println("se guardo el codigo "+ codigo +" en la sesion");

            String cuerpoCorreo = MessageFormat.format(
                    plantillaHtml,
                    usuarioPendiente.getNombre(),
                    codigo
            );


            try {
                EmailSender.sendMail(
                        usuarioPendiente.getCorreo(),
                        "Verificación de correo electrónico - LibriFlow",
                        cuerpoCorreo
                );
            } catch (Exception e) {
                System.err.println(e);
                req.setAttribute("error","No se pudo enviar el correo de verificacion");
                req.getRequestDispatcher("CrearCuenta.jsp").forward(req, resp);
            }

            resp.sendRedirect("validar-correo-cc");
            return;
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
