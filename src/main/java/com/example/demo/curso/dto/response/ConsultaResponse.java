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
public class ConsultaResponse {

    private String id;
    private String estudianteId;
    private String pregunta;
    private LocalDateTime fecha;
    private String estado;
}
