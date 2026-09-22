package com.example.demo.curso.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "estudiante")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor

public class Estudiante {
    @Id
    @Column(name = "id", length = 255, nullable = false)
    private String id;

    @Column(name = "nombre", length = 255, nullable = false)
    private String nombre;

    @Column(name = "nivel_experiencia", length = 50, nullable = false)
    private String nivelExperiencia;

    @Column(name = "area_interes", length = 255, nullable = false)
    private String areaInteres;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id", nullable = false)
    private Usuario usuario;
}
