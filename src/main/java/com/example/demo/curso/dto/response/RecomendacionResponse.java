package com.example.demo.curso.dto.response;

import java.time.LocalDateTime;
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
public class RecomendacionResponse {

    private String id;
    private String consultaId;
    private String contenido;
    private LocalDateTime fecha;
    private String estado;
}
