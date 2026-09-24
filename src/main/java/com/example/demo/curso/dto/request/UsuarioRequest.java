package com.example.demo.curso.dto.request;

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
public class UsuarioRequest {

    private String id;

    @NotBlank(message = "El email no puede estar vacío")
    @Size(max = 255, message = "El email no puede superar los 255 caracteres")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacío")
    @Size(max = 255, min = 8, message = "La contraseña debe tener minimo 8 caracteres y no puede superar los 255 caracteres")
    private String password;

    @NotBlank(message = "El rol no puede estar vacío")
    @Size(max = 50, message = "El rol no puede superar los 50 caracteres")
    private String rol;
}
