<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
 * Vista de interfaz de usuario que muestra el listado de rentas asociadas al usuario en sesión.
 * Despliega la información detallada de los libros en préstamo, incluyendo estados del servicio,
 * fechas vigentes (inicio, límite y devolución real), costos asociados y días restantes para la entrega.
 *
 * @author Monserrath
 * @since 25/08/2026
--%>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis rentas - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/MisPublicaciones.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Inicio.css"/>
</head>
<body class="p-3 p-md-4">
<div class="container-fluid max-width-xl mx-auto">

    <!-- HEADER -->
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
            <a href="inicio-js"
               class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>
            <span class="fw-bold fs-4 tracking-wide ms-2">
                Mis rentas
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
                        <a class="dropdown-item py-2 dropdown-lf-item" href="actualizar-perfil-js">
                            <i class="bi bi-person me-2"></i> Ver perfil
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-logout" href="cerrar-sesion">
                            <i class="bi bi-box-arrow-right me-2"></i> Cerrar sesión
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </header>

    <div class="row g-4">
        <!-- SIDEBAR -->
        <aside class="col-12 col-md-4 col-lg-3">
            <div class="collapse d-md-block" id="sidebarMenu">
                <div class="bg-lf-dark p-4 rounded-lf-sidebar d-flex flex-column gap-3 shadow-sm mb-3 mb-md-0">
                    <a href="inicio-js" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-house me-3 fs-5"></i> Inicio
                    </a>
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
                    <a href="mis-rentas" class="btn bg-lf-capsule btn-lf-pill sidebar-active w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-journal-bookmark me-3 fs-5"></i> Mis rentas
                    </a>
                    <a href="https://www.instagram.com/libriflow.oficial?igsh=MW9qbmNld2M2ZXNyeA==" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-globe me-3 fs-5"></i> Nuestras redes
                    </a>
                </div>
            </div>
        </aside>

        <!-- CONTENIDO PRINCIPAL -->
        <main class="col catalogo-scroll">
            <section class="mis-publicaciones-container">
                <div class="mis-publicaciones-header mb-4">
                    <h2 class="fw-bold" style="color: #4A4641;">
                        Mis rentas (<c:out value="${rentas.size()}" />)
                    </h2>
                </div>

                <c:choose>
                    <c:when test="${empty rentas}">
                        <div class="text-center bg-white rounded-4 shadow-sm p-5 mx-auto" style="max-width: 480px;">
                            <div class="bg-lf-capsule rounded-circle d-inline-flex align-items-center justify-content-center mb-4"
                                 style="width: 90px; height: 90px;">
                                <i class="bi bi-journal-bookmark" style="font-size: 2.5rem; color: #4A4641;"></i>
                            </div>
                            <h4 class="fw-bold mb-2" style="color: #4A4641;">
                                Aún no tienes rentas activas
                            </h4>
                            <p class="text-muted mb-4">
                                Explora el catálogo y renta tu próximo libro favorito —
                                aparecerá aquí en cuanto confirmes tu primera renta.
                            </p>
                            <a href="inicio" class="btn bg-lf-dark text-white btn-lf-pill px-4 py-2 fw-semibold">
                                <i class="bi bi-search me-2"></i> Explorar catálogo
                            </a>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <div class="row g-3">
                            <c:forEach var="renta" items="${rentas}">
                                <!-- UN SOLO REGISTRO POR FILA -->
                                <div class="col-12">
                                    <div class="card-libro h-100 shadow-sm border-0 position-relative p-3">

                                        <!-- BADGE DE ESTADO LIMPIO -->
                                        <div class="position-absolute top-0 end-0 mt-3 me-3">
                                            <c:choose>
                                                <c:when test="${renta.estado == 'ACTIVO' || renta.estado == 'ACTIVA'}">
                                                    <span class="badge bg-success-subtle text-success border border-success-subtle rounded-pill px-3 py-1.5 fs-7">
                                                        <i class="bi bi-clock-history me-1"></i> En Renta
                                                    </span>
                                                </c:when>
                                                <c:when test="${renta.estado == 'FINALIZADO' || renta.estado == 'DE VUELTO'}">
                                                    <span class="badge bg-secondary-subtle text-secondary border border-secondary-subtle rounded-pill px-3 py-1.5 fs-7">
                                                        <i class="bi bi-check-circle me-1"></i> Devuelto
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-warning-subtle text-dark border border-warning-subtle rounded-pill px-3 py-1.5 fs-7">
                                                        <c:out value="${renta.estado}" />
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <!-- PORTADA DEL LIBRO -->
                                        <div class="card-portada align-self-center">
                                            <img src="${renta.imagenPrincipal}" alt="${renta.titulo}">
                                        </div>

                                        <!-- DETALLES DE LA RENTA -->
                                        <div class="card-contenido d-flex flex-column justify-content-between w-100">
                                            <div class="card-info pe-5">
                                                <h3 class="card-titulo text-truncate mb-1" title="${renta.titulo}">
                                                    <c:out value="${renta.titulo}" />
                                                </h3>
                                                <p class="card-autor mb-3">
                                                    <i class="bi bi-person me-1 text-muted"></i>
                                                    <c:out value="${renta.autor}" />
                                                </p>

                                                <!-- DETALLE SUTIL DE FECHAS Y DÍAS -->
                                                <div class="py-2 border-top border-bottom my-2">
                                                    <div class="d-flex align-items-center gap-2 text-secondary flex-wrap" style="font-size: 0.85rem;">
                                                        <span class="fw-semibold text-dark">
                                                            <i class="bi bi-calendar-range me-1 text-muted"></i> Período:
                                                        </span>
                                                        <span><c:out value="${renta.fechaInicio}" /></span>
                                                        <i class="bi bi-arrow-right text-muted"></i>
                                                        <span class="fw-semibold text-dark"><c:out value="${renta.fechaLimite}" /></span>

                                                        <!-- TIEMPO RESTANTE INTEGRADOR -->
                                                        <c:if test="${renta.estado == 'ACTIVO' || renta.estado == 'ACTIVA'}">
                                                            <c:if test="${renta.diasRestantes != null && renta.diasRestantes != -1}">
                                                                <span class="ms-md-auto">
                                                                    <c:choose>
                                                                        <c:when test="${renta.diasRestantes > 3}">
                                                                            <span class="badge bg-success-subtle text-success fw-medium px-2 py-1">
                                                                                <i class="bi bi-hourglass-split me-1"></i>Quedan <c:out value="${renta.diasRestantes}" /> días
                                                                            </span>
                                                                        </c:when>
                                                                        <c:when test="${renta.diasRestantes >= 0}">
                                                                            <span class="badge bg-warning-subtle text-dark fw-medium px-2 py-1">
                                                                                <i class="bi bi-exclamation-triangle me-1"></i>¡Quedan solo <c:out value="${renta.diasRestantes}" /> días!
                                                                            </span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-danger-subtle text-danger fw-medium px-2 py-1">
                                                                                <i class="bi bi-x-circle me-1"></i>Renta vencida
                                                                            </span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </span>
                                                            </c:if>
                                                        </c:if>
                                                    </div>

                                                    <c:if test="${not empty renta.fechaDevolucion}">
                                                        <div class="text-muted mt-1" style="font-size: 0.8rem;">
                                                            <i class="bi bi-box-arrow-in-down-left me-1"></i> Devuelto el: <strong><c:out value="${renta.fechaDevolucion}" /></strong>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </div>

                                            <div class="d-flex justify-content-between align-items-center mt-1">
                                                <div class="card-precio m-0">
                                                    <span class="fs-6 fw-normal text-muted">Costo:</span> $<c:out value="${renta.precio}" />
                                                </div>
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

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
</body>
</html>