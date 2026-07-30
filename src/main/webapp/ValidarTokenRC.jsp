<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ingresar Código - LibriFlow</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
</head>
<body class="login-body">

<header class="login-top-bar">
    <a href="RestablecerContrasena.jsp" class="login-back-link" aria-label="Volver"></a>
    <h1 class="login-top-bar-title">Verificar código</h1>
</header>


<main class="login-card">

    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png"
         alt="LibriFlow"
         class="login-logo">

    <h2 class="login-card-title">INGRESA TU CÓDIGO</h2>
    <p class="login-card-subtitle">Ingresa el código de 6 dígitos que te enviamos por correo.</p>


    <% if (request.getAttribute("error") != null) { %>
    <div class="login-error">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>


    <form action="validar-token-rc" method="POST">
        <input type="hidden" name="accion" value="validarToken">
        <label for="codigoIngresado" class="login-label-pass">Código de verificación:</label>
        <input type="text" id="codigoIngresado" name="codigoIngresado" class="login-input" maxlength="6" required autocomplete="off" placeholder="123456">
        <button type="submit" class="login-btn">Validar</button>
    </form>

</main>

</body>
</html>