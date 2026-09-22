package com.example.demo.n8n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class N8nQueryService {

    private static final Logger log = LoggerFactory.getLogger(N8nQueryService.class);

    private final RestClient restClient;

    @Value("${rutaia.n8n.query-webhook-url}")
    private String queryWebhookUrl;

    public N8nQueryService(RestClient restClient) {
        this.restClient = restClient;
    }

    public N8nQueryResponse consultarRAG(String consultaId, String pregunta,
                                          String nivelExperiencia, String areaInteres) {
        log.info("Enviando consulta {} a webhook n8n: {}", consultaId, queryWebhookUrl);

        Map<String, Object> payload = new HashMap<>();
        payload.put("consultaId", consultaId);
        payload.put("pregunta", pregunta);
        payload.put("nivelExperiencia", nivelExperiencia);
        payload.put("areaInteres", areaInteres);

        return restClient.post()
                .uri(queryWebhookUrl)
                .body(payload)
                .retrieve()
                .body(N8nQueryResponse.class);
    }
}
