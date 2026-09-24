package com.example.demo.curso.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class ConsultaRequest {

    private String id;

    @NotBlank(message = "El identificador del estudiante es obligatorio")
    private String estudianteId;

    @NotBlank(message = "La pregunta es obligatoria")
    @Size(max = 1000, message = "La pregunta no puede superar los 1000 caracteres")
    private String pregunta;
}
