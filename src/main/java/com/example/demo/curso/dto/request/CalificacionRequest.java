package com.example.demo.curso.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalificacionRequest {

    private String id;

    @NotBlank(message = "El identificador del estudiante es obligatorio")
    private String estudianteId;

    @NotBlank(message = "El identificador de la recomendación es obligatorio")
    private String recomendacionId;

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    private Integer puntuacion;

    @Size(max = 1000, message = "El comentario no puede superar los 1000 caracteres")
    private String comentario;
}
