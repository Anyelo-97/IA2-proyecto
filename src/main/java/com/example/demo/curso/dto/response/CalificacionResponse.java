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
public class CalificacionResponse {

    private String id;
    private String estudianteId;
    private String recomendacionId;
    private Integer puntuacion;
    private String comentario;
}
