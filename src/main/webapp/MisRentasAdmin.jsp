<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rentas Activas - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/MisPublicaciones.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Inicio.css"/>
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
            <a href="inicio-admin"
               class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>
            <span class="fw-bold fs-4 tracking-wide">
                Rentas Activas
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
                    <a href="mis-rentas-admin" class="btn bg-lf-capsule sidebar-active btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-book-half me-3 fs-5"></i> Rentas activas
                    </a>
                    <a href="usuarios-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-people-fill me-3 fs-5"></i> Usuarios
                    </a>
                    <a href="ingresos-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-cash-stack me-3 fs-5"></i> Ingresos
                    </a>
                </div>
            </div>
        </aside>

        <main class="col catalogo-scroll">
            <section class="mis-publicaciones-container">
                <div class="mis-publicaciones-header">
                    <h2>Rentas (${rentas.size()})</h2>
                </div>
                <c:choose>
                    <c:when test="${empty rentas}">
                        <div class="text-center bg-white rounded-4 shadow-sm p-5 mx-auto" style="max-width: 480px;">
                            <div class="bg-lf-capsule rounded-circle d-inline-flex align-items-center justify-content-center mb-4"
                                 style="width: 90px; height: 90px;">
                                <i class="bi bi-journal-bookmark" style="font-size: 2.5rem; color: #4A4641;"></i>
                            </div>
                            <h4 class="fw-bold mb-2" style="color: #4A4641;">
                                No hay rentas registradas
                            </h4>
                            <p class="text-muted mb-0">
                                Cuando un usuario rente un libro, aparecerá aquí para que puedas darle seguimiento.
                            </p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="publicaciones-lista">
                            <c:forEach var="renta" items="${rentas}">
                                <div class="publicacion-card">
                                    <div class="publicacion-estado">
                                        <span class="estado">${renta.estado}</span>
                                    </div>
                                    <div class="publicacion-contenido">
                                        <div class="publicacion-portada">
                                            <img src="${renta.imagenPrincipal}" alt="${renta.titulo}">
                                        </div>
                                        <div class="publicacion-info">
                                            <h4>${renta.titulo}</h4>
                                            <p>${renta.autor}</p>
                                            <small>Rentado por: ${renta.nombreComprador}</small><br>
                                            <small>Vendedor: ${renta.nombreVendedor}</small><br>
                                            <small>
                                                Del ${renta.fechaInicio} al ${renta.fechaLimite}
                                                <c:if test="${not empty renta.fechaDevolucion}">
                                                    (Devuelto: ${renta.fechaDevolucion})
                                                </c:if>
                                            </small>
                                        </div>
                                        <div class="publicacion-precio">
                                            $${renta.precio}
                                        </div>
                                        <div class="publicacion-acciones">
                                            <form action="${pageContext.request.contextPath}/mis-rentas-admin" method="post" class="d-flex gap-2 align-items-center">
                                                <input type="hidden" name="idDetalle" value="${renta.idDetalle}">
                                                <select name="estado" class="form-select form-select-sm" style="width:auto;">
                                                    <option value="ACTIVA" ${renta.estado == 'ACTIVA' ? 'selected' : ''}>Activa</option>
                                                    <option value="ATRASADA" ${renta.estado == 'ATRASADA' ? 'selected' : ''}>Atrasada</option>
                                                    <option value="MUY ATRASADA" ${renta.estado == 'MUY ATRASADA' ? 'selected' : ''}>Muy atrasada</option>
                                                    <option value="DEVUELTA" ${renta.estado == 'DEVUELTA' ? 'selected' : ''}>Devuelta</option>
                                                    <option value="CANCELADA" ${renta.estado == 'CANCELADA' ? 'selected' : ''}>Cancelada</option>
                                                </select>
                                                <button type="submit" class="btn btn-sm btn-dark">Actualizar</button>
                                            </form>
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
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.js"></script>
</body>
</html>
