<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Libriflow - Crear Cuenta</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
    <link rel="stylesheet" href="assets/css/styles.css">
</head>
<body>

<div class="pantalla-registro">

    <div class="top-bar">
        <a href="index.jsp" class="back-link">&#x2190;</a>
        <h1>Crear cuenta</h1>
    </div>

    <div class="card-container">

        <div class="logo-area">
            <img src="assets/img/LogoLibriflow.png" alt="Logotipo LibriFlow" class="logo-img-completo">
        </div>

        <h3 class="card-title">Crear cuenta</h3>

        <c:if test="${not empty error}">
            <div class="error-msg">${error}</div>
        </c:if>

        <form method="post" action="Crear_cuenta_usuarioSv">
            <div class="form-grid">

                <div class="form-group">
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

                <div class="form-group">
                    <label for="telefono">Numero telefonico</label>
                    <input type="number" id="telefono" name="telefono" value="${param.telefono}" required>
                </div>

                <div class="form-group">
                    <label for="correo">Correo electrónico</label>
                    <input type="email" id="correo" name="correo" value="${param.correo}" required>
                </div>

                <div class="form-group">
                    <label for="correo2">Confirmar correo electrónico</label>
                    <input type="email" id="correo2" name="correo2" value="${param.correo2}" required>
                </div>

                <div class="form-group">
                    <label for="contrasena">Contraseña</label>
                    <input type="password" id="contrasena" name="contrasena" value="${param.contrasena}" required>
                </div>

                <div class="form-group">
                    <label for="contrasena2">Confirmar contraseña</label>
                    <input type="password" id="contrasena2" name="contrasena2" value="${param.contrasena2}" required>
                </div>

            </div>

            <input type="submit" class="btn-submit" value="Registrar">

            <a href="Iniciar_sesion.jsp" class="login-redirect">¿Ya tienes una cuenta? Inicia sesión</a>
        </form>
    </div>

</div>

</body>
</html>