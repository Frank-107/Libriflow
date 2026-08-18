<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Publicar Libro - LibriFlow</title>

    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Publicar.css">
</head>

<body class="p-3 p-md-4">
<div id="contenedor-notificaciones"></div>
<div class="container-fluid max-width-xl mx-auto">

    <!-- HEADER -->
    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5">
        <div class="d-flex align-items-center gap-2 gap-md-3">
            <button class="btn text-white d-md-none p-0 border-0 me-1"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#sidebarMenu"
                    aria-expanded="false"
                    aria-controls="sidebarMenu">
                <i class="bi bi-list" style="font-size:2rem;"></i>
            </button>

            <a href="${pageContext.request.contextPath}/inicio"
               class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>

            <span class="fw-bold fs-4 tracking-wide">
                Publicar Libro
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
                           href="${pageContext.request.contextPath}/actualizar-perfil">
                            <i class="bi bi-person me-2"></i> Ver perfil
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-logout"
                           href="${pageContext.request.contextPath}/cerrar-sesion">
                            <i class="bi bi-box-arrow-right me-2"></i> Cerrar sesión
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </header>

    <div class="row g-4">
        <!-- SIDEBAR -->
        <aside class="col-12 col-md-4 col-lg-3">
            <div class="collapse d-md-block" id="sidebarMenu">
                <div class="bg-lf-dark p-4 rounded-lf-sidebar d-flex flex-column gap-3 shadow-sm mb-3 mb-md-0">
                    <a href="${pageContext.request.contextPath}/inicio"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-house me-3 fs-5"></i> Inicio
                    </a>

                    <a href="${pageContext.request.contextPath}/carrito"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-cart3 me-3 fs-5"></i> Carrito
                    </a>

                    <a href="${pageContext.request.contextPath}/mis-compras"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-bag-check me-3 fs-5"></i> Compras
                    </a>

                    <a href="${pageContext.request.contextPath}/publicar-libro-usuario"
                       class="btn bg-lf-capsule btn-lf-pill sidebar-active w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-pencil-square me-3 fs-5"></i> Publicar
                    </a>

                    <a href="${pageContext.request.contextPath}/mis-publicaciones-js"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-grid-3x3-gap me-3 fs-5"></i> Mis publicaciones
                    </a>

                    <a href="${pageContext.request.contextPath}/mis-rentas"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-journal-bookmark me-3 fs-5"></i> Mis rentas
                    </a>
                    <a href="https://www.instagram.com/libriflow.oficial?igsh=MW9qbmNld2M2ZXNyeA==" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-globe me-3 fs-5"></i> Nuestras redes
                    </a>
                </div>
            </div>
        </aside>

        <!-- FORMULARIO PRINCIPAL -->
        <main class="col-12 col-md-8 col-lg-9 catalogo-scroll">
            <div class="form-container-lf p-4 p-md-5 shadow-sm bg-white">
                <div class="d-flex justify-content-between align-items-start mb-4 flex-wrap gap-3">
                    <div>
                        <h4 class="fw-bold text-dark mb-1">Nueva Publicación</h4>
                        <p class="text-muted mb-0">Completa los datos para poner tu libro disponible en la plataforma.</p>
                    </div>

                    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflowF.png"
                         alt="LibriFlow"
                         class="logo-editar"
                         style="max-height: 45px;">
                </div>

                <c:if test="${not empty error}">
                    <div id="errorServidor" data-mensaje="<c:out value='${error}' escapeXml='true'/>" style="display:none;"></div>
                </c:if>

                <form action="publicar-libro-usuario" id="formPublicar" method="POST" enctype="multipart/form-data">

                    <div class="row">
                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Nombre del libro</label>
                            <input type="text" name="titulo" value="<c:out value='${param.titulo}' escapeXml='true'/>" class="form-control form-control-lf" maxlength="150" required>
                        </div>

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Autor</label>
                            <input type="text" name="autor" value="<c:out value='${param.autor}' escapeXml='true'/>" class="form-control form-control-lf" maxlength="100" required>
                        </div>

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Editorial</label>
                            <input type="text" name="editorial" value="<c:out value='${param.editorial}' escapeXml='true'/>" class="form-control form-control-lf" maxlength="100" required>
                        </div>

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Género</label>
                            <select name="genero" class="form-control form-control-lf" required>
                                <option value="" disabled ${empty param.genero ? 'selected' : ''}>Selecciona un género</option>
                                <option value="Novela" ${param.genero == 'Novela' ? 'selected' : ''}>Novela</option>
                                <option value="Fantasía" ${param.genero == 'Fantasía' ? 'selected' : ''}>Fantasía</option>
                                <option value="Ciencia ficción" ${param.genero == 'Ciencia ficción' ? 'selected' : ''}>Ciencia ficción</option>
                                <option value="Terror" ${param.genero == 'Terror' ? 'selected' : ''}>Terror</option>
                                <option value="Romance" ${param.genero == 'Romance' ? 'selected' : ''}>Romance</option>
                                <option value="Misterio" ${param.genero == 'Misterio' ? 'selected' : ''}>Misterio</option>
                                <option value="Suspenso" ${param.genero == 'Suspenso' ? 'selected' : ''}>Suspenso</option>
                                <option value="Drama" ${param.genero == 'Drama' ? 'selected' : ''}>Drama</option>
                                <option value="Aventura" ${param.genero == 'Aventura' ? 'selected' : ''}>Aventura</option>
                                <option value="Historia" ${param.genero == 'Historia' ? 'selected' : ''}>Historia</option>
                                <option value="Biografía" ${param.genero == 'Biografía' ? 'selected' : ''}>Biografía</option>
                                <option value="Autobiografía" ${param.genero == 'Autobiografía' ? 'selected' : ''}>Autobiografía</option>
                                <option value="Ciencia" ${param.genero == 'Ciencia' ? 'selected' : ''}>Ciencia</option>
                                <option value="Tecnología" ${param.genero == 'Tecnología' ? 'selected' : ''}>Tecnología</option>
                                <option value="Educación" ${param.genero == 'Educación' ? 'selected' : ''}>Educación</option>
                                <option value="Infantil" ${param.genero == 'Infantil' ? 'selected' : ''}>Infantil</option>
                                <option value="Poesía" ${param.genero == 'Poesía' ? 'selected' : ''}>Poesía</option>
                                <option value="Filosofía" ${param.genero == 'Filosofía' ? 'selected' : ''}>Filosofía</option>
                                <option value="Religión" ${param.genero == 'Religión' ? 'selected' : ''}>Religión</option>
                                <option value="Cómic" ${param.genero == 'Cómic' ? 'selected' : ''}>Cómic</option>
                                <option value="Otro" ${param.genero == 'Otro' ? 'selected' : ''}>Otro</option>
                            </select>
                        </div>

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Precio MXN</label>
                            <input type="number"
                                   step="0.01"
                                   min="0.01"
                                   max="99999.99"
                                   id="precio"
                                   name="precio"
                                   value="<c:out value='${param.precio}' escapeXml='true'/>"
                                   class="form-control form-control-lf"
                                   placeholder="$"
                                   oninput="actualizarPrecioUI()"
                                   required>
                            <small id="mensaje15" class="text-secondary fw-bold d-block mt-1"></small>
                        </div>

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Fotografías del libro</label>
                            <button type="button"
                                    class="btn btn-action-lf shadow-sm w-100"
                                    data-bs-toggle="modal"
                                    data-bs-target="#modalSubirImagenes">
                                <i class="bi bi-images me-2"></i>
                                Adjuntar imágenes (3)
                            </button>
                        </div>

                        <div class="col-12 mb-4">
                            <label class="form-label-lf">Sinopsis</label>
                            <textarea name="sinopsis"
                                      id="sinopsis"
                                      class="form-control form-control-lf"
                                      rows="5"
                                      maxlength="5000"
                                      style="resize:none;"
                                      oninput="validarSinopsis()"
                                      required><c:out value='${param.sinopsis}' escapeXml='true'/></textarea>
                        </div>
                    </div>

                    <div class="text-center mt-4 mb-3">
                        <button type="submit" class="btn btn-action-lf shadow-sm btn-submit px-5">
                            Publicar
                        </button>
                    </div>

                    <!-- MODAL DE ADJUNCIÓN DE IMÁGENES MODERNIZADO -->
                    <div class="modal fade" id="modalSubirImagenes" tabindex="-1" aria-labelledby="modalImagenesLabel" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered modal-xl">
                            <div class="modal-content border-0 shadow-lg" style="border-radius: 1.25rem; overflow: hidden;">

                                <!-- Modal Header -->
                                <div class="modal-header bg-lf-dark text-white px-4 py-3 border-0">
                                    <div class="d-flex align-items-center gap-2">
                                        <i class="bi bi-images fs-4 text-white"></i>
                                        <div>
                                            <h5 class="modal-title fw-bold mb-0" id="modalImagenesLabel">Cargar Fotografías del Libro</h5>
                                            <small class="text-white-50" style="font-size: 0.8rem;">Adjunta 3 fotografías claras (Máximo 2MB cada una - JPG/PNG/WEBP)</small>
                                        </div>
                                    </div>
                                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>

                                <!-- Modal Body -->
                                <div class="modal-body p-4 bg-light">
                                    <div class="row g-3">

                                        <!-- Tarjeta 1: Portada -->
                                        <div class="col-12 col-md-4">
                                            <div class="card h-100 border-0 shadow-sm rounded-4 p-3 text-center d-flex flex-column justify-content-between bg-white position-relative">
                                                <div>
                                                    <span class="badge bg-lf-dark text-white position-absolute top-0 start-0 m-3 rounded-pill px-3 py-2 fw-semibold">
                                                        1. Portada
                                                    </span>
                                                    <div class="mt-4 mb-2">
                                                        <div class="preview-box border border-2 border-dashed rounded-3 d-flex align-items-center justify-content-center bg-light overflow-hidden position-relative" style="height: 220px; border-color: #dee2e6 !important;">
                                                            <img id="vistapreviaImg1" class="w-100 h-100" style="object-fit: cover; display: none;" alt="Vista previa 1" />
                                                            <div id="placeholderImg1" class="text-muted p-3">
                                                                <i class="bi bi-journal-album fs-1 d-block mb-1 text-secondary"></i>
                                                                <span class="small fw-semibold d-block">Imagen Principal</span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="mt-2">
                                                    <label for="imagen1" class="btn btn-outline-dark btn-sm w-100 rounded-pill fw-semibold py-2">
                                                        <i class="bi bi-upload me-1"></i> Seleccionar
                                                    </label>
                                                    <input type="file"
                                                           id="imagen1"
                                                           name="imagen1"
                                                           class="d-none"
                                                           accept="image/jpeg,image/png,image/webp"
                                                           onchange="previewImagenConValidacion(this, 'vistapreviaImg1', 'placeholderImg1')">
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Tarjeta 2: Contraportada -->
                                        <div class="col-12 col-md-4">
                                            <div class="card h-100 border-0 shadow-sm rounded-4 p-3 text-center d-flex flex-column justify-content-between bg-white position-relative">
                                                <div>
                                                    <span class="badge bg-lf-dark text-white position-absolute top-0 start-0 m-3 rounded-pill px-3 py-2 fw-semibold">
                                                        2. Contraportada
                                                    </span>
                                                    <div class="mt-4 mb-2">
                                                        <div class="preview-box border border-2 border-dashed rounded-3 d-flex align-items-center justify-content-center bg-light overflow-hidden position-relative" style="height: 220px; border-color: #dee2e6 !important;">
                                                            <img id="vistapreviaImg2" class="w-100 h-100" style="object-fit: cover; display: none;" alt="Vista previa 2" />
                                                            <div id="placeholderImg2" class="text-muted p-3">
                                                                <i class="bi bi-book-half fs-1 d-block mb-1 text-secondary"></i>
                                                                <span class="small fw-semibold d-block">Reverso o Lomo</span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="mt-2">
                                                    <label for="imagen2" class="btn btn-outline-dark btn-sm w-100 rounded-pill fw-semibold py-2">
                                                        <i class="bi bi-upload me-1"></i> Seleccionar
                                                    </label>
                                                    <input type="file"
                                                           id="imagen2"
                                                           name="imagen2"
                                                           class="d-none"
                                                           accept="image/jpeg,image/png,image/webp"
                                                           onchange="previewImagenConValidacion(this, 'vistapreviaImg2', 'placeholderImg2')">
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Tarjeta 3: Páginas / Estado -->
                                        <div class="col-12 col-md-4">
                                            <div class="card h-100 border-0 shadow-sm rounded-4 p-3 text-center d-flex flex-column justify-content-between bg-white position-relative">
                                                <div>
                                                    <span class="badge bg-lf-dark text-white position-absolute top-0 start-0 m-3 rounded-pill px-3 py-2 fw-semibold">
                                                        3. Estado General
                                                    </span>
                                                    <div class="mt-4 mb-2">
                                                        <div class="preview-box border border-2 border-dashed rounded-3 d-flex align-items-center justify-content-center bg-light overflow-hidden position-relative" style="height: 220px; border-color: #dee2e6 !important;">
                                                            <img id="vistapreviaImg3" class="w-100 h-100" style="object-fit: cover; display: none;" alt="Vista previa 3" />
                                                            <div id="placeholderImg3" class="text-muted p-3">
                                                                <i class="bi bi-file-earmark-text fs-1 d-block mb-1 text-secondary"></i>
                                                                <span class="small fw-semibold d-block">Páginas o detalles</span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="mt-2">
                                                    <label for="imagen3" class="btn btn-outline-dark btn-sm w-100 rounded-pill fw-semibold py-2">
                                                        <i class="bi bi-upload me-1"></i> Seleccionar
                                                    </label>
                                                    <input type="file"
                                                           id="imagen3"
                                                           name="imagen3"
                                                           class="d-none"
                                                           accept="image/jpeg,image/png,image/webp"
                                                           onchange="previewImagenConValidacion(this, 'vistapreviaImg3', 'placeholderImg3')">
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </div>

                                <!-- Modal Footer -->
                                <div class="modal-footer bg-white border-0 px-4 py-3 justify-content-between">
                                    <small class="text-muted">
                                        <i class="bi bi-info-circle me-1"></i> Las 3 imágenes son requeridas para publicar.
                                    </small>
                                    <!-- Busca esta línea en el footer del modal -->
                                    <button type="button" class="btn btn-modal-guardar rounded-pill px-4 py-2 fw-semibold shadow-sm" data-bs-dismiss="modal">
                                        <i class="bi bi-check2 me-1"></i> Guardar y Continuar
                                    </button>
                                </div>

                            </div>
                        </div>
                    </div>

                </form>
            </div>
        </main>
    </div>
</div>

<script src="assets/js/bootstrap.js"></script>
<script src="assets/js/Notificacion.js"></script>
<script src="assets/js/PublicarLibroUsuario.js"></script>
<c:if test="${param.exito == 'true'}">
    <jsp:include page="ConfirmacionPublicacion.jsp" />
</c:if>

</body>
</html>