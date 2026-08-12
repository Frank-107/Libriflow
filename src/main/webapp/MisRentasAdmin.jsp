<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rentas Activas - LibriFlow</title>

    <link rel="icon"
          href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png"
          type="image/png">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>

    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/MisPublicaciones.css"/>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/Inicio.css"/>

    <style>
        .renta-card {
            background: #CBC2B9;
            border-radius: 22px;
            padding: 18px 22px;
            margin-bottom: 18px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.06);
            transition: box-shadow 0.2s ease, transform 0.2s ease;
        }

        .renta-card:hover {
            box-shadow: 0 8px 22px rgba(0,0,0,0.10);
            transform: translateY(-2px);
        }

        .renta-card .publicacion-contenido {
            display: flex;
            align-items: center;
            gap: 18px;
        }

        .renta-card .publicacion-portada img {
            width: 64px;
            height: 90px;
            object-fit: cover;
            border-radius: 12px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.15);
        }

        .renta-card .publicacion-info h4 {
            font-weight: 700;
            color: #4A4641;
            margin-bottom: 2px;
            font-size: 1.05rem;
        }

        .renta-card .publicacion-info p {
            margin-bottom: 4px;
            color: #7A746E;
            font-size: 0.9rem;
        }

        .renta-card .codigo-renta {
            font-size: 0.75rem;
            color: #A39B93;
            font-weight: 600;
            letter-spacing: 0.5px;
            text-transform: uppercase;
        }

        .renta-card .fechas-renta {
            font-size: 0.82rem;
            color: #7A746E;
        }

        .renta-card .publicacion-precio {
            font-weight: 700;
            font-size: 1.1rem;
            color: #4A4641;
            white-space: nowrap;
        }

        .badge-estado {
            font-size: 0.72rem;
            font-weight: 700;
            letter-spacing: 0.5px;
            padding: 5px 14px;
            border-radius: 20px;
            text-transform: uppercase;
        }

        .badge-programada {
            background-color: #E9E4DD;
            color: #7A746E;
        }

        .badge-activa {
            background-color: #DCEFE1;
            color: #2E7D4F;
        }

        .badge-atrasada {
            background-color: #FCE8CC;
            color: #B4700A;
        }

        .badge-muy-atrasada {
            background-color: #FADBD8;
            color: #C0392B;
        }

        .btn-entregar-disabled {
            background-color: #E4DED5 !important;
            color: #A39B93 !important;
            border: none;
            border-radius: 20px;
            cursor: not-allowed;
            padding: 8px 18px;
        }

        .btn-accion-renta {
            background-color: #4A4641;
            color: #ffffff;
            border: none;
            border-radius: 20px;
            padding: 8px 18px;
            font-weight: 500;
            font-size: 0.85rem;
            transition: all 0.2s ease;
        }

        .btn-accion-renta:hover {
            background-color: #7A746E;
            color: #ffffff;
        }

        .disponible-desde {
            font-size: 0.75rem;
            color: #A39B93;
            margin-top: 4px;
        }
    </style>

</head>

<body class="p-3 p-md-4">

<div class="container-fluid max-width-xl mx-auto">

    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm
               d-flex justify-content-between align-items-center px-4 px-md-5">

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
               class="text-white text-decoration-none fs-4 btn-lf-pill p-2
                  d-inline-flex align-items-center justify-content-center">

                <i class="bi bi-arrow-left"></i>

            </a>

            <span class="fw-bold fs-4 tracking-wide">
                Rentas Activas
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

                <div class="bg-lf-capsule rounded-circle d-flex align-items-center
                        justify-content-center shadow-sm"
                     style="width:45px;height:45px;cursor:pointer;"
                     data-bs-toggle="dropdown"
                     aria-expanded="false">

                    <i class="bi bi-person-fill fs-4 text-dark"></i>

                </div>

                <ul class="dropdown-menu dropdown-menu-end shadow-lg
                       border-0 dropdown-menu-lf">

                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-item"
                           href="ActualizarPerfilAdmin.jsp">

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

            <div class="collapse d-md-block" id="menuLateral">

                <div class="bg-lf-dark p-4 rounded-lf-sidebar d-flex flex-column
                        gap-3 shadow-sm mb-3 mb-md-0">

                    <a href="inicio-admin"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5
                          text-start d-flex align-items-center px-4">

                        <i class="bi bi-house me-3 fs-5"></i>
                        Inicio

                    </a>

                    <a href="solicitud-publicacion-admin"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5
                          text-start d-flex align-items-center px-4">

                        <i class="bi bi-file-earmark-text me-3 fs-5"></i>
                        Solicitud de publicación

                    </a>

                    <a href="publicar-libro-admin"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5
                          text-start d-flex align-items-center px-4">

                        <i class="bi bi-file-earmark-text me-3 fs-5"></i>
                        Publicar

                    </a>

                    <a href="mis-rentas-admin"
                       class="btn bg-lf-capsule sidebar-active btn-lf-pill
                          w-100 py-2.5 text-start d-flex align-items-center px-4">

                        <i class="bi bi-book-half me-3 fs-5"></i>
                        Rentas activas

                    </a>

                    <a href="usuarios-admin"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5
                          text-start d-flex align-items-center px-4">

                        <i class="bi bi-people-fill me-3 fs-5"></i>
                        Usuarios

                    </a>

                    <a href="ingresos-admin"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5
                          text-start d-flex align-items-center px-4">

                        <i class="bi bi-cash-stack me-3 fs-5"></i>
                        Ingresos

                    </a>

                </div>

            </div>

        </aside>


        <main class="col catalogo-scroll">

            <section class="mis-publicaciones-container">

                <div class="mis-publicaciones-header">

                    <h2>
                        Rentas (${rentas.size()})
                    </h2>

                </div>


                <c:choose>

                    <c:when test="${empty rentas}">

                        <div class="text-center bg-white rounded-4 shadow-sm p-5 mx-auto"
                             style="max-width: 480px;">

                            <div class="bg-lf-capsule rounded-circle d-inline-flex
                                    align-items-center justify-content-center mb-4"
                                 style="width: 90px; height: 90px;">

                                <i class="bi bi-journal-bookmark"
                                   style="font-size: 2.5rem; color: #4A4641;">
                                </i>

                            </div>

                            <h4 class="fw-bold mb-2" style="color: #4A4641;">
                                No hay rentas registradas
                            </h4>

                            <p class="text-muted mb-0">
                                Cuando un usuario rente un libro, aparecerá aquí
                                para que puedas darle seguimiento.
                            </p>

                        </div>

                    </c:when>

                    <c:otherwise>

                        <div class="publicaciones-lista">

                            <c:forEach var="renta" items="${rentas}">

                                <c:if test="${renta.estado != 'FINALIZADA'}">

                                    <div class="renta-card">

                                        <div class="d-flex justify-content-between align-items-start mb-2">

                                        <span class="codigo-renta">
                                            Código #${renta.idDetalle}
                                        </span>

                                            <span class="badge-estado
                                            ${renta.estado == 'PROGRAMADA' ? 'badge-programada' : ''}
                                            ${renta.estado == 'ACTIVA' ? 'badge-activa' : ''}
                                            ${renta.estado == 'ATRASADA' ? 'badge-atrasada' : ''}
                                            ${renta.estado == 'MUY ATRASADA' ? 'badge-muy-atrasada' : ''}">
                                                    ${renta.estado}
                                            </span>

                                        </div>

                                        <div class="publicacion-contenido">

                                            <div class="publicacion-portada">

                                                <img src="${renta.imagenPrincipal}"
                                                     alt="${renta.titulo}">

                                            </div>


                                            <div class="publicacion-info flex-grow-1">

                                                <h4>
                                                        ${renta.titulo}
                                                </h4>

                                                <p class="mb-1">
                                                        ${renta.autor}
                                                </p>

                                                <p class="mb-1">
                                                    Rentado por:
                                                    <strong>${renta.nombreComprador}</strong>
                                                </p>

                                                <div class="fechas-renta">

                                                    Del ${renta.fechaInicio}
                                                    al ${renta.fechaLimite}

                                                    <c:if test="${not empty renta.fechaDevolucion}">
                                                        <br>
                                                        Devuelto: ${renta.fechaDevolucion}
                                                    </c:if>

                                                </div>

                                            </div>


                                            <div class="publicacion-precio">
                                                $${renta.precio}
                                            </div>


                                            <div class="publicacion-acciones">

                                                <c:choose>

                                                    <c:when test="${renta.estado == 'PROGRAMADA'}">

                                                        <c:choose>

                                                            <c:when test="${renta.puedeEntregar}">

                                                                <form action="${pageContext.request.contextPath}/mis-rentas-admin"
                                                                      method="post">

                                                                    <input type="hidden"
                                                                           name="idDetalle"
                                                                           value="${renta.idDetalle}">

                                                                    <input type="hidden"
                                                                           name="accion"
                                                                           value="ENTREGAR">

                                                                    <button type="submit"
                                                                            class="btn btn-accion-renta">

                                                                        <i class="bi bi-box-arrow-in-right me-1"></i>
                                                                        Marcar como entregado

                                                                    </button>

                                                                </form>

                                                            </c:when>

                                                            <c:otherwise>

                                                                <button type="button"
                                                                        class="btn btn-entregar-disabled"
                                                                        disabled>

                                                                    <i class="bi bi-lock me-1"></i>
                                                                    Marcar como entregado

                                                                </button>

                                                                <div class="disponible-desde">
                                                                    Disponible a partir del ${renta.fechaInicio}
                                                                </div>

                                                            </c:otherwise>

                                                        </c:choose>

                                                    </c:when>

                                                    <c:when test="${renta.estado == 'ACTIVA' || renta.estado == 'ATRASADA' || renta.estado == 'MUY ATRASADA'}">

                                                        <form action="${pageContext.request.contextPath}/mis-rentas-admin"
                                                              method="post">

                                                            <input type="hidden"
                                                                   name="idDetalle"
                                                                   value="${renta.idDetalle}">

                                                            <input type="hidden"
                                                                   name="accion"
                                                                   value="DEVOLVER">

                                                            <button type="submit"
                                                                    class="btn btn-accion-renta">

                                                                <i class="bi bi-check-circle me-1"></i>
                                                                Marcar devolución

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

                    </c:otherwise>

                </c:choose>

            </section>

        </main>

    </div>

</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.js"></script>

</body>
</html>
