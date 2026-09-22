package com.example.demo.curso.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "curso", indexes = {
        @Index(name = "idx_curso_estado", columnList = "estado"),
        @Index(name = "idx_curso_categoria", columnList = "categoria_id"),
        @Index(name = "idx_curso_nivel", columnList = "nivel_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curso {

    @Id
    @Column(name = "id", length = 255, nullable = false)
    private String id;

    @Column(name = "nombre", length = 255, nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 1000, nullable = false)
    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "nivel_id", nullable = false)
    private NivelDificultad nivel;

    @Column(name = "duracion", nullable = false)
    private Integer duracion;

    @Column(name = "estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;
}
