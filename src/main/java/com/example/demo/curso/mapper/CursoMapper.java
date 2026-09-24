package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.CursoRequest;
import com.example.demo.curso.dto.response.CursoResponse;
import com.example.demo.curso.model.Curso;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper {

    public CursoResponse entityToDto(Curso curso) {
        if (curso == null) return null;
        CursoResponse response = new CursoResponse();
        response.setId(curso.getId());
        response.setNombre(curso.getNombre());
        response.setDescripcion(curso.getDescripcion());
        response.setCategoriaId(curso.getCategoria() != null ? curso.getCategoria().getId() : null);
        response.setCategoriaNombre(curso.getCategoria() != null ? curso.getCategoria().getNombre() : null);
        response.setNivelId(curso.getNivel() != null ? curso.getNivel().getId() : null);
        response.setNivelNombre(curso.getNivel() != null ? curso.getNivel().getNombre() : null);
        response.setDuracion(curso.getDuracion());
        response.setModalidad(curso.getModalidad());
        response.setPrecio(curso.getPrecio());
        response.setEstado(curso.getEstado());
        return response;
    }

    public Curso requestToEntity(CursoRequest request) {
        if (request == null) return null;
        Curso curso = new Curso();
        curso.setId(request.getId());
        curso.setNombre(request.getNombre());
        curso.setDescripcion(request.getDescripcion());
        curso.setDuracion(request.getDuracion());
        curso.setModalidad(request.getModalidad());
        curso.setPrecio(request.getPrecio());
        curso.setEstado(request.getEstado());
        return curso;
    }
}
