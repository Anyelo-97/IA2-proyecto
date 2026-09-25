package com.example.demo.curso.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Perfil de información del estudiante")
public class EstudianteResponse {

    @Schema(description = "Identificador único del estudiante", example = "est-001")
    private String id;

    @Schema(description = "Nombre completo del estudiante", example = "Carlos Mendoza")
    private String nombre;

    @Schema(description = "Correo electrónico institucional", example = "carlos.mendoza@universidad.edu")
    private String email;

    @Schema(description = "Nivel de experiencia técnica", example = "Intermedio")
    private String nivelExperiencia;

    @Schema(description = "Área técnica de interés principal", example = "Desarrollo Web")
    private String areaInteres;

    public EstudianteResponse(String id, String nombre, String nivelExperiencia, String areaInteres) {
        this.id = id;
        this.nombre = nombre;
        this.nivelExperiencia = nivelExperiencia;
        this.areaInteres = areaInteres;
    }
}
