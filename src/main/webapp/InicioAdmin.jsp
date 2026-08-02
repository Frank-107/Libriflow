<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inicio Admin - LibriFlow</title>
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

            <a href="InicioAdmin.jsp" class="d-flex align-items-center text-decoration-none">
                <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" alt="Logo LibriFlow" style="height: 40px; width: auto;" class="me-2">
                <div class="text-start d-none d-sm-block">
                    <div class="fw-bold tracking-widest fs-5 text-white">LIBRIFLOW</div>
                    <small style="font-size: 0.65rem; letter-spacing: 1px; color: #CBC2B9; display: block;">TU BIBLIOTECA DIGITAL</small>
                </div>
            </a>
        </div>
        <div class="d-flex align-items-center gap-3">
            <div class="text-end d-none d-md-block">
                <div class="fw-bold mb-0" style="font-size: 0.95rem;">${sessionScope.usuario.nombre}</div>
                <small class="text-white-50" style="font-size: 0.8rem;">${sessionScope.usuario.correo}</small>
            </div>

            <div class="dropdown">
                <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm" style="width: 45px; height: 45px; cursor: pointer;" data-bs-toggle="dropdown" aria-expanded="false">
                    <i class="bi bi-person-fill fs-4 text-dark"></i>
                </div>

                <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 dropdown-menu-lf">
                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-item" href="ActualizarPerfilAdmin.jsp">
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
                    <a href="solicitud-publicacion" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-file-earmark-text me-3 fs-5"></i> Solicitud de publicación
                    </a>
                    <a href="publicar-libro-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-file-earmark-text me-3 fs-5"></i> Publicar
                    </a>
                    <a href="${pageContext.request.contextPath}/mis-rentas-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-book-half me-3 fs-5"></i> Rentas activas
                    </a>
                    <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-people-fill me-3 fs-5"></i> Usuarios
                    </a>
                    <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-cash-stack me-3 fs-5"></i> Ingresos
                    </a>
                </div>
            </div>
        </aside>

        <main class="col-12 col-md-8 col-lg-9 catalogo-scroll">

            <c:choose>
                <c:when test="${empty publicaciones}">
                    <div class="row g-4">
                        <div class="col-12">
                            <div class="p-5 text-center rounded-lf-header text-secondary bg-white shadow-sm border border-2 border-dashed">
                                <h4 class="fw-bold text-dark">No hay ninguna publicación...</h4>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row g-3">
                        <c:forEach var="publicacion" items="${publicaciones}">
                            <div class="col-12 col-md-6">
                                <article class="card-libro">

                                    <div class="card-portada">
                                        <img src="${publicacion.imagenPrincipal}" alt="Portada de ${publicacion.titulo}">
                                    </div>

                                    <div class="card-contenido">

                                        <div class="card-info">
                                            <h3 class="card-titulo">
                                                    ${publicacion.titulo}
                                            </h3>

                                            <p class="card-autor">
                                                Autor: ${publicacion.autor}
                                            </p>

                                            <p class="card-genero">
                                                Género: ${publicacion.genero}
                                            </p>

                                            <p class="card-precio">
                                                $${publicacion.precio}
                                            </p>
                                        </div>

                                        <button type="button" class="btn-detalles">
                                            Ver detalles
                                        </button>

                                    </div>

                                </article>
                            </div>
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