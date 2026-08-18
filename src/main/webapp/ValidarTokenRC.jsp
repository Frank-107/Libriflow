<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ingresar Código - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
</head>
<body class="login-body">

<header class="login-top-bar">
    <a href="RestablecerContrasena.jsp" class="login-back-link" aria-label="Volver"></a>
    <h1 class="login-top-bar-title">Verificar código</h1>
</header>


<main class="login-card">

    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png"
         alt="LibriFlow"
         class="login-logo">

    <h2 class="login-card-title">INGRESA TU CÓDIGO</h2>
    <p class="login-card-subtitle">Ingresa el código de 6 dígitos que te enviamos por correo.</p>

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



    <form action="validar-token-rc" method="POST">
        <input type="hidden" name="accion" value="validarToken">
        <label for="codigoIngresado" class="login-label-pass">Código de verificación:</label>
        <input type="text" id="codigoIngresado" name="codigoIngresado" class="login-input" maxlength="6" required autocomplete="off" placeholder="123456">
        <button type="submit" class="login-btn">Validar</button>
    </form>

</main>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/Notificacion.js"></script>
</body>
</html>