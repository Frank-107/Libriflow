<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Detalle de Publicación - LibriFlow</title>

  <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/DetalleLibroAdmin.css" />
</head>
<body class="p-3 p-md-4">

<div class="container-fluid max-width-xl mx-auto">

  <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5">
    <div class="d-flex align-items-center gap-2 gap-md-3">
      <button class="btn text-white d-md-none p-0 border-0 me-1" type="button" data-bs-toggle="collapse" data-bs-target="#sidebarMenu" aria-expanded="false" aria-controls="sidebarMenu">
        <i class="bi bi-list" style="font-size: 2rem;"></i>
      </button>

      <a href="inicio-admin" class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
        <i class="bi bi-arrow-left"></i>
      </a>
      <span class="fw-bold fs-4 tracking-wide">Detalles</span>
    </div>

    <div class="d-flex align-items-center gap-3">
      <div class="text-end d-none d-md-block">
        <div class="fw-bold mb-0" style="font-size: 0.95rem;"><c:out value="${sessionScope.usuario.nombre}" /></div>
        <small class="text-white-50" style="font-size: 0.8rem;"><c:out value="${sessionScope.usuario.correo}" /></small>
      </div>

      <div class="dropdown">
        <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm" style="width: 48px; height: 48px; cursor: pointer;" data-bs-toggle="dropdown" aria-expanded="false">
          <i class="bi bi-person-fill fs-4 text-dark"></i>
        </div>

        <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 dropdown-menu-lf">
          <li>
            <a class="dropdown-item py-2 dropdown-lf-item" href="ActualizarPerfilAdmin.jsp">
              <i class="bi bi-person me-2"></i>Ver perfil
            </a>
          </li>
          <li>
            <a class="dropdown-item py-2 dropdown-lf-logout" href="cerrar-sesion">
              <i class="bi bi-box-arrow-right me-2"></i>Cerrar sesión
            </a>
          </li>
        </ul>
      </div>
    </div>
  </header>

  <div class="row g-4">

    <aside class="col-12 col-md-4 col-lg-3">
      <div class="collapse d-md-block" id="sidebarMenu">
        <div class="bg-lf-dark p-4 rounded-lf-sidebar d-flex flex-column gap-3 shadow-sm mb-3 mb-md-0">
          <a href="inicio-admin" class="btn bg-lf-capsule sidebar-active btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
            <i class="bi bi-house me-3 fs-5"></i> Inicio
          </a>
          <a href="solicitud-publicacion-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
            <i class="bi bi-file-earmark-text me-3 fs-5"></i> Solicitud de publicación
          </a>
          <a href="publicar-libro-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
            <i class="bi bi-file-earmark-text me-3 fs-5"></i> Publicar
          </a>
          <a href="mis-rentas-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
            <i class="bi bi-book-half me-3 fs-5"></i> Rentas activas
          </a>
          <a href="usuarios-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
            <i class="bi bi-people-fill me-3 fs-5"></i> Usuarios
          </a>
          <a href="ingresos-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
            <i class="bi bi-cash-stack me-3 fs-5"></i> Ingresos
          </a>
        </div>
      </div>
    </aside>

    <main class="col-12 col-md-8 col-lg-9">
      <div class="row g-4">

        <div class="col-12 col-lg-6 d-flex flex-column align-items-center">
          <div class="portada-principal-container mb-3 shadow-sm">
            <img src="${publicacion.imagenPrincipal}" alt="Portada de ${publicacion.titulo}" class="img-fluid rounded-4">
          </div>

          <div class="d-flex gap-3 mb-3 w-100 justify-content-center" style="max-width: 360px;">
            <div class="miniatura-container shadow-sm">
              <img src="${publicacion.imagenReverso}" alt="Imagen reverso" class="img-fluid rounded-3">
            </div>
            <div class="miniatura-container shadow-sm">
              <img src="${publicacion.imagenInterior}" alt="Imagen interior" class="img-fluid rounded-3">
            </div>
          </div>

          <div class="d-flex align-items-center gap-2 mt-2 fw-bold fs-5 text-dark">
                       <span class="badge bg-secondary rounded-pill px-3 py-2">
                        <c:choose>
                          <c:when test="${publicacion.esVenta == 0}">
                            Solo renta
                          </c:when>
                          <c:otherwise>
                            Precio base: $<c:out value="${publicacion.precio}" />
                          </c:otherwise>
                        </c:choose>
                        </span>
          </div>
        </div>

        <div class="col-12 col-lg-6 d-flex flex-column gap-3">
          <div class="pill-info-lf shadow-sm">
            <span class="fw-bold">Título:</span> <c:out value="${publicacion.titulo}" />
          </div>

          <div class="pill-info-lf shadow-sm">
            <span class="fw-bold">Autor:</span> <c:out value="${publicacion.autor}" />
          </div>

          <div class="pill-info-lf shadow-sm">
            <span class="fw-bold">Editorial:</span> <c:out value="${publicacion.editorial}" />
          </div>

          <div class="pill-info-lf shadow-sm">
            <span class="fw-bold">Género:</span> <c:out value="${publicacion.genero}" />
          </div>

          <div class="box-sinopsis-lf shadow-sm">
            <h5 class="fw-bold mb-3">Sinopsis:</h5>
            <p class="mb-0 text-muted lh-base"><c:out value="${publicacion.sinopsis}" /></p>
          </div>

        </div>

      </div>
    </main>

  </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
</body>
</html>