package com.example.demo.curso.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EstudianteCursoId implements Serializable {

    @Column(name = "estudiante_id", length = 255, nullable = false)
    private String estudianteId;

    @Column(name = "curso_id", length = 255, nullable = false)
    private String cursoId;
}
