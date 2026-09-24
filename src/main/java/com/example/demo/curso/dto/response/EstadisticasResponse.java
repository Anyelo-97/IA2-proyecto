package com.example.demo.curso.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasResponse {
    private long totalConsultas;
    private long consultasRespondidas;
    private long consultasSinResultados;
    private double promedioCalificaciones;
    private String cursoMasRecomendado;
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
