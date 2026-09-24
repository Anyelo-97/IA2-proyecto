-- ========================================================
-- RutaIA - Datos semilla iniciales (MySQL)
-- Nota: Los IDs de cursos son UUIDs válidos para compatibilidad estricta con Qdrant
-- ========================================================

USE rutaIA;

-- 1. Categorías
INSERT IGNORE INTO categoria (id, nombre) VALUES
('cat-001', 'Desarrollo Web'),
('cat-002', 'Bases de Datos'),
('cat-003', 'Inteligencia Artificial'),
('cat-004', 'Automatización'),
('cat-005', 'Análisis de Datos'),
('cat-006', 'Seguridad Informática'),
('cat-007', 'DevOps');

-- 2. Niveles de Dificultad
INSERT IGNORE INTO nivel_dificultad (id, nombre) VALUES
('niv-001', 'Básico'),
('niv-002', 'Intermedio'),
('niv-003', 'Avanzado');

-- 3. Cursos (23 cursos con UUIDs válidos y descripciones ricas para embeddings semánticos)
INSERT IGNORE INTO curso (id, nombre, descripcion, categoria_id, nivel_id, duracion, modalidad, precio, estado) VALUES
-- Desarrollo Web
('c0000001-0000-4000-8000-000000000001', 'Desarrollo Web con HTML5, CSS3 y JavaScript', 
 'Aprende los fundamentos para crear páginas web interactivas y responsivas desde cero utilizando estándares modernos de HTML5, estilos avanzados con CSS3 y programación del DOM con JavaScript. El curso incluye diseño adaptable con Flexbox y Grid, consumo básico de APIs REST mediante Fetch y despliegue continuo de sitios en GitHub Pages. Al finalizar el estudiante podrá diseñar interfaces web limpias y funcionales para proyectos personales o corporativos.', 
 'cat-001', 'niv-001', 40, 'Virtual', 0.00, TRUE),

('c0000002-0000-4000-8000-000000000002', 'Desarrollo Backend con Java y Spring Boot', 
 'Domina la creación de APIs REST robustas y empresariales empleando Java 17 y el ecosistema Spring Boot 3. Aprende sobre inyección de dependencias, persistencia relacional con Spring Data JPA y Hibernate, validación con Jakarta Validation y documentación interactiva con Swagger OpenAPI. El estudiante construirá servicios web desacoplados listos para integrarse con clientes web y móviles.', 
 'cat-001', 'niv-002', 60, 'Virtual', 49.99, TRUE),

('c0000003-0000-4000-8000-000000000003', 'Frontend Moderno con React y TypeScript', 
 'Construye aplicaciones web de una sola página (SPA) altamente escalables con React, Hooks personalizados, Context API y TypeScript. El programa cubre arquitectura de componentes reutilizables, gestión eficiente del estado global, enrutamiento con React Router y optimización de rendimiento en el navegador. Al completar el curso el participante desarrollará dashboards e interfaces dinámicas con tipado estricto.', 
 'cat-001', 'niv-002', 50, 'Virtual', 39.99, TRUE),

('c0000004-0000-4000-8000-000000000004', 'Arquitectura de Microservicios con Spring Cloud y Docker', 
 'Diseña sistemas distribuidos de alto tráfico basados en microservicios con Spring Boot, Spring Cloud Gateway, Eureka Server y mensajería asíncrona. Se exploran patrones de resiliencia como Circuit Breaker, configuración centralizada, trazabilidad distribuida y empaquetado en contenedores ligeros de Docker. Ideal para desarrolladores que buscan escalar aplicaciones en la nube.', 
 'cat-001', 'niv-003', 70, 'Presencial', 150.00, TRUE),

-- Bases de Datos
('c0000005-0000-4000-8000-000000000005', 'Fundamentos de SQL y Modelado Relacional con MySQL', 
 'Comprende los principios del diseño relacional de bases de datos, diagramas entidad-relación, normalización de tablas y consultas SQL esenciales. Trabaja con sentencias DDL y DML en MySQL, filtros con WHERE, agrupamientos con GROUP BY, uniones complejas con JOINs y funciones de agregación. Capacita al estudiante para estructurar y gestionar información corporativa de forma ordenada y consistente.', 
 'cat-002', 'niv-001', 35, 'Virtual', 0.00, TRUE),

('c0000006-0000-4000-8000-000000000006', 'Optimización y Administración de PostgreSQL', 
 'Aprende a diseñar esquemas optimizados, administración de índices B-Tree y GIN, análisis de planes de ejecución con EXPLAIN ANALYZE y tuning de rendimiento en PostgreSQL. El curso profundiza en transacciones ACID, bloqueo de concurrencia, particionamiento de tablas masivas y copias de seguridad continuas. Dirigido a quienes deseen administrar bases de datos en entornos de producción con alta demanda.', 
 'cat-002', 'niv-002', 45, 'Virtual', 55.00, TRUE),

('c0000007-0000-4000-8000-000000000007', 'Bases de Datos Vectoriales y NoSQL para IA con Qdrant y MongoDB', 
 'Explora el almacenamiento no estructurado y la búsqueda semántica mediante colecciones de documentos en MongoDB y vectores de alta dimensionalidad en Qdrant. Aprende a indexar embeddings generados por modelos de lenguaje, aplicar filtros escalares en búsquedas por similitud de cosenos y diseñar arquitecturas de memoria para agentes de inteligencia artificial. Al terminar sabrás conectar bases vectoriales con aplicaciones backend.', 
 'cat-002', 'niv-003', 40, 'Virtual', 75.00, TRUE),

-- Inteligencia Artificial
('c0000008-0000-4000-8000-000000000008', 'Introducción a la Inteligencia Artificial y Machine Learning con Python', 
 'Iníciate en el mundo del aprendizaje automático aplicando algoritmos supervisados y no supervisados con Python, Scikit-Learn y Jupyter Notebooks. Cubre preprocesamiento de datos, regresión lineal, árboles de decisión, clustering K-Means y métricas de evaluación de modelos. Al finalizar podrás entrenar modelos predictivos capaces de resolver problemas prácticos de clasificación y estimación.', 
 'cat-003', 'niv-001', 45, 'Virtual', 0.00, TRUE),

('c0000009-0000-4000-8000-000000000009', 'Sistemas RAG y Aplicaciones con Modelos de Lenguaje (LLMs)', 
 'Construye aplicaciones inteligentes de Generación Aumentada por Recuperación (RAG) utilizando modelos avanzados de lenguaje vía OpenRouter y LangChain. Aprende a fragmentar documentos técnicos, generar embeddings densos, consultar bases de datos vectoriales como Qdrant e inyectar contexto dinámico en prompts para evitar alucinaciones. Ideal para desarrolladores que desean crear asistentes conversacionales fundamentados.', 
 'cat-003', 'niv-002', 55, 'Virtual', 89.99, TRUE),

('c0000010-0000-4000-8000-000000000010', 'Deep Learning y Redes Neuronales con PyTorch', 
 'Profundiza en la matemática y programación de redes neuronales profundas, arquitecturas convolucionales (CNN) para visión artificial y Transformers para procesamiento de lenguaje natural. Aprende entrenamiento con aceleración por GPU, regularización avanzada, ajuste fino (fine-tuning) de modelos pre-entrenados y despliegue con ONNX Runtime. Orientado a estudiantes que busquen crear soluciones avanzadas de IA.', 
 'cat-003', 'niv-003', 65, 'Presencial', 180.00, TRUE),

-- Automatización
('c0000011-0000-4000-8000-000000000011', 'Automatización de Flujos de Trabajo e Integraciones con n8n', 
 'Aprende a conectar sistemas, bases de datos y APIs externas sin código complejo mediante n8n. Diseña flujos automáticos activados por webhooks, programa tareas recurrentes con cron, transforma datos con nodos de JavaScript y envía notificaciones por correo o mensajería instantánea. Al finalizar el estudiante podrá automatizar tareas administrativas repetitivas en cualquier empresa.', 
 'cat-004', 'niv-001', 30, 'Virtual', 0.00, TRUE),

('c0000012-0000-4000-8000-000000000012', 'Automatización de Procesos Empresariales (BPA) y Web Scraping con Python', 
 'Desarrolla bots de automatización para extracción de información web, procesamiento de archivos Excel/PDF y sincronización periódica de inventarios con Python, Selenium y BeautifulSoup. Se abordan buenas prácticas de manejo de excepciones, rotación de encabezados, programación de servicios en segundo plano y generación automática de reportes ejecutivos. Ideal para agilizar operaciones manuales.', 
 'cat-004', 'niv-002', 45, 'Virtual', 45.00, TRUE),

('c0000013-0000-4000-8000-000000000013', 'Automatización Robótica de Procesos (RPA) y Orquestación con Agentes', 
 'Diseña robots de software capaces de interactuar con sistemas de escritorio heredados, validar transacciones contables complejas y coordinar flujos de trabajo inteligentes con agentes autónomos. Integra n8n con modelos de IA para interpretar documentos no estructurados (facturas, contratos) y tomar decisiones guiadas por reglas de negocio estrictas.', 
 'cat-004', 'niv-003', 50, 'Virtual', 85.00, TRUE),

-- Análisis de Datos
('c0000014-0000-4000-8000-000000000014', 'Análisis de Datos y Visualización con Python y Pandas', 
 'Aprende a limpiar, transformar y analizar grandes conjuntos de datos utilizando Python, Pandas, NumPy, Matplotlib y Seaborn. El curso guía al estudiante en el análisis exploratorio de datos (EDA), tratamiento de valores atípicos y diseño de gráficos estadísticos claros que comunican descubrimientos clave. Podrás responder preguntas de negocio analizando datos tabulares con solidez metodológica.', 
 'cat-005', 'niv-001', 40, 'Virtual', 0.00, TRUE),

('c0000015-0000-4000-8000-000000000015', 'Construcción de Dashboards Ejecutivos con Power BI y DAX', 
 'Transforma datos dispersos en paneles de control interactivos y dinámicos para la toma de decisiones gerenciales utilizando Microsoft Power BI Desktop. Aprende modelado en estrella, fórmulas DAX fundamentales para cálculos de inteligencia de tiempo y principios visuales de storytelling con datos. Capacita para crear tableros profesionales de ventas, retención y métricas operativas.', 
 'cat-005', 'niv-002', 35, 'Virtual', 35.00, TRUE),

('c0000016-0000-4000-8000-000000000016', 'Analítica Avanzada y Minería de Datos para Negocios', 
 'Descubre patrones ocultos en bases de datos masivas mediante técnicas avanzadas de minería de datos, análisis de cohortes, modelos de propensión de compra y segmentación de clientes RFM. Combina SQL analítico con librerías estadísticas de Python para pronósticos de demanda y optimización de precios. Diseñado para analistas que buscan impacto estratégico en la organización.', 
 'cat-005', 'niv-003', 55, 'Presencial', 120.00, TRUE),

-- Seguridad Informática
('c0000017-0000-4000-8000-000000000017', 'Fundamentos de Ciberseguridad y Seguridad de la Información', 
 'Comprende los pilares de la seguridad digital (confidencialidad, integridad, disponibilidad), modelos de amenazas, criptografía básica y autenticación segura. Aprende a identificar ataques comunes como phishing, malware e ingeniería social, además de configurar cortafuegos y políticas de contraseñas robustas. Brinda las bases necesarias para proteger activos tecnológicos.', 
 'cat-006', 'niv-001', 30, 'Virtual', 0.00, TRUE),

('c0000018-0000-4000-8000-000000000018', 'Protección y Seguridad en Aplicaciones Web (OWASP Top 10)', 
 'Aprende a blindar aplicaciones web frente a las principales vulnerabilidades reportadas por OWASP, incluyendo inyección SQL, Cross-Site Scripting (XSS), CSRF y fallos de autorización (BOLA/IDOR). Implementa cabeceras HTTP seguras, sanitización rigurosa de entradas y validación de tokens de sesión con Spring Security. Esencial para desarrolladores web que crean sistemas comerciales y transaccionales.', 
 'cat-006', 'niv-002', 45, 'Virtual', 65.00, TRUE),

('c0000019-0000-4000-8000-000000000019', 'Hacking Ético y Pruebas de Penetración (Pentesting)', 
 'Domina las metodologías profesionales de auditoría ofensiva y evaluación de vulnerabilidades en redes, servidores y aplicaciones usando Kali Linux, Nmap, Burp Suite y Metasploit. Aprende a redactar informes de impacto con remediaciones técnicas justificadas para directivos y equipos de ingeniería. Dirigido a futuros consultores y analistas de ciberseguridad.', 
 'cat-006', 'niv-003', 60, 'Presencial', 210.00, TRUE),

-- DevOps
('c0000020-0000-4000-8000-000000000020', 'Fundamentos de Linux y Control de Versiones con Git y GitHub', 
 'Aprende a navegar y administrar servidores Linux mediante comandos de terminal, permisos POSIX, gestión de procesos y scripts en Bash. Integra el flujo de trabajo colaborativo profesional con Git, controlando ramas, resolviendo conflictos de fusión y colaborando mediante Pull Requests en GitHub. Es la base técnica imprescindible para cualquier rol en desarrollo de software moderno.', 
 'cat-007', 'niv-001', 30, 'Virtual', 0.00, TRUE),

('c0000021-0000-4000-8000-000000000021', 'Despliegue y Contenerización de Aplicaciones con Docker', 
 'Aprende a empaquetar aplicaciones monolíticas y microservicios en imágenes reproducibles y portables mediante Dockerfiles optimizados de múltiples etapas (multi-stage). Gestiona redes internas, almacenamiento persistente mediante volúmenes y orquesta entornos de desarrollo multicontenedor con Docker Compose. Al finalizar podrás desplegar aplicaciones listas para producción sin conflictos de dependencias.', 
 'cat-007', 'niv-002', 40, 'Virtual', 45.00, TRUE),

('c0000022-0000-4000-8000-000000000022', 'Integración y Entrega Continua (CI/CD) con GitHub Actions', 
 'Diseña canalizaciones automatizadas que compilen código, ejecuten pruebas unitarias, analicen calidad estática y desplieguen artefactos a servidores de prueba o producción en cada commit. Configura runners autohospedados, gestión segura de secretos de entorno y despliegues sin tiempo de inactividad. Aumenta la velocidad y confiabilidad del ciclo de vida del software.', 
 'cat-007', 'niv-002', 40, 'Virtual', 55.00, TRUE),

('c0000023-0000-4000-8000-000000000023', 'Orquestación de Contenedores a Gran Escala con Kubernetes', 
 'Administra clústeres elásticos en producción mediante Kubernetes. Aprende la configuración de Pods, Deployments, Services, Ingress Controllers, ConfigMaps y autoescalado horizontal (HPA). Se cubren estrategias de despliegue progresivo (rolling updates, canary) y monitorización continua con Prometheus y Grafana. Diseñado para ingenieros que operan infraestructura crítica en la nube.', 
 'cat-007', 'niv-003', 65, 'Presencial', 240.00, TRUE);

-- 4. Estudiantes semilla para pruebas
INSERT IGNORE INTO usuario (id, email, password, rol) VALUES
('est-001', 'juan.perez@universidad.edu', 'password123', 'ESTUDIANTE');

INSERT IGNORE INTO estudiante (id, nombre, nivel_experiencia, area_interes) VALUES
('est-001', 'Juan Pérez', 'Principiante', 'Desarrollo Web');

-- 5. Administrador semilla
INSERT IGNORE INTO usuario (id, email, password, rol) VALUES
('adm-001', 'admin@rutaia.edu', 'admin123', 'ADMINISTRADOR');

INSERT IGNORE INTO administrador (id, nombre) VALUES
('adm-001', 'Administrador Principal');

-- 6. Consultas semilla para estadísticas iniciales
INSERT IGNORE INTO consulta (id, estudiante_id, pregunta, fecha, estado) VALUES
('con-001', 'est-001', 'Quiero aprender desarrollo web con JavaScript', '2026-09-20 10:00:00', 'Respondida'),
('con-002', 'est-001', 'Quiero aprender backend con Java y Spring Boot', '2026-09-21 11:30:00', 'Respondida'),
('con-003', 'est-001', 'Quiero aprender cocina internacional', '2026-09-22 15:45:00', 'Sin resultados');

-- 7. Recomendaciones semilla
INSERT IGNORE INTO recomendacion (id, consulta_id, contenido, fecha, estado) VALUES
('rec-001', 'con-001', 'Te sugerimos el curso de Desarrollo Web con HTML5, CSS3 y JavaScript para iniciar tus estudios en programación web.', '2026-09-20 10:00:05', 'Respondida'),
('rec-002', 'con-002', 'Para backend con Java te recomendamos nuestro curso de Desarrollo Backend con Java y Spring Boot.', '2026-09-21 11:30:05', 'Respondida'),
('rec-003', 'con-003', 'No se encontraron cursos de cocina en el catálogo actual.', '2026-09-22 15:45:05', 'Sin resultados');

-- 8. Fuentes semilla
INSERT IGNORE INTO fuente (id, recomendacion_id, curso_id, similitud) VALUES
('fue-001', 'rec-001', 'c0000001-0000-4000-8000-000000000001', 0.92),
('fue-002', 'rec-002', 'c0000002-0000-4000-8000-000000000002', 0.95),
('fue-003', 'rec-002', 'c0000001-0000-4000-8000-000000000001', 0.88);

-- 9. Calificaciones semilla
INSERT IGNORE INTO calificacion (id, estudiante_id, recomendacion_id, puntuacion, comentario) VALUES
('cal-001', 'est-001', 'rec-001', 5, 'Excelente recomendación, muy clara.'),
('cal-002', 'est-001', 'rec-002', 4, 'Buen contenido y respuesta rápida.');


