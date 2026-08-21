<%--
  Vista: RestablecerContrasena.jsp
  Descripción: Vista del formulario para solicitar el restablecimiento de contraseña.
               Permite al usuario ingresar su correo institucional y despliega
               alertas visuales en caso de haber errores devueltos por el Servlet.

  Acción / Servlet asociado: /restablecer-contrasena (POST)
  Atributos de Request utilizados:
    - "error" (String, opcional): Mensaje de error enviado por RestablecerContrasenaSv cuando la validación falla.

  @author Irvin Abarca Arenas
  @since 21/08/2026
--%>

<%-- Directivas de página y taglib JSTL para manejo de lógica condicional e impresiones seguras --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restablecer Contraseña - LibriFlow</title>
    <%-- Recursos de la vista: Fav-icon y hoja de estilos principal --%>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
</head>
<body class="login-body">
<%-- Encabezado con enlace de retorno al login --%>
<header class="login-top-bar">
    <a href="IniciarSesion.jsp" class="login-back-link" aria-label="Volver a Iniciar Sesión"></a>
    <h1 class="login-top-bar-title">Restablecer contraseña</h1>
</header>

<%-- Tarjeta principal de la interfaz --%>
<main class="login-card">
    <%-- Identidad visual de la plataforma LibriFlow --%>
    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png"
         alt="Logo LibriFlow"
         class="login-logo">

    <h2 class="login-card-title">RESTABLECER CONTRASEÑA</h2>
    <p class="login-card-subtitle">Ingresa tu correo para enviarte un código de recuperación</p>

    <%-- Bloque de notificación: Se renderiza dinámicamente si la variable "error" está presente en la solicitud --%>
    <c:if test="${not empty error}">
        <div id="errorToast" class="libri-toast libri-toast-error">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"/>
                <line x1="12" y1="9" x2="12" y2="13"/>
                <line x1="12" y1="17" x2="12.01" y2="17"/>
            </svg>
            <span><c:out value="${error}" escapeXml="true" /></span>
        </div>
    </c:if>
    <%-- Formulario de captura de correo procesado por RestablecerContrasenaSv --%>
    <form action="restablecer-contrasena" method="POST" novalidate>

        <label for="correo" class="login-label-pass">Correo:</label>
        <input type="email" id="correo" name="correo" class="login-input login-correo" placeholder="20263ds117@utez.edu.mx" required>
        <button type="submit" class="login-btn">Enviar</button>

    </form>

</main>
<%-- Inclusión de scripts de soporte para Bootstrap y alertas dinámicas --%>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/Notificacion.js"></script>
</body>
</html>