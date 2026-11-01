# 🃏 Baccarat App (Punto Banco) en Java

Sistema interactivo para simular el juego de casino **Baccarat (Punto Banco)** desarrollado en Java. El sistema incluye lógica de reglas oficiales, cálculo automático de tercera carta, persistencia de usuarios y partidas en PostgreSQL, y pruebas unitarias con JUnit 5.

---

## 🎲 ¿Cómo se juega al Baccarat (Punto Banco)?

El Baccarat es un juego de cartas donde el objetivo es apostar a cuál de las dos manos (**Jugador** o **Banca**) obtendrá un puntaje total lo más cercano posible a **9**, o si la ronda terminará en un **Empate**.

### Reglas Principales:
* **Valor de las Cartas:**
    * **As:** 1 punto.
    * **2 al 9:** Su valor nominal.
    * **10, J, Q, K:** 0 puntos.
* **Cálculo del Puntaje:** Se suman las cartas de la mano y se aplica el **módulo 10** (por ejemplo, $7 + 6 = 13 \rightarrow$ la mano vale 3 puntos).
* **Manos Naturales:** Si el Jugador o la Banca suma **8 o 9** con sus dos primeras cartas, la ronda finaliza automáticamente sin repartir más cartas.
* **Tercera Carta Automática:** Si no hay un Natural, el juego evalúa si se debe repartir una tercera carta siguiendo la tabla matemática oficial. **El jugador no decide si pide carta**, el sistema lo ejecuta automáticamente.

---

## 📋 Requerimientos del Sistema

### 1. Requerimientos Funcionales
* **Gestión de Usuarios:** Registro e inicio de sesión con persistencia de saldo (saldo inicial predeterminado: $1,000.00).
* **Sistema de Apuestas:** Apuestas a Jugador (paga 1:1), Banca (paga 0.95:1 aplicando comisión del 5%) y Empate (paga 8:1).
* **Lógica del Juego:** Reparto de 2 cartas iniciales, evaluación de manos naturales y aplicación estricta de las reglas de tercera carta para Jugador y Banca.
* **Persistencia y Reportes:** Registro automático de las partidas en base de datos PostgreSQL (actualización de saldo, historial de jugadas y reportes históricos).

### 2. Requerimientos No Funcionales
* **Lenguaje y Entorno:** Desarrollado en Java 17+ con gestión de dependencias en Maven.
* **Base de Datos:** Persistencia en PostgreSQL conectada a través de JDBC (`org.postgresql`).
* **Pruebas Unitarias:** Cobertura de pruebas con JUnit 5 para la lógica de cartas, puntajes y manos naturales.
* **Arquitectura:** Estructura modular basada en capas (Paquetes: `datos`, `operaciones` y `vista`).