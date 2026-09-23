package com.example.demo.curso.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuenteResponse {

    private String id;
    private String recomendacionId;
    private String cursoId;
    private Double similitud;
}
