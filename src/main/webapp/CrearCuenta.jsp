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
            <!-- 1. Estilos visuales redondeados y posicionamiento -->
            <style>
                .libri-toast {
                    position: fixed;
                    top: 100px; /* <--- Ajusta este número para subirlo o bajarlo más */
                    right: 40px; /* Distancia desde la derecha */
                    background-color: #fca5a5; /* Rosa/rojo suave idéntico a tu interfaz */
                    color: #b91c1c; /* Texto rojo oscuro */
                    padding: 10px 20px;
                    border-radius: 25px; /* Bordes muy redondeados (estilo píldora) */
                    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                    font-family: sans-serif;
                    font-size: 14px;
                    font-weight: 600;
                    display: flex;
                    align-items: center;
                    gap: 10px; /* Espacio entre el icono y el texto */
                    z-index: 9999;

                    /* Animación suave de entrada (deslice de arriba a abajo) */
                    opacity: 0;
                    transform: translateY(-20px);
                    transition: opacity 0.4s ease, transform 0.4s ease;
                }

                /* Clase para activar la animación */
                .libri-toast.show {
                    opacity: 1;
                    transform: translateY(0);
                }

                .libri-toast svg {
                    flex-shrink: 0;
                    stroke: #b91c1c;
                }
            </style>

            <!-- 2. Contenedor del Mensaje -->
            <div id="errorToast" class="libri-toast">
                <!-- Icono de advertencia en formato SVG (no requiere librerías externas) -->
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                    <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"/>
                    <line x1="12" y1="9" x2="12" y2="13"/>
                    <line x1="12" y1="17" x2="12.01" y2="17"/>
                </svg>

                <!-- Texto dinámico de tu error -->
                <span><c:out value="${error}" escapeXml="true" /></span>
            </div>

            <!-- 3. Animación y desaparición automática -->
            <script>
                document.addEventListener("DOMContentLoaded", function() {
                    const toast = document.getElementById('errorToast');

                    // Aparece suavemente a los 100ms de cargar la página
                    setTimeout(() => {
                        toast.classList.add('show');
                    }, 100);

                    // Se desvanece y se elimina por completo tras 3.5 segundos
                    setTimeout(() => {
                        toast.classList.remove('show');

                        // Espera a que termine la animación de desvanecimiento para borrar el HTML
                        setTimeout(() => {
                            toast.remove();
                        }, 400);
                    }, 3500);
                });
            </script>
        </c:if>

        <form method="post" action="crear-cuenta-usuario"
              onsubmit="this.querySelector('.btn-submit').disabled=true; this.querySelector('.btn-submit').value='Enviando...';">
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

            <a href="iniciar-sesion" class="login-redirect">¿Ya tienes una cuenta? Inicia sesión</a>
        </form>
    </div>

</div>

</body>
</html>