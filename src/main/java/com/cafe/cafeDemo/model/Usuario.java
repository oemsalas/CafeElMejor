package com.cafe.cafeDemo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombreApellido;
    private String usuario;
    private String password;
    private String rol;

    // Incrementa en cada login — invalida sesiones anteriores
    private int tokenVersion = 0;
}
