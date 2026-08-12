<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inicio - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
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
                <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" alt="Logo LibriFlow" style="height: 40px; width: auto;" class="me-2">
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
                        <a class="dropdown-item py-2 dropdown-lf-item" href="actualizar-perfil">
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
            <div class="d-flex gap-3 mb-4 align-items-center">
                <form action="inicio" method="GET" class="position-relative flex-grow-1 m-0">
                    <i class="bi bi-search search-icon-inside"></i>
                    <c:if test="${not empty paramGenero}">
                        <input type="hidden" name="genero" value="${paramGenero}">
                    </c:if>
                    <input type="text"
                           name="q"
                           value="${paramBusqueda != null ? paramBusqueda : ''}"
                           class="form-control search-bar-lf shadow-sm"
                           placeholder="Buscar libros, autores...">
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
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=TODOS">Todos</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Novela">Novela</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Fantasía">Fantasía</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Ciencia ficción">Ciencia ficción</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Terror">Terror</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Romance">Romance</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Misterio">Misterio</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Suspenso">Suspenso</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Drama">Drama</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Aventura">Aventura</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Historia">Historia</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Biografía">Biografía</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Autobiografía">Autobiografía</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Ciencia">Ciencia</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Tecnología">Tecnología</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Educación">Educación</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Infantil">Infantil</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Poesía">Poesía</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Filosofía">Filosofía</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Religión">Religión</a></li>
                        <li><a class="dropdown-item py-2 rounded-3 filter-item-lf" href="inicio?q=${paramBusqueda != null ? paramBusqueda : ''}&genero=Cómic">Cómic</a></li>
                    </ul>
                </div>

            </div>
            <c:choose>
                <c:when test="${empty publicaciones}">
                    <div class="row g-4">
                        <div class="col-12">
                            <div class="p-5 text-center rounded-lf-header text-secondary bg-white shadow-sm border border-2 border-dashed">
                                <h4 class="fw-bold text-dark">No hay ninguna publicacion...</h4>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row g-3">
                        <c:forEach var="publicacion" items="${publicaciones}">
                            <c:if test="${publicacion.idPropietario != sessionScope.usuario.id}">
                                <div class="col-12 col-md-6">
                                    <article class="card-libro" style="position: relative;">
                                        <c:if test="${publicacion.esLibriFlow}">
                                            <div style="position: absolute; top: 12px; right: 12px; background-color: #F1ECE5; color: #5B564F; font-size: 0.65rem; padding: 4px 12px; border-radius: 20px; font-weight: 600; box-shadow: 0 1px 3px rgba(0,0,0,0.05); z-index: 10;">
                                                Catálogo LibriFlow
                                            </div>
                                        </c:if>

                                        <div class="card-portada">
                                            <img src="${publicacion.imagenPrincipal}" alt="Portada de ${publicacion.titulo}">
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
<script src="assets/js/bootstrap.js"></script>
</body>
</html>