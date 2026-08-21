<%--
  Vista: IngresarNuevaContrasena.jsp
  Descripción: Formulario para el ingreso y actualización de la nueva contraseña del usuario.
               Despliega notificaciones dinámicas en caso de errores de validación presentados por el Servlet.

  Acción / Servlet asociado: /ingresar-nueva-contrasena (POST)
  Atributos de Request utilizados:
    - "error" (String, opcional): Mensaje devuelto por IngresarNuevaContrasenaSv cuando la contraseña no cumple la longitud mínima o falla la actualización.

  @author Irvin Abarca Arenas
  @since 21/08/2026
--%>

<%-- Directivas de página y librerías JSTL --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nueva Contraseña - LibriFlow</title>
    <%-- Recursos estáticos: Fav-icon y hoja de estilos principal --%>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
</head>
<body class="login-body">

<%-- Encabezado superior de la interfaz --%>
<header class="login-top-bar">
    <h1 class="login-top-bar-title">Restablecer contraseña</h1>
</header>

<%-- Contenedor principal de la tarjeta de cambio de contraseña --%>
<main class="login-card">
<%--Logotipo oficial de la plataforma LibriFlow --%>
    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" alt="LibriFlow" class="login-logo">

    <h2 class="login-card-title">NUEVA CONTRASEÑA</h2>
    <p class="login-card-subtitle">Ingresa tu nueva contraseña para actualizar tu cuenta</p>
    <%-- Bloque de notificación: Muestra alertas devueltas por el backend si falla la validación --%>
    <c:if test="${not empty error}">
        <div id="errorToast" class="libri-toast libri-toast-error">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"/>
                <line x1="12" y1="9" x2="12" y2="13"/>
                <line x1="12" y1="17" x2="12.01" y2="17"/>
            </svg>
            <span><c:out value="${error}" escapeXml="true" /></span>
        </div>
    </c:if>
    <%-- Formulario de actualización de credenciales enviado a IngresarNuevaContrasenaSv --%>
    <form action="ingresar-nueva-contrasena" method="POST">
        <%-- Parámetro oculto para identificar la acción solicitada --%>
        <input type="hidden" name="accion" value="cambiarContra">
        <label for="nuevaContrasena" class="login-label-pass">Nueva contraseña:</label>
        <input type="password" id="nuevaContrasena" name="nuevaContrasena" class="login-input login-contrasena" placeholder="Nueva contraseña" required>
        <button type="submit" class="login-btn">Restablecer</button>
    </form>

</main>
<%-- Inclusión de scripts JS para Bootstrap y componentes dinámicos --%>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/Notificacion.js"></script>
</body>
</html>