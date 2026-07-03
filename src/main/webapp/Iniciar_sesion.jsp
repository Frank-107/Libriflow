<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Libriflow</title>
</head>
<body>
<a href="index.jsp">Volver</a>

<h2>Inicio de sesión</h2>

<form action="Iniciar_sesionSv" method="post">
    <c:if test="${not empty error}">
        <div style="color: red;">${error}</div>
    </c:if>

    <label for="correo">Correo:</label>
    <br>
    <input type="email" id="correo" name="correo" value="${param.correo}" required>
    <br><br>

    <label for="contrasena">Contraseña:</label>
    <br>
    <input type="password" id="contrasena" name="contrasena" value="${param.contrasena}" required>
    <br><br>

    <button type="submit" >Iniciar sesión </button>
    <br>
    <a href="Crear_cuenta_usuario.jsp">¿No tienes cuenta? Crear cuenta</a>

</form>

</body>
</html>