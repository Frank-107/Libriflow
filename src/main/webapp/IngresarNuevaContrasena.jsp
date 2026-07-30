<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nueva Contraseña - LibriFlow</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
</head>
<body class="login-body">


<header class="login-top-bar">
    <h1 class="login-top-bar-title">Restablecer contraseña</h1>
</header>


<main class="login-card">

    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png"
         alt="LibriFlow"
         class="login-logo">

    <h2 class="login-card-title">NUEVA CONTRASEÑA</h2>
    <p class="login-card-subtitle">Ingresa tu nueva contraseña para actualizar tu cuenta</p>

    <% if (request.getAttribute("error") != null) { %>
    <div class="login-error">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>


    <form action="ingresar-nueva-contrasena" method="POST">
        <input type="hidden" name="accion" value="cambiarContra">
        <label for="nuevaContrasena" class="login-label-pass">Nueva contraseña:</label>
        <input type="password" id="nuevaContrasena" name="nuevaContrasena" class="login-input login-contrasena" placeholder="Nueva contraseña" required>
        <button type="submit" class="login-btn">Restablecer</button>
    </form>

</main>

</body>
</html>