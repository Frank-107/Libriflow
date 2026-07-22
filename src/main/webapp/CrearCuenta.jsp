<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Libriflow - Crear Cuenta</title>

    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body>

<div class="pantalla-registro">

    <div class="top-bar">
        <a href="index.jsp" class="back-link">&#x2190;</a>
        <h1>Crear cuenta</h1>
    </div>

    <div class="card-container">

        <div class="logo-area">
            <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" alt="Logotipo LibriFlow" class="logo-img-completo">
        </div>

        <h3 class="card-title">Crea tu cuenta</h3>
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

        <form method="post" action="crear-cuenta-usuario"
              onsubmit="let btn=this.querySelector('.btn-submit'); btn.disabled=true; btn.innerHTML='Registrando...';">

            <div class="form-section-title">
                <i class="bi bi-person-vcard me-2"></i> Datos Personales
            </div>
            <div class="form-grid">
                <div class="form-group" style="grid-column: span 2;">
                    <label for="nombre">Nombre(s)</label>
                    <input type="text" id="nombre" name="nombre" value="${param.nombre}" required>
                </div>
                <div class="form-group">
                    <label for="apellidoPaterno">Apellido Paterno</label>
                    <input type="text" id="apellidoPaterno" name="apellidoPaterno" value="${param.apellidoPaterno}" required>
                </div>
                <div class="form-group">
                    <label for="apellidoMaterno">Apellido Materno</label>
                    <input type="text" id="apellidoMaterno" name="apellidoMaterno" value="${param.apellidoMaterno}" required>
                </div>
            </div>

            <div class="form-section-title">
                <i class="bi bi-envelope-at me-2"></i> Información de Contacto
            </div>
            <div class="form-grid">
                <div class="form-group">
                    <label for="telefono">Número telefónico</label>
                    <input type="number" id="telefono" name="telefono" value="${param.telefono}" required>
                </div>
                <div class="form-group">
                    <label for="correo">Correo electrónico</label>
                    <input type="email" id="correo" name="correo" value="${param.correo}" required>
                </div>
            </div>

            <div class="form-section-title">
                <i class="bi bi-shield-lock me-2"></i> Seguridad
            </div>
            <div class="form-grid">
                <div class="form-group">
                    <label for="contrasena">Contraseña</label>
                    <input type="password" id="contrasena" name="contrasena" value="${param.contrasena}" required>
                </div>
                <div class="form-group">
                    <label for="contrasena2">Confirmar contraseña</label>
                    <input type="password" id="contrasena2" name="contrasena2" value="${param.contrasena2}" required>
                </div>
            </div>

            <button type="submit" class="btn btn-action-lf shadow-sm btn-submit" style="margin-top: 20px;">Registrar</button>

            <a href="iniciar-sesion" class="login-redirect">¿Ya tienes una cuenta? Inicia sesión</a>
        </form>
    </div>

</div>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/Notificacion.js"></script>
</body>
</html>