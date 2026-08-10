<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle de Usuario - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/LibriFlow.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/DetalleUsuario.css"/>
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
            <a href="inicio-admin"
               class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>
            <span class="fw-bold fs-4 tracking-wide ms-2">
                Usuarios
            </span>
        </div>
        <div class="d-flex align-items-center gap-3">
            <div class="text-end d-none d-md-block">
                <div class="fw-bold">${sessionScope.usuario.nombre}</div>
                <small class="text-white-50">${sessionScope.usuario.correo}</small>
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
                        <a class="dropdown-item py-2 dropdown-lf-item" href="ActualizarPerfilAdmin.jsp">
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
        <!-- SIDEBAR MENU -->
        <aside class="col-12 col-md-4 col-lg-3">
            <div class="collapse d-md-block" id="menuLateral">
                <div class="bg-lf-dark p-4 rounded-lf-sidebar d-flex flex-column gap-3 shadow-sm mb-3 mb-md-0">
                    <a href="inicio-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
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
                    <a href="usuarios-admin" class="btn bg-lf-capsule sidebar-active btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-people-fill me-3 fs-5"></i> Usuarios
                    </a>
                    <a href="ingresos-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-cash-stack me-3 fs-5"></i> Ingresos
                    </a>
                </div>
            </div>
        </aside>


        ```jsp
        <!-- ============================= -->
        <!-- DETALLES DEL USUARIO -->
        <!-- ============================= -->

        <div class="lf-user-details">

            <!-- Encabezado -->
            <div class="lf-user-header">

                <div>
                    <span class="lf-section-label">ADMINISTRACIÓN</span>

                    <h2>Detalles del usuario</h2>

                    <p>
                        Información general y movimientos de la cuenta.
                    </p>
                </div>

                <div class="lf-user-header-actions">

                    <!-- Estado -->
                    <c:choose>

                        <c:when test="${usuario.estado == 'ACTIVA'}">

                    <span class="lf-status lf-status-active">
                        <span class="lf-status-dot"></span>
                        ACTIVA
                    </span>

                        </c:when>

                        <c:otherwise>

                    <span class="lf-status lf-status-inactive">
                        <span class="lf-status-dot"></span>
                        ${usuario.estado}
                    </span>

                        </c:otherwise>

                    </c:choose>


                    <!-- Botón bloquear / desbloquear -->
                    <c:choose>

                        <c:when test="${usuario.estado == 'ACTIVA'}">

                            <form action="admin-bloquear-usuario"
                                  method="post"
                                  class="lf-user-action-form">

                                <input type="hidden"
                                       name="idUsuario"
                                       value="${usuario.id}">

                                <button type="submit"
                                        class="lf-user-action-btn lf-block-btn">

                                    <i class="bi bi-lock"></i>

                                    Bloquear usuario

                                </button>

                            </form>

                        </c:when>

                        <c:otherwise>

                            <form action="admin-desbloquear-usuario"
                                  method="post"
                                  class="lf-user-action-form">

                                <input type="hidden"
                                       name="idUsuario"
                                       value="${usuario.id}">

                                <button type="submit"
                                        class="lf-user-action-btn lf-unblock-btn">

                                    <i class="bi bi-unlock"></i>

                                    Desbloquear usuario

                                </button>

                            </form>

                        </c:otherwise>

                    </c:choose>

                </div>

            </div>


            <!-- ============================= -->
            <!-- INFORMACIÓN BÁSICA -->
            <!-- ============================= -->

            <div class="lf-user-info-card">

                <div class="lf-card-title">

                    <div class="lf-card-icon">
                        <i class="bi bi-person"></i>
                    </div>

                    <div>
                        <h3>Información personal</h3>

                        <p>
                            Datos básicos de la cuenta
                        </p>
                    </div>

                </div>


                <div class="lf-user-info-grid">

                    <div class="lf-info-item">

                <span class="lf-info-label">
                    Nombre completo
                </span>

                        <span class="lf-info-value">
                    ${usuario.nombre}
                    ${usuario.apellidoPaterno}
                    ${usuario.apellidoMaterno}
                </span>

                    </div>


                    <div class="lf-info-item">

                <span class="lf-info-label">
                    Correo electrónico
                </span>

                        <span class="lf-info-value">
                            ${usuario.correo}
                        </span>

                    </div>


                    <div class="lf-info-item">

                <span class="lf-info-label">
                    Teléfono
                </span>

                        <span class="lf-info-value">
                            ${empty usuario.telefono ? 'N/A' : usuario.telefono}
                        </span>

                    </div>


                    <div class="lf-info-item">

                <span class="lf-info-label">
                    Cuenta creada
                </span>

                        <span class="lf-info-value">
                            ${usuario.fechaCreacion}
                        </span>

                    </div>

                </div>

            </div>


            <!-- ============================= -->
            <!-- RESUMEN DE ACTIVIDAD -->
            <!-- ============================= -->

            <div class="lf-user-info-card">

                <div class="lf-card-title">

                    <div class="lf-card-icon">
                        <i class="bi bi-bar-chart"></i>
                    </div>

                    <div>

                        <h3>Resumen de actividad</h3>

                        <p>
                            Actividad actual del usuario
                        </p>

                    </div>

                </div>


                <div class="lf-stats-grid">

                    <!-- Publicaciones -->
                    <div class="lf-stat-card">

                        <div class="lf-stat-icon">
                            <i class="bi bi-book"></i>
                        </div>

                        <div>

                    <span class="lf-stat-number">
                        ${cantidadPublicaciones}
                    </span>

                            <span class="lf-stat-label">
                        Publicaciones activas
                    </span>

                        </div>

                    </div>


                    <!-- Ventas -->
                    <div class="lf-stat-card">

                        <div class="lf-stat-icon">
                            <i class="bi bi-bag-check"></i>
                        </div>

                        <div>

                    <span class="lf-stat-number">
                        ${cantidadVentas}
                    </span>

                            <span class="lf-stat-label">
                        Libros vendidos
                    </span>

                        </div>

                    </div>


                    <!-- Rentas -->
                    <div class="lf-stat-card">

                        <div class="lf-stat-icon">
                            <i class="bi bi-arrow-repeat"></i>
                        </div>

                        <div>

                    <span class="lf-stat-number">
                        ${cantidadRentasActivas}
                    </span>

                            <span class="lf-stat-label">
                        Rentas activas
                    </span>

                        </div>

                    </div>


                    <!-- Retrasos -->
                    <div class="lf-stat-card lf-stat-danger">

                        <div class="lf-stat-icon">
                            <i class="bi bi-exclamation-triangle"></i>
                        </div>

                        <div>

                    <span class="lf-stat-number">
                        ${cantidadRetrasos}
                    </span>

                            <span class="lf-stat-label">
                        Retrasos
                    </span>

                        </div>

                    </div>

                </div>

            </div>


            <!-- ============================= -->
            <!-- MOVIMIENTOS -->
            <!-- ============================= -->

            <div class="lf-user-info-card lf-movements-card">

                <div class="lf-card-title lf-movements-header">

                    <div class="lf-card-title-left">

                        <div class="lf-card-icon">
                            <i class="bi bi-clock-history"></i>
                        </div>

                        <div>

                            <h3>Movimientos</h3>

                            <p>
                                Historial de operaciones realizadas por el usuario
                            </p>

                        </div>

                    </div>


                    <span class="lf-movement-count">
                ${cantidadMovimientos} movimientos
            </span>

                </div>


                <!-- Tabla con scroll -->
                <div class="lf-table-wrapper">

                    <table class="lf-movements-table">

                        <thead>

                        <tr>

                            <th>
                                Tipo de movimiento
                            </th>

                            <th>
                                Fecha
                            </th>

                            <th>
                                Libro
                            </th>

                            <th>
                                Precio
                            </th>

                            <th>
                                Origen
                            </th>

                        </tr>

                        </thead>


                        <tbody>

                        <c:choose>

                            <c:when test="${not empty movimiento}">

                                <c:forEach var="mov"
                                           items="${movimiento}">

                                    <tr>

                                        <!-- Tipo -->
                                        <td>

                                            <c:choose>

                                                <c:when test="${mov.tipoMovimiento == 'COMPRA'}">

                                            <span class="lf-movement-type lf-movement-buy">

                                                <i class="bi bi-cart-check"></i>

                                                Compra

                                            </span>

                                                </c:when>


                                                <c:when test="${mov.tipoMovimiento == 'VENTA'}">

                                            <span class="lf-movement-type lf-movement-sale">

                                                <i class="bi bi-cash-coin"></i>

                                                Venta

                                            </span>

                                                </c:when>


                                                <c:when test="${mov.tipoMovimiento == 'RENTA'}">

                                            <span class="lf-movement-type lf-movement-rent">

                                                <i class="bi bi-arrow-repeat"></i>

                                                Renta

                                            </span>

                                                </c:when>


                                                <c:otherwise>

                                            <span class="lf-movement-type">
                                                    ${mov.tipoMovimiento}
                                            </span>

                                                </c:otherwise>

                                            </c:choose>

                                        </td>


                                        <!-- Fecha -->
                                        <td>

                                    <span class="lf-table-date">
                                            ${mov.fecha}
                                    </span>

                                        </td>


                                        <!-- Libro -->
                                        <td>

                                    <span class="lf-book-title">
                                            ${mov.titulo}
                                    </span>

                                        </td>


                                        <!-- Precio -->
                                        <td>

                                    <span class="lf-price">
                                        $${mov.precio}
                                    </span>

                                        </td>


                                        <!-- Origen -->
                                        <td>

                                            <c:choose>

                                                <c:when test="${mov.esLibriFlow}">

                                            <span class="lf-origin lf-origin-libriflow">

                                                <i class="bi bi-book"></i>

                                                LibriFlow

                                            </span>

                                                </c:when>


                                                <c:otherwise>

                                            <span class="lf-origin lf-origin-user">
                                                Usuario
                                            </span>

                                                </c:otherwise>

                                            </c:choose>

                                        </td>

                                    </tr>

                                </c:forEach>

                            </c:when>


                            <c:otherwise>

                                <tr>

                                    <td colspan="5">

                                        <div class="lf-empty-movements">

                                            <i class="bi bi-clock-history"></i>

                                            <strong>
                                                No hay movimientos
                                            </strong>

                                            <span>
                                        Este usuario todavía no tiene movimientos registrados.
                                    </span>

                                        </div>

                                    </td>

                                </tr>

                            </c:otherwise>

                        </c:choose>

                        </tbody>

                    </table>

                </div>

            </div>

        </div>
        ```




    </div>
</div>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.js"></script>
</body>
</html>