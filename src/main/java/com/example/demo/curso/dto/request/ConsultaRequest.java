package com.example.demo.curso.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud de consulta académica en lenguaje natural para el pipeline RAG")
public class ConsultaRequest {

    @Schema(description = "Identificador único del estudiante que realiza la consulta", example = "est-001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El ID del estudiante es obligatorio")
    private String estudianteId;

    @Schema(description = "Pregunta, interés vocacional o necesidad de formación en lenguaje natural", example = "Quiero aprender desarrollo backend con Java y microservicios", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La pregunta no puede estar vacía")
    @Size(max = 1000, message = "La pregunta no puede superar los 1000 caracteres")
    private String pregunta;
}
