package com.cafe.cafeDemo.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idFactura;
	
	private Date fechaEmision;
	private int idCliente;
	//private List<Producto> listProducto;
	private int total;
	private String estado;
	private String cobranza;
	private int impuestos;
	
}