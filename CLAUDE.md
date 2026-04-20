# CLAUDE.md — Café Demo Frontend

## Descripción del proyecto

Frontend SPA (Single Page Application) para el sistema de administración **Café Demo**, desarrollado en HTML5 y JavaScript vanilla puro. Se sirve directamente desde Spring Boot como recurso estático en `src/main/resources/static/index.html` y consume la API REST del backend en `localhost:8080`.

---

## Stack tecnológico

| Tecnología | Uso |
|---|---|
| HTML5 + CSS3 | Estructura y estilos (sin frameworks CSS) |
| JavaScript ES2020+ | Lógica, navegación y consumo de API |
| Google Fonts | `Playfair Display` (títulos) + `DM Sans` (cuerpo) |
| Spring Boot (static) | Servidor de archivos estáticos |
| Fetch API | Comunicación con el backend REST |

---

## Arquitectura del archivo

El frontend es un único archivo `index.html` que contiene tres secciones bien diferenciadas:

```
index.html
├── <style>          → Todos los estilos CSS con variables CSS (:root)
├── <body>           → Estructura HTML (login + app shell)
│   ├── #loginScreen       → Pantalla de autenticación
│   ├── #appShell          → Contenedor principal (sidebar + topbar + main)
│   │   ├── .sidebar       → Navegación lateral
│   │   ├── .topbar        → Barra superior con usuario y logout
│   │   └── .main          → Vistas intercambiables (una por sección)
│   └── Modales (overlay)  → Un modal por entidad + modal de confirmación
└── <script>         → Toda la lógica JS
```

---

## Endpoints consumidos

El frontend apunta a `BASE_URL = '/api'` y consume los siguientes endpoints:

### Autenticación
| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/usuarios/login` | Login con `{ usuario, password }` |

### Clientes
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/cliente/listar` | Listar todos |
| GET | `/api/cliente/{id}` | Obtener por ID |
| POST | `/api/cliente` | Crear |
| PUT | `/api/cliente/{id}` | Actualizar |
| DELETE | `/api/cliente/{id}` | Eliminar |

### Productos
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/producto/listar` | Listar todos |
| GET | `/api/producto/{id}` | Obtener por ID |
| POST | `/api/producto` | Crear |
| PUT | `/api/producto/{id}` | Actualizar |
| DELETE | `/api/producto/{id}` | Eliminar |

### Facturas
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/factura/listar` | Listar todas |
| GET | `/api/factura/{id}` | Obtener por ID |
| POST | `/api/factura` | Crear |
| PUT | `/api/factura/{id}` | Actualizar |
| DELETE | `/api/factura/{id}` | Eliminar |

### Cobranzas
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/cobranza/listar` | Listar todas |
| GET | `/api/cobranza/{id}` | Obtener por ID |
| POST | `/api/cobranza` | Crear |
| PUT | `/api/cobranza/{id}` | Actualizar |
| DELETE | `/api/cobranza/{id}` | Eliminar |

### Usuarios
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/usuarios` | Listar todos |
| GET | `/api/usuarios/{id}` | Obtener por ID |
| POST | `/api/usuarios` | Crear |
| PUT | `/api/usuarios/{id}` | Actualizar |
| DELETE | `/api/usuarios/{id}` | Eliminar |

---

## Modelo de datos esperado por el frontend

### Cliente
```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "dni": "12345678",
  "telefono": "11-1234-5678",
  "direccion": "Calle 123, Ciudad",
  "fechaCreacion": "2024-01-15"
}
```

### Producto
```json
{
  "id": 1,
  "descripcion": "Café molido 500g",
  "precioVenta": 1500.00,
  "lote": "LOT-001",
  "fechaVencimiento": "2025-12-31",
  "idProveedor": 1
}
```

### Factura
```json
{
  "id": 1,
  "idCliente": 1,
  "cobranza": 1,
  "total": 3000.00,
  "impuestos": 630.00,
  "estado": "PENDIENTE",
  "fechaEmision": "2024-03-01"
}
```
> Estados válidos: `PENDIENTE`, `PAGADA`, `ANULADA`

### Cobranza
```json
{
  "id": 1,
  "monto": 3000.00,
  "metodoDePago": "EFECTIVO",
  "fechaCobranza": "2024-03-05"
}
```
> Métodos válidos: `EFECTIVO`, `TRANSFERENCIA`, `TARJETA`, `CHEQUE`

### Usuario
```json
{
  "id": 1,
  "nombreApellido": "Admin Sistema",
  "usuario": "admin",
  "password": "1234"
}
```

---

## Flujo de autenticación

1. El usuario ingresa credenciales en la pantalla de login
2. Se hace `POST /api/usuarios/login` con `{ usuario, password }`
3. Si la respuesta contiene un objeto con `id`, la sesión se guarda en la variable `session` (en memoria)
4. Se oculta `#loginScreen` y se muestra `#appShell`
5. Al hacer logout, `session` se limpia y se vuelve a mostrar el login
6. **No hay JWT ni cookies** — la sesión dura mientras el tab esté abierto

---

## Estructura del JavaScript

### Variables globales
```javascript
const API = '/api';         // Base URL de la API
let session = null;         // Usuario autenticado en memoria
const cache = {             // Cache de datos para filtros client-side
  clientes: [],
  productos: [],
  facturas: [],
  cobranzas: [],
  usuarios: []
};
```

### Funciones principales

| Función | Descripción |
|---|---|
| `doLogin()` | Autentica al usuario |
| `doLogout()` | Cierra sesión |
| `api(path, opts)` | Helper fetch con manejo de errores |
| `toast(msg, type)` | Muestra notificaciones (`ok` / `err`) |
| `openModal(name)` / `closeModal(name)` | Control de modales |
| `loadDashboard()` | Carga estadísticas y tablas resumidas |
| `loadClientes()` / `renderClientes()` | Carga y renderiza clientes |
| `editCliente(id)` / `saveCliente()` | Edición y guardado de cliente |
| `filterTable(section)` | Filtro client-side sobre el cache |
| `confirmDel(type, id, label)` | Modal de confirmación de eliminación |

El mismo patrón `load → render → edit → save` se repite para todas las entidades.

---

## Sistema de diseño (CSS variables)

```css
:root {
  --bg:       #1a1208;   /* Fondo principal (café oscuro) */
  --surface:  #2a1f0e;   /* Superficie de cards y sidebar */
  --accent:   #c8862a;   /* Color principal (dorado café) */
  --accent2:  #e4a84a;   /* Acento secundario */
  --cream:    #f5e6c8;   /* Texto principal */
  --muted:    #8a7355;   /* Texto secundario */
  --green:    #6dbf8a;   /* Éxito / precios */
  --red:      #e07070;   /* Error / peligro */
  --blue:     #7aabcf;   /* Info / métodos de pago */
}
```

---

## Cómo agregar una nueva sección

1. **Agregar nav item** en el sidebar:
```html
<div class="nav-item" data-view="nueva"><span class="nav-icon">🆕</span> Nueva</div>
```

2. **Agregar la vista** en `.main`:
```html
<div class="view" id="view-nueva">...</div>
```

3. **Agregar el modal** con id `ov-nueva`

4. **Agregar el endpoint** al map de eliminación:
```javascript
const endpointMap = { ..., nueva: '/nueva' };
const reloadMap   = { ..., nueva: loadNueva };
```

5. **Implementar** las funciones `loadNueva()`, `renderNueva()`, `editNueva(id)`, `saveNueva()`

6. **Registrar** en el switch de navegación dentro del event listener de `.nav-item`

---

## Instalación y uso

### Requisitos
- Backend Spring Boot corriendo en `localhost:8080`
- Los controllers deben tener `@CrossOrigin` habilitado (ya incluido en el proyecto)

### Pasos
```bash
# Copiar el archivo al proyecto Spring Boot
cp index.html src/main/resources/static/index.html

# Iniciar el backend
mvn spring-boot:run

# Abrir en el navegador
http://localhost:8080
```

---

## Notas importantes

- **Sin dependencias externas de JS** — no usa jQuery, React ni ningún framework
- **Cache client-side** — los filtros de búsqueda operan sobre datos ya cargados, sin llamadas adicionales a la API
- **Responsive básico** — el layout usa CSS Grid con `auto-fit`, funciona en pantallas medianas/grandes
- **Sin paginación** — carga todos los registros de una vez; si el volumen de datos crece, considerar agregar paginación server-side
- **Contraseñas en texto plano** — el backend maneja la autenticación; se recomienda agregar hashing en el backend si no lo tiene
