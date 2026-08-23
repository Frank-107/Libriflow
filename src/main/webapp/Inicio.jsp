<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inicio - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Inicio.css"/>
</head>
<body class="p-3 p-md-4">

<div class="container-fluid max-width-xl mx-auto">

    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5">
        <div class="d-flex align-items-center">
            <button class="btn text-white d-md-none me-2 p-0 border-0" type="button" data-bs-toggle="collapse" data-bs-target="#sidebarMenu" aria-expanded="false" aria-controls="sidebarMenu">
                <i class="bi bi-list" style="font-size: 2rem;"></i>
            </button>

            <a href="inicio" class="d-flex align-items-center text-decoration-none">
                <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" alt="Logo LibriFlow" style="height: 40px; width: auto;" class="me-2">
                <div class="text-start d-none d-sm-block">
                    <div class="fw-bold tracking-widest fs-5 text-white">LIBRIFLOW</div>
                    <small style="font-size: 0.65rem; letter-spacing: 1px; color: #CBC2B9; display: block;">TU BIBLIOTECA DIGITAL</small>
                </div>
            </a>
        </div>

        <div class="d-flex align-items-center gap-3">
            <div class="text-end d-none d-md-block">
                <div class="fw-bold mb-0" style="font-size: 0.95rem;"><c:out value="${sessionScope.usuario.nombre}" /></div>
                <small class="text-white-50" style="font-size: 0.8rem;"><c:out value="${sessionScope.usuario.correo}" /></small>
            </div>

            <div class="dropdown">
                <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm" style="width: 45px; height: 45px; cursor: pointer;" data-bs-toggle="dropdown" aria-expanded="false">
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
                    <a href="carrito" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-cart3 me-3 fs-5"></i> Carrito
                    </a>
                    <a href="mis-compras" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-bag-check me-3 fs-5"></i> Compras
                    </a>
                    <a href="publicar-libro-usuario" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-pencil-square me-3 fs-5"></i> Publicar
                    </a>
                    <a href="mis-publicaciones-js" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-grid-3x3-gap me-3 fs-5"></i> Mis publicaciones
                    </a>
                    <a href="mis-rentas" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-journal-bookmark me-3 fs-5"></i> Mis rentas
                    </a>
                    <a href="https://www.instagram.com/libriflow.oficial?igsh=MW9qbmNld2M2ZXNyeA==" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-globe me-3 fs-5"></i> Nuestras redes
                    </a>
                </div>
            </div>
        </aside>

        <main class="col-12 col-md-8 col-lg-9 catalogo-scroll">

            <c:if test="${not empty error}">
                <div id="alertaError" class="alert alert-danger alert-dismissible fade show mb-4 rounded-3 shadow-sm libri-toast" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> <c:out value="${error}" />
                    <button type="button" class="btn-close" aria-label="Close" onclick="document.getElementById('alertaError').remove();"></button>
                </div>
            </c:if>

            <c:if test="${not empty mensaje}">
                <div class="alert alert-info alert-dismissible fade show mb-4 rounded-3 shadow-sm" role="alert">
                    <i class="bi bi-info-circle-fill me-2"></i> <c:out value="${mensaje}" />
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <div class="d-flex gap-3 mb-3 align-items-center">
                <form action="inicio" method="GET" class="position-relative flex-grow-1 m-0">
                    <i class="bi bi-search search-icon-inside"></i>
                    <c:if test="${not empty paramGenero && paramGenero != 'TODOS'}">
                        <input type="hidden" name="genero" value="<c:out value='${paramGenero}' />">
                    </c:if>
                    <input type="text"
                           name="q"
                           value="<c:out value='${paramBusqueda}' />"
                           class="form-control search-bar-lf shadow-sm"
                           placeholder="Buscar libros, autores..."
                           maxlength="100">
                </form>

                <div class="dropdown">
                    <button class="btn bg-white rounded-circle d-flex align-items-center justify-content-center shadow-sm border"
                            type="button"
                            id="filtroGenerosDropdown"
                            data-bs-toggle="dropdown"
                            aria-expanded="false"
                            style="width: 46px; height: 46px; transition: all 0.2s; color: #4A4641;">
                        <i class="bi bi-sliders fs-5"></i>
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 p-2 dropdown-menu-filter" aria-labelledby="filtroGenerosDropdown">
                        <li class="dropdown-header fw-bold text-secondary border-bottom pb-2 mb-1" style="font-size: 0.75rem; letter-spacing: 1px;">FILTRAR POR GÉNERO</li>

                        <c:set var="listaGeneros" value="TODOS,Novela,Fantasía,Ciencia ficción,Terror,Romance,Misterio,Suspenso,Drama,Aventura,Historia,Biografía,Autobiografía,Ciencia,Tecnología,Educación,Infantil,Poesía,Filosofía,Religión,Cómic" />
                        <c:forEach var="genItem" items="${listaGeneros}">
                            <c:url var="filtroUrl" value="inicio">
                                <c:param name="q" value="${paramBusqueda}" />
                                <c:param name="genero" value="${genItem}" />
                            </c:url>
                            <li>
                                <a class="dropdown-item py-2 rounded-3 filter-item-lf ${paramGenero == genItem ? 'fw-bold active' : ''}" href="${filtroUrl}">
                                    <c:out value="${genItem == 'TODOS' ? 'Todos' : genItem}" />
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <c:if test="${not empty paramBusqueda || (not empty paramGenero && paramGenero != 'TODOS')}">
                <div class="d-flex align-items-center gap-2 mb-4 flex-wrap">
                    <small class="text-secondary fw-semibold">Filtros aplicados:</small>
                    <c:if test="${not empty paramBusqueda}">
                        <span class="badge bg-secondary-subtle text-dark border px-3 py-2 rounded-pill">
                            Búsqueda: "<c:out value='${paramBusqueda}' />"
                        </span>
                    </c:if>
                    <c:if test="${not empty paramGenero && paramGenero != 'TODOS'}">
                        <span class="badge bg-secondary-subtle text-dark border px-3 py-2 rounded-pill">
                            Género: <c:out value='${paramGenero}' />
                        </span>
                    </c:if>
                    <a href="inicio" class="btn btn-sm btn-link text-danger text-decoration-none p-0 ms-2 fw-bold">
                        <i class="bi bi-x-circle-fill me-1"></i> Limpiar filtros
                    </a>
                </div>
            </c:if>

            <c:choose>
                <c:when test="${empty publicaciones}">
                    <div class="row g-4">
                        <div class="col-12">
                            <div class="p-5 text-center rounded-lf-header text-secondary bg-white shadow-sm border border-2 border-dashed">
                                <i class="bi bi-journal-x display-3 text-muted mb-3 d-block"></i>
                                <h4 class="fw-bold text-dark mb-2">No se encontraron publicaciones</h4>
                                <p class="text-muted mb-3">Intenta buscar con otras palabras o selecciona un género diferente.</p>
                                <a href="inicio" class="btn btn-outline-dark rounded-pill px-4">
                                    Ver todo el catálogo
                                </a>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row g-3 publicaciones-lista">
                        <c:forEach var="publicacion" items="${publicaciones}">
                            <%-- Solo mostrar publicaciones de otros usuarios o administradores --%>
                            <c:if test="${publicacion.idPropietario != sessionScope.usuario.id}">
                                <div class="col-12 col-md-6">
                                    <article class="card-libro ${publicacion.esLibriFlow ? 'tiene-badge' : ''}" style="position: relative;">
                                        <c:if test="${publicacion.esLibriFlow}">
                                            <div class="lf-badge">
                                                Catálogo LibriFlow
                                            </div>
                                        </c:if>

                                        <div class="card-portada">
                                            <img src="${pageContext.request.contextPath}/${publicacion.imagenPrincipal}" alt="Portada de ${publicacion.titulo}">
                                        </div>

                                        <div class="card-contenido">

                                            <div class="card-info">
                                                <h3 class="card-titulo">
                                                    <c:out value="${publicacion.titulo}" />
                                                </h3>

                                                <p class="card-autor">
                                                    Autor: <c:out value="${publicacion.autor}" />
                                                </p>

                                                <p class="card-genero">
                                                    Género: <c:out value="${publicacion.genero}" />
                                                </p>

                                                <p class="card-precio">
                                                    <c:choose>
                                                        <c:when test="${publicacion.precio == 0.0}">
                                                            <span class="texto-solo-renta">Solo renta</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            $<c:out value="${publicacion.precio}" />
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>
                                            </div>

                                            <c:choose>
                                                <c:when test="${publicacion.esLibriFlow}">
                                                    <a href="detalle-publicacion-superad?idPublicacion=${publicacion.idPublicacion}"
                                                       class="btn-detalles">
                                                        Ver detalles
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="detalle-publicacion?idPublicacion=${publicacion.idPublicacion}"
                                                       class="btn-detalles">
                                                        Ver detalles
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>

                                        </div>

                                    </article>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </main>

    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/Notificacion.js"></script>
</body>
</html>