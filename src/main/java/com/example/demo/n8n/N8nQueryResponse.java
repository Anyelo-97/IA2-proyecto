package com.example.demo.n8n;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class N8nQueryResponse {
    private String consultaId;
    private String respuesta;
    private String estado;
    private List<FuenteN8n> fuentes;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class FuenteN8n {
        private String cursoId;
        private double similitud;
    }
}
