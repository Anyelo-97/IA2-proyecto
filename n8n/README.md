# Flujos de Automatización (n8n) - RutaIA

Esta carpeta contiene los archivos JSON de los flujos de automatización en **n8n**, los cuales orquestan la comunicación entre la API REST (Spring Boot), la base de datos vectorial (Qdrant) y el modelo de Inteligencia Artificial (OpenRouter).

## 📂 Archivos Disponibles

1. **`curso_sync_workflow.json`**: Flujo de sincronización. Recibe notificaciones de Spring Boot cada vez que se crea, actualiza o desactiva un curso. Se encarga de generar el embedding del curso y guardarlo en Qdrant (o eliminarlo si se desactiva).
2. **`rag_query_workflow.json`**: Pipeline de Generación Aumentada por Recuperación (RAG). Recibe la pregunta del estudiante, la vectoriza, busca similitudes en Qdrant (aplicando el umbral de similitud y filtrando cursos activos), inyecta el contexto en el LLM y devuelve la respuesta al backend.
3. **`carga_inicial_qdrant_workflow.json`**: Flujo auxiliar utilizado para vectorizar masivamente el catálogo de cursos base.

---

## 🚀 Guía de Instalación y Configuración

Para poner en marcha estos flujos en tu entorno n8n, sigue estos pasos al pie de la letra:

### Paso 1: Importar los Workflows
1. Abre n8n en tu navegador (por defecto: `http://localhost:5678`).
2. Ve a la sección **Workflows** en el panel izquierdo y haz clic en **Add Workflow**.
3. Haz clic en el botón de opciones `...` (arriba a la derecha) y selecciona **Import from File**.
4. Selecciona y carga uno a uno los archivos `.json` de esta carpeta.

### Paso 2: Configurar las Credenciales de OpenRouter (IA)
Ambos flujos principales se comunican con OpenRouter para generar embeddings y texto.
1. Dentro del flujo, haz doble clic en los nodos que digan **OpenRouter (Embedding)** u **OpenRouter (LLM)**.
2. En la sección *Credential for Header Auth*, crea una nueva credencial.
3. Configúrala así:
   * **Name**: `Authorization`
   * **Value**: `Bearer TU_API_KEY_DE_OPENROUTER` (Reemplaza con tu token real, ej: `Bearer sk-or-v1-xxx...`)
4. Guarda la credencial y asegúrate de que esté seleccionada en el nodo.

### Paso 3: Configurar la Seguridad del Webhook (Solo para Sync)
El flujo `curso_sync_workflow.json` está protegido para que solo Spring Boot pueda llamarlo.
1. Haz doble clic en el nodo inicial **Webhook1**.
2. En *Credential for Header Auth*, crea una nueva credencial:
   * **Name**: `X-Webhook-Secret`
   * **Value**: *(Escribe aquí el mismo texto que pusiste en tu archivo `application.properties` en Spring Boot bajo la variable `rutaia.n8n.webhook-secret`)*.
3. Guarda y selecciona la credencial.

### Paso 4: Activar los Flujos
Para que los webhooks estén escuchando permanentemente las peticiones de Spring Boot:
1. Guarda el flujo haciendo clic en el icono del disquete o **Save** (arriba a la derecha).
2. En la esquina superior derecha, cambia el interruptor a **Active** (debe quedar en color verde).
3. Asegúrate de repetir este paso tanto para el flujo de Sync como para el de RAG.

---
**Nota de Arquitectura:** Si n8n y Qdrant se están ejecutando en la misma red de Docker, n8n accederá a la base vectorial internamente mediante `http://qdrant:6333` (ya configurado en los nodos). La API de Java se comunicará con n8n apuntando a la URL pública de los webhooks (o mediante ngrok en entornos locales mixtos).
