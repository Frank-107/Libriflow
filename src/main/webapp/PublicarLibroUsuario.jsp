<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Publicar Libro - LibriFlow</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
    <link class="icon" href="${pageContext.request.contextPath}/assets/css/bootstrap.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Publicar.css" />
</head>
<body class="p-3 p-md-4">

<div class="container-fluid max-width-xl mx-auto">

    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5">
        <div class="d-flex align-items-center gap-2 gap-md-3">
            <button class="btn text-white d-md-none p-0 border-0 me-1" type="button" data-bs-toggle="collapse" data-bs-target="#sidebarMenu" aria-expanded="false" aria-controls="sidebarMenu">
                <i class="bi bi-list" style="font-size: 2rem;"></i>
            </button>

            <a href="inicio" class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>
            <span class="fw-bold fs-4 tracking-wide">Publicar</span>
        </div>

        <div class="d-flex align-items-center gap-3">
            <div class="text-end d-none d-md-block">
                <div class="fw-bold mb-0" style="font-size: 0.95rem;">${usuario.getNombre()}</div>
                <small class="text-white-50" style="font-size: 0.8rem;">${usuario.getCorreo()}</small>
            </div>

            <div class="dropdown">
                <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm" style="width: 48px; height: 48px; cursor: pointer;" data-bs-toggle="dropdown" aria-expanded="false">
                    <i class="bi bi-person-fill fs-4 text-dark"></i>
                </div>

                <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 dropdown-menu-lf">
                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-item" href="actualizar-perfil">
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
                    <a href="inicio" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-house me-3 fs-5"></i> Inicio
                    </a>
                    <a href="carrito" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-cart3 me-3 fs-5"></i> Carrito
                    </a>
                    <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-bag-check me-3 fs-5"></i> Compras
                    </a>
                    <a href="publicar-libro-usuario" class="btn bg-lf-capsule btn-lf-pill sidebar-active w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-pencil-square me-3 fs-5"></i> Publicar
                    </a>
                    <a href="mis-publicaciones" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-grid-3x3-gap me-3 fs-5"></i> Mis publicaciones
                    </a>
                    <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-journal-bookmark me-3 fs-5"></i> Mis rentas
                    </a>
                    <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-globe me-3 fs-5"></i> Nuestras redes
                    </a>
                </div>
            </div>
        </aside>

        <main class="col-12 col-md-8 col-lg-9">
            <div class="form-container-lf p-4 p-md-5 shadow-sm bg-white">

                <div class="d-flex justify-content-between align-items-start mb-4 flex-wrap gap-3">
                    <div>
                        <h4 class="fw-bold text-dark mb-1">Nueva Publicacion</h4>
                    </div>
                    <div class="text-end">
                        <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" alt="LibriFlow" style="height: 35px; width: auto;">
                    </div>
                </div>
                <c:if test="${not empty mensaje}">
                    <div class="libri-toast libri-toast-success">
                        <i class="bi bi-check-circle-fill fs-5"></i>
                        <span><c:out value="${mensaje}" escapeXml="true" /></span>
                    </div>
                    <c:remove var="mensaje" scope="session"/>
                </c:if>

                <form action="publicar-libro-usuario" id="formPublicar" method="POST" enctype="multipart/form-data"
                      onsubmit="let btn=this.querySelector('.btn-submit'); btn.disabled=true; btn.innerHTML='Enviando...';">

                    <div class="row">

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Nombre del libro</label>
                            <input type="text" name="titulo" class="form-control form-control-lf" required>
                        </div>

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Autor</label>
                            <input type="text" name="autor" class="form-control form-control-lf" required>
                        </div>

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Editorial</label>
                            <input type="text" name="editorial" class="form-control form-control-lf" required>
                        </div>

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Género</label>

                            <select name="genero" class="form-control form-control-lf" required>
                                <option value="" disabled selected>Selecciona un género</option>
                                <option value="Novela">Novela</option>
                                <option value="Fantasía">Fantasía</option>
                                <option value="Ciencia ficción">Ciencia ficción</option>
                                <option value="Terror">Terror</option>
                                <option value="Romance">Romance</option>
                                <option value="Misterio">Misterio</option>
                                <option value="Suspenso">Suspenso</option>
                                <option value="Drama">Drama</option>
                                <option value="Aventura">Aventura</option>
                                <option value="Historia">Historia</option>
                                <option value="Biografía">Biografía</option>
                                <option value="Autobiografía">Autobiografía</option>
                                <option value="Ciencia">Ciencia</option>
                                <option value="Tecnología">Tecnología</option>
                                <option value="Educación">Educación</option>
                                <option value="Infantil">Infantil</option>
                                <option value="Poesía">Poesía</option>
                                <option value="Filosofía">Filosofía</option>
                                <option value="Religión">Religión</option>
                                <option value="Cómic">Cómic</option>
                                <option value="Otro">Otro</option>
                            </select>
                        </div>

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Precio MXN</label>

                            <input type="number"
                                   step="0.01"
                                   name="precio"
                                   class="form-control form-control-lf"
                                   placeholder="$"
                                   required>
                        </div>

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Imágenes del libro</label>

                            <button type="button"
                                    class="btn btn-action-lf shadow-sm w-100"
                                    data-bs-toggle="modal"
                                    data-bs-target="#modalSubirImagenes">

                                <i class="bi bi-images me-2"></i>
                                Subir 3 imágenes
                            </button>
                        </div>

                        <div class="col-12 mb-4">
                            <label class="form-label-lf">Sipnosis</label>

                            <textarea name="sinopsis"
                                      class="form-control form-control-lf"
                                      rows="5"
                                      style="resize:none;"
                                      required></textarea>
                        </div>

                    </div>

                    <div class="text-center mt-4 mb-3">
                        <button type="submit" class="btn btn-action-lf shadow-sm btn-submit px-5">
                            Publicar
                        </button>
                    </div>

                    <div class="modal fade" id="modalSubirImagenes" tabindex="-1" aria-labelledby="modalImagenesLabel" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered modal-lg">
                            <div class="modal-content" style="border-radius: 15px; overflow: hidden;">
                                <div class="modal-header bg-lf-dark text-white p-3">
                                    <h5 class="modal-title fw-bold" id="modalImagenesLabel">
                                        <i class="bi bi-cloud-arrow-up-fill me-2"></i> Cargar Imágenes Requeridas
                                    </h5>
                                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>

                                <div class="modal-body p-4 bg-light">
                                    <div class="row">

                                        <div class="col-12 mb-4">
                                            <label class="form-label-lf small fw-bold mb-2">
                                                1. Portada (Principal)
                                            </label>
                                            <input type="file" id="imagen1" name="imagen1" class="form-control form-control-lf p-2.5">
                                        </div>

                                        <div class="col-12 mb-4">
                                            <label class="form-label-lf small fw-bold mb-2">
                                                2. Reverso / Contraportada
                                            </label>
                                            <input type="file" id="imagen2" name="imagen2" class="form-control form-control-lf p-2.5">
                                        </div>

                                        <div class="col-12 mb-2">
                                            <label class="form-label-lf small fw-bold mb-2">
                                                3. Estado general / Páginas
                                            </label>
                                            <input type="file" id="imagen3" name="imagen3" class="form-control form-control-lf p-2.5">
                                        </div>

                                    </div>
                                </div>

                                <div class="modal-footer bg-white p-2">
                                    <button type="button" class="btn btn-action-lf shadow-sm px-4" data-bs-dismiss="modal">
                                        Listo
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
<script src="assets/js/Publicar.js"></script>
<script src="assets/js/Notificacion.js"></script>
</body>
</html>
