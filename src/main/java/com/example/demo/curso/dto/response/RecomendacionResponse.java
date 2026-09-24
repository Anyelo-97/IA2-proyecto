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
public class RecomendacionResponse {

    private String id;
    private String consultaId;
    private String contenido;
    private LocalDateTime fecha;
    private String estado;
}
