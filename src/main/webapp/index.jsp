<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>LibriFlow - Tu Biblioteca Digital</title>
  <link rel="stylesheet" href="assets/css/bootstrap.css" />
  <link rel="stylesheet" href="assets/css/index.css" />
</head>
<body>

<nav class="barra-nav">
  <div class="nav-links">
    <a href="#" class="btn-nav">Nuestras Redes</a>
  </div>
</nav>

<div class="container-pagina">

  <div class="hero">
    <div class="logo-contenedor">
      <img src="assets/img/LogoLibriflow.png" alt="LibriFlow Tu Biblioteca Digital" class="logo-img" />
    </div>

    <c:if test="${not empty mensaje}">
      <div class="alert-mensaje">${mensaje}</div>
    </c:if>

    <h1 class="titulo-principal">Miles de libros a un solo click</h1>

    <div class="botones-accion">
      <a href="Iniciar_sesion.jsp" class="btn-oscuro">Iniciar Sesión</a>
      <a href="Crear_cuenta_usuario.jsp" class="btn-claro">Crear Cuenta</a>
    </div>
  </div>

  <div class="catalogo-libros">
    <div class="row justify-content-center g-4">

      <div class="col-6 col-sm-4 col-md-3">
        <div class="tarjeta-libro">
          <div class="img-contenedor">
            <img src="./assets/img/El_Conde_de_Montecristo.png" alt="El Conde de Montecristo" class="portada-img" />
          </div>
          <div class="tarjeta-pie">
            <span class="estrellas">★★★★☆</span>
            <span class="resenas">(25 reseñas)</span>
          </div>
        </div>
      </div>

      <div class="col-6 col-sm-4 col-md-3">
        <div class="tarjeta-libro">
          <div class="img-contenedor">
            <img src="./assets/img/Twilight.png" alt="Twilight" class="portada-img" />
          </div>
          <div class="tarjeta-pie">
            <span class="estrellas">★★★☆☆</span>
            <span class="resenas">(33 reseñas)</span>
          </div>
        </div>
      </div>

      <div class="col-6 col-sm-4 col-md-3">
        <div class="tarjeta-libro">
          <div class="img-contenedor">
            <img src="./assets/img/It.png" alt="IT - Stephen King" class="portada-img" />
          </div>
          <div class="tarjeta-pie">
            <span class="estrellas">★★★★☆</span>
            <span class="resenas">(22 reseñas)</span>
          </div>
        </div>
      </div>

      <div class="col-6 col-sm-4 col-md-3">
        <div class="tarjeta-libro">
          <div class="img-contenedor">
            <img src="./assets/img/Jardin_de_las_Mariposas.png" alt="El Jardín de las Mariposas" class="portada-img" />
          </div>
          <div class="tarjeta-pie">
            <span class="estrellas">★★★★☆</span>
            <span class="resenas">(42 reseñas)</span>
          </div>
        </div>
      </div>

    </div>
  </div>

</div>

</body>
</html>