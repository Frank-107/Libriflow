<%--
    Esta vista se encarga de mostrar la información detallada de una publicación.
    Permite visualizar las imágenes, datos generales y sinopsis del libro.
    También adapta la navegación dependiendo de la sección desde la cual
    el usuario accedió al detalle de la publicación.
    @author Andres Gerardo Angelina Perez
    @since 24/08/2026
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">
    <title>Detalles - LibriFlow</title>
    <link rel="icon"
          href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png"
          type="image/png">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/DetalleLibro.css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/styles.css"/>
</head>
<body class="p-3 p-md-4 detalle-body">
<%--
    Esta sección determina la ruta a la que regresará el usuario.
    La ruta cambia dependiendo de si el detalle fue abierto desde
    el carrito, las compras o desde el inicio.
    @author Andres Gerardo Angelina Perez
    @since 24/08/2026
--%>
<c:set var="rutaRegreso"
       value="${pageContext.request.contextPath}/inicio-js"/>
<c:if test="${origen == 'carrito'}">
    <c:set var="rutaRegreso"
           value="${pageContext.request.contextPath}/carrito"/>
</c:if>
<c:if test="${origen == 'compras'}">
    <c:set var="rutaRegreso"
           value="${pageContext.request.contextPath}/mis-compras"/>
</c:if>
<div class="container-fluid max-width-xl mx-auto h-100 d-flex flex-column pb-2 detalle-layout">
    <%--
        Esta sección muestra el encabezado de la página.
        Incluye el botón para regresar, el título de la vista y
        la información del usuario que tiene una sesión activa.
        @author Andres Gerardo Angelina Perez
        @since 24/08/2026
    --%>
    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5 detalle-header flex-shrink-0">
        <div class="d-flex align-items-center gap-2 gap-md-3">
            <button class="btn text-white d-md-none p-0 border-0 me-1"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#sidebarMenu"
                    aria-expanded="false"
                    aria-controls="sidebarMenu">
                <i class="bi bi-list"
                   style="font-size:2rem;"></i>
            </button>
            <a href="${rutaRegreso}"
               class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>
            <span class="fw-bold fs-4 tracking-wide">
                Detalles
            </span>
        </div>
        <div class="d-flex align-items-center gap-3">
            <div class="text-end d-none d-md-block">
                <div class="fw-bold mb-0"
                     style="font-size:.95rem;">
                    <c:out value="${sessionScope.usuario.nombre}"/>
                </div>
                <small class="text-white-50"
                       style="font-size:.8rem;">
                    <c:out value="${sessionScope.usuario.correo}"/>
                </small>
            </div>
            <%--
                Este menú permite al usuario acceder a su perfil o cerrar
                la sesión actual dentro del sistema.
                @author Andres Gerardo Angelina Perez
                @since 24/08/2026
            --%>
            <div class="dropdown">
                <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm"
                     style="width:48px;height:48px;cursor:pointer;"
                     data-bs-toggle="dropdown"
                     aria-expanded="false">
                    <i class="bi bi-person-fill fs-4 text-dark"></i>
                </div>
                <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 dropdown-menu-lf">
                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-item"
                           href="${pageContext.request.contextPath}/actualizar-perfil-js">
                            <i class="bi bi-person me-2"></i>
                            Ver perfil
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-logout"
                           href="${pageContext.request.contextPath}/cerrar-sesion">
                            <i class="bi bi-box-arrow-right me-2"></i>
                            Cerrar sesión
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </header>
    <div class="row gx-4 gy-4 gy-md-0 flex-grow-1 overflow-hidden detalle-content-row">
        <%--
            Esta sección contiene el menú lateral de navegación.
            Permite acceder a las principales funciones disponibles para
            el usuario como inicio, carrito, compras, publicaciones y rentas.
            @author Andres Gerardo Angelina Perez
            @since 24/08/2026
        --%>
        <aside class="col-12 col-md-4 col-lg-3 h-100 detalle-sidebar">
            <div class="collapse d-md-block h-100"
                 id="sidebarMenu">
                <div class="bg-lf-dark p-4 rounded-lf-sidebar d-flex flex-column gap-3 shadow-sm mb-3 mb-md-0 h-100">
                    <a href="${pageContext.request.contextPath}/inicio"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-house me-3 fs-5"></i>
                        Inicio
                    </a>
                    <a href="${pageContext.request.contextPath}/carrito"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4 ${origen == 'carrito' ? 'sidebar-active' : ''}">
                        <i class="bi bi-cart3 me-3 fs-5"></i>
                        Carrito
                    </a>
                    <a href="${pageContext.request.contextPath}/mis-compras"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4 ${origen == 'compras' ? 'sidebar-active' : ''}">
                        <i class="bi bi-bag-check me-3 fs-5"></i>
                        Compras
                    </a>
                    <a href="${pageContext.request.contextPath}/publicar-libro-usuario"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-pencil-square me-3 fs-5"></i>
                        Publicar
                    </a>
                    <a href="${pageContext.request.contextPath}/mis-publicaciones-js"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-grid-3x3-gap me-3 fs-5"></i>
                        Mis publicaciones
                    </a>
                    <a href="${pageContext.request.contextPath}/mis-rentas"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-journal-bookmark me-3 fs-5"></i>
                        Mis rentas
                    </a>
                    <a href="#"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-globe me-3 fs-5"></i>
                        Nuestras redes
                    </a>
                </div>
            </div>
        </aside>
        <%--
            Esta sección contiene la información principal de la publicación.
            Se divide en un apartado para las imágenes del libro y otro
            para mostrar sus datos generales.
            @author Andres Gerardo Angelina Perez
            @since 24/08/2026
        --%>
        <main class="col-12 col-md-8 col-lg-9 h-100 overflow-hidden d-flex flex-column position-relative detalle-main">
            <div class="row gx-4 gy-4 gy-lg-0 flex-grow-1 h-100 detalle-inner-row">
                <%--
                    Esta sección muestra las imágenes asociadas con la publicación.
                    Incluye la portada principal, el reverso y una imagen del
                    interior del libro.
                    @author Andres Gerardo Angelina Perez
                    @since 24/08/2026
                --%>
                <div class="col-12 col-lg-6 detalle-left-scroll h-100">
                    <div class="d-flex flex-column align-items-center">
                        <div class="portada-principal-container mb-3 shadow-sm">
                            <img src="${publicacion.imagenPrincipal}"
                                 alt="Portada de ${publicacion.titulo}"
                                 class="img-fluid rounded-4">
                        </div>
                        <div class="d-flex gap-3 mb-3 w-100 justify-content-center"
                             style="max-width:360px;">
                            <div class="miniatura-container shadow-sm">
                                <img src="${publicacion.imagenReverso}"
                                     alt="Imagen reverso"
                                     class="img-fluid rounded-3">
                            </div>
                            <div class="miniatura-container shadow-sm">
                                <img src="${publicacion.imagenInterior}"
                                     alt="Imagen interior"
                                     class="img-fluid rounded-3">
                            </div>
                        </div>
                        <%--
                            Esta condición muestra el origen de la publicación.
                            Indica si pertenece al catálogo administrado por
                            LibriFlow o si fue publicada por un usuario.
                            @author Andres Gerardo Angelina Perez
                            @since 24/08/2026
                        --%>
                        <div class="d-flex align-items-center gap-2 mt-2 fw-bold fs-5 text-dark">
                            <span class="badge bg-secondary rounded-pill px-3 py-2">
                                <c:choose>
                                    <c:when test="${esLibriFlow}">
                                        Catálogo LibriFlow
                                    </c:when>
                                    <c:otherwise>
                                        Publicación de usuario
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </div>
                </div>
                <%--
                    Esta sección muestra los datos generales de la publicación.
                    Presenta el título, autor, editorial, género, precio
                    y sinopsis correspondientes al libro seleccionado.
                    @author Andres Gerardo Angelina Perez
                    @since 24/08/2026
                --%>
                <div class="col-12 col-lg-6 d-flex flex-column gap-3 detalle-right-scroll h-100">
                    <div class="pill-info-lf shadow-sm flex-shrink-0">
                        <span class="fw-bold">
                            Título:
                        </span>
                        <c:out value="${publicacion.titulo}"/>
                    </div>
                    <div class="pill-info-lf shadow-sm flex-shrink-0">
                        <span class="fw-bold">
                            Autor:
                        </span>
                        <c:out value="${publicacion.autor}"/>
                    </div>
                    <div class="pill-info-lf shadow-sm flex-shrink-0">
                        <span class="fw-bold">
                            Editorial:
                        </span>
                        <c:out value="${publicacion.editorial}"/>
                    </div>
                    <div class="pill-info-lf shadow-sm flex-shrink-0">
                        <span class="fw-bold">
                            Género:
                        </span>
                        <c:out value="${publicacion.genero}"/>
                    </div>
                    <div class="pill-info-lf shadow-sm flex-shrink-0">
                        <span class="fw-bold">
                            Precio:
                        </span>
                        $<c:out value="${publicacion.precio}"/>
                    </div>
                    <div class="box-sinopsis-lf shadow-sm flex-grow-1 flex-shrink-0 pe-2">
                        <h5 class="fw-bold mb-3">
                            Sinopsis:
                        </h5>
                        <p class="mb-0 text-muted lh-base">
                            <c:out value="${publicacion.sinopsis}"/>
                        </p>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
<%--
    Este script carga las funciones de Bootstrap utilizadas en elementos
    interactivos de la vista como el menú desplegable y el menú lateral.
    @author Andres Gerardo Angelina Perez
    @since 24/08/2026
--%>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
</body>
</html>