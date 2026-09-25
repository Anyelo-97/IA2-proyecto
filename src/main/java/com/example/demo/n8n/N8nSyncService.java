package com.example.demo.n8n;

import com.example.demo.curso.dto.response.CursoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class N8nSyncService {

    private static final Logger log = LoggerFactory.getLogger(N8nSyncService.class);

    private final RestClient restClient;

    @Value("${rutaia.n8n.sync-webhook-url}")
    private String syncWebhookUrl;

    public N8nSyncService(RestClient restClient) {
        this.restClient = restClient;
    }

    public void sincronizarCurso(CursoResponse curso, String accion) {
        try {
            log.info("Notificando a n8n sincronización de curso [accion={}, cursoId={}]", accion, curso.getId());

            Map<String, Object> payload = new HashMap<>();
            payload.put("accion", accion);
            payload.put("cursoId", curso.getId());
            payload.put("nombre", curso.getNombre());
            payload.put("descripcion", curso.getDescripcion());
            payload.put("categoriaNombre", curso.getCategoriaNombre());
            payload.put("nivelNombre", curso.getNivelNombre());
            payload.put("duracion", curso.getDuracion());
            payload.put("modalidad", curso.getModalidad() != null ? curso.getModalidad() : "Virtual");
            payload.put("precio", curso.getPrecio() != null ? curso.getPrecio() : java.math.BigDecimal.ZERO);
            payload.put("estado", curso.getEstado());

            restClient.post()
                    .uri(syncWebhookUrl)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();

            log.info("Curso sincronizado exitosamente con n8n: {} - {}", accion, curso.getId());
        } catch (Exception e) {
            log.error("Error al sincronizar curso con n8n [accion={}, cursoId={}]: {}", accion, curso.getId(), e.getMessage());
            // No interrumpir la transacción en caso de fallo externo (fire-and-forget)
        }
    }
}
