# Resumen Completo: Gestión de la Memoria (RAM, Segmentación, Paginación y Memoria Virtual)

Este documento sirve como guía de estudio completa basada en el archivo "01Memoria.pdf" para responder cualquier pregunta de examen sobre la gestión de memoria.

---

## 1. Conceptos Fundamentales
- **Bit**: Unidad mínima (0 o 1). Demasiado pequeña para información significativa.
- **Byte**: 8 bits.
- **Palabra**: Secuencia de $N$ bits (potencia de 2, ej. 32, 64). Es la **unidad mínima de transmisión y almacenamiento de datos**. 
    - *Ejemplo*: Si transmitimos 40 bits con palabras de 32 bits, necesitamos 2 palabras (desperdiciando 24 bits).
- **Instrucción**: Un programa ejecutable ocupa una palabra por instrucción.
- **Bloque**: Conjunto grande de bytes (32K, 64K), unidad mínima de transmisión en buses.
- **RAM (Random Access Memory)**: Hardware veloz, no persistente, que almacena datos y metadatos de procesos en ejecución. Se comporta lógicamente como un "hilo muy grande" con el espesor de una palabra.

### Direccionamiento
- **DB (Dirección Base)**: Dirección de inicio de un proceso en la RAM.
- **dv (Dirección Virtual/Lógica)**: Dirección de la palabra en el programa ejecutable (CPU).
- **df (Dirección Física/Real)**: Dirección real en la RAM.
- **Fórmula Fundamental**: $\text{df} = \text{DB} + \text{dv}$

### Componentes Clave
- **MMU (Memory Management Unit)**: Ubicada en la CPU. Controla la memoria y contiene la DB de los procesos.
- **TLB (Transaction Lookaside Buffer)**: Caché de hardware en la MMU que almacena traducciones recientes de $\text{dv} \to \text{df}$ para acelerar el acceso.
- **DMA (Direct Memory Access)**: Caché inteligente para intercomunicar RAM, CPU y E/S.

---

## 2. Estrategias de Alojamiento de Procesos

### A. Particiones Estáticas
- La RAM se divide en particiones de **tamaño fijo**.
- Un proceso se carga si su tamaño $\le$ capacidad de la partición. Si es mayor, ocupa 2 o más particiones.

### B. Particiones Dinámicas
- La RAM se divide en particiones del **tamaño exacto del proceso** al cargarse.
- **Problema**: Al finalizar procesos, quedan huecos vacíos (**fragmentos libres**).
- **Solución**: **Compactación de la RAM** (reorganizar procesos para unir fragmentos libres).

### C. Segmentación
- Divide el proceso en **segmentos lógicos** (Código, Datos, Pila/Stack, Montón/Heap).
- Los segmentos se cargan desde los extremos, dejando espacio de reserva en el centro.
- **Traducción**: $\text{dv} = \text{segmento} + \text{desplazamiento}$.
- **df** = $\text{DB del segmento} + \text{desplazamiento}$.

### D. Paginación
- Divide el proceso en **páginas** de tamaño fijo ($N$ líneas, potencia de 16, ej. 64K).
- La RAM se divide en **marcos de página** (frames) de la misma capacidad.
- **Direccionamiento**:
    - $\text{dv} = \text{pa} \text{li}$ (página y línea).
    - $\text{df} = \text{mpa} \text{li}$ (marco de página y línea).
- **Ventaja**: La conversión $\text{dv} \leftrightarrow \text{df}$ es rapidísima porque es una **concatenación** ($\text{pa} \to \text{mpa}$) y no una suma/resta.
- **Tabla de Marcos de Página**: Mapea $\text{pa} \to \text{mpa}$.

---

## 3. Memoria Virtual (por Paginación)
Permite que la CPU crea que la RAM es mucho más grande de lo que físicamente es, usando el disco como extensión.

- **Ubicación**:
    - Marcos de disco: Todas las páginas de los procesos preparados.
    - Marcos de RAM: Solo algunas páginas en ejecución.
- **Fallo de Página**: Cuando la CPU solicita una página que no está en la RAM ($\text{bv: bit de validez} = 1$), el SO la trae del disco.

### Algoritmos de Reemplazo de Marcos (Cuando la RAM está llena)
Cuando hay que cargar una página y no hay marcos libres, se debe desalojar una página existente:

| Método | Criterio de Reemplazo | Implementación |
| :--- | :--- | :--- |
| **FIFO** (First In, First Out) | El que lleva más tiempo en memoria (el más viejo). | Usa columna **TCP** (Tiempo de Carga). Reemplaza el menor TCP. |
| **LRU** (Least Recently Used) | El que lleva más tiempo sin ser referenciado. | Usa un **contador de referencias (C)**. Reemplaza el mayor C. |
| **NRU** (Not Recently Used) | El que no se haya usado recientemente. | Usa un **bit de Referencia (R)**. Si $R=0$, es candidato a reemplazo. |
| **Algoritmo del Reloj** | Similar a FIFO pero con referenciación. | Usa un **puntero circular** y el **bit R**. Si $R=1$, lo pone en $0$ y avanza; si $R=0$, lo reemplaza. |

---

## 4. Resumen de Conversiones Rápidas para el Examen

1. **Particiones/Segmentación**: $\text{df} = \text{DB} + \text{dv}$ (Suma $\to$ Lento).
2. **Paginación**: $\text{df} = \text{concat}(\text{mpa}, \text{li})$ (Concatenación $\to$ Rápido).
3. **Cálculo de Capacidad (Paginación)**:
    - Si las líneas se numeran con 2 cifras hex (00...FF) $\to 2^{16}$ instrucciones.
    - Máximo palabras en un proceso (6 dígitos hex $\text{FFFFFF}$) $\to 16^6 = 16 \text{ MB}$.
    - Máximo palabras en RAM (7 dígitos hex $\text{FFFFFFF}$) $\to 16^7 = 256 \text{ MB}$.
