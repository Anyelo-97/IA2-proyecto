package com.example.demo.curso.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class EstudianteCursoRequest {

    @NotBlank(message = "El identificador del estudiante es obligatorio")
    private String estudianteId;

    @NotBlank(message = "El identificador del curso es obligatorio")
    private String cursoId;
}
