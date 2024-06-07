package com.cafe.cafeDemo.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cobranza")
public class Cobranza {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCobranza;
	
	private Date fechaCobranza;
	private String metodoDePago;
	private int monto;
	
	
	public int getIdCobranza() {
		return idCobranza;
	}
	public void setIdCobranza(int idCobranza) {
		this.idCobranza = idCobranza;
	}
	public Date getFechaCobranza() {
		return fechaCobranza;
	}
	public void setFechaCobranza(Date fechaCobranza) {
		this.fechaCobranza = fechaCobranza;
	}
	public String getMetodoDePago() {
		return metodoDePago;
	}
	public void setMetodoDePago(String metodoDePago) {
		this.metodoDePago = metodoDePago;
	}
	public int getMonto() {
		return monto;
	}
	public void setMonto(int monto) {
		this.monto = monto;
	}
}
