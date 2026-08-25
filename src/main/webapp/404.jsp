<%--
    Archivo: 404.jsp
    Descripción: Vista personalizada de error 404 (Recurso o página no encontrada) adaptada a la identidad visual de LibriFlow.
    Autor: Francisco Emmanuel Fuentes Pérez
    since 22/08/2026

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Página no encontrada | LibriFlow</title>

    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Error404.css"/>
</head>
<body class="pantalla-registro error-page-body">

<div class="card-container error-card">

    <%-- Logo institucional LibriFlow --%>
    <div class="logo-area mb-2">
        <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png"
             alt="Logotipo LibriFlow"
             class="logo-img-completo">
    </div>

    <%-- Ilustración / Código de error visual --%>
    <div class="error-code-wrapper">
        <span class="error-number">4</span>
        <div class="error-icon-capsule">
            <i class="bi bi-journal-x"></i>
        </div>
        <span class="error-number">4</span>
    </div>

    <h2 class="card-title mt-2">¡PÁGINA NO ENCONTRADA!</h2>

    <p class="error-message">
        Parece que el libro o la sección que buscas se ha traspapelado o ya no existe en nuestro catálogo.
    </p>

    <%-- Botones de acción principal --%>
    <div class="error-actions">
        <a href="${pageContext.request.contextPath}/inicio" class="btn btn-action-lf shadow-sm w-100">
            <i class="bi bi-house-door-fill me-2"></i>Volver al Inicio
        </a>
    </div>

</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
</body>
</html>