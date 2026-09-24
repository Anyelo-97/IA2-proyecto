# Configuración de Qdrant

Esta carpeta contiene todo lo necesario para levantar la base de datos vectorial localmente y cargarle los 23 cursos semilla con sus respectivos embeddings.

## 1. Levantar Qdrant
Asegúrate de tener Docker abierto y ejecuta en esta misma carpeta:
```bash
docker compose up -d
```
Esto levantará Qdrant en el puerto `6333`.

## 2. Carga de Datos (Archivos JSON)
En la carpeta `/carga` tienes los archivos con los cuerpos (bodies) JSON listos para enviar a Qdrant mediante Postman, Insomnia o cURL.

1. **Crear Colección:** 
   Envía el JSON de `carga/01_crear_coleccion.json` mediante un **PUT** a:
   `http://localhost:6333/collections/cursos`

2. **Cargar los 23 Cursos (Puntos vectorizados):**
   Envía el enorme JSON de `carga/02_puntos.json` mediante un **PUT** a:
   `http://localhost:6333/collections/cursos/points` o usa la consola del dashboard

3. **Verificar que la carga fue exitosa:**
   Haz un **POST** a `http://localhost:6333/collections/cursos/points/scroll` enviando el JSON de `carga/03_verificar.json` (o usa la consola de  `http://localhost:6333/dashboard`).

*Nota: Una vez hecha esta carga inicial, cualquier curso nuevo que agregues desde el backend (Spring Boot) se sincronizará automáticamente hacia Qdrant usando el flujo de n8n (`curso_sync_workflow.json`).*
