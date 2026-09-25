package com.example.demo.curso.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para el registro o actualización de un estudiante")
public class EstudianteRequest {

    @Schema(description = "Identificador opcional del estudiante", example = "est-001")
    private String id;

    @Schema(description = "Nombre completo del estudiante", example = "Carlos Mendoza", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del estudiante es obligatorio")
    @Size(max = 255, message = "El nombre no puede superar los 255 caracteres")
    private String nombre;

    @Schema(description = "Correo electrónico institucional o personal (único)", example = "carlos.mendoza@universidad.edu", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico es inválido")
    @Size(max = 255, message = "El correo electrónico no puede superar los 255 caracteres")
    private String email;

    @Schema(description = "Contraseña de acceso inicial (opcional)", example = "Password123!")
    @Size(max = 255, message = "La contraseña no puede superar los 255 caracteres")
    private String password;

    @Schema(description = "Nivel de experiencia del estudiante", example = "Principiante", allowableValues = {"Principiante", "Intermedio", "Avanzado"}, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nivel de experiencia es obligatorio")
    @Pattern(regexp = "^(Principiante|Intermedio|Avanzado)$", message = "El nivel de experiencia debe ser estrictamente: Principiante, Intermedio o Avanzado")
    @Size(max = 50, message = "El nivel de experiencia no puede superar los 50 caracteres")
    private String nivelExperiencia;

    @Schema(description = "Área técnica de interés principal", example = "Desarrollo Web", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El área de interés es obligatoria")
    @Size(max = 255, message = "El área de interés no puede superar los 255 caracteres")
    private String areaInteres;

    public EstudianteRequest(String id, String nombre, String nivelExperiencia, String areaInteres) {
        this.id = id;
        this.nombre = nombre;
        this.nivelExperiencia = nivelExperiencia;
        this.areaInteres = areaInteres;
    }
}
