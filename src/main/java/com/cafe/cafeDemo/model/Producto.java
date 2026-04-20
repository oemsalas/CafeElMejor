package com.cafe.cafeDemo.model;

import java.sql.Date;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String descripcion;
    private double precioVenta;
    private String lote;
    private Date fechaVencimiento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_proveedor", nullable = true)
    private Proveedor proveedor;
}
