# 🛒 E-commerce REST API - Prueba Técnica Backend

API REST desarrollada con **Spring Boot**, **Java 21** y **PostgreSQL** para un sistema de E-commerce, implementando arquitectura limpia, seguridad con JWT y roles, validaciones y documentación interactiva.

---

## 🚀 Tecnologías Utilizadas

| Tecnología | Versión |
|------------|---------|
| **Java** | 21[span_1](start_span)[span_1](end_span) |
| **Spring Boot** | 4.1.1[span_2](start_span)[span_2](end_span) |
| **Gradle** | Gestor de dependencias[span_3](start_span)[span_3](end_span) |
| **PostgreSQL** | Base de datos relacional[span_4](start_span)[span_4](end_span) |
| **Spring Data JPA / Hibernate** | ORM y persistencia[span_5](start_span)[span_5](end_span) |
| **Spring Security & JWT** | Autenticación y autorización por roles[span_6](start_span)[span_6](end_span) |
| **Springdoc OpenAPI (Swagger)** | Documentación interactiva de la API[span_7](start_span)[span_7](end_span) |

---

## 📂 Arquitectura del Proyecto

El proyecto está organizado en una arquitectura en capas limpia y modular:

```text
src/main/java/com/example/examen/
│
├── Controller   # Endpoints REST (Auth, Categories, Products)
├── Model        # Entidades JPA (User, Category, Product, Order, OrderItem)
├── Repository   # Interfaces de persistencia (JpaRepository y JpaSpecificationExecutor)
├── Service      # Lógica de negocio de la aplicación
├── security     # Configuración de Spring Security, Filtro JWT y Utilitarios
└── specification# Lógica de filtros dinámicos para productos


⚙️ Instrucciones de Instalación y Ejecución
1. Clonar el repositorio
Bash
git clone <https://github.com/luisgomezs/wposs>
cd examen

2. Endpoints de Swagger y Documentación de la APILa aplicación cuenta con Springdoc OpenAPI (Swagger) integrado, permitiendo probar de forma visual e interactiva todos los endpoints de la API.  
 Una vez que la aplicación esté en ejecución, puedes acceder a la interfaz gráfica en el navegador:
 👉 http://localhost:8080/swagger-ui/index.html
 Resumen de Endpoints Disponibles:
Autenticación y Autorización (/api/auth) 
POST /api/auth/register - Registro de nuevos usuarios (Soporta ROLE_USER y ROLE_ADMIN). 
POST /api/auth/login - Autenticación de usuario con retorno de Token JWT.
  Categorías 
 (/api/categories)   GET /api/categories - Listado público de todas las categorías.  
POST /api/categories - Creación de una categoría (Ruta protegida, requiere rol ROLE_ADMIN).   
 DELETE /api/categories/{id} - Eliminación de una categoría (Ruta protegida, requiere rol ROLE_ADMIN).
Productos
(/api/products)   GET /api/products - Listado público con filtros opcionales (name, categoryId, minPrice, maxPrice) y paginación. 
GET /api/products/{id} - Obtener el detalle de un producto por su ID.  
 POST /api/products - Creación de un producto (Ruta protegida, requiere rol ROLE_ADMIN).
 DELETE /api/products/{id} - Eliminación de un producto (Ruta protegida, requiere rol ROLE_ADMIN). 
                  
Pasos para probar endpoints protegidos en Swagger:
Realiza una petición POST en /api/auth/register enviando un JSON con "role": "ROLE_ADMIN". 
  Haz login en POST /api/auth/login con ese mismo usuario para obtener el token JWT en la respuesta.   
  En la esquina superior derecha de la interfaz de Swagger, haz clic en el botón "Authorize", pega el token generado y confirma. 
¡A partir de ese momento podrás ejecutar las operaciones de creación y eliminación!
3.Decisiones de Diseño Tomadas
 Arquitectura en Capas:
 Se estructuró el proyecto separando responsabilidades de manera estricta (Controller → Service → Repository), facilitando la mantenibilidad, el orden y la escalabilidad del código.
 Uso de DTOs y Entidades Independientes:
Se estructuró el modelo de datos relacional con JPA y Hibernate[cite: 1], previniendo problemas de referencias circulares con anotaciones de control JSON y manteniendo un acoplamiento limpio.
 Filtros Dinámicos con Specifications:
Para cumplir con el requerimiento de búsqueda avanzada de productos (por nombre, categoría y rangos de precios)[cite: 1], se implementó JpaSpecificationExecutor, lo que permite construir consultas SQL dinámicas basadas en los parámetros opcionales enviados por el cliente de forma eficiente.
 Seguridad Stateless con JWT y Roles:
Se configuró Spring Security para que la arquitectura no mantenga estado de sesión (stateless)[cite: 1]. Los accesos de lectura son públicos para agilizar las consultas del e-commerce, mientras que las operaciones críticas de administración están estrictamente protegidas mediante la validación de tokens Bearer y control de roles (ROLE_USER y ROLE_ADMIN)[cite: 1].

3.decisiones tomadas
1. Arquitectura en Capas (Clean Architecture)
¿Qué se hizo? Se separó la aplicación en paquetes estrictos: Controller (manejo HTTP), Service (lógica de negocio), Repository (persistencia) y Model (entidades de base de datos).
Por qué se tomó esta decisión: Para garantizar la separación de responsabilidades. Esto hace que el código sea mucho más fácil de mantener, probar, escalar y depurar ante cualquier error, evitando mezclar consultas de base de datos con reglas de negocio o respuestas web.
2. Uso de JpaSpecificationExecutor para Filtros Dinámicos
¿Qué se hizo? En lugar de crear múltiples métodos fijos en el repositorio para buscar productos, se implementó JpaSpecificationExecutor junto con la clase ProductSpecification.
Por qué se tomó esta decisión: Permite construir consultas SQL dinámicas y condicionales en tiempo de ejecución. Si el cliente envía un filtro (por ejemplo, solo el nombre o solo un rango de precios), la aplicación construye la consulta de manera limpia y eficiente combinando los parámetros sin necesidad de escribir código repetitivo.
3. Seguridad sin Estado (Stateless) con JWT y BCrypt
¿Qué se hizo? Se configuró Spring Security implementando autenticación basada en JWT (JSON Web Tokens) y cifrado de contraseñas con BCrypt.
Por qué se tomó esta decisión:
Stateless: Al ser una API REST orientada a servicios, el servidor no guarda sesiones de usuario en memoria; cada petición es independiente y viaja firmada de forma segura mediante el Token Bearer.
BCrypt: Asegura que las contraseñas nunca se guarden en texto plano en la base de datos PostgreSQL, protegiendo la información sensible de los usuarios.
Control por Roles: Se permitió el acceso público a las consultas de productos y categorías para mejorar la experiencia del cliente, mientras que las operaciones de modificación (POST, DELETE) se blindaron estrictamente para administradores (ROLE_ADMIN).
4. Tipos de Datos y Prevención de Bucles (BigDecimal y JSON Ignore)
¿Qué se hizo? Se utilizó la clase BigDecimal para los precios monetarios de los productos y la anotación @JsonIgnore en las relaciones cruzadas de las entidades.
Por qué se tomó esta decisión:
BigDecimal evita los conocidos errores de redondeo y precisión matemática propios de los tipos de datos flotantes (float o double) cuando se trabaja con dinero.
@JsonIgnore previene errores de bucles infinitos (infinite recursion) al serializar objetos relacionados a JSON (por ejemplo, cuando una categoría lista sus productos y cada producto vuelve a apuntar a la categoría). 
```mermaid
erDiagram
    Category ||--o{ Product : "contiene"
    User ||--o{ Order : "realiza"
    Order ||--|{ OrderItem : "incluye"
    Product ||--o{ OrderItem : "se_encuentra_en"

    Category {
        Long id PK
        String name
        String description
    }

    Product {
        Long id PK
        String name
        String description
        BigDecimal price
        Integer stock
        Long category_id FK
    }

    User {
        Long id PK
        String email
        String password
        String role
    }

    Order {
        Long id PK
        LocalDateTime orderDate
        String status
        Long user_id FK
    }

    OrderItem {
        Long id PK
        Integer quantity
        BigDecimal price
        Long order_id FK
        Long product_id FK
    }
