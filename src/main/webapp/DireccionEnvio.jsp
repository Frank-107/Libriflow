<%--
    Esta vista permite al usuario capturar la dirección de envío necesaria
    para continuar con el proceso de compra. Solicita los datos del destinatario,
    domicilio, código postal, municipio y estado, además de mostrar mensajes
    de error cuando ocurre algún problema durante el proceso.

    @author Andres Gerardo Angelina Perez
    @since 24/08/2026
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dirección de Envío - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Publicar.css" />
</head>
<body class="p-3 p-md-4">

<div class="container-fluid max-width-xl mx-auto">

    <%--
        Esta sección contiene el encabezado de la vista de dirección de envío.
        Permite regresar al carrito y muestra el nombre y correo electrónico
        del usuario que está realizando la compra.

        @author Andres Gerardo Angelina Perez
        @since 24/08/2026
    --%>

    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5">
        <div class="d-flex align-items-center gap-2 gap-md-3">
            <a href="carrito" class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>
            <span class="fw-bold fs-4 tracking-wide">Dirección de Envío</span>
        </div>

        <div class="d-flex align-items-center gap-3">
            <div class="text-end d-none d-md-block">
                <div class="fw-bold mb-0" style="font-size: 0.95rem;"><c:out value="${usuario.getNombre()}" /></div>
                <small class="text-white-50" style="font-size: 0.8rem;"><c:out value="${usuario.getCorreo()}" /></small>
            </div>
            <div class="dropdown">
                <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm" style="width: 48px; height: 48px; cursor: pointer;" data-bs-toggle="dropdown">
                    <i class="bi bi-person-fill fs-4 text-dark"></i>
                </div>
            </div>
        </div>
    </header>

    <div class="row g-4">

        <%--
            Esta sección contiene el menú lateral de navegación del usuario.
            Permite acceder al inicio, carrito, compras, publicación de libros,
            publicaciones del usuario, rentas y redes sociales de LibriFlow.

            @author Andres Gerardo Angelina Perez
            @since 24/08/2026
        --%>

        <aside class="col-12 col-md-4 col-lg-3">
            <div class="bg-lf-dark p-4 rounded-lf-sidebar d-flex flex-column gap-3 shadow-sm">
                <a href="inicio-js" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-house me-3 fs-5"></i> Inicio</a>
                <a href="carrito" class="btn bg-lf-capsule btn-lf-pill sidebar-active w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-cart3 me-3 fs-5"></i> Carrito
                </a>
                <a href="compras" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-bag-check me-3 fs-5"></i> Compras</a>
                <a href="publicar-libro-usuario" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-pencil-square me-3 fs-5"></i> Publicar
                </a>
                <a href="mis-publicaciones-js" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-grid-3x3-gap me-3 fs-5"></i> Mis publicaciones
                </a>
                <a href="mis-rentas" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-journal-bookmark me-3 fs-5"></i> Mis rentas
                </a>
                <a href="https://www.instagram.com/libriflow.oficial?igsh=MW9qbmNld2M2ZXNyeA==" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-globe me-3 fs-5"></i> Nuestras redes
                </a>
            </div>
        </aside>

        <%--
            Esta sección contiene el formulario principal para capturar
            los datos necesarios para realizar el envío de la compra.

            @author Andres Gerardo Angelina Perez
            @since 24/08/2026
        --%>

        <main class="col-12 col-md-8 col-lg-9">
            <div class="form-container-lf p-4 p-md-5 shadow-sm bg-white">
                <div class="d-flex justify-content-between align-items-start mb-4">
                    <h4 class="fw-bold text-dark mb-1">Datos de Entrega</h4>
                    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png" alt="LibriFlow" style="height: 35px;">
                </div>

                <%--
                    Esta condición muestra una notificación cuando existe
                    un mensaje de error relacionado con el proceso de envío.

                    @author Andres Gerardo Angelina Perez
                    @since 24/08/2026
                --%>

                <c:if test="${not empty error}">
                    <div id="errorToast" class="libri-toast libri-toast-error">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                            <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"/>
                            <line x1="12" y1="9" x2="12" y2="13"/>
                            <line x1="12" y1="17" x2="12.01" y2="17"/>
                        </svg>
                        <span><c:out value="${error}" escapeXml="true" /></span>
                    </div>
                </c:if>

                <%--
                    Este formulario recopila los datos de la dirección de envío.
                    Cuando se envía, deshabilita el botón para evitar solicitudes
                    duplicadas mientras se procesa la información.

                    @author Andres Gerardo Angelina Perez
                    @since 24/08/2026
                --%>

                <form action="direccion-envio" method="POST" onsubmit="let btn=this.querySelector('.btn-submit'); btn.disabled=true; btn.innerHTML='Procesando...';">
                    <div class="row">

                        <%--
                            Esta sección solicita la información básica del
                            destinatario y del domicilio donde se entregará
                            la compra.

                            @author Andres Gerardo Angelina Perez
                            @since 24/08/2026
                        --%>

                        <div class="col-12 mb-4">
                            <label class="form-label-lf">Nombre completo de quien recibe</label>
                            <input type="text" name="destinatario" class="form-control form-control-lf" value="${param.destinatario}" required>
                        </div>

                        <div class="col-12 mb-4">
                            <label class="form-label-lf">Calle y Número (Exterior e Interior)</label>
                            <input type="text" name="calleNumero" class="form-control form-control-lf" value="${param.calleNumero}" required>
                        </div>
                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Colonia / Barrio</label>
                            <input type="text" name="colonia" class="form-control form-control-lf" value="${param.colonia}" required>
                        </div>

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Código Postal (C.P.)</label>
                            <input type="text" name="codigoPostal" maxlength="5" class="form-control form-control-lf" value="${param.codigoPostal}" required>
                        </div>
                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Municipio / Alcaldía</label>
                            <input type="text" name="municipio" class="form-control form-control-lf" value="${param.municipio}" required>
                        </div>

                        <%--
                            Este selector permite elegir el estado de la República
                            Mexicana correspondiente a la dirección de entrega.
                            Conserva el valor previamente seleccionado cuando
                            el formulario necesita mostrarse nuevamente.

                            @author Andres Gerardo Angelina Perez
                            @since 24/08/2026
                        --%>

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Estado</label>
                            <select name="estado" class="form-control form-control-lf" required>
                                <option value="" disabled ${empty param.estado ? 'selected' : ''}>Selecciona un estado</option>
                                <option value="Aguascalientes" ${param.estado == 'Aguascalientes' ? 'selected' : ''}>Aguascalientes</option>
                                <option value="Baja California" ${param.estado == 'Baja California' ? 'selected' : ''}>Baja California</option>
                                <option value="Baja California Sur" ${param.estado == 'Baja California Sur' ? 'selected' : ''}>Baja California Sur</option>
                                <option value="Campeche" ${param.estado == 'Campeche' ? 'selected' : ''}>Campeche</option>
                                <option value="Chiapas" ${param.estado == 'Chiapas' ? 'selected' : ''}>Chiapas</option>
                                <option value="Chihuahua" ${param.estado == 'Chihuahua' ? 'selected' : ''}>Chihuahua</option>
                                <option value="Ciudad de México" ${param.estado == 'Ciudad de México' ? 'selected' : ''}>Ciudad de México</option>
                                <option value="Coahuila" ${param.estado == 'Coahuila' ? 'selected' : ''}>Coahuila</option>
                                <option value="Colima" ${param.estado == 'Colima' ? 'selected' : ''}>Colima</option>
                                <option value="Durango" ${param.estado == 'Durango' ? 'selected' : ''}>Durango</option>
                                <option value="Estado de México" ${param.estado == 'Estado de México' ? 'selected' : ''}>Estado de México</option>
                                <option value="Guanajuato" ${param.estado == 'Guanajuato' ? 'selected' : ''}>Guanajuato</option>
                                <option value="Guerrero" ${param.estado == 'Guerrero' ? 'selected' : ''}>Guerrero</option>
                                <option value="Hidalgo" ${param.estado == 'Hidalgo' ? 'selected' : ''}>Hidalgo</option>
                                <option value="Jalisco" ${param.estado == 'Jalisco' ? 'selected' : ''}>Jalisco</option>
                                <option value="Michoacán" ${param.estado == 'Michoacán' ? 'selected' : ''}>Michoacán</option>
                                <option value="Morelos" ${param.estado == 'Morelos' ? 'selected' : ''}>Morelos</option>
                                <option value="Nayarit" ${param.estado == 'Nayarit' ? 'selected' : ''}>Nayarit</option>
                                <option value="Nuevo León" ${param.estado == 'Nuevo León' ? 'selected' : ''}>Nuevo León</option>
                                <option value="Oaxaca" ${param.estado == 'Oaxaca' ? 'selected' : ''}>Oaxaca</option>
                                <option value="Puebla" ${param.estado == 'Puebla' ? 'selected' : ''}>Puebla</option>
                                <option value="Querétaro" ${param.estado == 'Querétaro' ? 'selected' : ''}>Querétaro</option>
                                <option value="Quintana Roo" ${param.estado == 'Quintana Roo' ? 'selected' : ''}>Quintana Roo</option>
                                <option value="San Luis Potosí" ${param.estado == 'San Luis Potosí' ? 'selected' : ''}>San Luis Potosí</option>
                                <option value="Sinaloa" ${param.estado == 'Sinaloa' ? 'selected' : ''}>Sinaloa</option>
                                <option value="Sonora" ${param.estado == 'Sonora' ? 'selected' : ''}>Sonora</option>
                                <option value="Tabasco" ${param.estado == 'Tabasco' ? 'selected' : ''}>Tabasco</option>
                                <option value="Tamaulipas" ${param.estado == 'Tamaulipas' ? 'selected' : ''}>Tamaulipas</option>
                                <option value="Tlaxcala" ${param.estado == 'Tlaxcala' ? 'selected' : ''}>Tlaxcala</option>
                                <option value="Veracruz" ${param.estado == 'Veracruz' ? 'selected' : ''}>Veracruz</option>
                                <option value="Yucatán" ${param.estado == 'Yucatán' ? 'selected' : ''}>Yucatán</option>
                                <option value="Zacatecas" ${param.estado == 'Zacatecas' ? 'selected' : ''}>Zacatecas</option>
                            </select>
                        </div>
                    </div>

                    <%--
                        Este botón envía los datos de entrega capturados y permite
                        continuar con la siguiente etapa del proceso de pago.

                        @author Andres Gerardo Angelina Perez
                        @since 24/08/2026
                    --%>

                    <div class="text-center mt-4">
                        <button type="submit" class="btn btn-action-lf shadow-sm btn-submit px-5">Continuar al Pago</button>
                    </div>
                </form>
            </div>
        </main>
    </div>
</div>

<%--
    Estos scripts cargan las funciones de Bootstrap y el archivo utilizado
    para mostrar las notificaciones correspondientes dentro de la vista.

    @author Andres Gerardo Angelina Perez
    @since 24/08/2026
--%>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/Notificacion.js"></script>
</body>
</html>