#  LibriFlow — Plataforma Web de Gestión, Venta y Renta de Libros

![Java](https://img.shields.io/badge/Java-JakartaEE-orange?style=for-the-badge&logo=openjdk)
![Tomcat](https://img.shields.io/badge/Apache%20Tomcat-10.1-yellow?style=for-the-badge&logo=apachetomcat)
![Maven](https://img.shields.io/badge/Build-Apache%20Maven-red?style=for-the-badge&logo=apachemaven)
![Oracle](https://img.shields.io/badge/Database-Oracle%20DB-red?style=for-the-badge&logo=oracle)
![Bootstrap](https://img.shields.io/badge/Frontend-Bootstrap%205-purple?style=for-the-badge&logo=bootstrap)

---

## Miembros del Equipo de Desarrollo

| Nombre Completo | Matrícula |                 Usuario de GitHub                  |
| :--- | :---: |:--------------------------------------------------:|
| **Francisco Emmanuel Fuentes Pérez** | `20253ds107` |    [@frank-107](https://github.com/) *(Owner)*     |
| **Andrés Gerardo Angelina Pérez** | `20253ds094` | [@AndrecitoFlow](https://github.com/AndrecitoFlow) |
| **Irvin Abarca Arenas** | `20253ds103` | [@irvincitoflow](https://github.com/irvincitoflow) |
| **Alejandro Mena Pereyda** | `20243ds113` |     [@alemena22](https://github.com/alemena22)     |
| **Santiago** | `20253ds099` |     [@Sant04535](https://github.com/Sant04535)     |
| **Monserrath Anzurez Visoso** | `20253ds100` | [@20253ds100-ux](https://github.com/20253ds100-ux) |

---

##  Descripción del Proyecto

**LibriFlow** es un sistema web integral de comercio y gestión bibliográfica diseñado para facilitar la compra, renta física y publicación colaborativa de libros.

El proyecto resuelve la necesidad de centralizar la administración de inventarios físicos y la interacción con los usuarios mediante las siguientes funcionalidades principales:

1. **Gestión de Cuentas y Seguridad:** Registro de nuevos clientes con verificación de identidad mediante código OTP enviado a su correo electrónico (`ValidarCorreoCC.jsp`) y cifrado de contraseñas mediante algoritmos **SHA-256**.
2. **Catálogo y Publicaciones:** Catálogo digital responsivo para la exploración de libros, junto con un módulo para que los usuarios envíen solicitudes para publicar sus propios libros.
3. **Carrito y Pasarela de Pago:** Proceso interactivo de adición de ejemplares y checkout con un módulo de validación bancaria para tarjetas de crédito/débito (`ValidarTarjeta.jsp`).
4. **Control de Rentas y Penalizaciones:** Monitoreo transparente de ejemplares físicos en préstamo, fechas límite de devolución y cálculo automático de penalizaciones o bloqueos de cuenta por entregas tardías.
5. **Panel de Administración:** Control total sobre el inventario, solicitudes de publicación, historial de ingresos y gestión/bloqueo de cuentas de usuarios (`usuarios-admin.jsp`).
6. **Manejo de Errores Personalizados:** Pantallas amigables de error `404` (Página no encontrada) y `500` (Error de servidor) totalmente integradas con el descriptor de despliegue `web.xml`.

---

##  Explicación de la Estructura del Código

El proyecto sigue el patrón de arquitectura **MVC (Modelo-Vista-Controlador)** utilizando el estándar **Jakarta EE** y la construcción con **Apache Maven**.

```text
LibriFlow/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── mx/
        │       └── edu/
        │           └── utez/
        │               └── libriflow/
        │                   ├── controller/     # CONTROLADORES (Jakarta Servlets)
        │                   ├── model/          # MODELO Y PERSISTENCIA (POJOs y DAOs)
        │                   │   └── Dao/
        │                   └── utils/          # UTILIDADES Y CONEXIONES (JDBC)
        └── webapp/                             # VISTAS Y RECURSOS ESTÁTICOS
            ├── 404.jsp
            ├── 500.jsp
            ├── assets/
            │   ├── css/
            │   ├── js/
            │   └── img/
            ├── WEB-INF/
            │   └── web.xml
            ├── usuarios-admin.jsp
            ├── ValidarCorreoCC.jsp
            └── ValidarTarjeta.jsp