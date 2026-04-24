# CLAUDE.md — Proyecto CaféDemo

Este archivo le da contexto a Claude sobre el proyecto CaféDemo para que pueda continuar el desarrollo sin necesidad de re-explicar lo que ya se hizo.

---

## ¿Qué es CaféDemo?

Sistema de gestión para una cafetería, compuesto por un backend en Spring Boot y dos frontends: uno en React y otro en HTML5 vanilla. Corre en una **Raspberry Pi 2 (475MB RAM)** con DietPi como sistema operativo.

---

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Backend | Java 17 + Spring Boot 3.2 |
| Base de datos | H2 embebida en archivo (sin MySQL) |
| Servidor web | Undertow (reemplaza Tomcat, más liviano) |
| Pool de conexiones | commons-dbcp2 (reemplaza HikariCP) |
| PDF | iText 7.2.5 |
| Frontend 1 | React + Vite + Bootstrap 5 |
| Frontend 2 | HTML5 vanilla + Bootstrap 5 + Chart.js 4.4.1 |
| Proxy reverso | Nginx |
| Servicio | systemd en DietPi |

---

## Infraestructura

- **Dominio:** `caelus.homelinuxserver.org`
- **Nginx** sirve el HTML estático en el puerto 80 y redirige `/api/*` al backend en el puerto 8080
- **Spring Boot** corre en el puerto 8080 (no expuesto directamente)
- **Servicio systemd:** `/etc/systemd/system/cafedemo.service`
- **JAR:** arranca con `java -Xms64m -Xmx200m -jar cafeDemo-0.0.1-SNAPSHOT.jar`
- **Base de datos:** archivo `cafedemo-data` en el mismo directorio del JAR

### Configuración Nginx
```nginx
location /api/ {
    proxy_pass http://localhost:8080/api/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
}
location / {
    try_files $uri $uri/ /index.html;
}
```

---

## Modelo de datos

### Entidades y relaciones

```
Cliente ←── Factura ──→ Cobranza
                │
                └──→ Producto ──→ Proveedor
Usuario (independiente)
```

- `Factura → Cliente`: `@ManyToOne` obligatorio
- `Factura → Cobranza`: `@ManyToOne` opcional. **Regla de negocio:** al asignar una cobranza la factura pasa automáticamente a estado `PAGADA`. Al quitarla vuelve a `PENDIENTE` (salvo que esté `CANCELADA`)
- `Factura → Producto`: `@ManyToMany` tabla intermedia `factura_producto`
- `Producto → Proveedor`: `@ManyToOne` opcional

### Campos relevantes por entidad

**Cliente:** id, nombre, dni, telefono, direccion, fechaCreacion

**Proveedor:** id, nombre, contacto, telefono, direccion

**Producto:** id, descripcion, precioVenta, lote, fechaVencimiento, proveedor

**Cobranza:** id, monto, fechaCobranza, metodoDePago (EFECTIVO / TRANSFERENCIA / TARJETA_DEBITO / TARJETA_CREDITO / CHEQUE)

**Factura:** idFactura, fechaEmision, total, impuestos, estado (PAGADA / PENDIENTE / VENCIDA / CANCELADA), cliente, cobranza, productos

**Usuario:** id, nombreApellido, usuario, password

---

## Package structure del backend

```
com.cafe.cafeDemo
├── api/
│   ├── ClienteController.java
│   ├── ProveedorController.java
│   ├── ProductoController.java
│   ├── CobranzaController.java
│   ├── FacturaController.java
│   └── UsuarioController.java
├── model/
│   ├── Cliente.java
│   ├── Proveedor.java
│   ├── Producto.java
│   ├── Cobranza.java
│   ├── Factura.java
│   └── Usuario.java
├── repository/
│   ├── ClienteRepository.java      ← tiene método search()
│   ├── ProveedorRepository.java    ← tiene método search()
│   ├── ProductosRepository.java    ← tiene método search()
│   ├── CobranzaRepository.java     ← tiene método search()
│   ├── FacturaRepository.java      ← tiene método search()
│   └── UsuarioRepository.java      ← tiene getUsuarioByPassword() y search()
├── service/
│   └── FacturaPdfService.java      ← genera PDF con iText 7
├── exception/
│   └── ResourceNotFoundException.java
└── CafeDemoApplication.java
```

---

## Endpoints API

Todos los endpoints de listado soportan paginación server-side:
`GET /api/{entidad}/listar?page=0&size=8&search=término`

La respuesta es un objeto `Page<T>` de Spring con:
```json
{
  "content": [...],
  "totalElements": 100,
  "totalPages": 13,
  "number": 0,
  "size": 8
}
```

### Endpoints especiales

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/usuarios/login` | Login, recibe `{usuario, password}` |
| GET | `/api/cliente/todos` | Lista todos los clientes sin paginar (para dropdowns) |
| GET | `/api/producto/todos` | Lista todos los productos sin paginar (para dropdowns) |
| GET | `/api/cobranza/todas` | Lista todas las cobranzas sin paginar (para dropdowns) |
| GET | `/api/factura/{id}/pdf` | Descarga la factura como PDF |
| PUT | `/api/factura/{id}/cobranza/{idCobranza}` | Asigna cobranza → pasa a PAGADA |
| DELETE | `/api/factura/{id}/cobranza` | Quita cobranza → vuelve a PENDIENTE |
| POST | `/api/factura/{id}/producto/{idProducto}` | Agrega producto a factura |
| DELETE | `/api/factura/{id}/producto/{idProducto}` | Quita producto de factura |

---

## Frontend HTML5 (cafedemo-v2.html)

Archivo único con todo incluido. Puntos clave de la arquitectura JS:

```javascript
const API = '/api';                    // base URL — funciona con proxy Nginx
const cache = { clientes:[], ... };    // cache de la página actual
const pageState = {};                  // { seccion: { page, totalPages, totalElements } }
const PAGE_SIZE = 8;                   // registros por página
```

### Paginación server-side
- `loadSection(section, page)` — carga una página del backend
- `filterTable(section)` — debounce 350ms, llama a `loadSection` con `?search=`
- `goPage(section, page)` — cambia de página
- Los loaders `loadClientes()`, `loadFacturas()` etc. son wrappers de `loadSection()`

### Separación render interno / público
- `_renderClientes(data)` etc. — pintan el array recibido directamente
- `renderClientes(data)` etc. — wrappers públicos que llaman a `_render*()`

### Dashboard
- Llama a todos los endpoints con `size=1000` para tener datos completos para los gráficos
- Usa `Page.content` para los arrays y `Page.totalElements` para los contadores
- 5 gráficos Chart.js: ventas por mes (barras), recaudado por mes (línea), productos más facturados (barras horizontal), facturas por estado (donut), cobranzas por método (donut)

### Lógica de estado en modal Factura
- Al seleccionar cobranza → estado se fuerza a PAGADA y el select se deshabilita
- Al quitar cobranza → estado vuelve a PENDIENTE y el select se habilita

---

## Frontend React (cafedemo-frontend/)

```
src/
├── App.jsx              — rutas con react-router-dom v6
├── main.jsx
├── index.css            — tema café con variables CSS
├── api/index.js         — axios con base URL localhost:8080/api
├── context/AuthContext.jsx — sesión en localStorage
├── components/
│   ├── Layout.jsx
│   ├── Sidebar.jsx
│   └── ConfirmModal.jsx
└── pages/
    ├── Dashboard.jsx
    ├── Clientes/Clientes.jsx
    ├── Productos/Productos.jsx
    ├── Facturas/Facturas.jsx    ← dropdown de clientes, sin paginación server-side aún
    ├── Cobranzas/Cobranzas.jsx
    └── Usuarios/ (Login.jsx + Usuarios.jsx)
```

**Nota:** el frontend React tiene paginación client-side (no server-side). Si se migra a server-side hay que actualizar igual que se hizo en el HTML.

---

## application.properties (Raspberry Pi)

```properties
spring.datasource.url=jdbc:h2:file:./cafedemo-data;DB_CLOSE_ON_EXIT=FALSE
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:data.sql
spring.jpa.defer-datasource-initialization=true
server.undertow.threads.io=2
server.undertow.threads.worker=8
spring.main.banner-mode=off
spring.jmx.enabled=false
```

---

## data.sql

Usa `MERGE INTO ... KEY(id)` para evitar duplicados en reinicios. Tiene 30 registros por tabla distribuidos entre agosto 2025 y abril 2026 para que los gráficos del dashboard luzcan bien. Usuario admin: `usuario=admin`, `password=1234`.

---

## Funcionalidades pendientes (sugeridas pero no implementadas)

- **Sistema de roles Admin/Operador** — el usuario tiene campo `rol` (ADMIN/OPERADOR). Se empezó a diseñar pero el usuario lo pausó. El Operador tendría acceso limitado según lo que se decida.
- Reportes PDF de listado de facturas del mes y cobranzas del mes
- Historial de facturas por cliente
- Validación de stock en productos (campo `stock` en Producto)
- Módulo de Caja diaria
- Backup automático de H2 a Google Drive
- Paginación server-side en el frontend React

---

## Errores conocidos y soluciones aplicadas

| Error | Causa | Solución |
|---|---|---|
| `doLogin is not defined` | Backtick sin cerrar en el JS (número impar) | Limpiar restos de funciones viejas pegadas en loaders |
| `Unexpected token 'catch'` | Restos del try/catch viejo en loadProductos/loadCobranzas | Limpiar líneas sobrantes post-transformación |
| `cbs.reduce is not a function` | Dashboard recibía `Page<T>` en lugar de array | Extraer `.content` y usar `.totalElements` |
| `searchProveedore not found` | `section.slice(0,-1)` da `proveedore` para `proveedores` | Reemplazar con mapa explícito `{proveedores:'searchProveedor'}` |
| `ColorConstants.WHITE` type mismatch | `ColorConstants.WHITE` es `Color` no `DeviceRgb` | Reemplazar con `new DeviceRgb(255, 255, 255)` |
| `setBackgroundColor(color, opacidad)` type mismatch | El segundo parámetro devuelve `Color` genérico | Usar color sólido sin opacidad |
| Import path incorrecto en Dashboard.jsx | `../../api` desde `src/pages/` sube demasiado | Cambiar a `../api` |
