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
public class Cobranza {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCobranza;
	
	private Date fechaCobranza;
	private String metodoDePago;
	private int monto;
	
}
