<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>LibriFlow</title>
  <link rel="stylesheet" href="assets/css/bootstrap.css" />
  <link rel="stylesheet" href="assets/css/styles.css" />
</head>
<body>

<nav class="barra-nav">
  <a href="#">Nuestras Redes</a>
</nav>

<div class="pagina">

  <div class="hero">
    <div class="logo">
      <span class="icono">📖</span>
      <div class="nombre"><b>LIBRI</b>FLOW</div>
      <small>TU BIBLIOTECA DIGITAL</small>
    </div>

    <h1>Miles de libros a un solo click</h1>

    <div class="botones">
      <a href="login.jsp" class="btn-oscuro">Iniciar Sesión</a>
      <a href="registro.jsp" class="btn-claro">Crear Cuenta</a>
    </div>
  </div>

  <div class="catalogo">
    <div class="row g-4">

      <div class="col-3">
        <div class="tarjeta">
          <img src="img/conde-montecristo.jpg" alt="El Conde de Montecristo" />
          <div class="pie">
            <span class="estrellas">★★★★☆</span>
            <span class="resenas">(25 reseñas)</span>
          </div>
        </div>
      </div>

      <div class="col-3">
        <div class="tarjeta">
          <img src="img/twilight.jpg" alt="Twilight" />
          <div class="pie">
            <span class="estrellas">★★★☆☆</span>
            <span class="resenas">(33 reseñas)</span>
          </div>
        </div>
      </div>

      <div class="col-3">
        <div class="tarjeta">
          <img src="img/it.jpg" alt="IT - Stephen King" />
          <div class="pie">
            <span class="estrellas">★★★☆☆</span>
            <span class="resenas">(22 reseñas)</span>
          </div>
        </div>
      </div>

      <div class="col-3">
        <div class="tarjeta">
          <img src="img/jardin-mariposas.jpg" alt="El Jardín de las Mariposas" />
          <div class="pie">
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