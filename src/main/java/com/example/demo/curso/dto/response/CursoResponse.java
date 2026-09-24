package com.example.demo.curso.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursoResponse {

    private String id;
    private String nombre;
    private String descripcion;
    private String categoriaId;
    private String categoriaNombre;
    private String nivelId;
    private String nivelNombre;
    private Integer duracion;
    private Boolean estado;
}
