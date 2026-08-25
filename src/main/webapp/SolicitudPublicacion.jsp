<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
 * Vista del panel de administración para la gestión y revisión de solicitudes de publicación en la plataforma LibriFlow.
 * Muestra el listado de libros pendientes de aprobación subidos por los usuarios, presentando la portada, título,
 * propietario, autor, género y precio. Proporciona enlaces para acceder al detalle de evaluación de cada solicitud.
 *
 * @author Alejandro
 * @since 25/08/2026
--%>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Solicitudes de publicación - LibriFlow</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/SolicitudPublicacion.css"/>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png">
</head>

<body class="p-3 p-md-4">
<div class="container-fluid max-width-xl mx-auto h-100 d-flex flex-column pb-2">

    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5 flex-shrink-0">
        <div class="d-flex align-items-center">
            <button class="btn text-white d-md-none me-2 p-0 border-0"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#menuLateral"
                    aria-expanded="false"
                    aria-controls="menuLateral">
                <i class="bi bi-list" style="font-size:2rem;"></i>
            </button>

            <a href="${pageContext.request.contextPath}/inicio-admin"
               class="d-flex align-items-center text-decoration-none">
                <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png"
                     alt="Logo LibriFlow"
                     style="height:40px;width:auto;"
                     class="me-2">

                <div class="text-start d-none d-sm-block">
                    <div class="fw-bold tracking-widest fs-5 text-white">LIBRIFLOW</div>
                    <small style="font-size:.65rem;letter-spacing:1px;color:#CBC2B9;display:block;">
                        TU BIBLIOTECA DIGITAL
                    </small>
                </div>
            </a>
        </div>

        <div class="d-flex align-items-center gap-3">
            <div class="text-end d-none d-md-block">
                <div class="fw-bold mb-0" style="font-size:.95rem;">
                    <c:out value="${sessionScope.usuario.nombre}"/>
                </div>
                <small class="text-white-50" style="font-size:.8rem;">
                    <c:out value="${sessionScope.usuario.correo}"/>
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
                           href="${pageContext.request.contextPath}/ActualizarPerfilAdmin.jsp">
                            <i class="bi bi-person me-2"></i>Ver perfil
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-logout"
                           href="${pageContext.request.contextPath}/cerrar-sesion">
                            <i class="bi bi-box-arrow-right me-2"></i>Cerrar sesión
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </header>

    <div class="row gx-4 gy-3 gy-md-0 flex-grow-1 overflow-hidden">

        <aside class="col-12 col-md-4 col-lg-3 collapse d-md-block h-100" id="menuLateral">
            <div class="bg-lf-dark p-4 rounded-lf-sidebar d-flex flex-column gap-3 shadow-sm mb-3 mb-md-0 h-100">
                <a href="${pageContext.request.contextPath}/inicio-admin"
                   class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-house me-3 fs-5"></i>Inicio
                </a>
                <a href="${pageContext.request.contextPath}/solicitud-publicacion-admin"
                   class="btn bg-lf-capsule sidebar-active btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-file-earmark-text me-3 fs-5"></i>Solicitud de publicación
                </a>
                <a href="${pageContext.request.contextPath}/publicar-libro-admin"
                   class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-file-earmark-text me-3 fs-5"></i>Publicar
                </a>
                <a href="${pageContext.request.contextPath}/mis-rentas-admin"
                   class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-book-half me-3 fs-5"></i>Rentas activas
                </a>
                <a href="${pageContext.request.contextPath}/usuarios-admin"
                   class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-people-fill me-3 fs-5"></i>Usuarios
                </a>
                <a href="${pageContext.request.contextPath}/ingresos-admin"
                   class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-cash-stack me-3 fs-5"></i>Ingresos
                </a>
            </div>
        </aside>

        <c:if test="${not empty sessionScope.mensaje}">
            <div id="successToast" class="libri-toast libri-toast-success">
                <i class="bi bi-check-circle-fill fs-5"></i>
                <span><c:out value="${sessionScope.mensaje}" escapeXml="true"/></span>
            </div>
            <c:remove var="mensaje" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.error}">
            <div id="errorToast" class="libri-toast libri-toast-error">
                <i class="bi bi-exclamation-triangle-fill fs-5"></i>
                <span><c:out value="${sessionScope.error}" escapeXml="true"/></span>
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <main class="col-12 col-md-8 col-lg-9 h-100 overflow-hidden d-flex flex-column position-relative">
            <div class="d-flex justify-content-between align-items-center mb-4 flex-shrink-0">
                <div>
                    <h3 class="fw-bold mb-1">Solicitudes de publicación</h3>
                    <p class="text-muted mb-0">
                        Revisa cada publicación antes de aprobarla.
                    </p>
                </div>
                <span class="badge bg-secondary rounded-pill px-3 py-2">
                    <c:out value="${publicaciones.size()}"/> pendientes
                </span>
            </div>

            <div class="solicitudes-scroll flex-grow-1">
                <c:choose>
                    <c:when test="${empty publicaciones}">
                        <div class="p-5 text-center rounded-lf-header text-secondary bg-white shadow-sm border border-2">
                            <i class="bi bi-check2-circle fs-1 d-block mb-3"></i>
                            <h4 class="fw-bold text-dark">No hay solicitudes pendientes</h4>
                            <p class="mb-0">Las nuevas solicitudes aparecerán aquí.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="solicitudes-grid">
                            <c:forEach var="publicacion" items="${publicaciones}">
                                <article class="solicitud-card">
                                    <div class="solicitud-portada">
                                        <img src="${publicacion.imagenPrincipal}"
                                             alt="Portada de ${publicacion.titulo}">
                                    </div>
                                    <div class="solicitud-info">
                                        <h3>
                                            <c:out value="${publicacion.titulo}"/>
                                        </h3>
                                        <p>
                                            <i class="bi bi-person me-1"></i>
                                            Publicado por:
                                            <strong>
                                                <c:out value="${publicacion.nombrePropietario}"/>
                                            </strong>
                                        </p>
                                        <p>
                                            <i class="bi bi-pen me-1"></i>
                                            <c:out value="${publicacion.autor}"/>
                                        </p>
                                        <p>
                                            <i class="bi bi-bookmark me-1"></i>
                                            <c:out value="${publicacion.genero}"/>
                                        </p>
                                        <div class="solicitud-extra">
                                            <span class="precio">
                                                $<c:out value="${publicacion.precio}"/>
                                            </span>
                                            <span class="estado PENDIENTE">
                                                PENDIENTE
                                            </span>
                                        </div>
                                        <a href="${pageContext.request.contextPath}/detalle-solicitud-publicacion-admin?idPublicacion=${publicacion.idPublicacion}"
                                           class="btn-ver-solicitud">
                                            <i class="bi bi-eye me-2"></i>
                                            Ver detalles
                                        </a>
                                    </div>
                                </article>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>
</div>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/Notificacion.js"></script>
</body>
</html>