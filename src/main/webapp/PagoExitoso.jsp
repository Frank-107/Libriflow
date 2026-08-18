<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pago Procesado - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="pantalla-registro">
<%
    HttpSession sessionActual = request.getSession(false);

    if (sessionActual == null || sessionActual.getAttribute("pagoRealizado") == null) {
        response.sendRedirect(request.getContextPath() + "/inicio");
        System.out.println("no puedes ver el pago realizado sin haber pagado");
        return;
    }
    session.removeAttribute("pagoRealizado");

%>
<div class="top-bar">
    <a href="${pageContext.request.contextPath}/inicio" class="back-link" title="Ir al Inicio">←</a>
    <h1>Confirmación de Compra</h1>
</div>

<div class="card-container" style="padding: 45px 40px;">
    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" alt="LibriFlow" class="logo-img-completo">

    <div style="margin: 15px 0;">
        <i class="bi bi-check-circle-fill" style="font-size: 4.2rem; color: #15803d;"></i>
    </div>

    <h2 class="card-title" style="margin-bottom: 10px;">¡PAGO EXITOSO!</h2>
    <p style="color: #6e6762; font-size: 15px; font-weight: 500; margin-bottom: 30px; line-height: 1.5;">
        Tu transacción ha sido procesada correctamente.<br>
        Hemos enviado toda la informacion a tu correo. <br>
        ¡Gracias por comprar en <strong>LibriFlow</strong>!
    </p>

    <a href="${pageContext.request.contextPath}/inicio" class="btn-submit" style="display: block; text-decoration: none; width: 100%; box-sizing: border-box;">
        <i class="bi bi-house-door-fill" style="margin-right: 8px;"></i>Volver al Inicio</a>

</div>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
</body>
</html>