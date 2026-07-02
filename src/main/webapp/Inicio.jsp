<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inicio - LibriFlow</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <style>

        body {
            background-color: #F4EFEA;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }


        .bg-lf-dark {
            background-color: #4A4641;
        }
        .bg-lf-capsule {
            background-color: #A39B93;
            color: #2D2A26 !important;
            border: none;
            font-weight: 500;
        }
        .bg-lf-card {
            background-color: #CBC2B9;
            border-radius: 25px !important;
        }


        .rounded-lf-header {
            border-radius: 35px;
        }
        .rounded-lf-sidebar {
            border-radius: 40px;
            min-height: 80vh;
        }
        .btn-lf-pill {
            border-radius: 20px;
            transition: all 0.2s ease;
        }
        .btn-lf-pill:hover {
            background-color: #8C847C;
            color: #fff !important;
        }
        .search-bar-lf {
            border-radius: 30px;
            background-color: #FFFFFF;
            border: none;
            padding-left: 50px;
            height: 55px;
            font-size: 1.1rem;
        }
        .search-icon-inside {
            position: absolute;
            left: 20px;
            top: 50%;
            transform: translateY(-50%);
            font-size: 1.2rem;
            color: #7A746E;
            z-index: 5;
        }
        .btn-details-lf {
            background-color: #E6DFD7;
            color: #4A4641;
            border-radius: 20px;
            font-weight: 500;
            border: none;
            padding: 6px 20px;
        }
        .btn-details-lf:hover {
            background-color: #4A4641;
            color: #FFF;
        }
        .book-img-container {
            background-color: #F4EFEA;
            border-radius: 20px;
            padding: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 180px;
        }
    </style>
</head>
<body class="p-3 p-md-4">

<div class="container-fluid max-width-xl mx-auto">

    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5">
        <div class="d-flex align-items-center mx-auto mx-md-0">
            <div class="text-center text-md-start">
                <div class="fw-bold tracking-widest fs-4">[LOGO] LIBRIFLOW</div>
                <small style="font-size: 0.65rem; letter-spacing: 2px; color: #CBC2B9; display: block; text-align: center;">TU BIBLIOTECA DIGITAL</small>
            </div>
        </div>

        <div class="d-none d-md-flex align-items-center gap-3">
            <div class="text-end">
                <div class="fw-bold mb-0" style="font-size: 0.95rem;">Usuario</div>
                <small class="text-white-50" style="font-size: 0.8rem;">usuario@gmail.com</small>
            </div>
            <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm" style="width: 48px; height: 48px;">
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
                <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
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
            <div class="position-relative mb-4">
                <i class="bi bi-search search-icon-inside"></i>
                <input type="text" class="form-control search-bar-lf shadow-sm" placeholder="Buscar">
            </div>

            <div class="row g-4">

                <div class="col-12">
                    <div class="p-5 text-center rounded-lf-header text-secondary bg-white shadow-sm border border-2 border-dashed">
                        <h4 class="fw-bold text-dark">Poner libros</h4>
                        <p class="mb-0">Aqui se pondran todos los libros.</p>
                    </div>
                </div>

            </div>
        </main>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/dist/umd/bootstrap.bundle.min.js"></script>
</body>
</html>