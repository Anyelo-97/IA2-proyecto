package com.example.demo.curso.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nivel_dificultad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NivelDificultad {

    @Id
    @Column(name = "id", length = 255, nullable = false)
    private String id;

    @Column(name = "nombre", length = 50, nullable = false, unique = true)
    private String nombre;
}
