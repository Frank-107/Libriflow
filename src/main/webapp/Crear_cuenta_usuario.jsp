<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Libriflow</title>
</head>
<body>
<a href="index.jsp">Volver</a>
<H1>Crear cuenta</h1>
<form method="post" action="crear_cuenta_usuarioSv">
    <c:if test="${not empty error}">
        <div style="color: red;">${error}</div>
    </c:if>


        <label for="nombre">Nombre:</label><br>
    <input type="text" id="nombre" name="nombre" value="${param.nombre}" required><br><br>

    <label for="apellidoPaterno">Apellido Paterno:</label><br>
    <input type="text" id="apellidoPaterno" name="apellidoPaterno" value="${param.apellidoPaterno}" required><br><br>

    <label for="apellidoMaterno">Apellido Materno:</label><br>
    <input type="text" id="apellidoMaterno" name="apellidoMaterno" value="${param.apellidoMaterno}" required><br><br>

    <label for="correo">Correo Electrónico:</label><br>
    <input type="email" id="correo" name="correo" value="${param.correo}" required><br><br>

    <label for="contrasena">Crea tu contraseña:</label><br>
    <input type="password" id="contrasena" name="contrasena" value="${param.contrasena}" required><br><br>

    <label for="contrasena2">Confirma tu contraseña:</label><br>
    <input type="password" id="contrasena2" name="contrasena2" value="${param.contrasena2}" required><br><br>


    <input type="submit" value="Crear cuenta">
    <br>
    <a href="Iniciar_sesion.jsp"> ¿Ya tienes cuenta? Iniciar sesión</a>
</form>

</body>
</html>