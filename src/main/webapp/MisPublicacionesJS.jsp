<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
 * Vista de interfaz de usuario que despliega el historial general de publicaciones realizadas por el usuario en sesión.
 * Permite visualizar el catálogo de libros ofertados, consultar sus estados operativos (PENDIENTE, ACTIVO, RECHAZADO, etc.),
 * aplicar filtros de ordenamiento cronológico por fecha y gestionar acciones como la cancelación de publicaciones pendientes.
 *
 * @author Francisco
 * @since 25/08/2026
--%>
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Mis publicaciones - LibriFlow</title>
  <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/MisPublicaciones.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/LibriFlow.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/MisPublicacionesVista.css?v=5"/>
</head>

<body class="p-3 p-md-4">
<div class="container-fluid max-width-xl mx-auto">

  <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5">
    <div class="d-flex align-items-center">
      <button class="btn text-white d-md-none p-0 border-0 me-1" type="button" data-bs-toggle="collapse" data-bs-target="#sidebarMenu" aria-expanded="false" aria-controls="sidebarMenu">
        <i class="bi bi-list" style="font-size:2rem;"></i>
      </button>

      <a href="inicio-js" class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
        <i class="bi bi-arrow-left"></i>
      </a>

      <span class="fw-bold fs-4 tracking-wide">Mis publicaciones</span>
    </div>

    <div class="d-flex align-items-center gap-3">
      <div class="text-end d-none d-md-block">
        <div class="fw-bold">
          <c:out value="${sessionScope.usuario.nombre}"/>
        </div>
        <small class="text-white-50">
          <c:out value="${sessionScope.usuario.correo}"/>
        </small>
      </div>

      <div class="dropdown">
        <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm"
             style="width:45px;height:45px;cursor:pointer;"
             data-bs-toggle="dropdown"
             aria-expanded="false">
          <i class="bi bi-person-fill fs-4 text-dark"></i>
        </div>

        <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 dropdown-menu-lf">
          <li>
            <a class="dropdown-item py-2 dropdown-lf-item" href="actualizar-perfil-js">
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

          <a href="inicio-js" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
            <i class="bi bi-house me-3 fs-5"></i>Inicio
          </a>

          <a href="carrito" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
            <i class="bi bi-cart3 me-3 fs-5"></i>Carrito
          </a>

          <a href="mis-compras" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
            <i class="bi bi-bag-check me-3 fs-5"></i>Compras
          </a>

          <a href="publicar-libro-usuario" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
            <i class="bi bi-pencil-square me-3 fs-5"></i>Publicar
          </a>

          <a href="mis-publicaciones-js" class="btn bg-lf-capsule btn-lf-pill sidebar-active w-100 py-2.5 text-start d-flex align-items-center px-4">
            <i class="bi bi-grid-3x3-gap me-3 fs-5"></i>Mis publicaciones
          </a>

          <a href="mis-rentas" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
            <i class="bi bi-journal-bookmark me-3 fs-5"></i>Mis rentas
          </a>

          <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
            <i class="bi bi-globe me-3 fs-5"></i>Nuestras redes
          </a>

        </div>
      </div>
    </aside>

    <main class="col">
      <section class="mis-publicaciones-container position-relative pt-2">

        <div id="contenedor-notificaciones"></div>

        <div class="mis-publicaciones-header d-flex justify-content-between align-items-center gap-3">
          <h2>
            Publicaciones totales
            (<span id="total-publicaciones"><c:out value="${publicaciones.size()}"/></span>)
          </h2>

          <div class="form-orden">
            <span class="orden-label">Ordenar por:</span>

            <div class="dropdown">
              <button class="btn orden-select dropdown-toggle d-flex justify-content-between align-items-center" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                ${ordenActual == 'antiguas' ? 'Más antiguas' : 'Más recientes'}
              </button>
              <ul class="dropdown-menu dropdown-menu-end dropdown-menu-orden border-0">
                <li>
                  <a class="dropdown-item ${ordenActual != 'antiguas' ? 'active-orden' : ''}" href="${pageContext.request.contextPath}/mis-publicaciones-js?orden=recientes">
                    Más recientes
                  </a>
                </li>
                <li>
                  <a class="dropdown-item ${ordenActual == 'antiguas' ? 'active-orden' : ''}" href="${pageContext.request.contextPath}/mis-publicaciones-js?orden=antiguas">
                    Más antiguas
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>

        <c:choose>
          <c:when test="${empty publicaciones}">
            <div class="sin-publicaciones" id="mensaje-sin-publicaciones">
              <i class="bi bi-journal-x"></i>
              <h4>No tienes publicaciones.</h4>
              <p>Cuando publiques un libro aparecerá aquí.</p>
            </div>
          </c:when>

          <c:otherwise>
            <div class="publicaciones-lista" id="lista-publicaciones">

              <c:forEach var="publicacion" items="${publicaciones}">
                <div class="publicacion-card">
                  <div class="publicacion-contenido">

                    <div class="publicacion-portada">
                      <img src="${publicacion.imagenPrincipal}" alt="Portada de ${publicacion.titulo}">
                    </div>

                    <div class="publicacion-info">
                      <h4><c:out value="${publicacion.titulo}"/></h4>
                      <p><c:out value="${publicacion.autor}"/></p>
                      <small><c:out value="${publicacion.genero}"/></small>
                    </div>

                    <div class="publicacion-acciones">

                       <span class="estado ${publicacion.estado}">
                         <c:out value="${publicacion.estado}"/>
                        </span>
                      <div class="publicacion-precio">
                        $<c:out value="${publicacion.precio}"/>
                      </div>

                      <div class="botones-accion">

                        <a href="${pageContext.request.contextPath}/detalle-publicacion?idPublicacion=${publicacion.idPublicacion}"
                           class="btn-ver-detalles">
                          <i class="bi bi-eye"></i>
                          Ver detalles
                        </a>

                        <c:if test="${publicacion.estado == 'PENDIENTE'}">
                          <button type="button"
                                  onclick="confirmarCancelacion(${publicacion.idPublicacion}, this)"
                                  class="btn-cancelar-publicacion">
                            <i class="bi bi-trash"></i>
                            Cancelar publicación
                          </button>
                        </c:if>

                      </div>

                    </div>

                  </div>
                </div>
              </c:forEach>

            </div>
          </c:otherwise>
        </c:choose>

      </section>
    </main>
  </div>
</div>

<div class="modal fade" id="modalConfirmarCancelacion" tabindex="-1" aria-labelledby="modalConfirmarCancelacionLabel" aria-hidden="true" data-bs-backdrop="static">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content shadow-lg modal-cancelar-content">
      <div class="modal-header border-0 pb-0 position-relative">
        <h5 class="modal-title fw-bold w-100 text-center" id="modalConfirmarCancelacionLabel" style="color: #4A4641;">Cancelar publicación</h5>
        <button type="button" class="btn-close position-absolute end-0 me-4 mt-2" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body py-4 text-center">
        <p class="mb-0 fs-6 fw-medium" style="color: #6e6762;">¿Estás seguro de que deseas cancelar esta publicación? Esta acción no se puede deshacer.</p>
      </div>
      <div class="modal-footer border-0 pt-0 d-flex justify-content-center gap-3">
        <button type="button" class="btn btn-modal-confirmar" id="btnConfirmarCancelacion">
          Sí, cancelar
        </button>
        <button type="button" class="btn bg-lf-capsule btn-lf-pill btn-modal-cerrar" data-bs-dismiss="modal">
          Cerrar
        </button>
      </div>
    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/MisPublicacionesJS.js"></script>
</body>
</html>