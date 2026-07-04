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

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body class="login-body">

<a href="index.jsp" class="login-back-link">Volver</a>

<h2 class="login-top-bar">Inicio de sesión</h2>

<form action="Iniciar_sesionSv" method="post" class="login-card">
    <c:if test="${not empty error}">
        <div style="color: red;" class="login-error">${error}</div>
    </c:if>

    <label for="correo" class="login-label-email">Correo:</label>
    <br class="login-br">
    <input type="email" id="correo" name="correo" value="${param.correo}" required class="login-input" placeholder="Correo electrónico">
    <br class="login-br"><br class="login-br">

    <label for="contrasena" class="login-label-pass">Contraseña:</label>
    <br class="login-br">
    <input type="password" id="contrasena" name="contrasena" value="${param.contrasena}" required class="login-input" placeholder="Contraseña">
    <br class="login-br"><br class="login-br">

    <button type="submit" class="login-btn">Iniciar sesión </button>
    <br class="login-br">
    <a href="Crear_cuenta_usuario.jsp" class="login-footer-link">¿No tienes cuenta? Crear cuenta</a>

</form>

</body>
</html>