<%--
    Archivo: ValidarTarjeta.jsp
    Descripción: Vista que contiene el formulario para la captura y validación de datos bancarios (tarjeta de crédito/débito) durante el proceso de pago en LibriFlow.
    Autor: Francisco Emmanuel Fuentes Pérez
    since 22/08/2026


    Atributos requeridos en request/session:
        - requestScope.total : Monto total a cobrar en la transacción.
        - requestScope.error : Mensaje explicativo en caso de un rechazo o error en la transacción.
        - requestScope.titular : (Opcional) Nombre del titular previamente ingresado para rellenar el campo.
        - requestScope.numeroTarjeta : (Opcional) Número de tarjeta previamente ingresado para rellenar el campo.
        - requestScope.fechaVencimiento : (Opcional) Fecha de vencimiento previamente ingresada para rellenar el campo.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Validar Tarjeta - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="pantalla-registro">

<%-- Encabezado de navegación superior para regresar al carrito de compras --%>
<div class="top-bar">
    <a href="${pageContext.request.contextPath}/carrito" class="back-link" title="Volver">←</a>
    <h1>Método de Pago</h1>
</div>

<%-- Contenedor principal de la tarjeta de pago --%>
<div class="card-container">
    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" alt="LibriFlow" class="logo-img-completo">

    <h2 class="card-title">VALIDAR TARJETA</h2>

    <%-- Notificación de errores en caso de fallo en la validación o procesamiento bancario --%>
    <c:if test="${error != null && not empty error}">
        <div class="libri-toast libri-toast-error">
            <i class="bi bi-exclamation-circle-fill fs-5"></i>
            <span>
                <c:out value="${error}" escapeXml="true"/>
            </span>
        </div>
    </c:if>

    <%-- Formulario de captura de credenciales bancarias --%>
    <form action="validar-tarjeta" method="POST" onsubmit="this.querySelector('.btn-submit').disabled=true; this.querySelector('.btn-submit').value='Enviando...';">

        <div class="form-section-title">Datos de la Tarjeta</div>

        <%-- Campo: Nombre del Titular --%>
        <div class="form-group" style="margin-bottom: 12px;">
            <label for="titular">Nombre del Titular</label>
            <input type="text" id="titular" name="titular" value="${titular != null ? titular : ''}" placeholder="Como aparece en la tarjeta" required>
        </div>

        <%-- Campo: Número de Tarjeta (16 dígitos) --%>
        <div class="form-group" style="margin-bottom: 12px;">
            <label for="numeroTarjeta">Número de Tarjeta</label>
            <input type="text" id="numeroTarjeta" name="numeroTarjeta" value="${numeroTarjeta != null ? numeroTarjeta : ''}" placeholder="16 dígitos continuos" maxlength="16" required>
        </div>

        <%-- Bloque para Fecha de Vencimiento y CVV --%>
        <div class="form-grid">
            <div class="form-group">
                <label for="fechaVencimiento">Vencimiento</label>
                <input type="text" id="fechaVencimiento" name="fechaVencimiento" value="${fechaVencimiento != null ? fechaVencimiento : ''}" placeholder="MM/AA" maxlength="5" required>
            </div>

            <div class="form-group">
                <label for="cvv">CVV</label>
                <input type="password" id="cvv" name="cvv" placeholder="3 o 4 dígitos" maxlength="4" required>
            </div>
        </div>

        <%-- Desglose del resumen de cobro --%>
        <div class="resumen-pago mt-3">
            <div class="resumen-item">
                <span>Total a pagar</span>
                <strong>$<c:out value="${total}" /></strong>
            </div>
        </div>

        <%-- Parámetro oculto con el monto total y botón de envío --%>
        <input type="hidden" value="${total}" name="precio">
        <button type="submit" class="btn-submit" style="margin-top: 20px;">
            <i class="bi bi-credit-card-fill" style="margin-right: 8px;"></i>Pagar ahora
        </button>

    </form>

</div>

<%-- Scripts de Bootstrap y notificaciones --%>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="assets/js/Notificacion.js"></script>
</body>
</html>