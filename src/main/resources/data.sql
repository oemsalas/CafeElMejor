-- MySQL dump 10.13  Distrib 8.0.43, for Linux (x86_64)
--
-- Host: localhost    Database: cafedb
-- ------------------------------------------------------
-- Server version	8.0.43

INSERT INTO cliente (dni,fecha_creacion,telefono,direccion,nombre) VALUES (29000999,'2026-04-18',1122456700,'san ramon 3000','omar salas');
INSERT INTO cobranza (fecha_cobranza,monto,metodo_de_pago) VALUES ('2026-04-19',100.0,'EFECTIVO');
INSERT INTO proveedor (contacto,direccion,nombre,telefono) VALUES ('marcelo tevez','bonaparte 123','zucamor','11123456');
INSERT INTO factura (fecha_emision,id_cliente,id_cobranza,impuestos,total,estado) VALUES ('2026-04-18',1,1,9,100,'PAGADA');
INSERT INTO producto (fecha_vencimiento,id_proveedor,precio_venta,descripcion,lote) VALUES ('2026-04-29',1,100.0,'caja','LOT-001');
INSERT INTO factura_producto (id_factura,id_producto) VALUES (1,1);

INSERT INTO usuario VALUES (3,'administrador','1234','admin');

-- Dump completed on 2026-03-30  0:04:15
