package com.example.demo.curso.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuenteResponse {

    private String id;
    private String recomendacionId;
    private String cursoId;
    private String cursoNombre;
    private String cursoDescripcion;
    private String categoriaNombre;
    private Double similitud;
}
