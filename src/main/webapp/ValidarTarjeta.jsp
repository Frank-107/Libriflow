<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Validar Tarjeta - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="pantalla-registro">
<div class="top-bar">
    <a href="${pageContext.request.contextPath}/carrito" class="back-link" title="Volver">←</a>
    <h1>Método de Pago</h1>
</div>
<div class="card-container">
    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" alt="LibriFlow" class="logo-img-completo">

    <h2 class="card-title">VALIDAR TARJETA</h2>
    <% if (request.getAttribute("error") != null) { %>
    <div class="error-msg" style="color: #cc0000; background-color: #f8d7da; padding: 10px; border-radius: 10px; margin-bottom: 15px; font-size: 14px;">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>
    <form action="validar-tarjeta" method="POST">

        <div class="form-section-title">Datos de la Tarjeta</div>

        <!-- Campo Nombre del Titular -->
        <div class="form-group" style="margin-bottom: 12px;">
            <label for="titular">Nombre del Titular</label>
            <input type="text" id="titular" name="titular" value="${titular != null ? titular : ''}" placeholder="Como aparece en la tarjeta" required>
        </div>
        <div class="form-group" style="margin-bottom: 12px;">
            <label for="numeroTarjeta">Número de Tarjeta</label>
            <input type="text" id="numeroTarjeta" name="numeroTarjeta" value="${numeroTarjeta != null ? numeroTarjeta : ''}" placeholder="16 dígitos continuos" maxlength="16" required>
        </div>
        <div class="form-grid">
            <div class="form-group">
                <label for="fechaVencimiento">Vencimiento</label>
                <input type="text" id="fechaVencimiento" name="fechaVencimiento" value="${fechaVencimiento != null ? fechaVencimiento : ''}" placeholder="MM/AA" maxlength="5" required>
            </div>

            <div class="form-group">
                <label for="cvv">CVV</label>
                <input type="password" id="cvv" name="cvv" placeholder="3 o 4 dígitos" maxlength="4" required>
            </div></div>
        <button type="submit" class="btn-submit" style="margin-top: 20px;">
            <i class="bi bi-credit-card-fill" style="margin-right: 8px;"></i>Pagar ahora
        </button>

    </form>

</div>

</body>
</html>