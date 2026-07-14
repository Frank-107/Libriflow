<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verificar correo</title>
</head>
<body>

<h2>Verifica tu correo electrónico</h2>

<p>
    Hemos enviado un código de verificación al correo:
    <strong>${sessionScope.usuarioPendiente.correo}</strong>
</p>

<p>
    Ingresa el código que recibiste para activar tu cuenta.
</p>
<c:if test="${not empty error}">
    <div class="error-msg">${error}</div>
</c:if>
<form action="validar-correo-cc" method="POST">
    <input type="text" name="codigo" placeholder="Código de verificación" required>
    <br><br>
    <button type="submit">Verificar</button>
</form>

</body>
</html>