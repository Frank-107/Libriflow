<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Publicar Libro - LibriFlow</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Publicar.css" />
</head>
<body class="p-3 p-md-4">

<div class="container-fluid max-width-xl mx-auto">

    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5">
        <div class="d-flex align-items-center gap-3">
            <a href="Inicio.jsp" class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>
            <span class="fw-bold fs-4 tracking-wide">Publicar</span>
        </div>

        <div class="d-flex align-items-center gap-3">
            <div class="text-end d-none d-md-block">
                <div class="fw-bold mb-0" style="font-size: 0.95rem;">Usuario</div>
                <small class="text-white-50" style="font-size: 0.8rem;">usuario@gmail.com</small>
            </div>
            <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center" style="width: 48px; height: 48px;">
                <i class="bi bi-person-fill fs-4 text-dark"></i>
            </div>
        </div>
    </header>

    <div class="row g-4">

        <aside class="col-12 col-md-4 col-lg-3">
            <div class="bg-lf-dark p-4 rounded-lf-sidebar d-flex flex-column gap-3 shadow-sm">
                <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-cart3 me-3 fs-5"></i> Carrito
                </a>
                <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-bag-check me-3 fs-5"></i> Compras
                </a>
                <a href="Publicar.jsp" class="btn bg-lf-capsule btn-lf-pill sidebar-active w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-pencil-square me-3 fs-5"></i> Publicar
                </a>
                <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-grid-3x3-gap me-3 fs-5"></i> Mis publicaciones
                </a>
                <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-journal-bookmark me-3 fs-5"></i> Mis rentas
                </a>
                <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                    <i class="bi bi-globe me-3 fs-5"></i> Nuestras redes
                </a>
            </div>
        </aside>

        <main class="col-12 col-md-8 col-lg-9">
            <div class="form-container-lf p-4 p-md-5 shadow-sm bg-white">

                <div class="d-flex justify-content-between align-items-start mb-4 flex-wrap gap-3">
                    <div>
                        <h4 class="fw-bold text-dark mb-1">Nueva Publicacion</h4>
                    </div>
                    <div class="text-end">
                        <img src="${pageContext.request.contextPath}/img/LogoLibriflow.png" alt="LibriFlow" style="height: 35px; width: auto;">
                    </div>
                </div>

                <form action="PublicarLibroSv" method="POST" enctype="multipart/form-data">

                    <div class="mb-4">
                        <label class="form-label-lf">Nombre del libro</label>
                        <% if (request.getAttribute("errorNombre") != null) { %>
                        <span class="error-texto-lf"><i class="bi bi-exclamation-circle-fill"></i> No es posible ese nombre</span>
                        <% } %>
                        <input type="text" name="nombreLibro" class="form-control form-control-lf w-100" required>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-4">
                            <label class="form-label-lf">Autor</label>
                            <% if (request.getAttribute("errorAutor") != null) { %>
                            <span class="error-texto-lf"><i class="bi bi-exclamation-circle-fill"></i> Autor requerido o inválido</span>
                            <% } %>
                            <input type="text" name="autor" class="form-control form-control-lf" required>
                        </div>
                        <div class="col-md-6 mb-4">
                            <label class="form-label-lf">Editorial</label>
                            <% if (request.getAttribute("errorEditorial") != null) { %>
                            <span class="error-texto-lf"><i class="bi bi-exclamation-circle-fill"></i> Editorial requerida</span>
                            <% } %>
                            <input type="text" name="editorial" class="form-control form-control-lf" required>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label class="form-label-lf">Sinopsis</label>
                        <% if (request.getAttribute("errorSinopsis") != null) { %>
                        <span class="error-texto-lf"><i class="bi bi-exclamation-circle-fill"></i> La sinopsis es muy corta o vacía</span>
                        <% } %>
                        <textarea name="sinopsis" class="form-control form-control-lf rows-3" style="resize: none;" required></textarea>
                    </div>

                    <div class="row align-items-end">
                        <div class="col-md-6 mb-4">
                            <label class="form-label-lf">Precio MXN</label>
                            <% if (request.getAttribute("errorPrecio") != null) { %>
                            <span class="error-texto-lf"><i class="bi bi-exclamation-circle-fill"></i> Ingresa un precio válido</span>
                            <% } %>
                            <input type="number" step="0.01" name="precio" class="form-control form-control-lf" placeholder="$" required>
                        </div>
                        <div class="col-md-6 mb-4">
                            <label class="form-label-lf">Imágenes (3 necesarias)</label>
                            <% if (request.getAttribute("errorImagenes") != null) { %>
                            <span class="error-texto-lf"><i class="bi bi-exclamation-circle-fill"></i> Debes subir las 3 imágenes</span>
                            <% } %>
                            <div class="input-group">
                                <input type="file" name="imagenes" class="form-control form-control-lf" id="inputGroupFile" multiple required>
                            </div>
                        </div>
                    </div>

                    <div class="d-flex justify-content-center gap-3 mt-4 flex-wrap">
                        <button type="submit" class="btn btn-action-lf shadow-sm">Publicar</button>
                        <button type="button" class="btn btn-preview-lf shadow-sm">Vista Previa</button>
                    </div>

                </form>

            </div>
        </main>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/dist/umd/bootstrap.bundle.min.js"></script>
</body>
</html>