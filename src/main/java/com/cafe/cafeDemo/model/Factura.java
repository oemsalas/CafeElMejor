package com.cafe.cafeDemo.model;

import java.sql.Date;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    private int total;
    private String estado;
    private int impuestos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cobranza", nullable = true)
    private Cobranza cobranza;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "factura_producto",
        joinColumns = @JoinColumn(name = "id_factura"),
        inverseJoinColumns = @JoinColumn(name = "id_producto")
    )
    @JsonIgnoreProperties("facturas")
    private List<Producto> productos;
}
