<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restablecer Contraseña - LibriFlow</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
</head>
<body class="login-body">

<header class="login-top-bar">
    <a href="IniciarSesion.jsp" class="login-back-link" aria-label="Volver a Iniciar Sesión"></a>
    <h1 class="login-top-bar-title">Restablecer contraseña</h1>
</header>


<main class="login-card">

    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png"
         alt="Logo LibriFlow"
         class="login-logo">

    <h2 class="login-card-title">RESTABLECER CONTRASEÑA</h2>
    <p class="login-card-subtitle">Ingresa tu correo para enviarte un código de recuperación</p>


    <% if (request.getAttribute("error") != null) { %>
    <div class="login-error">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <c:if test="${not empty error}">
        <div class="login-success">
                ${error}
        </div>
    </c:if>


    <form action="restablecer-contrasena" method="POST">

        <label for="correo" class="login-label-pass">Correo:</label>
        <input type="email" id="correo" name="correo" class="login-input login-correo" placeholder="20263ds117@utez.edu.mx" required>
        <button type="submit" class="login-btn">Enviar</button>

    </form>

</main>

</body>
</html>