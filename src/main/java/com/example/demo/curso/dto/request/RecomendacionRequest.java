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
public class RecomendacionRequest {

    private String id;

    @NotBlank(message = "El identificador de la consulta es obligatorio")
    private String consultaId;

    @NotBlank(message = "El contenido es obligatorio")
    @Size(max = 2000, message = "El contenido no puede superar los 2000 caracteres")
    private String contenido;
}
