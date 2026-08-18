<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Revisar publicación - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>

    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/LibriFlow.css"/>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/DetalleLibro.css"/>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/DetalleSolicitudPublicacion.css?v=3"/>
</head>

<body class="p-3 p-md-4 detalle-body">

<div class="container-fluid max-width-xl mx-auto h-100 d-flex flex-column pb-2 detalle-layout">

    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5 flex-shrink-0 detalle-header">

        <div class="d-flex align-items-center gap-2 gap-md-3">

            <button class="btn text-white d-md-none p-0 border-0 me-1"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#menuLateral"
                    aria-expanded="false"
                    aria-controls="menuLateral">
                <i class="bi bi-list" style="font-size:2rem;"></i>
            </button>

            <a href="${pageContext.request.contextPath}/solicitud-publicacion-admin"
               class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>

            <span class="fw-bold fs-4 tracking-wide">
                Revisar publicación
            </span>

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
                     style="width:48px;height:48px;cursor:pointer;"
                     data-bs-toggle="dropdown"
                     aria-expanded="false">

                    <i class="bi bi-person-fill fs-4 text-dark"></i>

                </div>

                <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 dropdown-menu-lf">

                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-item"
                           href="${pageContext.request.contextPath}/ActualizarPerfilAdmin.jsp">
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

    <div class="row g-4 flex-grow-1 overflow-hidden detalle-content-row">

        <aside class="col-12 col-md-4 col-lg-3 h-100 detalle-sidebar">

            <div class="collapse d-md-block h-100" id="menuLateral">

                <div class="bg-lf-dark p-4 rounded-lf-sidebar d-flex flex-column gap-3 shadow-sm mb-3 mb-md-0 h-100">

                    <a href="${pageContext.request.contextPath}/inicio-admin"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-house me-3 fs-5"></i>
                        Inicio
                    </a>

                    <a href="${pageContext.request.contextPath}/solicitud-publicacion-admin"
                       class="btn bg-lf-capsule sidebar-active btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-file-earmark-text me-3 fs-5"></i>
                        Solicitud de publicación
                    </a>

                    <a href="${pageContext.request.contextPath}/publicar-libro-admin"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-pencil-square me-3 fs-5"></i>
                        Publicar
                    </a>

                    <a href="${pageContext.request.contextPath}/mis-rentas-admin"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-book-half me-3 fs-5"></i>
                        Rentas activas
                    </a>

                    <a href="${pageContext.request.contextPath}/usuarios-admin"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-people-fill me-3 fs-5"></i>
                        Usuarios
                    </a>

                    <a href="${pageContext.request.contextPath}/ingresos-admin"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-cash-stack me-3 fs-5"></i>
                        Ingresos
                    </a>

                </div>

            </div>

        </aside>

        <main class="col-12 col-md-8 col-lg-9 h-100 overflow-hidden d-flex flex-column position-relative detalle-main">

            <div class="row g-4 flex-grow-1 h-100 detalle-inner-row">

                <div class="col-12 col-lg-6 detalle-left-scroll h-100">

                    <div class="d-flex flex-column align-items-center">

                        <div class="portada-principal-container mb-3 shadow-sm">
                            <img src="${publicacion.imagenPrincipal}"
                                 alt="Portada de ${publicacion.titulo}"
                                 class="img-fluid rounded-4">
                        </div>

                        <div class="d-flex gap-3 mb-3 w-100 justify-content-center"
                             style="max-width:360px;">

                            <div class="miniatura-admin">
                                <span>Reverso</span>

                                <div class="miniatura-container shadow-sm">
                                    <img src="${publicacion.imagenReverso}"
                                         alt="Reverso"
                                         class="img-fluid rounded-3">
                                </div>
                            </div>

                            <div class="miniatura-admin">
                                <span>Interior</span>

                                <div class="miniatura-container shadow-sm">
                                    <img src="${publicacion.imagenInterior}"
                                         alt="Interior"
                                         class="img-fluid rounded-3">
                                </div>
                            </div>

                        </div>

                        <span class="badge estado-pendiente-admin rounded-pill px-3 py-2">
                            <i class="bi bi-clock-history me-1"></i>
                            Pendiente de revisión
                        </span>

                    </div>

                </div>

                <div class="col-12 col-lg-6 d-flex flex-column gap-3 detalle-right-scroll h-100">

                    <div class="pill-info-lf shadow-sm flex-shrink-0">
                        <span class="fw-bold">Título:</span>
                        <c:out value="${publicacion.titulo}"/>
                    </div>

                    <div class="pill-info-lf shadow-sm flex-shrink-0">
                        <span class="fw-bold">Autor:</span>
                        <c:out value="${publicacion.autor}"/>
                    </div>

                    <div class="pill-info-lf shadow-sm flex-shrink-0">
                        <span class="fw-bold">Editorial:</span>
                        <c:out value="${publicacion.editorial}"/>
                    </div>

                    <div class="pill-info-lf shadow-sm flex-shrink-0">
                        <span class="fw-bold">Género:</span>
                        <c:out value="${publicacion.genero}"/>
                    </div>

                    <div class="pill-info-lf shadow-sm flex-shrink-0">
                        <span class="fw-bold">Precio:</span>
                        $<c:out value="${publicacion.precio}"/> MXN
                    </div>

                    <div class="box-sinopsis-lf shadow-sm pe-2 flex-shrink-0">
                        <h5 class="fw-bold mb-3">
                            Sinopsis:
                        </h5>

                        <p class="mb-0 text-muted lh-base">
                            <c:out value="${publicacion.sinopsis}"/>
                        </p>
                    </div>

                    <div class="revision-admin-box flex-shrink-0">

                        <div class="revision-admin-label">
                            <i class="bi bi-shield-check"></i>

                            <div>
                                <strong>Decisión del administrador</strong>
                                <small>
                                    Revisa los datos e imágenes antes de continuar.
                                </small>
                            </div>
                        </div>

                        <div class="revision-admin-botones">

                            <button type="button"
                                    class="btn-rechazar-admin"
                                    data-bs-toggle="modal"
                                    data-bs-target="#modalRechazar">

                                <i class="bi bi-x-circle me-2"></i>
                                Rechazar
                            </button>

                            <button type="button"
                                    class="btn-aprobar-admin"
                                    data-bs-toggle="modal"
                                    data-bs-target="#modalAprobar">

                                <i class="bi bi-check-circle me-2"></i>
                                Aprobar
                            </button>

                        </div>

                    </div>

                </div>

            </div>

        </main>

    </div>

</div>

<div class="modal fade"
     id="modalAprobar"
     tabindex="-1"
     aria-labelledby="modalAprobarLabel"
     aria-hidden="true">

    <div class="modal-dialog modal-dialog-centered">

        <div class="modal-content modal-decision-admin">

            <div class="modal-body p-4 text-center">

                <div class="modal-icono-admin aprobar">
                    <i class="bi bi-check-lg"></i>
                </div>

                <h4 class="fw-bold mt-3"
                    id="modalAprobarLabel">
                    ¿Aprobar publicación?
                </h4>

                <p class="text-muted">
                    <strong>
                        <c:out value="${publicacion.titulo}"/>
                    </strong>
                    quedará activa y disponible en el catálogo.
                </p>

                <div class="d-flex gap-2 mt-4">

                    <button type="button"
                            class="btn btn-secondary rounded-pill flex-grow-1"
                            data-bs-dismiss="modal">
                        Cancelar
                    </button>

                    <form action="${pageContext.request.contextPath}/solicitud-publicacion-admin"
                          method="POST"
                          class="flex-grow-1">

                        <input type="hidden"
                               name="idPublicacion"
                               value="${publicacion.idPublicacion}">

                        <input type="hidden"
                               name="accion"
                               value="aprobar">

                        <button type="submit"
                                class="btn-confirmar-aprobar-admin">
                            Sí, aprobar
                        </button>

                    </form>

                </div>

            </div>

        </div>

    </div>

</div>

<div class="modal fade"
     id="modalRechazar"
     tabindex="-1"
     aria-labelledby="modalRechazarLabel"
     aria-hidden="true">

    <div class="modal-dialog modal-dialog-centered">

        <div class="modal-content modal-decision-admin">

            <div class="modal-body p-4 text-center">

                <div class="modal-icono-admin rechazar">
                    <i class="bi bi-x-lg"></i>
                </div>

                <h4 class="fw-bold mt-3"
                    id="modalRechazarLabel">
                    ¿Rechazar publicación?
                </h4>

                <p class="text-muted">
                    La publicación
                    <strong>
                        <c:out value="${publicacion.titulo}"/>
                    </strong>
                    dejará de aparecer en solicitudes pendientes.
                </p>

                <div class="d-flex gap-2 mt-4">

                    <button type="button"
                            class="btn btn-secondary rounded-pill flex-grow-1"
                            data-bs-dismiss="modal">
                        Cancelar
                    </button>

                    <form action="${pageContext.request.contextPath}/solicitud-publicacion-admin"
                          method="POST"
                          class="flex-grow-1">

                        <input type="hidden"
                               name="idPublicacion"
                               value="${publicacion.idPublicacion}">

                        <input type="hidden"
                               name="accion"
                               value="rechazar">

                        <button type="submit"
                                class="btn-confirmar-rechazar-admin">
                            Sí, rechazar
                        </button>

                    </form>

                </div>

            </div>

        </div>

    </div>

</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
</body>
</html>