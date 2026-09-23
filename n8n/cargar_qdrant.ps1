# ========================================================
# Script de Carga Inicial a Qdrant (RutaIA - RF05)
# Requiere: Spring Boot corriendo en localhost:8080
#           Qdrant corriendo en localhost:6333
#           OPENROUTER_API_KEY configurada
# ========================================================

param(
    [string]$OpenRouterKey = $env:OPENROUTER_API_KEY,
    [string]$SpringBootUrl = "http://localhost:8080",
    [string]$QdrantUrl = "http://localhost:6333"
)

if (-not $OpenRouterKey) {
    Write-Host "Error: Por favor proporciona tu OPENROUTER_API_KEY:" -ForegroundColor Red
    Write-Host "Ejemplo: .\cargar_qdrant.ps1 -OpenRouterKey 'sk-or-v1-...'" -ForegroundColor Yellow
    exit 1
}

Write-Host "1. Verificando colección 'cursos' en Qdrant ($QdrantUrl)..." -ForegroundColor Cyan
try {
    $collRes = Invoke-RestMethod -Uri "$QdrantUrl/collections/cursos" -Method Get
    Write-Host "   Colección 'cursos' encontrada." -ForegroundColor Green
} catch {
    Write-Host "   Creando colección 'cursos' en Qdrant..." -ForegroundColor Yellow
    $body = '{"vectors": {"size": 1536, "distance": "Cosine"}}'
    Invoke-RestMethod -Uri "$QdrantUrl/collections/cursos" -Method Put -ContentType "application/json" -Body $body | Out-Null
    Write-Host "   Colección creada con éxito." -ForegroundColor Green
}

Write-Host "2. Creando índices de payload en Qdrant..." -ForegroundColor Cyan
try {
    Invoke-RestMethod -Uri "$QdrantUrl/collections/cursos/index" -Method Put -ContentType "application/json" -Body '{"field_name": "estado", "field_schema": "bool"}' | Out-Null
    Invoke-RestMethod -Uri "$QdrantUrl/collections/cursos/index" -Method Put -ContentType "application/json" -Body '{"field_name": "categoriaNombre", "field_schema": "keyword"}' | Out-Null
    Invoke-RestMethod -Uri "$QdrantUrl/collections/cursos/index" -Method Put -ContentType "application/json" -Body '{"field_name": "nivelNombre", "field_schema": "keyword"}' | Out-Null
    Write-Host "   Índices configurados." -ForegroundColor Green
} catch {
    Write-Host "   Aviso en índices (posiblemente ya existían): $_" -ForegroundColor DarkGray
}

Write-Host "3. Obteniendo catálogo de cursos desde Spring Boot ($SpringBootUrl/api/cursos?estado=true)..." -ForegroundColor Cyan
try {
    $cursos = Invoke-RestMethod -Uri "$SpringBootUrl/api/cursos?estado=true" -Method Get
} catch {
    Write-Host "Error al conectar con Spring Boot en $SpringBootUrl. Asegúrate de que la aplicación esté corriendo." -ForegroundColor Red
    exit 1
}

Write-Host "   Se obtuvieron $($cursos.Count) cursos desde Spring Boot." -ForegroundColor Green

$headers = @{
    "Authorization" = "Bearer $OpenRouterKey"
    "Content-Type" = "application/json"
}

$puntos = @()
$total = $cursos.Count
$actual = 0

Write-Host "4. Generando embeddings con OpenRouter e indexando en Qdrant..." -ForegroundColor Cyan

foreach ($curso in $cursos) {
    $actual++
    
    # Validar formato UUID requerido por Qdrant
    $isGuid = [guid]::TryParse($curso.id, [ref][guid]::Empty)
    if (-not $isGuid) {
        Write-Host "   [Omitido] Curso '$($curso.nombre)' tiene ID no-UUID: '$($curso.id)'. Qdrant solo acepta UUIDs." -ForegroundColor Yellow
        continue
    }

    $textoEmbedding = "$($curso.nombre). $($curso.descripcion). Categoria: $($curso.categoriaNombre). Nivel: $($curso.nivelNombre)."
    
    $embedPayload = @{
        model = "text-embedding-3-small"
        input = $textoEmbedding
    } | ConvertTo-Json -Compress

    try {
        $embedRes = Invoke-RestMethod -Uri "https://openrouter.ai/api/v1/embeddings" -Method Post -Headers $headers -Body $embedPayload
        $vector = $embedRes.data[0].embedding

        $punto = @{
            id = $curso.id
            vector = $vector
            payload = @{
                cursoId = $curso.id
                nombre = $curso.nombre
                descripcion = $curso.descripcion
                categoriaNombre = $curso.categoriaNombre
                nivelNombre = $curso.nivelNombre
                duracion = $curso.duracion
                estado = $curso.estado
            }
        }
        $puntos += $punto
        Write-Host "   [$actual/$total] Vectorizado: $($curso.nombre)" -ForegroundColor DarkGray
    } catch {
        Write-Host "   Error al vectorizar curso $($curso.id): $_" -ForegroundColor Red
    }
}

if ($puntos.Count -eq 0) {
    Write-Host "No se encontraron cursos con ID UUID válidos para indexar." -ForegroundColor Yellow
    exit 1
}

Write-Host "5. Guardando $($puntos.Count) puntos en Qdrant..." -ForegroundColor Cyan
$qdrantPayload = @{
    points = $puntos
} | ConvertTo-Json -Depth 10

try {
    $qdrantRes = Invoke-RestMethod -Uri "$QdrantUrl/collections/cursos/points" -Method Put -ContentType "application/json" -Body $qdrantPayload
    Write-Host "¡Carga inicial completada con éxito! ($($puntos.Count) cursos guardados en Qdrant)" -ForegroundColor Green
} catch {
    Write-Host "Error al guardar en Qdrant: $_" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errBody = $reader.ReadToEnd()
        Write-Host "Detalle del error de Qdrant: $errBody" -ForegroundColor Red
    }
}
