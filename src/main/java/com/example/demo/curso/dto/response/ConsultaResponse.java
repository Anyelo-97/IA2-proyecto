package com.example.demo.curso.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaResponse {
    private String id;
    private String estudianteId;
    private String pregunta;
    private LocalDateTime fecha;
    private String estado;
}
