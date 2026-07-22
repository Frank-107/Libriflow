<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Libriflow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body class="login-body">

<div class="login-top-bar">
    <a href="index.jsp" class="login-back-link">Volver</a>
    <h2 class="login-top-bar-title">Iniciar sesión</h2>
</div>

<form action="iniciar-sesion" method="post" class="login-card"  onsubmit="let btn=this.querySelector('.btn-submit'); btn.disabled=true; btn.innerHTML='Iniciando sesión...';">

    <c:if test="${not empty error}">
        <div class="libri-toast libri-toast-error">
            <i class="bi bi-exclamation-circle-fill fs-5"></i>
            <span><c:out value="${error}" escapeXml="true" /></span>
        </div>
    </c:if>

    <!-- Imagen .jpg agregada en la parte superior del contenedor -->
    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" alt="Logo Libriflow" class="login-logo">

    <label for="correo" class="login-label-email">Correo:</label>
    <input type="email" id="correo" name="correo" value="${param.correo}" required class="login-input login-correo" placeholder="Ej: 20263ds117@utez.edu.mx">

    <label for="contrasena" class="login-label-pass">Contraseña:</label>
    <input type="password" id="contrasena" name="contrasena" value="${param.contrasena}" required class="login-input login-contrasena" placeholder="Ej: 12345678">

    <button type="submit" class="btn btn-action-lf shadow-sm btn-submit">Iniciar sesión </button>
    <br class="login-br">

    <div class="login-footer-row">
        <a href="#" class="login-footer-link">Restablecer contraseña</a>
        <a href="crear-cuenta-usuario" class="login-footer-link">¿No tienes cuenta? Crear cuenta</a>
    </div>

</form>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/Notificacion.js"></script>
</body>
</html>