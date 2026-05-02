-- ══════════════════════════════════════════════════════
--  CaféDemo — Datos de prueba (30 registros por tabla)
--  Usa MERGE INTO para evitar duplicados en reinicios
-- ══════════════════════════════════════════════════════

-- ── CLIENTES ──
MERGE INTO cliente (id, dni, fecha_creacion, telefono, direccion, nombre) KEY(id) VALUES
(1,  29000999, '2025-08-01', 1122456700, 'San Ramón 3000',          'Omar Salas'),
(2,  30112233, '2025-08-05', 1133221100, 'Av. Corrientes 1500',     'Laura Méndez'),
(3,  25987654, '2025-08-10', 1144556677, 'Rivadavia 800',           'Carlos Pereira'),
(4,  32456789, '2025-08-15', 1155443322, 'Belgrano 2200',           'Sofía Torres'),
(5,  28765432, '2025-08-20', 1166778899, 'Santa Fe 400',            'Miguel Ríos'),
(6,  33210987, '2025-09-01', 1177889900, 'Mitre 1100',              'Valeria Gómez'),
(7,  27654321, '2025-09-05', 1188990011, 'Sarmiento 600',           'Diego Fernández'),
(8,  31098765, '2025-09-10', 1199001122, 'Entre Ríos 950',          'Ana Juárez'),
(9,  26543210, '2025-09-15', 1100112233, 'Callao 300',              'Pablo Núñez'),
(10, 34321098, '2025-09-20', 1111223344, 'Tucumán 750',             'Martina López'),
(11, 29876543, '2025-10-01', 1122334455, 'Lavalle 1200',            'Rodrigo Castillo'),
(12, 31234567, '2025-10-05', 1133445566, 'Av. de Mayo 600',         'Florencia Ibáñez'),
(13, 28543210, '2025-10-10', 1144557788, 'Reconquista 400',         'Sebastián Mora'),
(14, 30987654, '2025-10-15', 1155668899, 'Viamonte 220',            'Camila Vega'),
(15, 27321098, '2025-10-20', 1166779900, 'Maipú 900',               'Gustavo Herrera'),
(16, 33456789, '2025-11-01', 1177880011, 'Perón 1500',              'Natalia Castro'),
(17, 26789012, '2025-11-05', 1188991122, 'Diagonal Norte 800',      'Alejandro Ruiz'),
(18, 32123456, '2025-11-10', 1199002233, 'Suipacha 300',            'Verónica Salinas'),
(19, 28901234, '2025-11-15', 1100113344, 'Esmeralda 700',           'Federico Molina'),
(20, 31567890, '2025-11-20', 1111224455, 'Florida 100',             'Lucía Paredes'),
(21, 29234567, '2025-12-01', 1122335566, 'Bartolomé Mitre 400',     'Hernán Suárez'),
(22, 30678901, '2025-12-05', 1133446677, 'San Martín 1800',         'Patricia Vidal'),
(23, 27012345, '2025-12-10', 1144558899, 'Córdoba 2500',            'Marcelo Tevez'),
(24, 33789012, '2025-12-15', 1155669900, 'Pte. Perón 600',          'Claudia Reyes'),
(25, 26456789, '2026-01-05', 1166770011, 'Independencia 900',       'Sergio Blanco'),
(26, 32890123, '2026-01-10', 1177881122, 'Chile 780',               'Graciela Ríos'),
(27, 29345678, '2026-01-15', 1188992233, 'Humberto Primo 300',      'Roberto Medina'),
(28, 30789012, '2026-02-01', 1199003344, 'San Juan 1100',           'Adriana Romero'),
(29, 27234567, '2026-02-10', 1100114455, 'Defensa 530',             'Ernesto Silva'),
(30, 33901234, '2026-03-01', 1111225566, 'Av. Caseros 800',         'Daniela Pereyra');

-- ── PROVEEDORES ──
MERGE INTO proveedor (id, contacto, direccion, nombre, telefono) KEY(id) VALUES
(1,  'Marcelo Tevez',    'Bonaparte 123',        'Zucamor',             '11-1234-5600'),
(2,  'Graciela Ríos',    'Lavalle 450',          'CaféBeans S.A.',      '11-2345-6700'),
(3,  'Hernán Suárez',    'Av. San Martín 80',    'AromaCo',             '11-3456-7800'),
(4,  'Patricia Vidal',   'Córdoba 1200',         'TostadoFino',         '11-4567-8900'),
(5,  'Roberto Medina',   'Maipú 900',            'GranoVerde',          '11-5678-9000'),
(6,  'Claudia Reyes',    'Balcarce 340',         'DulceLeche Dist.',    '11-6789-0100'),
(7,  'Sergio Blanco',    'Reconquista 670',      'TeAndino',            '11-7890-1200'),
(8,  'Florencia Paz',    'Viamonte 220',         'AzúcarNorte',         '11-8901-2300'),
(9,  'Gustavo Herrera',  'Defensa 530',          'CondimentosSur',      '11-9012-3400'),
(10, 'Natalia Castro',   'Chile 780',            'EmpaquesBA',          '11-0123-4500'),
(11, 'Alejandro Ruiz',   'Suipacha 300',         'CaféOrígenes',        '11-1234-5601'),
(12, 'Verónica Salinas', 'Florida 500',          'ImportaCafé',         '11-2345-6701'),
(13, 'Federico Molina',  'Esmeralda 700',        'FiltroMax',           '11-3456-7801'),
(14, 'Lucía Paredes',    'Perón 1500',           'TazaFeliz Dist.',     '11-4567-8901'),
(15, 'Rodrigo Castillo', 'Rivadavia 1200',       'MolinoCentral',       '11-5678-9001'),
(16, 'Camila Vega',      'Tucumán 800',          'ArábicaSA',           '11-6789-0101'),
(17, 'Sebastián Mora',   'Callao 400',           'RobustaDist.',        '11-7890-1201'),
(18, 'Florencia Ibáñez', 'Mitre 600',            'CapsulasYa',          '11-8901-2301'),
(19, 'Omar Salas',       'Belgrano 900',         'ProveedorNorte',      '11-9012-3401'),
(20, 'Laura Méndez',     'Santa Fe 300',         'SaborAndino',         '11-0123-4501'),
(21, 'Carlos Pereira',   'Corrientes 2000',      'MateyCafé',           '11-1234-5602'),
(22, 'Sofía Torres',     'Entre Ríos 700',       'InsumosCafé',         '11-2345-6702'),
(23, 'Miguel Ríos',      'Sarmiento 900',        'GranelBA',            '11-3456-7802'),
(24, 'Valeria Gómez',    'Av. de Mayo 400',      'ColdBrewSA',          '11-4567-8902'),
(25, 'Diego Fernández',  'Reconquista 200',      'LatteInsumos',        '11-5678-9002'),
(26, 'Ana Juárez',       'Maipú 700',            'TostadoresBA',        '11-6789-0102'),
(27, 'Pablo Núñez',      'San Martín 300',       'CaféDeLaCosta',       '11-7890-1202'),
(28, 'Martina López',    'Diagonal Norte 600',   'DistribuidoraPlata',  '11-8901-2302'),
(29, 'Adriana Romero',   'Humberto Primo 200',   'GranoFinoSA',         '11-9012-3402'),
(30, 'Ernesto Silva',    'Independencia 400',    'CaféPatagonia',       '11-0123-4502');

-- ── PRODUCTOS ──
MERGE INTO producto (id, fecha_vencimiento, id_proveedor, precio_venta, descripcion, lote) KEY(id) VALUES
(1,  '2026-08-01', 1,  100.00, 'Caja café molido 500g',          'LOT-001'),
(2,  '2026-08-15', 1,  180.00, 'Café en grano premium 1kg',      'LOT-002'),
(3,  '2026-09-01', 2,   55.00, 'Filtros de papel x100',          'LOT-003'),
(4,  '2026-07-10', 3,  220.00, 'Café orgánico etiopía 250g',     'LOT-004'),
(5,  '2026-10-01', 4,   90.00, 'Tostado oscuro espresso 500g',   'LOT-005'),
(6,  '2026-12-31', 5,   40.00, 'Azúcar blanca 1kg',              'LOT-006'),
(7,  '2026-11-15', 6,   75.00, 'Leche en polvo 800g',            'LOT-007'),
(8,  '2026-10-20', 7,  130.00, 'Té verde japonés 200g',          'LOT-008'),
(9,  '2027-01-10', 8,   25.00, 'Saquitos de azúcar x50',         'LOT-009'),
(10, '2026-09-05', 9,   60.00, 'Canela molida 100g',             'LOT-010'),
(11, '2026-08-20', 10,  85.00, 'Bolsa kraft 500u',               'LOT-011'),
(12, '2026-11-01', 11, 195.00, 'Café Colombia single origin 1kg','LOT-012'),
(13, '2026-09-15', 12, 310.00, 'Café Vietnam Robusta 1kg',       'LOT-013'),
(14, '2027-02-01', 13,  45.00, 'Filtros metálicos reutilizables','LOT-014'),
(15, '2026-10-10', 14, 160.00, 'Cápsulas compatibles x20',       'LOT-015'),
(16, '2026-12-01', 15, 120.00, 'Café descafeinado 500g',         'LOT-016'),
(17, '2027-03-01', 16, 250.00, 'Café Arábica lavado 1kg',        'LOT-017'),
(18, '2026-08-25', 17, 175.00, 'Blend espresso casa 500g',       'LOT-018'),
(19, '2026-11-20', 18,  35.00, 'Palitos de azúcar x100',         'LOT-019'),
(20, '2027-01-20', 19,  70.00, 'Leche entera UHT 1L x6',        'LOT-020'),
(21, '2026-09-25', 20, 145.00, 'Café sabor avellana 250g',       'LOT-021'),
(22, '2026-10-30', 21,  50.00, 'Cacao en polvo 200g',            'LOT-022'),
(23, '2026-12-15', 22,  80.00, 'Jarabe vainilla 750ml',          'LOT-023'),
(24, '2027-02-15', 23,  95.00, 'Jarabe caramelo 750ml',          'LOT-024'),
(25, '2026-11-05', 24, 200.00, 'Cold brew concentrado 1L',       'LOT-025'),
(26, '2026-09-30', 25, 115.00, 'Café torrefacto 500g',           'LOT-026'),
(27, '2027-04-01', 26, 280.00, 'Café geisha especial 200g',      'LOT-027'),
(28, '2026-10-05', 27,  65.00, 'Crema de leche 500ml x6',        'LOT-028'),
(29, '2026-12-20', 28,  30.00, 'Servilletas cafetería x500',     'LOT-029'),
(30, '2027-01-30', 29, 140.00, 'Café instantáneo premium 200g',  'LOT-030');

-- ── COBRANZAS ──
MERGE INTO cobranza (id, fecha_cobranza, monto, metodo_de_pago) KEY(id) VALUES
(1,  '2025-08-05',   100.00, 'EFECTIVO'),
(2,  '2025-08-12',   180.00, 'TRANSFERENCIA'),
(3,  '2025-08-20',   275.00, 'TARJETA_DEBITO'),
(4,  '2025-09-03',   220.00, 'EFECTIVO'),
(5,  '2025-09-10',   400.00, 'TARJETA_CREDITO'),
(6,  '2025-09-18',    90.00, 'EFECTIVO'),
(7,  '2025-09-25',   310.00, 'TRANSFERENCIA'),
(8,  '2025-10-02',   205.00, 'TARJETA_DEBITO'),
(9,  '2025-10-08',   540.00, 'TRANSFERENCIA'),
(10, '2025-10-15',   180.00, 'EFECTIVO'),
(11, '2025-10-22',   320.00, 'TARJETA_CREDITO'),
(12, '2025-10-30',   155.00, 'EFECTIVO'),
(13, '2025-11-04',   410.00, 'TRANSFERENCIA'),
(14, '2025-11-10',   260.00, 'TARJETA_DEBITO'),
(15, '2025-11-17',   730.00, 'TARJETA_CREDITO'),
(16, '2025-11-24',   190.00, 'EFECTIVO'),
(17, '2025-12-01',   475.00, 'TRANSFERENCIA'),
(18, '2025-12-08',   340.00, 'TARJETA_DEBITO'),
(19, '2025-12-15',   620.00, 'TARJETA_CREDITO'),
(20, '2025-12-22',   110.00, 'EFECTIVO'),
(21, '2026-01-05',   385.00, 'TRANSFERENCIA'),
(22, '2026-01-12',   245.00, 'TARJETA_DEBITO'),
(23, '2026-01-20',   560.00, 'TARJETA_CREDITO'),
(24, '2026-01-27',   175.00, 'EFECTIVO'),
(25, '2026-02-03',   430.00, 'TRANSFERENCIA'),
(26, '2026-02-10',   290.00, 'TARJETA_DEBITO'),
(27, '2026-02-18',   650.00, 'TARJETA_CREDITO'),
(28, '2026-03-01',   120.00, 'EFECTIVO'),
(29, '2026-03-10',   510.00, 'TRANSFERENCIA'),
(30, '2026-04-01',   380.00, 'TARJETA_DEBITO');

-- ── FACTURAS ──
MERGE INTO factura (id_factura, fecha_emision, id_cliente, id_cobranza, impuestos, total, estado) KEY(id_factura) VALUES
(1,  '2025-08-03',  1,  1,    9,  100, 'PAGADA'),
(2,  '2025-08-10',  2,  2,   16,  180, 'PAGADA'),
(3,  '2025-08-18',  3,  NULL, 24,  275, 'VENCIDA'),
(4,  '2025-09-01',  4,  4,   19,  220, 'PAGADA'),
(5,  '2025-09-08',  5,  5,   36,  400, 'PAGADA'),
(6,  '2025-09-16',  6,  NULL,  8,   90, 'VENCIDA'),
(7,  '2025-09-22',  7,  7,   28,  310, 'PAGADA'),
(8,  '2025-09-30',  8,  8,   18,  205, 'PAGADA'),
(9,  '2025-10-06',  9,  9,   48,  540, 'PAGADA'),
(10, '2025-10-12', 10, 10,   16,  180, 'PAGADA'),
(11, '2025-10-20', 11, 11,   29,  320, 'PAGADA'),
(12, '2025-10-28', 12, NULL,  14,  155, 'PENDIENTE'),
(13, '2025-11-02', 13, 13,   37,  410, 'PAGADA'),
(14, '2025-11-08', 14, 14,   23,  260, 'PAGADA'),
(15, '2025-11-15', 15, 15,   66,  730, 'PAGADA'),
(16, '2025-11-22', 16, NULL,  17,  190, 'VENCIDA'),
(17, '2025-11-29', 17, 17,   43,  475, 'PAGADA'),
(18, '2025-12-06', 18, 18,   31,  340, 'PAGADA'),
(19, '2025-12-13', 19, 19,   56,  620, 'PAGADA'),
(20, '2025-12-20', 20, NULL,  10,  110, 'PENDIENTE'),
(21, '2026-01-03', 21, 21,   35,  385, 'PAGADA'),
(22, '2026-01-10', 22, 22,   22,  245, 'PAGADA'),
(23, '2026-01-18', 23, 23,   51,  560, 'PAGADA'),
(24, '2026-01-25', 24, NULL,  16,  175, 'PENDIENTE'),
(25, '2026-02-01', 25, 25,   39,  430, 'PAGADA'),
(26, '2026-02-08', 26, 26,   26,  290, 'PAGADA'),
(27, '2026-02-16', 27, 27,   59,  650, 'PAGADA'),
(28, '2026-02-28', 28, NULL,  11,  120, 'PENDIENTE'),
(29, '2026-03-08', 29, 29,   46,  510, 'PAGADA'),
(30, '2026-03-28', 30, NULL,  34,  380, 'PENDIENTE');

-- ── FACTURA_PRODUCTO ──
MERGE INTO factura_producto (id_factura, id_producto) KEY(id_factura, id_producto) VALUES
(1,  1),
(2,  2),
(3,  3), (3,  6),
(4,  4),
(5,  5), (5,  7), (5,  2),
(6,  6),
(7,  1), (7,  8),
(8,  3), (8,  9),
(9,  10),(9,  4), (9,  2),
(10, 1),
(11, 11),(11, 5),
(12, 12),
(13, 13),(13, 3),
(14, 14),(14, 6),
(15, 15),(15, 7), (15, 2),
(16, 16),
(17, 17),(17, 8),
(18, 18),(18, 9),
(19, 19),(19, 10),(19, 4),
(20, 20),
(21, 21),(21, 11),
(22, 22),(22, 6),
(23, 23),(23, 7), (23, 2),
(24, 24),
(25, 25),(25, 8),
(26, 26),(26, 9),
(27, 27),(27, 10),(27, 4),
(28, 28),
(29, 29),(29, 11),(29, 5),
(30, 30),(30, 12);


-- La contraseña hasheada corresponde a: 1234

--MERGE INTO usuario KEY(id) VALUES (
--  1, 'Administrador'
--  , '$2a$10$NH0i54pcsyc7Cxwl4EFV2OOCyv2UVH24zGMo.HC1Cj82B2nY/gi3G'
--  , 'admin'
--);

-- Admin con rol ADMIN (reemplaza tu MERGE actual de usuario admin)
MERGE INTO usuario KEY(id) VALUES (
  1, 'Administrador'
  ,'$2a$10$NH0i54pcsyc7Cxwl4EFV2OOCyv2UVH24zGMo.HC1Cj82B2nY/gi3G'  
  ,'ADMIN'
  ,'admin'
);

-- Ejemplo de usuario OPERADOR (opcional, podés borrarlo)
MERGE INTO usuario KEY(id) VALUES (
   2, 'Operador Demo'   
   ,'$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
   ,'OPERADOR'
   ,'operador'
);

ALTER TABLE CLIENTE      ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE PROVEEDOR    ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE PRODUCTO     ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE COBRANZA     ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE FACTURA      ALTER COLUMN ID_FACTURA RESTART WITH 100;
ALTER TABLE USUARIO      ALTER COLUMN ID RESTART WITH 3;