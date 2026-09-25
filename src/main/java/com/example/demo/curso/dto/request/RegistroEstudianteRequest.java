package com.example.demo.curso.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@lombok.AllArgsConstructor
@Schema(description = "Datos de registro para un nuevo estudiante")
public class RegistroEstudianteRequest {
    @Schema(description = "Nombre completo del estudiante", example = "Ana Gómez", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no puede superar los 255 caracteres")
    private String nombre;

    @Schema(description = "Correo electrónico único", example = "ana.gomez@universidad.edu", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato de correo no es válido")
    @Size(max = 255, message = "El correo no puede superar los 255 caracteres")
    private String email;

    @Schema(description = "Contraseña de acceso", example = "Password123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 255, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Schema(description = "Nivel de experiencia", example = "Principiante", allowableValues = {"Principiante", "Intermedio", "Avanzado"}, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nivel de experiencia es obligatorio")
    @Pattern(regexp = "^(Principiante|Intermedio|Avanzado)$", message = "El nivel de experiencia debe ser estrictamente: Principiante, Intermedio o Avanzado")
    private String nivelExperiencia;

    @Schema(description = "Área técnica de interés principal", example = "Inteligencia Artificial", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El área de interés es obligatoria")
    @Size(max = 255, message = "El área de interés no puede superar los 255 caracteres")
    private String areaInteres;
}
