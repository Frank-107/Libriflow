<%--
    Archivo: 500.jsp
    Descripción: Vista personalizada de error 500 (Error interno del servidor) adaptada a la identidad visual de LibriFlow.
    Autor: Francisco Emmanuel Fuentes Pérez
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 - Error Interno del Servidor | LibriFlow</title>

    <link rel="icon" href="${pageContext.request.contextPath}/main/webapp/assets/img/LogoLibriflowF.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/main/webapp/assets/css/bootstrap.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/main/webapp/assets/css/styles.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/main/webapp/assets/css/Error500.css"/>
</head>
<body class="pantalla-registro error-page-body">

<div class="card-container error-card">

    <%-- Logo institucional LibriFlow --%>
    <div class="logo-area mb-2">
        <img src="${pageContext.request.contextPath}/main/webapp/assets/img/LogoLibriflowF.png"
             alt="Logotipo LibriFlow"
             class="logo-img-completo">
    </div>

    <%-- Ilustración / Código de error visual con engrane/servidor --%>
    <div class="error-code-wrapper">
        <span class="error-number">5</span>
        <div class="error-icon-capsule">
            <i class="bi bi-gear-fill spin-icon"></i>
        </div>
        <span class="error-number">0</span>
    </div>

    <h2 class="card-title mt-2">¡ERROR DEL SERVIDOR!</h2>

    <p class="error-message">
        Algo ha fallado en nuestros estantes digitales. Estamos trabajando para solucionar este inconveniente técnico lo antes posible.
    </p>

    <%-- Botones de acción principal --%>
    <div class="error-actions">
        <a href="${pageContext.request.contextPath}/" class="btn btn-action-lf shadow-sm w-100">
            <i class="bi bi-house-door-fill me-2"></i>Volver al Inicio
        </a>
    </div>

</div>

<script src="${pageContext.request.contextPath}/main/webapp/assets/js/bootstrap.js"></script>
</body>
</html>