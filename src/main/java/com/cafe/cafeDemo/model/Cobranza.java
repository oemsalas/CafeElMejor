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
public class Cobranza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double monto;
    private Date fechaCobranza;
    private String metodoDePago;
}
