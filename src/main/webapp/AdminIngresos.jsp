<%--
  Vista: IngresosAdmin.jsp
  Descripción: Vista del módulo de administración para la consulta, filtrado y
               análisis del reporte general de ingresos, ventas y comisiones
               obtenidas por la plataforma, desglosados en periodos temporales.

  Acción / Servlet asociado: /ingresos-admin
  Atributos de Request utilizados:
    - "ingresos" (List<Ingreso>, opcional): Colección con los registros de compras, ganancia y origen de la publicación.
    - "sessionScope.usuario" (Usuario): Datos del usuario administrador autenticado en sesión.

  @author Fuentes Perez Francisco Emmanuel
  @since 22/08/2026
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reporte de Ingresos - LibriFlow</title>
    <!-- Estilos y recursos visuales del módulo de finanzas -->
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/LibriFlow.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Ingresos.css"/>
</head>
<body class="p-3 p-md-4">
<div class="container-fluid max-width-xl mx-auto">
    <%-- Encabezado principal: Identificación del usuario en sesión y controles de navegación --%>
    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5">
        <div class="d-flex align-items-center">
            <button class="btn text-white d-md-none p-0 border-0 me-1"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#menuLateral"
                    aria-expanded="false"
                    aria-controls="menuLateral">
                <i class="bi bi-list" style="font-size:2rem;"></i>
            </button>
            <a href="inicio-admin"
               class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>
            <span class="fw-bold fs-4 tracking-wide ms-2">
                Ingresos
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
        <%-- Menú lateral de navegación con accesos directos de administración --%>
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
                    <a href="usuarios-admin" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-people-fill me-3 fs-5"></i> Usuarios
                    </a>
                    <a href="ingresos-admin" class="btn bg-lf-capsule sidebar-active btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-cash-stack me-3 fs-5"></i> Ingresos
                    </a>
                </div>
            </div>
        </aside>

            <%-- Contenido principal: Métricas KPI y tabla de movimientos financieros --%>
        <main class="col-12 col-md-8 col-lg-9 pb-7">
            <div class="lf-ingresos-wrapper">

                <%-- Encabezado de la sección financiera --%>
                <div class="lf-ingresos-header">
                    <div>
                        <span class="lf-section-label">FINANZAS & PLATAFORMA</span>
                        <h2>Reporte de Ingresos</h2>
                        <p>Resumen general de ventas, comisiones y ganancias netas de LibriFlow.</p>
                    </div>
                </div>

                    <%-- Indicadores clave de rendimiento (KPIs) segmentados por periodo --%>
                <div class="lf-kpi-grid">
                    <!-- Hoy -->
                    <div class="lf-kpi-card">
                        <div class="lf-kpi-icon bg-hoy">
                            <i class="bi bi-calendar-day"></i>
                        </div>
                        <div class="lf-kpi-content">
                            <span class="lf-kpi-label">Ganancias de Hoy</span>
                            <h3 class="lf-kpi-value" id="kpi-hoy">$0.00</h3>
                            <small class="lf-kpi-sub" id="count-hoy">0 transacciones</small>
                        </div>
                    </div>

                    <!-- Ganancias de la semana en curso -->
                    <div class="lf-kpi-card">
                        <div class="lf-kpi-icon bg-semana">
                            <i class="bi bi-calendar-week"></i>
                        </div>
                        <div class="lf-kpi-content">
                            <span class="lf-kpi-label">Esta Semana</span>
                            <h3 class="lf-kpi-value" id="kpi-semana">$0.00</h3>
                            <small class="lf-kpi-sub" id="count-semana">0 transacciones</small>
                        </div>
                    </div>

                    <!-- Ganancias del mes actual -->
                    <div class="lf-kpi-card">
                        <div class="lf-kpi-icon bg-mes">
                            <i class="bi bi-calendar-month"></i>
                        </div>
                        <div class="lf-kpi-content">
                            <span class="lf-kpi-label">Este Mes</span>
                            <h3 class="lf-kpi-value" id="kpi-mes">$0.00</h3>
                            <small class="lf-kpi-sub" id="count-mes">0 transacciones</small>
                        </div>
                    </div>

                    <!-- Total histórico acumulado -->
                    <div class="lf-kpi-card lf-kpi-card-total">
                        <div class="lf-kpi-icon bg-total">
                            <i class="bi bi-wallet2"></i>
                        </div>
                        <div class="lf-kpi-content">
                            <span class="lf-kpi-label">Ganancias Totales</span>
                            <h3 class="lf-kpi-value text-success" id="kpi-total">$0.00</h3>
                            <small class="lf-kpi-sub" id="count-total">0 transacciones</small>
                        </div>
                    </div>
                </div>

                    <%-- Tabla interactiva de movimientos con filtros por rango de tiempo --%>
                <div class="lf-user-info-card lf-movements-card">
                    <!-- Pestañas de filtrado rápido por rango temporal -->
                    <div class="lf-period-tabs-bar">
                        <div class="lf-period-tabs">
                            <button type="button" class="lf-tab-btn active" data-period="total">
                                <i class="bi bi-grid-fill me-1"></i> Totales
                            </button>
                            <button type="button" class="lf-tab-btn" data-period="mes">
                                <i class="bi bi-calendar-month me-1"></i> Este Mes
                            </button>
                            <button type="button" class="lf-tab-btn" data-period="semana">
                                <i class="bi bi-calendar-week me-1"></i> Esta Semana
                            </button>
                            <button type="button" class="lf-tab-btn" data-period="hoy">
                                <i class="bi bi-calendar-day me-1"></i> Hoy
                            </button>
                        </div>

                        <div class="lf-tab-summary ms-auto mt-2 mt-md-0">
                            <span class="text-muted">Total acumulado vista: </span>
                            <strong class="text-success fs-5 ms-1" id="tab-sum-display">$0.00</strong>
                        </div>
                    </div>

                    <!-- Detalle de transacciones registradas -->
                    <div class="lf-table-wrapper">
                        <table class="lf-movements-table" id="tabla-ingresos">
                            <thead>
                            <tr>
                                <th>Comprador</th>
                                <th>Fecha y Hora</th>
                                <th>Libro Comprado</th>
                                <th>Total Venta</th>
                                <th>Ganancia LibriFlow</th>
                                <th>Origen</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${not empty ingresos}">
                                    <c:forEach var="item" items="${ingresos}">
                                        <tr class="lf-ingreso-row"
                                            data-time="${item.fecha.time}"
                                            data-ganancia="${item.ganaciaLibriflow}"
                                            data-total="${item.precio}">

                                            <!-- Comprador -->
                                            <td>
                                                <div class="d-flex align-items-center gap-2">
                                                    <div class="lf-avatar-sm">
                                                        <i class="bi bi-person"></i>
                                                    </div>
                                                    <span class="fw-semibold text-dark">
                                                        <c:out value="${item.comprador}" />
                                                    </span>
                                                </div>
                                            </td>

                                            <!-- Fecha y Hora -->
                                            <td>
                                                <span class="lf-table-date">
                                                    <fmt:formatDate value="${item.fecha}" pattern="dd/MM/yyyy HH:mm"/>
                                                </span>
                                            </td>

                                            <!-- Título del Libro -->
                                            <td>
                                                <span class="lf-book-title"><c:out value="${item.titulo}" /></span>
                                            </td>

                                            <!-- Monto Total de la Transacción -->
                                            <td>
                                                <span class="lf-price-total">
                                                    $<fmt:formatNumber value="${item.precio}" pattern="#,##0.00"/>
                                                </span>
                                            </td>

                                            <!-- Ganancia/Comisión obtenida por la plataforma -->
                                            <td>
                                                <span class="lf-gain-badge">
                                                    +$<fmt:formatNumber value="${item.ganaciaLibriflow}" pattern="#,##0.00"/>
                                                </span>
                                            </td>

                                            <!-- Procedencia de la publicación -->
                                            <td>
                                                <c:choose>
                                                    <c:when test="${item.esLibriFlow}">
                                                        <span class="lf-origin lf-origin-libriflow">
                                                            <i class="bi bi-book-half"></i> LibriFlow
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="lf-origin lf-origin-user">
                                                            <i class="bi bi-person-badge"></i> Usuario
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>

                                <c:otherwise>
                                    <tr id="row-empty-initial">
                                        <td colspan="6">
                                            <div class="lf-empty-movements">
                                                <i class="bi bi-cash-stack"></i>
                                                <strong>Sin ingresos registrados</strong>
                                                <span>Aún no se han generado transacciones en la plataforma.</span>
                                            </div>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>

                            <!-- Mensaje dinámico cuando la pestaña o filtro no arroja resultados -->
                            <tr id="row-empty-filter" style="display: none;">
                                <td colspan="6">
                                    <div class="lf-empty-movements">
                                        <i class="bi bi-funnel"></i>
                                        <strong>Sin movimientos en este periodo</strong>
                                        <span>No hay compras registradas para el periodo seleccionado.</span>
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
        </main>
    </div>
</div>
<!-- Lógica de cliente y manipulación dinámica de ingresos -->
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/Ingresos.js"></script>

</body>
</html>