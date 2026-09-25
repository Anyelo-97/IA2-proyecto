package com.example.demo.curso.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dashboard de métricas globales consolidadas del sistema de recomendación")
public class EstadisticasResponse {

    @Schema(description = "Total de consultas académicas procesadas en la plataforma", example = "125")
    private long totalConsultas;

    @Schema(description = "Número de consultas respondidas con recomendación exitosa", example = "118")
    private long consultasRespondidas;

    @Schema(description = "Número de consultas sin coincidencia semántica en el catálogo", example = "7")
    private long consultasSinResultados;

    @Schema(description = "Calificación promedio otorgada por los estudiantes (escala 1 a 5)", example = "4.8")
    private double promedioCalificaciones;

    @Schema(description = "Nombre del curso más recomendado frecuentemente", example = "Desarrollo Backend con Java y Spring Boot")
    private String cursoMasRecomendado;

    @Schema(description = "Cantidad de veces que dicho curso ha sido sugerido como fuente", example = "42")
    private long vecesRecomendado;

    public long getTotal() {
        return totalConsultas;
    }

    public long getRespondidas() {
        return consultasRespondidas;
    }

    public long getSinResultados() {
        return consultasSinResultados;
    }

    public double getPromedioCalificacion() {
        return promedioCalificaciones;
    }
}
