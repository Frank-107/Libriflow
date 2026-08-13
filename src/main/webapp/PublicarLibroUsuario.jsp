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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Inicio.css" />
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
                <div class="fw-bold mb-0" style="font-size: 0.95rem;"><c:out value="${usuario.getNombre()}" /></div>
                <small class="text-white-50" style="font-size: 0.8rem;"><c:out value="${usuario.getCorreo()}" /></small>
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
                    <a href="mis-compras" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-bag-check me-3 fs-5"></i> Compras
                    </a>
                    <a href="publicar-libro-usuario" class="btn bg-lf-capsule btn-lf-pill sidebar-active w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-pencil-square me-3 fs-5"></i> Publicar
                    </a>
                    <a href="mis-publicaciones-js" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
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

        <main class="col-12 col-md-8 col-lg-9 catalogo-scroll">
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

                <c:if test="${not empty error}">
                    <div class="alert alert-danger d-flex align-items-center gap-2 mb-4" role="alert">
                        <i class="bi bi-exclamation-triangle-fill fs-5"></i>
                        <div><c:out value="${error}" escapeXml="true" /></div>
                    </div>
                </c:if>

                <form action="publicar-libro-usuario" id="formPublicar" method="POST" enctype="multipart/form-data">

                    <div class="row">

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Nombre del libro</label>
                            <input type="text" name="titulo" value="${param.titulo}" class="form-control form-control-lf" required>
                        </div>

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Autor</label>
                            <input type="text" name="autor" value="${param.autor}" class="form-control form-control-lf" required>
                        </div>

                        <div class="col-12 col-md-6 mb-4">
                            <label class="form-label-lf">Editorial</label>
                            <input type="text" name="editorial" value="${param.editorial}" class="form-control form-control-lf" required>
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
                                   id="precio"
                                   name="precio"
                                   value="${param.precio}"
                                   class="form-control form-control-lf"
                                   placeholder="$"
                                   oninput="actualizarPrecioUI()"
                                   required>

                            <small id="mensaje15" class="text-secondary fw-bold d-block mt-1"></small>
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

                        <!-- MODIFICACION SINOPSIS INICIO: Se dejó una sola Sinopsis limpia sin etiquetas de texto abajo -->
                        <div class="col-12 mb-4">
                            <label class="form-label-lf">Sinopsis</label>

                            <textarea name="sinopsis"
                                      id="sinopsis"
                                      class="form-control form-control-lf"
                                      rows="5"
                                      style="resize:none;"
                                      oninput="validarSinopsis()"
                                      required>${param.sinopsis}</textarea>
                        </div>
                        <!-- MODIFICACION SINOPSIS FIN -->

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

<!-- MODIFICACION SCRIPT INICIO: Lógica de validaciones nativas para precio y sinopsis -->
<script>
    function contarPalabras(texto) {
        if (!texto) return 0;
        const palabras = texto.trim().split(/\s+/);
        return palabras.filter(p => p.length > 0).length;
    }

    function validarSinopsis() {
        const inputSinopsis = document.getElementById('sinopsis');
        if (!inputSinopsis) return;

        const numPalabras = contarPalabras(inputSinopsis.value);

        if (numPalabras < 100) {
            const faltantes = 100 - numPalabras;
            inputSinopsis.setCustomValidity("La sinopsis debe tener al menos 100 palabras.");
        } else {
            inputSinopsis.setCustomValidity("");
        }
    }

    function actualizarPrecioUI() {
        const inputPrecio = document.getElementById('precio');
        const mensaje15 = document.getElementById('mensaje15');
        if (!inputPrecio) return;

        const valor = parseFloat(inputPrecio.value);

        inputPrecio.setCustomValidity("");

        if (!isNaN(valor) && valor > 0) {
            const comision = (valor * 0.15).toFixed(2);
            const ganancia = (valor * 0.85).toFixed(2);
            if (mensaje15) {
                mensaje15.textContent = "Se aplicará el 15% de comisión ($" + comision + "). Recibirás: $" + ganancia + " MXN";
            }
        } else {
            if (mensaje15) mensaje15.textContent = "";
        }
    }

    document.addEventListener("DOMContentLoaded", function () {
        const form = document.getElementById('formPublicar');
        const inputPrecio = document.getElementById('precio');

        actualizarPrecioUI();
        validarSinopsis();

        if (form) {
            form.addEventListener('submit', function (e) {
                const valorPrecio = parseFloat(inputPrecio.value);

                if (isNaN(valorPrecio) || valorPrecio <= 0) {
                    inputPrecio.setCustomValidity("El precio debe ser mayor a $0 MXN.");
                } else {
                    inputPrecio.setCustomValidity("");
                }

                validarSinopsis();

                if (!form.checkValidity()) {
                    e.preventDefault();
                    e.stopPropagation();
                    form.reportValidity();
                }
            });
        }
    });
</script>

</body>
</html>