<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Carrito - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Inicio.css"/>
</head>
<body class="p-3 p-md-4 vh-100 overflow-hidden">
<div class="container-fluid max-width-xl mx-auto">
    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5">
        <div class="d-flex align-items-center gap-2 gap-md-3">
            <button class="btn text-white d-md-none p-0 border-0" type="button" data-bs-toggle="collapse" data-bs-target="#sidebarMenu">
                <i class="bi bi-list fs-2"></i>
            </button>
            <a href="inicio" class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>
            <span class="fw-bold fs-4 tracking-wide">
                Carrito
            </span>
        </div>
        <div class="d-flex align-items-center gap-3">
            <div class="text-end d-none d-md-block">
                <div class="fw-bold mb-0">
                    ${usuario.nombre}
                </div>
                <small class="text-white-50">
                    ${usuario.correo}
                </small>
            </div>
            <div class="dropdown">
                <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm" data-bs-toggle="dropdown">
                    <i class="bi bi-person-fill fs-4 text-dark"></i>
                </div>
                <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 dropdown-menu-lf">
                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-item" href="actualizar-perfil">
                            <i class="bi bi-person me-2"></i>
                            Ver perfil
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-logout" href="cerrar-sesion">
                            <i class="bi bi-box-arrow-right me-2"></i>
                            Cerrar sesión
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
                    <a href="inicio" class="btn bg-lf-capsule btn-lf-pill w-100 text-start d-flex align-items-center px-4">
                        <i class="bi bi-house me-3 fs-5"></i>
                        Inicio
                    </a>
                    <a href="carrito" class="btn bg-lf-capsule btn-lf-pill sidebar-active w-100 text-start d-flex align-items-center px-4">
                        <i class="bi bi-cart3 me-3 fs-5"></i>
                        Carrito
                    </a>
                    <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 text-start d-flex align-items-center px-4">
                        <i class="bi bi-bag-check me-3 fs-5"></i>
                        Compras
                    </a>
                    <a href="publicar-libro-usuario" class="btn bg-lf-capsule btn-lf-pill w-100 text-start d-flex align-items-center px-4">
                        <i class="bi bi-pencil-square me-3 fs-5"></i>
                        Publicar
                    </a>
                    <a href="mis-publicaciones" class="btn bg-lf-capsule btn-lf-pill w-100 text-start d-flex align-items-center px-4">
                        <i class="bi bi-grid-3x3-gap me-3 fs-5"></i>
                        Mis publicaciones
                    </a>
                    <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 text-start d-flex align-items-center px-4">
                        <i class="bi bi-journal-bookmark me-3 fs-5"></i>
                        Mis rentas
                    </a>
                    <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 text-start d-flex align-items-center px-4">
                        <i class="bi bi-globe me-3 fs-5"></i>
                        Nuestras redes
                    </a>
                </div>
            </div>
        </aside>
        <main class="col-12 col-md-8 col-lg-9">
            <div class="form-container-lf p-4 p-md-5 shadow-sm">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h4 class="fw-bold text-dark mb-0">
                        <i class="bi bi-cart3 me-2"></i>
                        Mi carrito
                    </h4>
                </div>
                <c:choose>
                    <c:when test="${empty publicaciones}">
                        <div class="text-center p-5">
                            <i class="bi bi-cart-x fs-1 text-secondary"></i>
                            <h5 class="mt-3 fw-bold">
                                Tu carrito está vacío
                            </h5>
                            <p class="text-secondary">
                                Explora nuestro catálogo y agrega libros.
                            </p>
                            <a href="inicio" class="btn btn-action-lf">
                                Ver libros
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:set var="subtotal" value="0"/>
                        <c:set var="contieneEnvio" value="false"/>
                        <div class="row g-4 align-items-start">
                            <div class="col-12 col-lg-7 col-xl-8 catalogo-scroll">
                                <c:forEach var="publicacion" items="${publicaciones}">
                                    <c:if test="${!publicacion.esLibriFlow && !contieneEnvio}">
                                        <c:set var="contieneEnvio" value="true"/>
                                    </c:if>
                                    <div class="card-libro mb-4">
                                        <c:if test="${publicacion.esLibriFlow}">
                                            <div class="badge-libriflow">
                                                Catálogo LibriFlow
                                            </div>
                                        </c:if>
                                        <div class="card-portada">
                                            <img src="${publicacion.imagenPrincipal}" alt="${publicacion.titulo}">
                                        </div>
                                        <div class="card-contenido">
                                            <div class="card-info">
                                                <h3 class="card-titulo">
                                                        ${publicacion.titulo}
                                                </h3>
                                                <p class="card-autor">
                                                    Autor:
                                                        ${publicacion.autor}
                                                </p>
                                                <p class="card-genero">
                                                    Género:
                                                        ${publicacion.genero}
                                                </p>
                                                <p class="card-precio">
                                                    $${publicacion.precio}
                                                </p>
                                            </div>
                                            <form action="carrito" method="post">
                                                <input type="hidden" name="action" value="eliminar">
                                                <input type="hidden" name="idPublicacion" value="${publicacion.idPublicacion}">
                                                <button type="submit" class="btn btn-outline-danger btn-sm rounded-pill px-3 py-1 shadow-sm d-inline-flex align-items-center gap-1 mt-2">
                                                    <i class="bi bi-trash"></i>
                                                    Eliminar
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                    <c:set var="subtotal" value="${subtotal + publicacion.precio}"/>
                                </c:forEach>
                            </div>
                            <div class="col-12 col-lg-5 col-xl-4">
                                <div class="p-3 bg-white rounded-3 shadow-sm text-end">
                                    <h4 class="fw-bold">
                                        <c:choose>
                                            <c:when test="${contieneEnvio}">
                                                Subtotal:
                                                $${subtotal}
                                                <br>
                                                Envío:
                                                $50-$100
                                                <small class="detalle-envio">
                                                    (Dependiendo ubicación)
                                                </small>
                                            </c:when>
                                            <c:otherwise>
                                                Total:
                                                $${subtotal}
                                            </c:otherwise>
                                        </c:choose>
                                    </h4>
                                    <form action="carrito" method="post" class="mt-3">
                                        <input type="hidden" name="action" value="comprar">
                                        <input type="hidden" name="subtotal" value="${subtotal}">
                                        <input type="hidden" name="contieneEnvio" value="${contieneEnvio}">
                                        <button type="submit" class="btn bg-lf-capsule btn-lf-pill text-dark fw-semibold px-4 py-2 shadow-sm d-inline-flex align-items-center justify-content-center gap-2 w-100">
                                            <i class="bi bi-credit-card-fill fs-5"></i>
                                            Continuar compra
                                        </button>
                                    </form>
                                </div>
                            </div>

                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>
</div>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
</body>
</html>