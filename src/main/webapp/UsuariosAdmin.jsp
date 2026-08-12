<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Usuarios Admin - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
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


        <!-- MAIN CONTENT -->
        <main class="col catalogo-scroll">
            <section class="mis-publicaciones-container">
                <div class="mis-publicaciones-header">
                    <h2>Lista de usuarios (<c:out value="${usuarios.size()}" />)</h2>
                </div>

                <c:choose>
                    <c:when test="${empty usuarios}">
                        <div class="sin-publicaciones">
                            <i class="bi bi-people fs-1"></i>
                            <h4>No hay usuarios registrados.</h4>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive bg-white rounded-4 p-3 shadow-sm border">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light">
                                <tr>
                                    <th>ID</th>
                                    <th>Nombre completo</th>
                                    <th>Correo electrónico</th>
                                    <th>Teléfono</th>
                                    <th>Estado</th>
                                    <th class="text-center">Acciones</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="u" items="${usuarios}">

                                    <c:if test="${u.correo != 'adminLibri@utez.edu.mx' && u.id != sessionScope.usuario.id}">
                                        <tr>
                                            <td class="fw-bold">#<c:out value="${u.id}" /></td>
                                            <td><c:out value="${u.nombre}" /> <c:out value="${u.apellidoPaterno}" /> <c:out value="${u.apellidoMaterno}" /></td>
                                            <td><c:out value="${u.correo}" /></td>
                                            <td><c:out value="${empty u.telefono ? 'N/A' : u.telefono}" /></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${u.estado == 'ACTIVA' || u.estado == 'ACTIVO'}">
                                                        <span class="badge rounded-pill bg-success px-3 py-2">ACTIVA</span>
                                                    </c:when>
                                                    <c:otherwise>
                        <span class="badge rounded-pill bg-danger px-3 py-2">
                            <c:out value="${empty u.estado ? 'BLOQUEADO' : u.estado}" />
                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-center">
                                                <a href="detalle-usuario-admin?idUsuario=${u.id}" class="btn bg-lf-capsule btn-lf-pill text-dark btn-sm px-3 shadow-sm">
                                                    <i class="bi bi-eye me-1"></i> Ver detalles
                                                </a>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                                </tbody>
                            </table>
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