<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Libriflow</title>

    <!-- Hojas de estilo con rutas dinámicas de Tomcat -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body class="login-body">

<a href="index.jsp" class="login-back-link">Volver</a>

<h2 class="login-top-bar">Inicio de sesión</h2>

<form action="iniciar-sesion" method="post" class="login-card">

    <!-- Imagen .jpg agregada en la parte superior del contenedor -->
    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" alt="Logo Libriflow" class="login-logo">

    <c:if test="${not empty error}">
        <div style="color: red;" class="login-error">${error}</div>
    </c:if>

    <label for="correo" class="login-label-email">Correo:</label>
    <br class="login-br">
    <input type="email" id="correo" name="correo" value="${param.correo}" required class="login-input login-correo" placeholder="20263ds117@utez.edu.mx">
    <br class="login-br"><br class="login-br">

    <label for="contrasena" class="login-label-pass">Contraseña:</label>
    <br class="login-br">
    <input type="password" id="contrasena" name="contrasena" value="${param.contrasena}" required class="login-input login-contrasena" placeholder="•••••••••••••••••">
    <br class="login-br"><br class="login-br">

    <button type="submit" class="login-btn">Iniciar sesión </button>
    <br class="login-br">
    <a href="crear-cuenta-usuario" class="login-footer-link">¿No tienes cuenta? Crear cuenta</a>

</form>

</body>
</html>