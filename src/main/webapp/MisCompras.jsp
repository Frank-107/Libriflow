<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis compras - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/LibriFlow.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/MisCompras.css"/>
</head>
<body class="p-3 p-md-4">
<div class="container-fluid max-width-xl mx-auto">
    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5">
        <div class="d-flex align-items-center">
            <button class="btn text-white d-md-none p-0 border-0 me-1"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#sidebarMenu"
                    aria-expanded="false"
                    aria-controls="sidebarMenu">
                <i class="bi bi-list" style="font-size:2rem;"></i>
            </button>
            <a href="inicio"
               class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>
            <span class="fw-bold fs-4 tracking-wide">
                Mis compras
            </span>
        </div>
        <div class="d-flex align-items-center gap-3">
            <div class="text-end d-none d-md-block">
                <div class="fw-bold"><c:out value="${sessionScope.usuario.nombre}" /></div>
                <small class="text-white-50"><c:out value="${sessionScope.usuario.correo}" /></small>
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
                        <a class="dropdown-item py-2 dropdown-lf-item"
                           href="actualizar-perfil-js">
                            <i class="bi bi-person me-2"></i>
                            Ver perfil
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-logout"
                           href="cerrar-sesion">
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
                    <a href="inicio-js"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-house me-3 fs-5"></i>
                        Inicio
                    </a>
                    <a href="carrito"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-cart3 me-3 fs-5"></i>
                        Carrito
                    </a>
                    <a href="mis-compras"
                       class="btn bg-lf-capsule btn-lf-pill sidebar-active w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-bag-check me-3 fs-5"></i>
                        Compras
                    </a>
                    <a href="publicar-libro-usuario"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-pencil-square me-3 fs-5"></i>
                        Publicar
                    </a>
                    <a href="mis-publicaciones-js"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-grid-3x3-gap me-3 fs-5"></i>
                        Mis publicaciones
                    </a>
                    <a href="mis-rentas"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-journal-bookmark me-3 fs-5"></i>
                        Mis rentas
                    </a>
                    <a href="https://www.instagram.com/libriflow.oficial?igsh=MW9qbmNld2M2ZXNyeA=="
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-globe me-3 fs-5"></i>
                        Nuestras redes
                    </a>
                </div>
            </div>
        </aside>
        <main class="col d-flex flex-column h-100 min-vh-0">
            <section class="mis-publicaciones-container d-flex flex-column h-100">
                <div class="mis-publicaciones-header flex-shrink-0">
                    <h2>
                        Mis compras (${compras.size()})
                    </h2>
                </div>
                <c:choose>
                    <c:when test="${empty compras}">
                        <div class="text-center bg-white rounded-4 shadow-sm p-5 mx-auto" style="max-width: 480px;">
                            <div class="bg-lf-capsule rounded-circle d-inline-flex align-items-center justify-content-center mb-4"
                                 style="width: 90px; height: 90px;">
                                <i class="bi bi-bag-check" style="font-size: 2.5rem; color: #4A4641;"></i>
                            </div>
                            <h4 class="fw-bold mb-2" style="color: #4A4641;">
                                Aún no tienes compras
                            </h4>
                            <p class="text-muted mb-4">
                                Cuando compres un libro en LibriFlow, aparecerá aquí con todos sus detalles.
                            </p>
                            <a href="inicio" class="btn bg-lf-dark text-white btn-lf-pill px-4 py-2 fw-semibold">
                                <i class="bi bi-search me-2"></i>
                                Explorar catálogo
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- NUEVO WRAPPER CON SCROLL ÚNICO PARA LAS CARDS -->
                        <div class="compras-scroll-wrapper">
                            <div class="publicaciones-lista">
                                <c:forEach var="compra" items="${compras}">
                                    <div class="publicacion-card">
                                        <div class="publicacion-estado">
                                            <span class="estado">
                                                   <c:out value="${compra.estadoTransaccion}" />
                                            </span>
                                        </div>
                                        <div class="publicacion-contenido">
                                            <div class="publicacion-portada">
                                                <img src="${compra.imagenPrincipal}" alt="${compra.titulo}">
                                            </div>
                                            <div class="publicacion-info">
                                                <h4>
                                                    <c:out value="${compra.titulo}" />
                                                </h4>
                                                <p>
                                                    <c:out value="${compra.autor}" />
                                                </p>
                                                <small>
                                                    Vendedor: <c:out value="${compra.nombreVendedor}" />
                                                </small><br>
                                                <small>
                                                    <c:out value="${compra.fechaFormateada}" />
                                                </small>
                                            </div>
                                            <div class="d-flex flex-column align-items-end gap-2">

                                                <div class="publicacion-precio">
                                                    $<c:out value="${compra.precio}" />
                                                </div>

                                                <a href="${pageContext.request.contextPath}/detalle-informativo?idPublicacion=${compra.idPublicacion}&tipo=${compra.esLibriFlow ? 'libriflow' : 'usuario'}&origen=compras"
                                                   class="btn-detalles">

                                                    <i class="bi bi-eye me-1"></i>
                                                    Ver detalles

                                                </a>

                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>
        </main>
    </div>
</div>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
</body>
</html>