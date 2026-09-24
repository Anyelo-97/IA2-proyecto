package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.CategoriaRequest;
import com.example.demo.curso.dto.response.CategoriaResponse;
import com.example.demo.curso.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public CategoriaResponse entityToDto(Categoria categoria) {
        if (categoria == null) return null;
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre()
        );
    }

    public Categoria requestToEntity(CategoriaRequest request) {
        if (request == null) return null;
        return new Categoria(
                request.getId(),
                request.getNombre()
        );
    }
}
