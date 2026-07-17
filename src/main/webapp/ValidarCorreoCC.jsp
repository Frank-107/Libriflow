<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Verificar Correo</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body class="login-body">
s
<div class="login-top-bar">
    <a href="index.jsp" class="login-back-link">Volver</a>
    <h2 class="login-top-bar-title">Verifica tu correo electrónico</h2>
</div>

<form action="validar-correo-cc" method="POST" class="login-card" onsubmit="this.querySelector('.login-btn').disabled=true; this.querySelector('.login-btn').value='Enviando...';">

    <!-- Imagen .jpg agregada en la parte superior del contenedor -->
    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" alt="Logo Libriflow" class="login-logo">

    <c:if test="${not empty error}">
        <div class="login-error">${error}</div>
    </c:if>

    <div style="text-align: left; margin-bottom: 20px; color: #6e6762; font-size: 14px;">
        <p style="margin-bottom: 10px;">
            Hemos enviado un código de verificación al correo:
            <strong style="color: #4A4641;">${sessionScope.usuarioPendiente.correo}</strong>
        </p>
        <p style="margin-bottom: 0;">
            Ingresa el código que recibiste para activar tu cuenta.
        </p>
    </div>

    <label for="codigo" class="login-label-pass">Código de verificación:</label>
    <input type="text" id="codigo" name="codigo" placeholder="Código de verificación" required class="login-input login-contrasena">

    <button type="submit" class="login-btn">Verificar</button>

</form>

</body>
</html>