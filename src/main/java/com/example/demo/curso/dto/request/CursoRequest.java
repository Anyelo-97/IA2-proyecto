package com.example.demo.curso.dto.request;

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
public class CursoRequest {

    private String id;

    @NotBlank(message = "El nombre del curso es obligatorio")
    @Size(max = 255, message = "El nombre no puede superar los 255 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción del curso es obligatoria")
    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String descripcion;

    @NotBlank(message = "El identificador de la categoría es obligatorio")
    private String categoriaId;

    @NotBlank(message = "El identificador del nivel de dificultad es obligatorio")
    private String nivelId;

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración debe ser mayor que cero")
    private Integer duracion;

    private Boolean estado = true;
}
