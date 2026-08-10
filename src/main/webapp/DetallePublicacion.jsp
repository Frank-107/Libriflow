<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/DetalleLibro.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
</head>
<body class="p-3 p-md-4 detalle-body">

<div class="container-fluid max-width-xl mx-auto detalle-layout">
    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5 detalle-header">
        <div class="d-flex align-items-center gap-2 gap-md-3">
            <button class="btn text-white d-md-none p-0 border-0 me-1" type="button" data-bs-toggle="collapse" data-bs-target="#sidebarMenu" aria-expanded="false" aria-controls="sidebarMenu">
                <i class="bi bi-list" style="font-size: 2rem;"></i>
            </button>

            <a href="inicio" class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>
            <span class="fw-bold fs-4 tracking-wide">Detalles</span>
        </div>

        <div class="d-flex align-items-center gap-3">
            <div class="text-end d-none d-md-block">
                <div class="fw-bold mb-0" style="font-size: 0.95rem;">${usuario.nombre}</div>
                <small class="text-white-50" style="font-size: 0.8rem;">${usuario.correo}</small>
            </div>

            <div class="dropdown">
                <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm" style="width: 48px; height: 48px; cursor: pointer;" data-bs-toggle="dropdown" aria-expanded="false">
                    <i class="bi bi-person-fill fs-4 text-dark"></i>
                </div>

                <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 dropdown-menu-lf">
                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-item" href="ActualizarPerfil.jsp">
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

    <div class="row g-4 detalle-content-row">

        <!-- Sidebar -->
        <aside class="col-12 col-md-4 col-lg-3 detalle-sidebar">
            <div class="collapse d-md-block" id="sidebarMenu">
                <div class="bg-lf-dark p-4 rounded-lf-sidebar d-flex flex-column gap-3 shadow-sm mb-3 mb-md-0">
                    <a href="inicio" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-house me-3 fs-5"></i> Inicio
                    </a>
                    <a href="carrito" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-cart3 me-3 fs-5"></i> Carrito
                    </a>
                    <a href="compras" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-bag-check me-3 fs-5"></i> Compras
                    </a>
                    <a href="publicar-libro-usuario" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-pencil-square me-3 fs-5"></i> Publicar
                    </a>
                    <a href="mis-publicaciones" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-grid-3x3-gap me-3 fs-5"></i> Mis publicaciones
                    </a>
                    <a href="mis-rentas" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-journal-bookmark me-3 fs-5"></i> Mis rentas
                    </a>
                    <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-globe me-3 fs-5"></i> Nuestras redes
                    </a>
                </div>
            </div>
        </aside>

        <!-- Contenido Principal -->
        <main class="col-12 col-md-8 col-lg-9 detalle-main">
            <div class="row g-4 h-100 detalle-inner-row">

                <!-- Columna izquierda: Galería + Reseñas (con scroll interno) -->
                <div class="col-12 col-lg-6 detalle-left-scroll">

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
                        <c:when test="${publicacion.esVenta == 0}">
                            Solo renta
                        </c:when>
                        <c:otherwise>
                            Precio base: $${publicacion.precio}
                        </c:otherwise>
                    </c:choose>
                    </span>
                        </div>
                    </div>

                    <div class="mt-4">
                        <c:if test="${not empty esAdminPub}">

                            <c:set var="sumaCalificaciones" value="0"/>
                            <c:forEach var="r" items="${resenas}">
                                <c:set var="sumaCalificaciones" value="${sumaCalificaciones + r.calificacion}"/>
                            </c:forEach>
                            <c:set var="totalResenas" value="${fn:length(resenas)}"/>

                            <h4 class="fw-bold mb-4 d-flex align-items-center gap-2">
                                <i class="bi bi-star-fill text-warning"></i> Reseñas
                                <c:if test="${totalResenas > 0}">
        <span class="fs-6 fw-normal text-muted">
            (<fmt:formatNumber value="${sumaCalificaciones / totalResenas}" maxFractionDigits="1"/>/5 · ${totalResenas} ${totalResenas == 1 ? 'reseña' : 'reseñas'})
        </span>
                                </c:if>
                            </h4>

                            <%-- ALERTAS ESTILO LOGIN (LIBRI-TOAST) --%>
                            <c:if test="${not empty sessionScope.mensaje}">
                                <div id="successToast" class="libri-toast libri-toast-success">
                                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                                        <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                                        <polyline points="22 4 12 14.01 9 11.01"/>
                                    </svg>
                                    <span><c:out value="${sessionScope.mensaje}" escapeXml="true" /></span>
                                </div>
                                <c:remove var="mensaje" scope="session" />
                            </c:if>

                            <c:if test="${not empty sessionScope.error}">
                                <div id="errorToast" class="libri-toast libri-toast-error">
                                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                                        <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"/>
                                        <line x1="12" y1="9" x2="12" y2="13"/>
                                        <line x1="12" y1="17" x2="12.01" y2="17"/>
                                    </svg>
                                    <span><c:out value="${sessionScope.error}" escapeXml="true" /></span>
                                </div>
                                <c:remove var="error" scope="session" />
                            </c:if>

                            <!-- FORMULARIO DE RESEÑA -->
                            <div class="card shadow-sm p-4 mb-4 rounded-4">
                                <form action="resena" method="post">

                                    <input type="hidden" name="idPublicacion" value="${publicacion.idPublicacionLf}">

                                    <div class="mb-3">
                                        <label for="comentario" class="form-label fw-bold">Deja tu opinión:</label>
                                        <textarea id="comentario" name="comentario" class="form-control" rows="3" placeholder="¿Qué te pareció este libro?" required></textarea>
                                    </div>

                                    <div class="mb-3">
                                        <label for="calificacion" class="form-label fw-bold">Calificación:</label>
                                        <select id="calificacion" name="calificacion" class="form-select">
                                            <option value="5">⭐⭐⭐⭐⭐ (5/5)</option>
                                            <option value="4">⭐⭐⭐⭐ (4/5)</option>
                                            <option value="3">⭐⭐⭐ (3/5)</option>
                                            <option value="2">⭐⭐ (2/5)</option>
                                            <option value="1">⭐ (1/5)</option>
                                        </select>
                                    </div>

                                    <button type="submit" class="btn btn-action-lf shadow-sm btn-submit px-4">Enviar reseña</button>

                                </form>
                            </div>

                            <!-- LISTA DE RESEÑAS -->
                            <c:choose>
                                <c:when test="${empty resenas}">
                                    <div class="alert alert-light border rounded-4 text-muted">
                                        No hay reseñas aún. ¡Sé el primero en opinar sobre este libro!
                                    </div>
                                </c:when>

                                <c:otherwise>
                                    <div class="pe-2">
                                        <c:forEach var="r" items="${resenas}">
                                            <div class="card p-3 mb-3 shadow-sm rounded-4 border-0 bg-light">
                                                <div class="d-flex justify-content-between align-items-center mb-1">
                                                    <div class="d-flex align-items-center gap-3">
                                                        <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm" style="width: 45px; height: 45px;">
                                                            <i class="bi bi-person-fill fs-4 text-dark"></i>
                                                        </div>
                                                        <strong class="text-dark">${not empty r.nombreUsuario ? r.nombreUsuario : 'Usuario'}</strong>
                                                    </div>
                                                    <div>
                                                        <c:forEach begin="1" end="${r.calificacion}">
                                                            <i class="bi bi-star-fill text-warning"></i>
                                                        </c:forEach>
                                                        <c:forEach begin="${r.calificacion + 1}" end="5">
                                                            <i class="bi bi-star text-muted"></i>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                                <p class="mb-0 text-secondary">${r.comentario}</p>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:otherwise>
                            </c:choose>

                        </c:if>
                    </div>

                </div>
                <!-- fin columna izquierda -->

                <!-- Columna derecha: Info de la publicación (fija) -->
                <div class="col-12 col-lg-6 d-flex flex-column gap-3 detalle-right-fixed">
                    <div class="pill-info-lf shadow-sm">
                        <span class="fw-bold">Título:</span> ${publicacion.titulo}
                    </div>

                    <div class="pill-info-lf shadow-sm">
                        <span class="fw-bold">Autor:</span> ${publicacion.autor}
                    </div>

                    <div class="pill-info-lf shadow-sm">
                        <span class="fw-bold">Editorial:</span> ${publicacion.editorial}
                    </div>

                    <div class="pill-info-lf shadow-sm">
                        <span class="fw-bold">Género:</span> ${publicacion.genero}
                    </div>

                    <div class="box-sinopsis-lf shadow-sm flex-grow-1">
                        <h5 class="fw-bold mb-3">Sinopsis:</h5>
                        <p class="mb-0 text-muted lh-base">${publicacion.sinopsis}</p>
                    </div>

                    <div class="mt-2">
                        <c:choose>
                            <c:when test="${not empty esAdminPub}">
                                <div class="d-flex flex-column gap-2 w-100">
                                    <form action="detalle-publicacion" method="POST" class="w-100">
                                        <input type="hidden" name="idPublicacion" value="${publicacion.idPublicacionLf}">
                                        <input type="hidden" name="tipoOperacion" value="venta">
                                        <input type="hidden" name="precioCalculado" value="${publicacion.precio}">
                                        <button type="submit" class="btn btn-action-lf w-100 py-3 rounded-pill fw-bold shadow-sm"
                                                <c:if test="${publicacion.esVenta == 0}">disabled</c:if>>
                                            <i class="bi bi-cart-plus me-2"></i> Agregar al carrito
                                        </button>
                                    </form>

                                    <button type="button" class="btn btn-secondary-lf w-100 py-3 rounded-pill fw-bold shadow-sm"
                                            data-bs-toggle="modal" data-bs-target="#modalRenta"
                                            <c:if test="${publicacion.esRenta == 0}">disabled</c:if>>
                                        <i class="bi bi-calendar-check me-2"></i> Selecciona la fecha para tu renta
                                    </button>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <form action="detalle-publicacion" method="POST" class="w-100">
                                    <input type="hidden" name="idPublicacion" value="${publicacion.idPublicacion}">
                                    <input type="hidden" name="tipoOperacion" value="venta">
                                    <input type="hidden" name="precioCalculado" value="${publicacion.precio}">
                                    <button type="submit" class="btn btn-action-lf w-100 py-3 rounded-pill fw-bold shadow-sm">
                                        <i class="bi bi-cart-plus me-2"></i> Agregar al carrito
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

            </div>
        </main>

    </div>
</div>

<c:if test="${not empty esAdminPub}">
    <div class="modal fade" id="modalRenta" tabindex="-1" aria-labelledby="modalRentaLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content modal-content-lf shadow-lg">

                <div class="modal-header modal-header-lf">
                    <h5 class="modal-title fw-bold" id="modalRentaLabel">
                        <i class="bi bi-calendar-event me-2"></i>Seleccionar Período de Renta
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <form action="detalle-publicacion-superad" method="POST">
                    <div class="modal-body p-4">
                        <input type="hidden" name="idPublicacion" value="${publicacion.idPublicacionLf}">
                        <input type="hidden" name="tipoOperacion" value="renta">
                        <input type="hidden" name="precioCalculado" id="precioRentaInput" value="0.0">

                        <div class="mb-3">
                            <label for="fechaInicio" class="form-label fw-bold text-dark">
                                <i class="bi bi-calendar-check me-1"></i>Fecha de inicio:
                            </label>
                            <input type="date" class="form-control input-date-lf" id="fechaInicio" name="fechaInicio" required>
                        </div>

                        <div class="mb-4">
                            <label for="fechaFin" class="form-label fw-bold text-dark">
                                <i class="bi bi-calendar-x me-1"></i>Fecha de fin:
                            </label>
                            <input type="date" class="form-control input-date-lf" id="fechaFin" name="fechaFin" required>
                        </div>

                        <div class="box-tarifa-lf text-center shadow-sm">
                            <small class="d-block text-muted mb-1">
                                Tarifa: $5/día (días 1-7) | $3/día (día 8 en adelante)
                            </small>
                            <span class="fs-4 fw-bold text-dark d-block">
                                Monto total: $<span id="montoMostrado">0.0</span>
                            </span>
                        </div>
                    </div>

                    <div class="modal-footer modal-footer-lf d-flex justify-content-end gap-2">
                        <button type="button" class="btn btn-secondary-lf rounded-pill px-4" data-bs-dismiss="modal">
                            Cancelar
                        </button>
                        <button type="submit" class="btn btn-action-lf rounded-pill px-4" id="btnConfirmarRenta" disabled>
                            <i class="bi bi-cart-plus me-1"></i> Agregar al Carrito
                        </button>
                    </div>
                </form>

            </div>
        </div>
    </div>
</c:if>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/Renta.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/Notificacion.js"></script>
</body>
</html>