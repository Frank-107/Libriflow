<%--
    Documento   : index.jsp
    Autor       : Monserrath Anzurez
    Fecha       : 23/08/26
    Descripción : Vista principal/landing page de LibriFlow para acceso pública y registro de usuarios.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>LibriFlow - Tu Biblioteca Digital</title>
  <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="stylesheet" href="assets/css/bootstrap.css" />
  <link rel="stylesheet" href="assets/css/index.css?v=3" />
</head>
<body>


<nav class="bg-lf-dark rounded-lf-header navbar-outer shadow-sm d-flex align-items-center px-4 px-md-5">
  <div class="nav-links">
    <a href="https://www.instagram.com/libriflow.oficial?igsh=MW9qbmNld2M2ZXNyeA==" class="btn-nav">Nuestras Redes</a>
  </div>
</nav>

<!-- Contenido principal dentro del contenedor -->
<div class="container-pagina">

  <div class="hero">
    <div class="logo-contenedor">
      <img src="assets/img/LogoLibriflow.png" alt="LibriFlow Tu Biblioteca Digital" class="logo-img" />
    </div>

    <c:if test="${not empty sessionScope.mensaje}">
      <div class="libri-toast libri-toast-success">
        <i class="bi bi-check-circle-fill fs-5"></i>
        <span><c:out value="${sessionScope.mensaje}" escapeXml="true" /></span>
      </div>
      <c:remove var="mensaje" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
      <div class="libri-toast libri-toast-error">
        <i class="bi bi-exclamation-circle-fill fs-5"></i>
        <span><c:out value="${sessionScope.error}" escapeXml="true" /></span>
      </div>
      <c:remove var="error" scope="session"/>
    </c:if>

    <h1 class="titulo-principal">Miles de libros a un solo click</h1>

    <div class="botones-accion">
      <a href="iniciar-sesion" class="btn-oscuro">Iniciar Sesión</a>
      <a href="crear-cuenta-usuario" class="btn-claro">Crear Cuenta</a>
    </div>
  </div>

  <div class="catalogo-libros">
    <div class="row justify-content-center g-4">

      <div class="col-6 col-sm-4 col-md-3">
        <div class="tarjeta-libro">
          <div class="img-contenedor">
            <img src="./assets/img/El_Conde_de_Montecristo.png" alt="El Conde de Montecristo" class="portada-img" />
          </div>
        </div>
      </div>

      <div class="col-6 col-sm-4 col-md-3">
        <div class="tarjeta-libro">
          <div class="img-contenedor">
            <img src="./assets/img/Twilight.png" alt="Twilight" class="portada-img" />
          </div>
        </div>
      </div>

      <div class="col-6 col-sm-4 col-md-3">
        <div class="tarjeta-libro">
          <div class="img-contenedor">
            <img src="./assets/img/It.png" alt="IT - Stephen King" class="portada-img" />
          </div>
        </div>
      </div>

      <div class="col-6 col-sm-4 col-md-3">
        <div class="tarjeta-libro">
          <div class="img-contenedor">
            <img src="./assets/img/Jardin_de_las_Mariposas.png" alt="El Jardín de las Mariposas" class="portada-img" />
          </div>
        </div>
      </div>

    </div>
  </div>

</div>

<footer class="footer">
  <div class="footer-container">
    <div class="footer-col brand-col">
      <h3 class="footer-logo">Libriflow</h3>
      <p class="footer-tagline">Miles de libros a un solo click.</p>
    </div>

    <div class="footer-col">
      <h4>Soporte</h4>
      <ul>
        <li><a href="#">Centro de ayuda</a></li>
        <li><a href="#">Preguntas Frecuentes</a></li>
        <li><a href="#">Contacto</a></li>
      </ul>
    </div>

    <div class="footer-col">
      <h4>Legales</h4>
      <ul>
        <li><a href="#">Términos y Condiciones</a></li>
        <li><a href="#">Aviso de Privacidad</a></li>
        <li><a href="#">Política de reembolsos</a></li>
      </ul>
    </div>

  </div>

  <div class="footer-bottom">
    <p class="copyright-text">
      <img src="assets/css/bi/c-circle.svg" alt="Copyright" width="14" height="14">
      2026 Libriflow. Todos los derechos reservados.
    </p>

    <div class="social-links">
      <a href="https://www.instagram.com/libriflow.oficial?igsh=MW9qbmNld2M2ZXNyeA==" aria-label="Instagram">
        <img src="assets/css/bi/instagram.svg" alt="Instagram" width="20" height="20">
      </a>
      <a href="#" aria-label="X">
        <img src="assets/css/bi/twitter-x.svg" alt="X" width="20" height="20">
      </a>
      <a href="#" aria-label="Facebook">
        <img src="assets/css/bi/facebook.svg" alt="Facebook" width="20" height="20">
      </a>
    </div>

  </div>
</footer>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/Notificacion.js"></script>
</body>
</html>