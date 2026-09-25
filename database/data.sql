-- ========================================================
-- RutaIA - Datos semilla iniciales (MySQL)
-- Nota: Los IDs de cursos son UUIDs válidos para compatibilidad estricta con Qdrant
-- ========================================================

USE rutaIA;

-- 1. Categorías (7 categorías que cubren el espectro de formación técnica)
INSERT IGNORE INTO categoria (id, nombre) VALUES
('cat-001', 'Desarrollo Web'),
('cat-002', 'Bases de Datos'),
('cat-003', 'Inteligencia Artificial'),
('cat-004', 'Automatización'),
('cat-005', 'Análisis de Datos'),
('cat-006', 'Seguridad Informática'),
('cat-007', 'DevOps');

-- 2. Niveles de Dificultad (Catálogo fijo institucional de 3 niveles)
INSERT IGNORE INTO nivel_dificultad (id, nombre) VALUES
('niv-001', 'Básico'),
('niv-002', 'Intermedio'),
('niv-003', 'Avanzado');

-- 3. Cursos (23 cursos con descripciones ricas en prerrequisitos, temario y herramientas para embeddings semánticos en Qdrant)
INSERT IGNORE INTO curso (id, nombre, descripcion, categoria_id, nivel_id, duracion, modalidad, precio, estado) VALUES
-- Desarrollo Web
('c0000001-0000-4000-8000-000000000001', 'Desarrollo Web con HTML5, CSS3 y JavaScript', 
 'Prerrequisitos: Conocimientos elementales de informática y navegación web. Temario: Estructura semántica con HTML5, maquetación responsiva con CSS3, Flexbox, CSS Grid, manipulación dinámica del DOM con JavaScript moderno (ES6+), programación asíncrona con promesas y Fetch API, consumo de servicios RESTful y almacenamiento local en el navegador. Herramientas: Visual Studio Code, Git, GitHub Pages, Chrome DevTools y linters de código. Al finalizar, el estudiante programa y despliega sitios web interactivos preparados para producción.', 
 'cat-001', 'niv-001', 40, 'Virtual', 0.00, TRUE),

('c0000002-0000-4000-8000-000000000002', 'Desarrollo Backend con Java y Spring Boot', 
 'Prerrequisitos: Fundamentos de programación orientada a objetos en Java. Temario: Creación de APIs REST empresariales con Spring Boot 3 y Java 17, arquitectura en capas, inyección de dependencias, persistencia relacional con Spring Data JPA y Hibernate, validación declarativa con Jakarta Validation, manejo centralizado de excepciones y documentación interactiva con OpenAPI Swagger. Herramientas: IntelliJ IDEA, Maven, MySQL, Postman y Docker. Al completar el curso, el alumno construye servicios backend desacoplados y escalables.', 
 'cat-001', 'niv-002', 60, 'Virtual', 49.99, TRUE),

('c0000003-0000-4000-8000-000000000003', 'Frontend Moderno con React y TypeScript', 
 'Prerrequisitos: Dominio de JavaScript intermedio y nociones de maquetación CSS. Temario: Desarrollo de Single Page Applications (SPA) con React, componentes funcionales, Hooks estándar y personalizados, gestión del estado con Context API y Zustand, enrutamiento con React Router, tipado estático riguroso con TypeScript y consumo de APIs asíncronas. Herramientas: Vite, React DevTools, npm, ESLint y Tailwind CSS. El estudiante queda capacitado para construir interfaces de usuario reactivas, mantenibles y profesionales.', 
 'cat-001', 'niv-002', 50, 'Virtual', 39.99, TRUE),

('c0000004-0000-4000-8000-000000000004', 'Arquitectura de Microservicios con Spring Cloud y Docker', 
 'Prerrequisitos: Experiencia previa en desarrollo backend con Spring Boot y SQL. Temario: Diseño de arquitecturas distribuidas de microservicios, API Gateway con Spring Cloud Gateway, descubrimiento de servicios con Netflix Eureka, resiliencia y tolerancia a fallos con Resilience4j Circuit Breaker, configuración centralizada, balanceo de carga y trazabilidad distribuida. Herramientas: Docker, Docker Compose, Spring Cloud, Zipkin y RabbitMQ. Permite diseñar y orquestar ecosistemas backend empresariales de alta disponibilidad.', 
 'cat-001', 'niv-003', 70, 'Presencial', 150.00, TRUE),

-- Bases de Datos
('c0000005-0000-4000-8000-000000000005', 'Fundamentos de SQL y Modelado Relacional con MySQL', 
 'Prerrequisitos: Manejo básico de computadoras y lógica matemática elemental. Temario: Diseño de bases de datos relacionales, diagramas entidad-relación (ER), normalización de tablas hasta la tercera forma normal, sentencias DDL y DML en MySQL, consultas filtradas, uniones entre tablas mediante INNER y LEFT JOIN, subconsultas, agrupaciones con GROUP BY y funciones de agregación. Herramientas: MySQL Server, MySQL Workbench y scripts SQL. Otorga las bases sólidas para estructurar datos institucionales.', 
 'cat-002', 'niv-001', 35, 'Virtual', 0.00, TRUE),

('c0000006-0000-4000-8000-000000000006', 'Optimización y Administración de PostgreSQL', 
 'Prerrequisitos: Conocimientos intermedios de lenguaje SQL y bases de datos relacionales. Temario: Arquitectura interna de PostgreSQL, diseño de índices B-Tree, Hash y GIN, análisis de costos de planes de ejecución con EXPLAIN ANALYZE, optimización de consultas complejas, particionamiento declarativo de tablas, transacciones concurrentes con niveles de aislamiento MVCC y políticas de respaldo. Herramientas: PostgreSQL, pgAdmin, psql y PgBouncer. Forma especialistas en bases de datos de alto rendimiento.', 
 'cat-002', 'niv-002', 45, 'Virtual', 55.00, TRUE),

('c0000007-0000-4000-8000-000000000007', 'Bases de Datos Vectoriales y NoSQL para IA con Qdrant y MongoDB', 
 'Prerrequisitos: Programación básica en Python o JavaScript y nociones de bases de datos. Temario: Almacenamiento no relacional basado en documentos JSON con MongoDB, conceptos de indexación vectorial, generación de embeddings densos de alta dimensionalidad, bases de datos vectoriales con Qdrant, búsqueda de vecinos más cercanos (k-NN) por similitud coseno y filtrado por metadatos payload. Herramientas: Qdrant Vector DB, MongoDB Compass, Python y OpenRouter API. Prepara para implementar la memoria de agentes de IA.', 
 'cat-002', 'niv-003', 40, 'Virtual', 75.00, TRUE),

-- Inteligencia Artificial
('c0000008-0000-4000-8000-000000000008', 'Introducción a la Inteligencia Artificial y Machine Learning con Python', 
 'Prerrequisitos: Conceptos básicos de álgebra y nociones iniciales de programación. Temario: Principios del aprendizaje automático, preprocesamiento y limpieza de datos, algoritmos de aprendizaje supervisado (regresión lineal, regresión logística, árboles de decisión) y no supervisado (K-Means, reducción de dimensionalidad PCA), y métricas de exactitud y precisión. Herramientas: Python 3, Scikit-Learn, NumPy, Jupyter Notebooks y Google Colab. Habilita al estudiante para construir sus primeros modelos predictivos.', 
 'cat-003', 'niv-001', 45, 'Virtual', 0.00, TRUE),

('c0000009-0000-4000-8000-000000000009', 'Sistemas RAG y Aplicaciones con Modelos de Lenguaje (LLMs)', 
 'Prerrequisitos: Programación intermedia en Python y nociones de APIs REST. Temario: Arquitectura de Generación Aumentada por Recuperación (RAG), fragmentación semántica de documentos (chunking), generación de embeddings con modelos densos, indexación en bases de datos vectoriales Qdrant, inyección dinámica de contexto en prompts, mitigación de alucinaciones y llamadas a APIs de LLMs como GPT y OSS vía OpenRouter. Herramientas: Python, LangChain, Qdrant, OpenRouter y n8n. Ideal para desarrollar asistentes educativos.', 
 'cat-003', 'niv-002', 55, 'Virtual', 89.99, TRUE),

('c0000010-0000-4000-8000-000000000010', 'Deep Learning y Redes Neuronales con PyTorch', 
 'Prerrequisitos: Cálculo diferencial, álgebra lineal y experiencia sólida programando en Python. Temario: Fundamentos matemáticos del descenso por gradiente y backpropagation, diseño de perceptrones multicapa (MLP), redes neuronales convolucionales (CNN) para visión artificial, redes recurrentes y arquitectura Transformer para procesamiento de lenguaje natural (NLP). Herramientas: PyTorch, Torchvision, CUDA con aceleración GPU y HuggingFace Transformers. Dirigido a ingenieros que diseñan arquitecturas de IA.', 
 'cat-003', 'niv-003', 65, 'Presencial', 180.00, TRUE),

-- Automatización
('c0000011-0000-4000-8000-000000000011', 'Automatización de Flujos de Trabajo e Integraciones con n8n', 
 'Prerrequisitos: Conocimientos elementales de navegación web y concepto de servicios en línea. Temario: Fundamentos de automatización de flujos de trabajo sin código (low-code), disparadores por webhook y programación cron, manipulación y transformación de datos en formato JSON, nodos de control condicional, integración con APIs externas y envío automatizado de notificaciones. Herramientas: n8n Workflow Automation, Webhooks, Postman y JavaScript básico para expresiones. Capacita para agilizar procesos institucionales.', 
 'cat-004', 'niv-001', 30, 'Virtual', 0.00, TRUE),

('c0000012-0000-4000-8000-000000000012', 'Automatización de Procesos Empresariales (BPA) y Web Scraping con Python', 
 'Prerrequisitos: Conocimiento básico de sintaxis Python. Temario: Extracción sistemática de datos de páginas web mediante Web Scraping, manejo de sesiones y encabezados HTTP, interacción con navegadores mediante automatización headless, procesamiento por lotes de hojas de cálculo Excel y archivos PDF, y programación de scripts periódicos. Herramientas: Python, BeautifulSoup, Requests, Selenium WebDriver y Pandas. Permite reemplazar tareas administrativas repetitivas mediante bots de software.', 
 'cat-004', 'niv-002', 45, 'Virtual', 45.00, TRUE),

('c0000013-0000-4000-8000-000000000013', 'Automatización Robótica de Procesos (RPA) y Orquestación con Agentes', 
 'Prerrequisitos: Experiencia en automatización de scripts y desarrollo de software empresarial. Temario: Principios avanzados de RPA, orquestación de flujos de trabajo entre sistemas legados de escritorio y plataformas cloud, integración de agentes autónomos guiados por LLMs para toma de decisiones y extracción inteligente de datos en documentos no estructurados. Herramientas: n8n avanzado, Python, OpenRouter y Docker. Forma especialistas en modernización y transformación digital empresarial.', 
 'cat-004', 'niv-003', 50, 'Virtual', 85.00, TRUE),

-- Análisis de Datos
('c0000014-0000-4000-8000-000000000014', 'Análisis de Datos y Visualización con Python y Pandas', 
 'Prerrequisitos: Habilidades informáticas básicas y nociones elementales de estadística. Temario: Adquisición, limpieza y transformación de datos tabulares, manejo de valores nulos y formatos de fecha con Pandas, cálculo de estadísticas descriptivas con NumPy y diseño de gráficos analíticos claros para presentación de hallazgos. Herramientas: Python, Pandas, Matplotlib, Seaborn y Jupyter Notebooks. El estudiante adquiere la competencia para realizar análisis exploratorio de datos (EDA) con rigor analítico.', 
 'cat-005', 'niv-001', 40, 'Virtual', 0.00, TRUE),

('c0000015-0000-4000-8000-000000000015', 'Construcción de Dashboards Ejecutivos con Power BI y DAX', 
 'Prerrequisitos: Conocimiento de hojas de cálculo Excel a nivel intermedio. Temario: Conexión y transformación de fuentes de datos heterogéneas mediante Power Query, modelado de datos en esquema de estrella, diseño de métricas y cálculos con fórmulas DAX (Data Analysis Expressions), inteligencia de tiempo y creación de paneles interactivos de control gerencial. Herramientas: Microsoft Power BI Desktop y Power BI Service. Capacita para comunicar información ejecutiva de alto impacto para toma de decisiones.', 
 'cat-005', 'niv-002', 35, 'Virtual', 35.00, TRUE),

('c0000016-0000-4000-8000-000000000016', 'Analítica Avanzada y Minería de Datos para Negocios', 
 'Prerrequisitos: Conocimientos previos en SQL y programación en Python para análisis de datos. Temario: Minería de datos aplicada a grandes volúmenes de información corporativa, análisis de correlación y causalidad, modelos de segmentación de clientes con RFM, pronósticos de demanda y series temporales, y visualizaciones complejas de impacto estratégico. Herramientas: Python, Scikit-Learn, Statsmodels, SQL analítico y Tableau. Dirigido a consultores y analistas de negocios con visión estratégica.', 
 'cat-005', 'niv-003', 55, 'Presencial', 120.00, TRUE),

-- Seguridad Informática
('c0000017-0000-4000-8000-000000000017', 'Fundamentos de Ciberseguridad y Seguridad de la Información', 
 'Prerrequisitos: Nociones básicas sobre redes informáticas y sistemas operativos. Temario: Principios de la tríada CIA (Confidencialidad, Integridad y Disponibilidad), vectores de ataque habituales como phishing, ingeniería social y malware, fundamentos de criptografía simétrica y asimétrica, protocolos seguros (HTTPS, SSH) y políticas de protección de contraseñas. Herramientas: Wireshark, Nmap básico, navegadores seguros y gestores de credenciales. Brinda las bases esenciales para proteger activos digitales.', 
 'cat-006', 'niv-001', 30, 'Virtual', 0.00, TRUE),

('c0000018-0000-4000-8000-000000000018', 'Protección y Seguridad en Aplicaciones Web (OWASP Top 10)', 
 'Prerrequisitos: Desarrollo web previo con backend en Java, Python o NodeJS. Temario: Identificación y remediación de vulnerabilidades web del catálogo OWASP Top 10: Inyección SQL, Cross-Site Scripting (XSS), falsificación de peticiones (CSRF), control de acceso roto (IDOR), configuración segura de cabeceras HTTP y mecanismos robustos de autenticación JWT y sesiones. Herramientas: Burp Suite Community, OWASP ZAP, Postman y Spring Security. Indispensable para desarrolladores de software seguro.', 
 'cat-006', 'niv-002', 45, 'Virtual', 65.00, TRUE),

('c0000019-0000-4000-8000-000000000019', 'Hacking Ético y Pruebas de Penetración (Pentesting)', 
 'Prerrequisitos: Conocimientos avanzados de redes TCP/IP, Linux y seguridad de aplicaciones. Temario: Fases de una prueba de penetración profesional (reconocimiento, escaneo, enumeración, explotación y post-explotación), análisis de vulnerabilidades en infraestructura y servidores, técnicas de escalada de privilegios y redacción de informes técnicos de remediación para directivos. Herramientas: Kali Linux, Metasploit Framework, Nmap, Hydra y John the Ripper. Forma consultores de ciberseguridad ofensiva.', 
 'cat-006', 'niv-003', 60, 'Presencial', 210.00, TRUE),

-- DevOps
('c0000020-0000-4000-8000-000000000020', 'Fundamentos de Linux y Control de Versiones con Git y GitHub', 
 'Prerrequisitos: Uso cotidiano de computadoras personales. Temario: Administración de sistemas Linux por línea de comandos (Bash), estructura del árbol de directorios, permisos POSIX, gestión de procesos y paquetes de software, control de versiones distribuido con Git, creación y fusión de ramas, resolución de conflictos y flujo de trabajo colaborativo mediante Pull Requests en GitHub. Herramientas: Ubuntu Server, Bash terminal, Git y GitHub. Constituye la base técnica fundamental para el desarrollo moderno.', 
 'cat-007', 'niv-001', 30, 'Virtual', 0.00, TRUE),

('c0000021-0000-4000-8000-000000000021', 'Despliegue y Contenerización de Aplicaciones con Docker', 
 'Prerrequisitos: Manejo de comandos Linux y desarrollo de aplicaciones en cualquier lenguaje. Temario: Conceptos de aislamiento y contenedores, anatomía de un contenedor vs máquina virtual, creación de imágenes reproducibles mediante Dockerfile con compilación multi-etapa (multi-stage builds), gestión de volúmenes para almacenamiento persistente, redes virtuales internas y orquestación con Docker Compose. Herramientas: Docker Engine, Docker CLI y Docker Compose. Facilita entornos reproducibles sin conflictos.', 
 'cat-007', 'niv-002', 40, 'Virtual', 45.00, TRUE),

('c0000022-0000-4000-8000-000000000022', 'Integración y Entrega Continua (CI/CD) con GitHub Actions', 
 'Prerrequisitos: Manejo de Git, Docker y pruebas unitarias de software. Temario: Fundamentos de la cultura DevOps y automatización de despliegues, diseño de canalizaciones de CI/CD con GitHub Actions, ejecución automática de pruebas en cada commit, análisis de cobertura y calidad estática de código, gestión de secretos de entorno y despliegue continuo hacia servidores en la nube. Herramientas: GitHub Actions, YAML, SonarQube y servidores Linux. Acelera el ciclo de entrega de valor.', 
 'cat-007', 'niv-002', 40, 'Virtual', 55.00, TRUE),

('c0000023-0000-4000-8000-000000000023', 'Orquestación de Contenedores a Gran Escala con Kubernetes', 
 'Prerrequisitos: Experiencia en contenedores Docker y redes de microservicios. Temario: Arquitectura de clústeres Kubernetes (control plane y worker nodes), definición declarativa de recursos (Pods, Deployments, ReplicaSets, Services, Ingress), gestión de configuración con ConfigMaps y Secrets, escalado horizontal automático (HPA), estrategias de actualización continua (Rolling Updates) y monitoreo. Herramientas: Minikube, kubectl, Helm, Prometheus y Grafana. Dirigido a ingenieros de confiabilidad de sitios (SRE).', 
 'cat-007', 'niv-003', 65, 'Presencial', 240.00, TRUE);

-- 4. Estudiantes semilla (10 estudiantes con perfiles diversos, correos únicos y niveles válidos)
INSERT IGNORE INTO usuario (id, email, password, rol) VALUES
('est-001', 'juan.perez@universidad.edu', 'password123', 'ESTUDIANTE'),
('est-002', 'maria.rodriguez@universidad.edu', 'password123', 'ESTUDIANTE'),
('est-003', 'carlos.mendoza@universidad.edu', 'password123', 'ESTUDIANTE'),
('est-004', 'laura.morales@universidad.edu', 'password123', 'ESTUDIANTE'),
('est-005', 'andres.castro@universidad.edu', 'password123', 'ESTUDIANTE'),
('est-006', 'valentina.rios@universidad.edu', 'password123', 'ESTUDIANTE'),
('est-007', 'diego.fernandez@universidad.edu', 'password123', 'ESTUDIANTE'),
('est-008', 'camila.torres@universidad.edu', 'password123', 'ESTUDIANTE'),
('est-009', 'gabriel.navarro@universidad.edu', 'password123', 'ESTUDIANTE'),
('est-010', 'sofia.vargas@universidad.edu', 'password123', 'ESTUDIANTE');

INSERT IGNORE INTO estudiante (id, nombre, nivel_experiencia, area_interes) VALUES
('est-001', 'Juan Pérez', 'Principiante', 'Desarrollo Web'),
('est-002', 'María Rodríguez', 'Intermedio', 'Inteligencia Artificial'),
('est-003', 'Carlos Mendoza', 'Avanzado', 'DevOps y Cloud'),
('est-004', 'Laura Morales', 'Principiante', 'Análisis de Datos'),
('est-005', 'Andrés Castro', 'Intermedio', 'Desarrollo Backend'),
('est-006', 'Valentina Ríos', 'Avanzado', 'Ciberseguridad'),
('est-007', 'Diego Fernández', 'Principiante', 'Automatización de Procesos'),
('est-008', 'Camila Torres', 'Intermedio', 'Bases de Datos y SQL'),
('est-009', 'Gabriel Navarro', 'Avanzado', 'Arquitectura de Microservicios'),
('est-010', 'Sofía Vargas', 'Principiante', 'Frontend y UI/UX');

-- 5. Administrador semilla
INSERT IGNORE INTO usuario (id, email, password, rol) VALUES
('adm-001', 'admin@rutaia.edu', 'admin123', 'ADMINISTRADOR');

INSERT IGNORE INTO administrador (id, nombre) VALUES
('adm-001', 'Administrador Principal');

-- 6. Consultas semilla de prueba (8 dentro del catálogo con recomendación y 2 fuera de catálogo 'Sin resultados')
INSERT IGNORE INTO consulta (id, estudiante_id, pregunta, fecha, estado) VALUES
('con-001', 'est-001', 'Quiero aprender a programar páginas web interactivas desde cero con JavaScript.', '2026-09-20 10:00:00', 'Respondida'),
('con-002', 'est-005', 'Busco especializarme en desarrollo backend empresarial usando Java y Spring Boot.', '2026-09-21 11:30:00', 'Respondida'),
('con-003', 'est-002', 'Deseo construir asistentes inteligentes y aplicar técnicas de RAG con modelos de lenguaje.', '2026-09-21 14:15:00', 'Respondida'),
('con-004', 'est-004', 'Me interesa aprender análisis y visualización de datos cuantitativos con Python y Pandas.', '2026-09-22 09:20:00', 'Respondida'),
('con-005', 'est-003', 'Quiero aprender a desplegar aplicaciones contenerizadas utilizando Docker y Kubernetes.', '2026-09-22 16:40:00', 'Respondida'),
('con-006', 'est-006', 'Busco formarme en auditoría de seguridad y pruebas de penetración contra vulnerabilidades web.', '2026-09-23 10:10:00', 'Respondida'),
('con-007', 'est-007', 'Necesito automatizar tareas operativas de oficina y flujos de integración sin código con n8n.', '2026-09-23 15:30:00', 'Respondida'),
('con-008', 'est-008', 'Quiero dominar el diseño de bases de datos relacionales y optimización de consultas SQL.', '2026-09-24 11:00:00', 'Respondida'),
('con-009', 'est-001', 'Quiero aprender cocina italiana tradicional, preparación de pastas frescas y salsas mediterráneas.', '2026-09-24 14:00:00', 'Sin resultados'),
('con-010', 'est-004', 'Deseo tomar un taller de carpintería y restauración de muebles antiguos de madera.', '2026-09-24 15:30:00', 'Sin resultados');

-- 7. Recomendaciones semilla vinculadas a las consultas de prueba
INSERT IGNORE INTO recomendacion (id, consulta_id, contenido, fecha, estado) VALUES
('rec-001', 'con-001', 'Para iniciar en el desarrollo web te recomendamos nuestro curso de Desarrollo Web con HTML5, CSS3 y JavaScript, donde aprenderás las bases de estructuración, estilos responsivos y manipulación dinámica del DOM.', '2026-09-20 10:00:05', 'Respondida'),
('rec-002', 'con-002', 'Para backend profesional, te sugerimos nuestro curso de Desarrollo Backend con Java y Spring Boot 3, complementándolo con el curso de Fundamentos de SQL y Modelado Relacional para el diseño adecuado de bases de datos.', '2026-09-21 11:30:05', 'Respondida'),
('rec-003', 'con-003', 'Tu perfil encaja perfectamente con el curso de Sistemas RAG y Aplicaciones con Modelos de Lenguaje (LLMs) con Qdrant y OpenRouter, precedido por Introducción a Machine Learning con Python.', '2026-09-21 14:15:05', 'Respondida'),
('rec-004', 'con-004', 'Te sugerimos iniciar con Análisis de Datos y Visualización con Python y Pandas, y posteriormente potenciar tus informes con Construcción de Dashboards Ejecutivos con Power BI y DAX.', '2026-09-22 09:20:05', 'Respondida'),
('rec-005', 'con-005', 'Para infraestructura moderna y despliegue ágil, te recomendamos la ruta de Despliegue y Contenerización con Docker y Orquestación de Contenedores a Gran Escala con Kubernetes.', '2026-09-22 16:40:05', 'Respondida'),
('rec-006', 'con-006', 'Para especializarte en ciberseguridad, te sugerimos Protección y Seguridad en Aplicaciones Web (OWASP Top 10) y el programa avanzado de Hacking Ético y Pruebas de Penetración con Kali Linux.', '2026-09-23 10:10:05', 'Respondida'),
('rec-007', 'con-007', 'Te recomendamos el curso de Automatización de Flujos de Trabajo e Integraciones con n8n, el cual te permitirá crear workflows automatizados sin complejidad técnica.', '2026-09-23 15:30:05', 'Respondida'),
('rec-008', 'con-008', 'Para bases de datos, te sugerimos Fundamentos de SQL y Modelado Relacional con MySQL y el curso de Optimización y Administración de PostgreSQL para tuning avanzado.', '2026-09-24 11:00:05', 'Respondida'),
('rec-009', 'con-009', 'Ningún curso del catálogo actual cubre gastronomía o cocina internacional. El catálogo institucional se especializa en tecnologías de la información, ingeniería de software e inteligencia artificial.', '2026-09-24 14:00:05', 'Sin resultados'),
('rec-010', 'con-010', 'El catálogo institucional no dispone de ofertas académicas en oficios manuales o carpintería. Te invitamos a explorar nuestras categorías de programación, datos y ciberseguridad.', '2026-09-24 15:30:05', 'Sin resultados');

-- 8. Fuentes semilla vinculadas a las recomendaciones respondidas
INSERT IGNORE INTO fuente (id, recomendacion_id, curso_id, similitud) VALUES
('fue-001', 'rec-001', 'c0000001-0000-4000-8000-000000000001', 0.94),
('fue-002', 'rec-002', 'c0000002-0000-4000-8000-000000000002', 0.96),
('fue-003', 'rec-002', 'c0000005-0000-4000-8000-000000000005', 0.86),
('fue-004', 'rec-003', 'c0000009-0000-4000-8000-000000000009', 0.95),
('fue-005', 'rec-003', 'c0000008-0000-4000-8000-000000000008', 0.88),
('fue-006', 'rec-004', 'c0000014-0000-4000-8000-000000000014', 0.93),
('fue-007', 'rec-004', 'c0000015-0000-4000-8000-000000000015', 0.87),
('fue-008', 'rec-005', 'c0000021-0000-4000-8000-000000000021', 0.95),
('fue-009', 'rec-005', 'c0000023-0000-4000-8000-000000000023', 0.91),
('fue-010', 'rec-006', 'c0000018-0000-4000-8000-000000000018', 0.93),
('fue-011', 'rec-006', 'c0000019-0000-4000-8000-000000000019', 0.89),
('fue-012', 'rec-007', 'c0000011-0000-4000-8000-000000000011', 0.96),
('fue-013', 'rec-008', 'c0000005-0000-4000-8000-000000000005', 0.94),
('fue-014', 'rec-008', 'c0000006-0000-4000-8000-000000000006', 0.89);

-- 9. Calificaciones semilla (6 calificaciones distribuidas con notas de 1 a 5 y comentarios cualitativos)
INSERT IGNORE INTO calificacion (id, estudiante_id, recomendacion_id, puntuacion, comentario) VALUES
('cal-001', 'est-001', 'rec-001', 5, 'Excelente recomendación, el curso de JavaScript era exactamente lo que buscaba para empezar.'),
('cal-002', 'est-005', 'rec-002', 5, 'Muy acertada la combinación de Spring Boot con modelado relacional.'),
('cal-003', 'est-002', 'rec-003', 4, 'Buena sugerencia sobre RAG y bases de datos vectoriales con Qdrant.'),
('cal-004', 'est-004', 'rec-004', 5, 'La ruta sugerida de Python y Power BI me dio una visión clara de mi plan de estudios.'),
('cal-005', 'est-003', 'rec-005', 4, 'Muy buena orientación para infraestructura con Docker y Kubernetes.'),
('cal-006', 'est-006', 'rec-006', 5, 'Excelente precisión, las fuentes de ciberseguridad web se adaptaron a mi nivel avanzado.');
