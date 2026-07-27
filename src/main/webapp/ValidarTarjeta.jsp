<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Validar Tarjeta - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Inicio.css"/>
</head>
<body class="pantalla-registro">

<!-- Barra superior estilo LibriFlow -->
<div class="top-bar">
    <a href="carrito" class="back-link me-2">
        <i class="bi bi-arrow-left"></i>
    </a>
    <h1 class="m-0 text-white fs-5 fw-semibold">Método de Pago</h1>
</div>

<!-- Contenedor Tarjeta -->
<div class="card-container">
    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" alt="Logo LibriFlow" class="logo-img-completo">
    <h2 class="card-title">Validar Tarjeta</h2>

    <c:if test="${not empty error}">
        <div class="error-msg">
                ${error}
        </div>
    </c:if>

    <form action="validar-tarjeta" method="post">
        <div class="form-group mb-3">
            <label for="titular">Nombre del Titular</label>
            <input type="text" id="titular" name="titular" value="${titular}" placeholder="Como aparece en la tarjeta" required>
        </div>

        <div class="form-group mb-3">
            <label for="numeroTarjeta">Número de Tarjeta</label>
            <input type="text" id="numeroTarjeta" name="numeroTarjeta" value="${numeroTarjeta}" placeholder="16 dígitos continuos" maxlength="16" required>
        </div>

        <div class="form-grid">
            <div class="form-group">
                <label for="fechaVencimiento">Vencimiento (MM/AA)</label>
                <input type="text" id="fechaVencimiento" name="fechaVencimiento" value="${fechaVencimiento}" placeholder="MM/AA" maxlength="5" required>
            </div>
            <div class="form-group">
                <label for="cvv">CVV</label>
                <input type="password" id="cvv" name="cvv" placeholder="3 o 4 dígitos" maxlength="4" required>
            </div>
        </div>

        <button type="submit" class="btn-submit mt-4">
            <i class="bi bi-credit-card me-2"></i>Pagar ahora
        </button>
    </form>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
</body>
</html>