package com.example.demo.curso.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "consulta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @Id
    @Column(name = "id", length = 255, nullable = false)
    private String id;

    // TODO: Link to Estudiante entity when available (@ManyToOne)
    @Column(name = "estudiante_id", length = 255, nullable = false)
    private String estudianteId;

    @Column(name = "pregunta", length = 1000, nullable = false)
    private String pregunta;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "estado", length = 50, nullable = false)
    private String estado = "Pendiente";
}
