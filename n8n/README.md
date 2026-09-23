# RutaIA — Automatizaciones n8n y Base Vectorial Qdrant

Este directorio contiene los flujos de automatización para **n8n** y utilidades para la sincronización con **Spring Boot**, **OpenRouter** y **Qdrant**.

---

## 1. Preparación de Qdrant (Paso único inicial)

Antes de indexar cursos, crea la colección `cursos` con dimensión 1536 (`text-embedding-3-small` de OpenRouter) y distancia `Cosine`, además de sus índices de filtrado:

### En la consola web de Qdrant (http://localhost:6333/dashboard -> Console):
```http
// 1. Crear colección
PUT collections/cursos
{
  "vectors": {
    "size": 1536,
    "distance": "Cosine"
  }
}

// 2. Índice para filtrar solo cursos activos (RF10)
PUT collections/cursos/index
{
  "field_name": "estado",
  "field_schema": "bool"
}

// 3. Índice para filtros por categoría
PUT collections/cursos/index
{
  "field_name": "categoriaNombre",
  "field_schema": "keyword"
}

// 4. Índice para filtros por nivel de dificultad
PUT collections/cursos/index
{
  "field_name": "nivelNombre",
  "field_schema": "keyword"
}
```

---

## 2. Carga Inicial de Cursos en Qdrant (RF05)

Tienes **dos opciones** para realizar la carga inicial de los 23 cursos semilla hacia Qdrant:

### Opción A: Mediante el flujo de n8n (`carga_inicial_qdrant_workflow.json`)
1. En n8n, importa el archivo `n8n/carga_inicial_qdrant_workflow.json`.
2. En el nodo **Generar Vector OpenRouter**, añade tu API key de OpenRouter.
3. Haz clic en **Test Step** o **Execute Workflow** en el nodo disparador manual.
4. El flujo consulta `GET http://host.docker.internal:8080/api/cursos?estado=true`, genera los embeddings e inserta los 23 cursos en Qdrant.

### Opción B: Script directo en PowerShell (`cargar_qdrant.ps1`)
Con Spring Boot corriendo en el puerto 8080 y Qdrant en el 6333:
```powershell
cd n8n
.\cargar_qdrant.ps1 -OpenRouterKey "sk-or-v1-TU_CLAVE_AQUI"
```
El script creará automáticamente la colección, los índices, obtendrá los cursos desde Spring Boot y los indexará en lote en Qdrant.

---

## 3. Flujo: Sincronización Automática (`curso_sync_workflow.json`)

Mantiene Qdrant actualizado en tiempo real cada vez que un administrador crea, actualiza o desactiva un curso desde Spring Boot.

- **`CREAR` / `ACTUALIZAR`**: Genera el vector vía OpenRouter y hace upsert del punto en la colección `cursos` de Qdrant.
- **`DESACTIVAR`**: Elimina el vector de Qdrant usando su `cursoId`.

### Cómo importarlo en n8n:
1. En n8n, importa `n8n/curso_sync_workflow.json`.
2. Configura tu credencial de OpenRouter en el nodo correspondiente.
3. Activa el workflow (**Active** toggle).
4. El webhook escucha en `http://localhost:5678/webhook/curso-sync` (o la URL configurada en `application.properties`).

---

## 4. Verificar datos en Qdrant

Para comprobar que los cursos están almacenados con sus metadatos (payload):
```http
POST collections/cursos/points/scroll
{
  "limit": 10,
  "with_payload": true,
  "with_vector": false
}
```
