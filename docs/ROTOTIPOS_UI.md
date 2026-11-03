# Prototipos de Interfaz de Usuario (UI) - BaccaratApp

Este documento describe la arquitectura y el diseño visual de las pantallas desarrolladas para la aplicación **BaccaratApp**.

---

## 1. Flujo de Navegación del Usuario

El flujo del juego sigue la estructura definida a continuación:

```mermaid
graph TD
    A[Inicio / Autenticación] --> B[Obtener o Crear Usuario en BD]
    B --> C[Ventana Principal - Mesa de Baccarat]
    C --> D[Seleccionar Apuesta: Jugador / Banquero / Empate]
    D --> E[Ejecutar Partida / Repartir Cartas]
    E --> F[Actualización de Saldo en Supabase]