<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pago Procesado - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Inicio.css"/>
</head>
<body class="pantalla-registro">
<div class="card-container text-center py-5">
    <i class="bi bi-check-circle-fill text-success" style="font-size: 4rem;"></i>
    <h2 class="card-title mt-3">¡Pago Realizado con Éxito!</h2>
    <p class="text-muted">Tu transacción ha sido procesada correctamente.</p>
    <a href="inicio" class="btn-submit d-inline-block text-decoration-none mt-3" style="max-width: 200px;">
        Volver al Inicio
    </a>
</div>
</body>
</html>