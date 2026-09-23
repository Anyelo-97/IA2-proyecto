package com.example.demo.curso.mapper;

import com.example.demo.curso.dto.request.CategoriaRequest;
import com.example.demo.curso.dto.response.CategoriaResponse;
import com.example.demo.curso.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public CategoriaResponse entityToDto(Categoria categoria) {
        if (categoria == null) return null;
        return CategoriaResponse.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .build();
    }

    public Categoria requestToEntity(CategoriaRequest request) {
        if (request == null) return null;
        return Categoria.builder()
                .id(request.getId())
                .nombre(request.getNombre())
                .build();
    }
}
