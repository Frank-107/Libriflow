<%--
    Documento   : MisPublicaciones.jsp
    Autor       : Monserrath Anzurez
    Fecha       : 23/08/26
    Descripción : Vista JSP para la gestión y visualización de las publicaciones creadas por el usuario en LibriFlow.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis publicaciones - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/MisPublicaciones.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/LibriFlow.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/MisPublicacionesVista.css?v=1"/>
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
            <a href="inicio-js"
               class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>
            <span class="fw-bold fs-4 tracking-wide">
            Mis publicaciones
        </span>
        </div>

        <div class="d-flex align-items-center gap-3">
            <div class="text-end d-none d-md-block">
                <div class="fw-bold">
                    ${sessionScope.usuario.nombre}
                </div>
                <small class="text-white-50">
                    ${sessionScope.usuario.correo}
                </small>
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
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-bag-check me-3 fs-5"></i>
                        Compras
                    </a>
                    <a href="publicar-libro-usuario"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-pencil-square me-3 fs-5"></i>
                        Publicar
                    </a>
                    <a href="mis-publicaciones-js"
                       class="btn bg-lf-capsule btn-lf-pill sidebar-active w-100 py-2.5 text-start d-flex align-items-center px-4">
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
        <main class="col">
            <section class="mis-publicaciones-container position-relative pt-2">
                <c:if test="${not empty error}">
                    <div class="libri-toast libri-toast-error">
                        <i class="bi bi-exclamation-circle-fill fs-5"></i>
                        <span><c:out value="${error}" escapeXml="true" /></span>
                    </div>
                </c:if>
                <c:if test="${not empty exito}">
                    <div class="libri-toast libri-toast-success">
                        <i class="bi bi-check-circle-fill fs-5"></i>
                        <span><c:out value="${exito}" escapeXml="true" /></span>
                    </div>
                </c:if>

                <div class="mis-publicaciones-header">
                    <h2>
                        Publicaciones totales (<c:out value="${publicaciones.size()}" />)
                    </h2>
                </div>
                <c:choose>
                    <c:when test="${empty publicaciones}">
                        <div class="sin-publicaciones">
                            <i class="bi bi-journal-x"></i>
                            <h4>
                                No tienes publicaciones.
                            </h4>
                            <p>
                                Cuando publiques un libro aparecerá aquí.
                            </p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="publicaciones-lista">
                            <c:forEach var="publicacion" items="${publicaciones}">
                                <div class="publicacion-card">
                                    <div class="publicacion-contenido">
                                        <div class="publicacion-portada">
                                            <img src="${publicacion.imagenPrincipal}" alt="${publicacion.titulo}">
                                        </div>
                                        <div class="publicacion-info">
                                            <h4>
                                                    <c:out value="${publicacion.titulo}" />}
                                            </h4>
                                            <p>
                                                    <c:out value="${publicacion.autor}" />}
                                            </p>
                                            <small>
                                                    <c:out value="${publicacion.genero}" />}
                                            </small>
                                        </div>
                                        <div class="d-flex flex-column align-items-end me-3 gap-2">
                                    <span class="estado ${publicacion.estado}">
                                            <c:out value="${publicacion.estado}" />
                                    </span>
                                            <div class="publicacion-precio" style="margin-right: 0;">
                                                $<c:out value="${publicacion.precio}" />
                                            </div>
                                            <c:if test="${publicacion.estado == 'PENDIENTE'}">
                                                <form action="mis-publicaciones" method="post" class="m-0">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="idPublicacion" value="${publicacion.idPublicacion}">
                                                    <button type="submit" class="btn btn-outline-danger btn-sm rounded-pill px-3 py-1 shadow-sm d-inline-flex align-items-center gap-1">
                                                        <i class="bi bi-trash"></i>
                                                        Cancelar publicación
                                                    </button>
                                                </form>
                                            </c:if>
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
<script src="${pageContext.request.contextPath}/assets/js/Notificacion.js"></script>
</body>
</html>