<%--
    Archivo: ValidarCorreoCC.jsp
    Descripción: Vista para la validación del código de verificación enviado por correo electrónico durante el proceso de registro de un nuevo usuario en LibriFlow.
    Autor: Francisco Emmanuel Fuentes Pérez

    Atributos requeridos en request/session:
        - sessionScope.usuarioPendiente : Objeto Usuario con la información de la cuenta en proceso de verificación.
        - requestScope.error : Mensaje explicativo en caso de ingresar un código incorrecto o expirado.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Libriflow - Verificar Correo</title>

    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body>

<div class="pantalla-registro">

    <%-- Barra superior con enlace de retorno al formulario de registro --%>
    <div class="top-bar">
        <a href="crear-cuenta-usuario" class="back-link">&#x2190;</a>
        <h1>Verificar correo</h1>
    </div>

    <%-- Tarjeta principal de verificación --%>
    <div class="card-container">

        <div class="logo-area">
            <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" alt="Logotipo LibriFlow" class="logo-img-completo">
        </div>

        <h3 class="card-title">Verificar Correo</h3>

        <%-- Componente de notificación flotante para visualización de errores --%>
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

        <%-- Formulario de captura para el código de verificación --%>
        <form method="post" action="validar-correo-cc"
              onsubmit="let btn=this.querySelector('.btn-submit'); btn.disabled=true; btn.innerHTML='Verificando...';">

            <div style="text-align: center; margin-bottom: 20px; color: #6e6762; font-size: 14px;">
                <p style="margin-bottom: 8px;">
                    Hemos enviado un código de verificación al correo:
                </p>
                <p style="margin-bottom: 12px;">
                    <strong style="color: #4A4641; font-size: 15px;"><c:out value="${sessionScope.usuarioPendiente.correo}" /></strong>
                </p>
                <p style="margin-bottom: 0;">
                    Ingresa el código que recibiste para activar tu cuenta.
                </p>
            </div>

            <div class="form-grid">
                <div class="form-group" style="grid-column: span 2;">
                    <label for="codigo">Código de verificación:</label>
                    <input type="text" id="codigo" name="codigo" placeholder="Código de verificación" required style="text-align: center;" maxlength="10">
                </div>
            </div>

            <button type="submit" class="btn btn-action-lf shadow-sm btn-submit" style="margin-top: 20px;">Verificar</button>

        </form>
    </div>

</div>

<%-- Scripts globales de Bootstrap y notificaciones personalizadas --%>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/Notificacion.js"></script>
</body>
</html>