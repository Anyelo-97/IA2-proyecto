### Base de datos
CREATE TABLE usuario (
    id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,

    CONSTRAINT chk_usuario_rol
        CHECK (rol IN ('ESTUDIANTE', 'ADMINISTRADOR'))
);


CREATE TABLE estudiante (
    id VARCHAR(255) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    nivel_experiencia VARCHAR(50) NOT NULL,
    area_interes VARCHAR(255) NOT NULL,

    CONSTRAINT fk_estudiante_usuario
        FOREIGN KEY (id) REFERENCES usuario(id)
);


CREATE TABLE administrador (
    id VARCHAR(255) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,

    CONSTRAINT fk_administrador_usuario
        FOREIGN KEY (id) REFERENCES usuario(id)
);


CREATE TABLE categoria (
    id VARCHAR(255) PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL
);


CREATE TABLE nivel_dificultad (
    id VARCHAR(255) PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL
);


CREATE TABLE curso (
    id VARCHAR(255) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    categoria_id VARCHAR(255) NOT NULL,
    nivel_id VARCHAR(255) NOT NULL,
    duracion INT NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_curso_categoria
        FOREIGN KEY (categoria_id) REFERENCES categoria(id),

    CONSTRAINT fk_curso_nivel
        FOREIGN KEY (nivel_id) REFERENCES nivel_dificultad(id),

    CONSTRAINT chk_curso_duracion
        CHECK (duracion > 0)
);


CREATE TABLE estudiante_curso (
    estudiante_id VARCHAR(255) NOT NULL,
    curso_id VARCHAR(255) NOT NULL,

    PRIMARY KEY (estudiante_id, curso_id),

    CONSTRAINT fk_estudiante_curso_estudiante
        FOREIGN KEY (estudiante_id) REFERENCES estudiante(id),

    CONSTRAINT fk_estudiante_curso_curso
        FOREIGN KEY (curso_id) REFERENCES curso(id)
);


CREATE TABLE consulta (
    id VARCHAR(255) PRIMARY KEY,
    estudiante_id VARCHAR(255) NOT NULL,
    pregunta VARCHAR(1000) NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_consulta_estudiante
        FOREIGN KEY (estudiante_id) REFERENCES estudiante(id)
);


CREATE TABLE recomendacion (
    id VARCHAR(255) PRIMARY KEY,
    consulta_id VARCHAR(255) NOT NULL,
    curso_id VARCHAR(255),
    contenido VARCHAR(2000) NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_recomendacion_consulta
        FOREIGN KEY (consulta_id) REFERENCES consulta(id),

    CONSTRAINT fk_recomendacion_curso
        FOREIGN KEY (curso_id) REFERENCES curso(id)
);


CREATE TABLE fuente (
    id VARCHAR(255) PRIMARY KEY,
    recomendacion_id VARCHAR(255) NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    url VARCHAR(1000) NOT NULL,

    CONSTRAINT fk_fuente_recomendacion
        FOREIGN KEY (recomendacion_id) REFERENCES recomendacion(id)
);


CREATE TABLE calificacion (
    id VARCHAR(255) PRIMARY KEY,
    estudiante_id VARCHAR(255) NOT NULL,
    recomendacion_id VARCHAR(255) NOT NULL,
    puntuacion INT NOT NULL,
    comentario VARCHAR(1000),

    CONSTRAINT fk_calificacion_estudiante
        FOREIGN KEY (estudiante_id) REFERENCES estudiante(id),

    CONSTRAINT fk_calificacion_recomendacion
        FOREIGN KEY (recomendacion_id) REFERENCES recomendacion(id),

    CONSTRAINT chk_calificacion_puntuacion
        CHECK (puntuacion BETWEEN 1 AND 5)
);
