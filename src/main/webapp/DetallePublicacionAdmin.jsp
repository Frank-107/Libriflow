<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Detalle de Publicación Admin - LibriFlow</title>

  <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/DetalleLibroAdmin.css" />
</head>
<body class="p-3 p-md-4">

<div class="container-fluid max-width-xl mx-auto h-100 d-flex flex-column pb-2">

  <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5 flex-shrink-0">
    <div class="d-flex align-items-center gap-2 gap-md-3">
      <button class="btn text-white d-md-none p-0 border-0 me-1" type="button" data-bs-toggle="collapse" data-bs-target="#sidebarMenu">
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
        <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm" style="width: 48px; height: 48px; cursor: pointer;" data-bs-toggle="dropdown">
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

  <div class="row gx-4 gy-4 gy-md-0 flex-grow-1 overflow-hidden">
    <aside class="col-12 col-md-4 col-lg-3 h-100">
      <div class="collapse d-md-block h-100" id="sidebarMenu">
        <div class="bg-lf-dark p-4 rounded-lf-sidebar d-flex flex-column gap-3 shadow-sm mb-3 mb-md-0 h-100">
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

    <main class="col-12 col-md-8 col-lg-9 h-100 overflow-hidden d-flex flex-column position-relative">

      <c:if test="${not empty param.error}">
        <div class="libri-toast libri-toast-error">
          <i class="bi bi-exclamation-circle-fill fs-5"></i>
          <span>No se pudo dar de baja la publicación.</span>
        </div>
      </c:if>

      <div class="row gx-4 gy-4 gy-lg-0 detalle-admin-content flex-grow-1 h-100">

        <div class="col-12 col-lg-6 detalle-admin-left h-100">

          <div class="detalle-libro-superior">
            <div class="d-flex flex-column align-items-center">
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
                    <c:when test="${publicacion.esVenta == 0}">Solo renta</c:when>
                    <c:otherwise>Precio base: $<c:out value="${publicacion.precio}" /></c:otherwise>
                  </c:choose>
                </span>
              </div>
            </div>
          </div>

          <c:if test="${not empty esAdminPub}">
            <div class="resenas-admin-section">
              <c:set var="sumaCalificaciones" value="0"/>
              <c:set var="totalResenas" value="${fn:length(resenas)}"/>

              <c:forEach var="r" items="${resenas}">
                <c:set var="sumaCalificaciones" value="${sumaCalificaciones + r.calificacion}"/>
              </c:forEach>

              <h4 class="fw-bold mb-3 d-flex align-items-center gap-2">
                <i class="bi bi-star-fill text-warning"></i> Reseñas
                <c:if test="${totalResenas > 0}">
                  <span class="fs-6 fw-normal text-muted">
                    (<fmt:formatNumber value="${sumaCalificaciones / totalResenas}" maxFractionDigits="1"/>/5 ·
                    ${totalResenas} ${totalResenas == 1 ? 'reseña' : 'reseñas'})
                  </span>
                </c:if>
              </h4>

              <c:choose>
                <c:when test="${empty resenas}">
                  <div class="alert alert-light border rounded-4 text-muted mb-0">
                    <i class="bi bi-info-circle me-2"></i> No hay reseñas para esta publicación.
                  </div>
                </c:when>
                <c:otherwise>
                  <div class="resenas-admin-scroll pe-2">
                    <c:forEach var="r" items="${resenas}">
                      <div class="card p-3 mb-3 shadow-sm rounded-4 border-0 bg-light resena-admin-card">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                          <div class="d-flex align-items-center gap-3">
                            <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm resena-admin-avatar">
                              <i class="bi bi-person-fill fs-4 text-dark"></i>
                            </div>
                            <strong class="text-dark">
                                ${not empty r.nombreUsuario ? r.nombreUsuario : 'Usuario'}
                            </strong>
                          </div>
                          <div class="resena-admin-estrellas">
                            <c:forEach begin="1" end="${r.calificacion}">
                              <i class="bi bi-star-fill text-warning"></i>
                            </c:forEach>
                            <c:forEach begin="${r.calificacion + 1}" end="5">
                              <i class="bi bi-star text-muted"></i>
                            </c:forEach>
                          </div>
                        </div>
                        <p class="mb-0 text-secondary"><c:out value="${r.comentario}"/></p>
                      </div>
                    </c:forEach>
                  </div>
                </c:otherwise>
              </c:choose>
            </div>
          </c:if>

        </div>

        <div class="col-12 col-lg-6 d-flex flex-column gap-3 detalle-admin-right h-100">

          <div class="pill-info-lf shadow-sm flex-shrink-0">
            <span class="fw-bold">Título:</span> <c:out value="${publicacion.titulo}" />
          </div>
          <div class="pill-info-lf shadow-sm flex-shrink-0">
            <span class="fw-bold">Autor:</span> <c:out value="${publicacion.autor}" />
          </div>
          <div class="pill-info-lf shadow-sm flex-shrink-0">
            <span class="fw-bold">Editorial:</span> <c:out value="${publicacion.editorial}" />
          </div>
          <div class="pill-info-lf shadow-sm flex-shrink-0">
            <span class="fw-bold">Género:</span> <c:out value="${publicacion.genero}" />
          </div>

          <div class="box-sinopsis-lf shadow-sm pe-2">
            <h5 class="fw-bold mb-3 flex-shrink-0">Sinopsis:</h5>
            <p class="mb-0 text-muted lh-base"><c:out value="${publicacion.sinopsis}" /></p>
          </div>

          <div class="pt-4 mt-auto pb-4 flex-shrink-0">
            <c:choose>
              <c:when test="${not empty esAdminPub}">
                <c:set var="actionUrl" value="detalle-publicacion-admin"/>
                <c:set var="pubId" value="${publicacion.idPublicacionLf}"/>
              </c:when>
              <c:otherwise>
                <c:set var="actionUrl" value="detalle-publicacion-us-admin"/>
                <c:set var="pubId" value="${publicacion.idPublicacion}"/>
              </c:otherwise>
            </c:choose>

            <form action="${actionUrl}" method="post" class="w-100" id="formBajaPublicacion">
              <input type="hidden" name="idPublicacion" value="${pubId}">
              <button type="button" class="btn btn-delete-lf w-100 d-flex align-items-center justify-content-center gap-2 py-3" data-bs-toggle="modal" data-bs-target="#modalBajaPublicacion">
                <i class="bi bi-trash fs-5"></i> Dar de baja publicación
              </button>
            </form>
          </div>

        </div>
      </div>
    </main>
  </div>
</div>

<div class="modal fade" id="modalBajaPublicacion" tabindex="-1" aria-labelledby="modalBajaLabel" aria-hidden="true" data-bs-backdrop="static">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content shadow-lg" style="border-radius: 40px; background-color: #e3ded7; color: #4A4641; border: none;">

      <div class="modal-header border-0 pb-0 position-relative">
        <h5 class="modal-title fw-bold w-100 text-center" id="modalBajaLabel" style="color: #4A4641;">
          <i class="bi bi-exclamation-triangle me-2" style="color: #d93025;"></i> Confirmar acción
        </h5>
        <button type="button" class="btn-close position-absolute end-0 me-4 mt-2" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <div class="modal-body py-4 text-center">
        <p class="mb-0 fs-6 fw-medium" style="color: #6e6762;">¿Estás seguro de que deseas dar de baja esta publicación?</p>
      </div>

      <div class="modal-footer border-0 pt-0 d-flex justify-content-center gap-3">
        <button type="button" class="btn bg-lf-capsule btn-lf-pill" data-bs-dismiss="modal" style="font-weight: 600; padding: 12px 30px; border-radius: 25px;">
          Cancelar
        </button>

        <button type="button" class="btn btn-delete-lf" id="btnConfirmarBaja" style="font-weight: 600; padding: 12px 30px; border-radius: 25px; margin: 0;">
          Sí, dar de baja
        </button>
      </div>

    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/BajaAdmin.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/Notificacion.js"></script>
</body>
</html>