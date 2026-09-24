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
public class EstudianteRequest {

    private String id;

    @NotBlank(message = "El nombre del estudiante es obligatorio")
    @Size(max = 255, message = "El nombre no puede superar los 255 caracteres")
    private String nombre;

    @NotBlank(message = "El nivel de experiencia es obligatorio")
    @Size(max = 50, message = "El nivel de experiencia no puede superar los 50 caracteres")
    private String nivelExperiencia;

    @NotBlank(message = "El área de interés es obligatoria")
    @Size(max = 255, message = "El área de interés no puede superar los 255 caracteres")
    private String areaInteres;
}
