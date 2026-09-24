package com.example.demo.curso.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class FuenteRequest {

    private String id;

    @NotBlank(message = "El identificador de la recomendación es obligatorio")
    private String recomendacionId;

    @NotBlank(message = "El identificador del curso es obligatorio")
    private String cursoId;

    @NotNull(message = "La similitud es obligatoria")
    private Double similitud;
}
