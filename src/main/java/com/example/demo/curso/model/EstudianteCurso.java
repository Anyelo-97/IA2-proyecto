package com.example.demo.curso.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "estudiante_curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteCurso {

    @EmbeddedId
    private EstudianteCursoId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("estudianteId")
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("cursoId")
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
}
