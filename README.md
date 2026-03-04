# 💊 FarmaApp — Backend API REST

> Sistema de gestión de farmacia desarrollado con **Spring Boot 3**, autenticación **JWT** y base de datos relacional. Diseñado con arquitectura en capas, seguridad robusta y buenas prácticas de desarrollo.

---

## 🚀 Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17+ | Lenguaje principal |
| Spring Boot | 3.x | Framework base |
| Spring Security | 6.x | Autenticación y autorización |
| Spring Data JPA | 3.x | Persistencia y ORM |
| Hibernate | 6.x | Mapeo objeto-relacional |
| JWT (jjwt) | 0.12.x | Tokens de autenticación |
| Lombok | Latest | Reducción de boilerplate |
| PostgreSQL / MySQL | — | Base de datos relacional |
| Maven | 3.x | Gestión de dependencias |

---

## 📐 Arquitectura

El proyecto sigue una arquitectura en capas estándar:

```
com.app.farma
├── config/          # Configuración de seguridad (SecurityConfig)
├── controllers/     # Capa de presentación — endpoints REST
├── services/        # Lógica de negocio
├── repositories/    # Acceso a datos (Spring Data JPA)
├── entities/        # Modelos JPA / tablas de BD
├── security/        # JWT, UserDetails, filtros
└── filters/         # JwtAuthenticationFilter
```

---

## 🗂️ Modelo de datos

### Diagrama de entidades

```
┌─────────────┐       ┌─────────────┐       ┌──────────────┐
│  categories │       │  products   │       │    sales     │
│─────────────│       │─────────────│       │──────────────│
│ id (UUID)   │──────<│ id (UUID)   │──────<│ id (UUID)    │
│ name        │       │ name        │       │ quantity     │
└─────────────┘       │ price       │       │ unit_price   │
                      │ lab         │       │ total_price  │
                      │ quantity    │       │ sold_at      │
                      │ active      │       │ product_id   │
                      │ expiration  │       └──────────────┘
                      │ category_id │
                      └─────────────┘

┌─────────────┐       ┌─────────────┐       ┌──────────────┐
│    users    │       │  role_user  │       │    roles     │
│─────────────│       │─────────────│       │──────────────│
│ id (UUID)   │──────<│ user_id     │>──────│ id (UUID)    │
│ name        │       │ role_id     │       │ name         │
│ last_name   │       └─────────────┘       └──────────────┘
│ email       │
│ password    │
│ active      │
└─────────────┘
```

### Relaciones implementadas

- `Category` → `Product`: **OneToMany** (una categoría tiene muchos productos)
- `Product` → `Sale`: **OneToMany** (un producto puede tener muchas ventas)
- `User` ↔ `Role`: **ManyToMany** con tabla intermedia `role_user`

---

## 🔐 Seguridad

El sistema implementa autenticación **stateless** con JWT:

1. El cliente hace `POST /api/auth/login` con email y contraseña
2. El servidor valida credenciales y retorna un **JWT firmado**
3. En cada request, el filtro `JwtAuthenticationFilter` intercepta el token
4. Se valida la firma, expiración y el usuario cargado desde BD
5. Si es válido, se establece el contexto de seguridad con sus **roles**

```
Request
  │
  ▼
JwtAuthenticationFilter
  │  extrae y valida el token
  ▼
CustomUserDetailsService
  │  carga el usuario desde BD por email
  ▼
SecurityContextHolder
  │  establece la autenticación
  ▼
Controller (acceso permitido)
```

### Roles y autorización

Los roles se mapean automáticamente con el prefijo `ROLE_`:

```java
// En UserCustomDetail.java
.map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
```

Se puede proteger endpoints por rol usando `@PreAuthorize`:

```java
@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable UUID id) { ... }
```

---

## 📦 Endpoints principales

### Autenticación

| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| `POST` | `/api/auth/register` | Registrar usuario | ❌ |
| `POST` | `/api/auth/login` | Iniciar sesión, obtener JWT | ❌ |

### Productos

| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| `GET` | `/api/products` | Listar todos los productos | ✅ |
| `GET` | `/api/products/{id}` | Obtener producto por ID | ✅ |
| `POST` | `/api/products` | Crear producto | ✅ |
| `PUT` | `/api/products/{id}` | Actualizar producto | ✅ |
| `DELETE` | `/api/products/{id}` | Eliminar producto | ✅ |

### Ventas

| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| `GET` | `/api/sales` | Listar ventas con producto | ✅ |
| `POST` | `/api/sales/{productId}` | Registrar venta | ✅ |

### Categorías

| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| `GET` | `/api/categories` | Listar categorías | ✅ |
| `POST` | `/api/categories` | Crear categoría | ✅ |

---

## ⚙️ Lógica de negocio — Ventas

Al registrar una venta el sistema:

1. Verifica que el producto **exista** y esté **activo**
2. Valida que haya **stock suficiente**
3. **Descuenta la cantidad** del inventario automáticamente
4. Guarda el **precio unitario como snapshot** (aunque el precio del producto cambie en el futuro, el historial es inmutable)

---

## 📋 Ejemplos de Request / Response

### 🔑 Auth — Registro

**POST** `/api/auth/register`

```json
{
  "name": "Carlos",
  "lastname": "Mendoza",
  "email": "carlos@farma.com",
  "password": "segura123"
}
```

### 🔑 Auth — Login

**POST** `/api/auth/login`

```json
{
  "email": "carlos@farma.com",
  "password": "segura123"
}
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "carlos@farma.com",
  "name": "Carlos"
}
```

---

### 📦 Productos — Crear producto

**POST** `/api/products`
> Header: `Authorization: Bearer <token>`

```json
{
  "name": "Paracetamol 500mg",
  "price": 5.00,
  "quantity": 100,
  "lab": "Laboratorio Genfar",
  "expirationDate": "2026-12-31",
  "categoryId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

**Response:**

```json
{
  "id": "f1e2d3c4-b5a6-7890-abcd-ef0987654321",
  "name": "Paracetamol 500mg",
  "price": 5.00,
  "quantity": 100,
  "lab": "Laboratorio Genfar",
  "expirationDate": "2026-12-31",
  "active": true,
  "createdAt": "2025-03-04T10:30:00",
  "updatedAt": "2025-03-04T10:30:00",
  "categoryId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "categoryName": "Analgésicos"
}
```

---

### 🏷️ Categorías — Crear categoría

**POST** `/api/categories`
> Header: `Authorization: Bearer <token>`

```json
{
  "name": "Analgésicos"
}
```

---

### 🛒 Ventas — Registrar venta

**POST** `/api/sales/{productId}?quantity=3`
> Header: `Authorization: Bearer <token>`

```json
{
  "quantity": 2
}
```

**Response:**

```json
{
  "id": "c9d8e7f6-a5b4-3210-dcba-fe9876543210",
  "quantity": 3,
  "unitPrice": 5.00,
  "totalPrice": 15.00,
  "productName": "Paracetamol 500mg",
  "productPrice": 5.00,
  "productQuantityRest": 97,
  "soldAt": "2025-03-04T11:00:00"
}
```

### 🛒 Ventas — Listar todas

**GET** `/api/sales`
> Header: `Authorization: Bearer <token>`

**Response:**

```json
[
  {
    "id": "c9d8e7f6-a5b4-3210-dcba-fe9876543210",
    "quantity": 3,
    "unitPrice": 5.00,
    "totalPrice": 15.00,
    "productName": "Paracetamol 500mg",
    "productPrice": 5.00,
    "productQuantityRest": 97,
    "soldAt": "2025-03-04T11:00:00"
  }
]
```

---

### 🛡️ Roles — Crear rol

**POST** `/api/roles`
> Header: `Authorization: Bearer <token>`

```json
{
  "name": "ADMIN"
}
```

---

## ▶️ Cómo correr el proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/farmaapp.git
cd farmaapp
```

### 2. Configurar `application.properties`

```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/farmadb
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# JWT
jwt.secret=tu_clave_secreta_muy_larga_y_segura_minimo_32_chars
jwt.expiration=86400000
```

### 3. Ejecutar

```bash
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080`

---

## ✅ Validaciones implementadas

Los DTOs usan **Bean Validation** (`jakarta.validation`) para garantizar integridad:

- `@NotBlank` — campos de texto obligatorios
- `@NotNull` — campos de objeto/fecha obligatorios
- `@Positive` — precio debe ser mayor a 0
- `@Min(0)` — cantidad no puede ser negativa
- `@Size(max = 200)` — límite de caracteres en nombre y laboratorio

---

## 👨‍💻 Autor

Desarrollado por Rodrigo André — [LinkedIn](www.linkedin.com/in/rodrigo-aquiño-valdezdev) · [GitHub](https://github.com/whoysAndre)
