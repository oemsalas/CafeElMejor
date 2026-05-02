# CafeElMejor
este es el sitio de el cafe el mejor
backend desarrollado en java 17 con springboot y frontend desarrollado en html5 con bootstrap
como infrastructura esta deployado en una raspberry pi 2 de 480mb

## Backend
CRUD completo con 6 entidades relacionadas
Paginación server-side con búsqueda en todos los endpoints
Lógica de negocio (factura → PAGADA al asignar cobranza)
Generación de PDF de facturas con iText 7 ← nuevo
Optimizado para Raspberry Pi 2 con H2 + Undertow

## Frontend HTML5
Dashboard con 5 gráficos Chart.js
CRUD completo con modales y paginación
Búsqueda con debounce server-side
Botón ⬇ PDF por cada factura ← nuevo

## Infraestructura
Nginx como proxy reverso
Servicio systemd en DietPi

## sitio produccion ##
http://caelus.homelinuxserver.org/cafe

## el acceso a su api via swagger-ui es : 
http://localhost:8080/swagger-ui/index.html

## el acceso a su pagina principal es :
http://localhost:8080/index.html
