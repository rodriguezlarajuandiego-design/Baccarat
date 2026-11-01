-- Script SQL para PostgreSQL
CREATE DATABASE baccarat_db;

\c baccarat_db;

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    saldo NUMERIC(10, 2) NOT NULL DEFAULT 1000.00
);

CREATE TABLE historial_partidas (
    id SERIAL PRIMARY KEY,
    usuario_id INT REFERENCES usuarios(id),
    apuesta_tipo VARCHAR(20) NOT NULL,
    monto_apostado NUMERIC(10, 2) NOT NULL,
    resultado_juego VARCHAR(20) NOT NULL,
    estado_apuesta VARCHAR(20) NOT NULL,
    ganancia NUMERIC(10, 2) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
