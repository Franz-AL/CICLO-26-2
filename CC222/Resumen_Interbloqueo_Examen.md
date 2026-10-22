# 📘 Resumen Maestro: Interbloqueo e Inanición (Para Examen)

## 1. Conceptos Fundamentales de Recursos
### Tipos de Recursos
*   **Hardware:** Procesador, memoria (RAM, disco), red, monitor, impresora.
*   **Software:** Buffers, archivos, directorios.
*   **Nota:** Existen interbloqueos sin recursos involucrados, causados por falta de sincronización.

### Clasificación por Expropiabilidad
*   **Expropiables:** Pueden ser retirados del proceso sin causar daño (ej. Memoria RAM $\rightarrow$ swap en disco).
*   **No Expropiables:** No pueden retirarse sin que el proceso falle (ej. Unidad de CD, impresora). **Los interbloqueos suelen ocurrir con recursos no expropiables.**

### Protocolo de Utilización
1. Solicitar recurso $\rightarrow$ 2. Utilizar recurso $\rightarrow$ 3. Liberar inmediatamente.
*   **Si el recurso no está disponible:** El sistema puede **bloquear** el proceso hasta que esté libre o hacer que la solicitud **falle** con un código de error.

---

## 2. El Interbloqueo (Deadlock)
### Definición
Situación en la que un conjunto de procesos/hebras está bloqueado porque cada uno espera un suceso que solo puede ser causado por otro proceso del mismo conjunto.
**Resultado:** Ningún proceso puede ejecutarse, liberar recursos ni ser despertado.

### Las 4 Condiciones Necesarias (Simultáneas)
Para que exista un interbloqueo, deben cumplirse estas cuatro condiciones:
1.  **Exclusividad:** El recurso es asignado a un solo proceso a la vez o está libre.
2.  **Retención y Espera:** Un proceso que ya tiene recursos puede solicitar nuevos.
3.  **No Expropiación:** Los recursos solo se liberan voluntariamente por el poseedor.
4.  **Espera Circular:** Existe una cadena donde $P_1$ espera a $P_2$, $P_2$ a $P_3...$ y $P_n$ espera a $P_1$.

---

## 3. Estrategias de Resolución de Interbloqueos

### A. Prevenir (Anular una condición necesaria)
*   **Atacar Exclusividad:** Usar *spooling* (colas) o virtualización (multiplexación de tiempo).
*   **Atacar Retención y Espera:** Obligar al proceso a solicitar **todos** los recursos al inicio. (*Inconveniente: Ineficiente, espera larga*).
*   **Atacar No Expropiación:** Expropiar recursos (requiere guardar el estado del recurso para devolverlo).
*   **Atacar Espera Circular:** Imponer un **orden estricto** de asignación de recursos (solo se puede pedir el recurso $j$ si $j > i$).

### B. Evitar (Análisis dinámico)
El sistema decide si conceder un recurso basándose en si el estado resultante es **Seguro**.
*   **Estado Seguro:** Existe al menos una secuencia de ejecución que permite que todos los procesos finalicen.
*   **Algoritmo del Banquero:** 
    *   Lleva control de: Recursos Totales, Asignados, Disponibles y la Demanda Máxima de cada proceso.
    *   Solo concede el recurso si, tras la asignación, el sistema permanece en un estado seguro.
    *   **Complejidad:** $O(n^2 m)$, donde $n$ es el número de hebras y $m$ el de recursos.

### C. Detectar y Recuperar
Se permite que el interbloqueo ocurra, se detecta mediante un grafo de asignación y se resuelve.
*   **Detección:** Se busca ciclos en el grafo de recursos.
*   **Recuperación:**
    1.  **Finalización:** Matar todos los procesos interbloqueados (burdo pero eficaz) o matarlos uno a uno hasta romper el ciclo (consume más recursos pero aprovecha más el trabajo).
    2.  **Expropiación:** Quitar el recurso a una "víctima" basándose en criterios (prioridad, tiempo de ejecución, cantidad de recursos).
    3.  **Vuelta atrás (Rollback):** Reiniciar el proceso a un estado previo guardado (checkpoint).

### D. Ignorar (Algoritmo del Avestruz)
Suponer que el problema no existe. Es razonable si los interbloqueos son muy raros y el coste de prevenirlos es demasiado alto. **Utilizado por Linux y Windows.**

---

## 4. Políticas Combinadas y Otros Conceptos
*   **Políticas Combinadas:** Agrupar recursos en categorías (swap, E/S, memoria), usar prevención de cola circular entre categorías y el método más apropiado dentro de cada una.
*   **Inanición (Starvation):** Situación donde un proceso nunca recibe los recursos que necesita porque otros siempre tienen prioridad.

---

### 💡 Tips para el Examen:
*   Si te piden **diferencia entre Prevenir y Evitar**: Prevenir ataca las *condiciones* (estático), Evitar analiza el *estado* antes de asignar (dinámico).
*   Si te preguntan por el **Algoritmo del Banquero**, recuerda que su clave es la **Matriz de Solicitudes Máximas** y la búsqueda de una **secuencia segura**.
*   Si mencionan **Linux/Windows**, la respuesta suele ser el **Algoritmo del Avestruz** (Ignorar).
