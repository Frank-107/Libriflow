<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rentas Activas - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/LibriFlow.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/MisRentasAdmin.css"/>
</head>
<body class="p-3 p-md-4">
<div class="container-fluid max-width-xl mx-auto">
    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5">
        <div class="d-flex align-items-center">
            <button class="btn text-white d-md-none p-0 border-0 me-1" type="button" data-bs-toggle="collapse" data-bs-target="#sidebarMenu" aria-expanded="false" aria-controls="sidebarMenu">
                <i class="bi bi-list" style="font-size:2rem;"></i>
            </button>
            <a href="${pageContext.request.contextPath}/inicio-admin" class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>
            <span class="fw-bold fs-4 tracking-wide">Rentas Activas</span>
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
                <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm" style="width:45px;height:45px;cursor:pointer;" data-bs-toggle="dropdown" aria-expanded="false">
                    <i class="bi bi-person-fill fs-4 text-dark"></i>
                </div>
                <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 dropdown-menu-lf">
                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-item" href="ActualizarPerfilAdmin.jsp">
                            <i class="bi bi-person me-2"></i>Ver perfil
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-logout" href="${pageContext.request.contextPath}/cerrar-sesion">
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
                    <a href="${pageContext.request.contextPath}/inicio-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-house me-3 fs-5"></i>Inicio
                    </a>
                    <a href="${pageContext.request.contextPath}/solicitud-publicacion-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-file-earmark-text me-3 fs-5"></i>Solicitud de publicación
                    </a>
                    <a href="${pageContext.request.contextPath}/publicar-libro-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-file-earmark-text me-3 fs-5"></i>Publicar
                    </a>
                    <a href="${pageContext.request.contextPath}/mis-rentas-admin" class="btn bg-lf-capsule sidebar-active btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-book-half me-3 fs-5"></i>Rentas activas
                    </a>
                    <a href="${pageContext.request.contextPath}/usuarios-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-people-fill me-3 fs-5"></i>Usuarios
                    </a>
                    <a href="${pageContext.request.contextPath}/ingresos-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-cash-stack me-3 fs-5"></i>Ingresos
                    </a>
                </div>
            </div>
        </aside>
        <main class="col d-flex flex-column h-100 min-vh-0">
            <section class="rentas-container d-flex flex-column h-100">
                <div class="rentas-header flex-shrink-0">
                    <h2>Rentas (${rentas.size()})</h2>
                </div>
                <c:choose>
                    <c:when test="${empty rentas}">
                        <div class="text-center bg-white rounded-4 shadow-sm p-5 mx-auto" style="max-width:480px;">
                            <div class="bg-lf-capsule rounded-circle d-inline-flex align-items-center justify-content-center mb-4" style="width:90px;height:90px;">
                                <i class="bi bi-journal-bookmark" style="font-size:2.5rem;color:#4A4641;"></i>
                            </div>
                            <h4 class="fw-bold mb-2" style="color:#4A4641;">No hay rentas registradas</h4>
                            <p class="text-muted mb-0">Cuando un usuario rente un libro, aparecerá aquí para que puedas darle seguimiento.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="rentas-scroll-wrapper">
                            <div class="rentas-lista">
                                <c:forEach var="renta" items="${rentas}">
                                    <c:if test="${renta.estado != 'FINALIZADA'}">
                                        <div class="renta-card">
                                            <div class="d-flex justify-content-between align-items-start mb-2">
                                                <span class="codigo-renta">
                                                    Código: <c:out value="${renta.codigo}"/>
                                                </span>
                                                <div class="d-flex align-items-center gap-2 flex-wrap justify-content-end">
                                                    <span class="badge-estado ${renta.estado == 'PROGRAMADA' ? 'badge-programada' : ''} ${renta.estado == 'ACTIVA' ? 'badge-activa' : ''}">
                                                        <c:out value="${renta.estado}"/>
                                                    </span>
                                                    <c:if test="${renta.penalizacion == 1}">
                                                        <span class="badge-estado badge-atrasada">
                                                            <i class="bi bi-exclamation-circle me-1"></i>ATRASADA
                                                        </span>
                                                    </c:if>
                                                    <c:if test="${renta.penalizacion == 2}">
                                                        <span class="badge-estado badge-muy-atrasada">
                                                            <i class="bi bi-exclamation-triangle-fill me-1"></i>MUY ATRASADA
                                                        </span>
                                                    </c:if>
                                                </div>
                                            </div>
                                            <div class="publicacion-contenido">
                                                <div class="publicacion-portada">
                                                    <img src="${renta.imagenPrincipal}" alt="${renta.titulo}">
                                                </div>
                                                <div class="publicacion-info flex-grow-1">
                                                    <h4>
                                                        <c:out value="${renta.titulo}"/>
                                                    </h4>
                                                    <p class="mb-1">
                                                        <c:out value="${renta.autor}"/>
                                                    </p>
                                                    <p class="mb-1">
                                                        Rentado por: <strong><c:out value="${renta.nombreComprador}"/></strong>
                                                    </p>
                                                    <div class="fechas-renta">
                                                        Del <c:out value="${renta.fechaInicio}"/> al <c:out value="${renta.fechaLimite}"/>
                                                        <c:if test="${not empty renta.fechaDevolucion}">
                                                            <br>
                                                            Devuelto: <c:out value="${renta.fechaDevolucion}"/>
                                                        </c:if>
                                                    </div>
                                                </div>
                                                <div class="publicacion-precio">
                                                    $<c:out value="${renta.precio}"/>
                                                </div>
                                                <div class="publicacion-acciones">
                                                    <c:choose>
                                                        <c:when test="${renta.estado == 'PROGRAMADA'}">
                                                            <c:choose>
                                                                <c:when test="${renta.puedeEntregar}">
                                                                    <form action="${pageContext.request.contextPath}/mis-rentas-admin" method="post">
                                                                        <input type="hidden" name="idDetalle" value="${renta.idDetalle}">
                                                                        <input type="hidden" name="accion" value="ENTREGAR">
                                                                        <button type="submit" class="btn btn-accion-renta">
                                                                            <i class="bi bi-box-arrow-in-right me-1"></i>Marcar como entregado
                                                                        </button>
                                                                    </form>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <button type="button" class="btn btn-entregar-disabled" disabled>
                                                                        <i class="bi bi-lock me-1"></i>Marcar como entregado
                                                                    </button>
                                                                    <div class="disponible-desde">
                                                                        Disponible a partir del <c:out value="${renta.fechaInicio}"/>
                                                                    </div>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:when>
                                                        <c:when test="${renta.estado == 'ACTIVA'}">
                                                            <form action="${pageContext.request.contextPath}/mis-rentas-admin" method="post">
                                                                <input type="hidden" name="idDetalle" value="${renta.idDetalle}">
                                                                <input type="hidden" name="accion" value="DEVOLVER">
                                                                <button type="submit" class="btn btn-accion-renta">
                                                                    <i class="bi bi-check-circle me-1"></i>Marcar devolución
                                                                </button>
                                                            </form>
                                                        </c:when>
                                                    </c:choose>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
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