# Resoluciones Detalladas - Práctica Dirigida 2
**Curso:** Análisis y Modelamiento Numérico I
**Temas:** Estabilidad, Normas y Condicionamiento

---

## Problema 1: Análisis de Estabilidad en Recurrencias

**Enunciado:**
Muestre que la sucesión:
$I_0 = \log\left(\frac{6}{\pi}\right), \quad I_k = \frac{1}{k} - 5I_{k-1}, \quad k=1, 2, 3, \dots$
no es apropiada para aproximar la integral $I_n = \int_0^1 \frac{x^n}{x+5} dx$ (corregido el límite y la variable según el contexto estándar de este problema), a pesar de que funciona en aritmética infinita.

### Resolución Detallada:

#### 1. Verificación de la Recurrencia (Aritmética Infinita)
Primero, veamos por qué la fórmula es matemáticamente correcta. Partimos de la integral:
$$I_k = \int_0^1 \frac{x^k}{x+5} dx$$
Sumamos y restamos $5x^{k-1}$ en el numerador:
$$I_k = \int_0^1 \frac{x^k + 5x^{k-1} - 5x^{k-1}}{x+5} dx = \int_0^1 \frac{x^{k-1}(x+5) - 5x^{k-1}}{x+5} dx$$
$$I_k = \int_0^1 x^{k-1} dx - 5 \int_0^1 \frac{x^{k-1}}{x+5} dx$$
$$I_k = \left[ \frac{x^k}{k} \right]_0^1 - 5 I_{k-1} \implies I_k = \frac{1}{k} - 5 I_{k-1}$$
La relación es matemáticamente exacta.

#### 2. Análisis de Estabilidad Numérica
El problema surge cuando implementamos esto en una computadora con precisión finita. Sea $\epsilon_k$ el error cometido en el paso $k$. La relación con error sería:
$$\hat{I}_k = \frac{1}{k} - 5 \hat{I}_{k-1}$$
Si definimos el error como $e_k = \hat{I}_k - I_k$, entonces:
$$e_k = -5 e_{k-1}$$
Esta es una ecuación en diferencias lineal homogénea. Su solución es:
$$e_n = e_0 (-5)^n$$

#### 3. Conclusión
El error inicial $e_0$ (por ejemplo, el error de redondeo al calcular $\log(6/\pi)$) se multiplica por $(-5)$ en cada iteración. 
- Para $n=10$, el error se amplifica por $(-5)^{10} \approx 9.7 \times 10^6$.
- Para $n=20$, el error se amplifica por $\approx 9.5 \times 10^{13}$.

Debido a que el factor de amplificación $|-5| > 1$, la recurrencia es **numéricamente inestable**. El error crece exponencialmente y rápidamente domina el valor real de la integral, haciendo que la sucesión sea inapropiada para el cálculo numérico.

---

## Problema 7: Sistemas con Matriz de Hilbert y Pivoteo

**Enunciado:**
Considere resolver el sistema $Hx = b$, donde $H(i,j) = 1/(i+j-1)$ y $b(j) = 1$. Compare:
a) Eliminación Gaussiana sin pivoteo.
b) Eliminación Gaussiana con pivoteo parcial.
c) Eliminación Gaussiana con pivoteo total.

### Resolución Detallada (para $n=3$):

El sistema es:
$$\begin{pmatrix} 1 & 1/2 & 1/3 \\ 1/2 & 1/3 & 1/4 \\ 1/3 & 1/4 & 1/5 \end{pmatrix} \begin{pmatrix} x_1 \\ x_2 \\ x_3 \end{pmatrix} = \begin{pmatrix} 1 \\ 1 \\ 1 \end{pmatrix}$$

#### a) Sin Pivoteo
Se procesa la matriz tal cual. 
1. Eliminamos $h_{21}$ y $h_{31}$ usando $h_{11}=1$ como pivote.
2. El nuevo pivote será $h_{22}' = 1/3 - (1/2)(1/2) = 1/12 \approx 0.0833$.
3. Este valor es pequeño. En matrices de Hilbert más grandes, estos pivotes se vuelven extremadamente pequeños, lo que provoca que las divisiones por números cercanos a cero amplifiquen masivamente los errores de redondeo.

#### b) Pivoteo Parcial
En cada columna $j$, buscamos el elemento con el mayor valor absoluto en la fila $i \ge j$ y movemos esa fila hacia arriba.
- En la columna 1, el máximo es $1$ (ya está en la posición 1,1).
- Tras la primera eliminación, buscamos el máximo en la columna 2 (filas 2 y 3).
El pivoteo parcial reduce la probabilidad de dividir por un número muy pequeño, mejorando la estabilidad, pero no elimina la mala condición intrínseca de la matriz de Hilbert.

#### c) Pivoteo Total
Buscamos el elemento máximo en **toda la submatriz** restante (filas y columnas). Esto implica intercambiar tanto filas como columnas (cambiando el orden de las variables $x_i$). 
Es el método más estable ya que garantiza el pivote más grande posible de todo el sistema disponible, minimizando el crecimiento del error.

#### Comparación de Precisión (Análisis Teórico):
- **Precisión Simple (float32):** Debido a que el número de condición de la matriz de Hilbert crece exponencialmente con $n$ ($K(H_3) \approx 524$), en precisión simple los errores de redondeo se acumulan rápidamente. Sin pivoteo, la solución puede divergir totalmente del valor real.
- **Precisión Doble (float64):** Ofrece más dígitos significativos, lo que permite "soportar" la mala condición de la matriz por más tiempo. Sin embargo, para $n > 12$, incluso la precisión doble falla.

---

## Problema 9: Error y Número de Condición

**Enunciado:**
Sistema $Ax = b$ con $A = \begin{pmatrix} 0.03 & 58.9 \\ 5.31 & -6.10 \end{pmatrix}$, $b = \begin{pmatrix} 59.2 \\ 47.0 \end{pmatrix}$.
Solución exacta: $x = (10, 1)^T$. Solución aprox: $\tilde{x} = (30, 0.990)^T$.
Calcular: $\|x - \tilde{x}\|_\infty$ y $\frac{K_\infty(A) \|b - A\tilde{x}\|_\infty}{\|A\|_\infty}$.

### Resolución Detallada:

#### 1. Cálculo del error absoluto $\infty$-norm
$$x - \tilde{x} = \begin{pmatrix} 10 \\ 1 \end{pmatrix} - \begin{pmatrix} 30 \\ 0.990 \end{pmatrix} = \begin{pmatrix} -20 \\ 0.010 \end{pmatrix}$$
$$\|x - \tilde{x}\|_\infty = \max(|-20|, |0.010|) = 20$$

#### 2. Cálculo de $\|A\|_\infty$
La norma $\infty$ de una matriz es la suma máxima de los valores absolutos de sus filas:
- Fila 1: $|0.03| + |58.9| = 58.93$
- Fila 2: $|5.31| + |-6.10| = 11.41$
$$\|A\|_\infty = 58.93$$

#### 3. Cálculo de $K_\infty(A)$
Primero hallamos $A^{-1}$. $\det(A) = (0.03)(-6.10) - (58.9)(5.31) = -0.183 - 312.759 = -312.942$.
$$A^{-1} = \frac{1}{-312.942} \begin{pmatrix} -6.10 & -58.9 \\ -5.31 & 0.03 \end{pmatrix}$$
Norma de $A^{-1}$:
- Fila 1: $\frac{|-6.10| + |-58.9|}{312.942} = \frac{65}{312.942} \approx 0.2077$
- Fila 2: $\frac{|-5.31| + |0.03|}{312.942} = \frac{5.34}{312.942} \approx 0.0170$
$\|A^{-1}\|_\infty = 0.2077$

$$K_\infty(A) = \|A\|_\infty \cdot \|A^{-1}\|_\infty = 58.93 \cdot 0.2077 \approx 12.24$$

#### 4. Cálculo del Residuo $r = b - A\tilde{x}$
$$A\tilde{x} = \begin{pmatrix} 0.03 & 58.9 \\ 5.31 & -6.10 \end{pmatrix} \begin{pmatrix} 30 \\ 0.990 \end{pmatrix} = \begin{pmatrix} 0.9 + 58.311 \\ 159.3 - 6.039 \end{pmatrix} = \begin{pmatrix} 59.211 \\ 153.261 \end{pmatrix}$$
$$r = \begin{pmatrix} 59.2 \\ 47.0 \end{pmatrix} - \begin{pmatrix} 59.211 \\ 153.261 \end{pmatrix} = \begin{pmatrix} -0.011 \\ -106.261 \end{pmatrix}$$
$$\|r\|_\infty = 106.261$$

#### 5. Resultado Final
Calculamos el cociente solicitado:
$$\text{Valor} = \frac{K_\infty(A) \cdot \|r\|_\infty}{\|A\|_\infty} = \frac{12.24 \cdot 106.261}{\|A\|_\infty}$$
Sustituyendo $\|A\|_\infty$:
$$\text{Valor} = \frac{1300.63}{58.93} \approx 22.07$$

**Análisis:** Observamos que la cota teórica ($22.07$) es cercana al error real calculado ($20$), lo cual valida la teoría de que el error en la solución está acotado por el producto del número de condición y la norma del residuo relativa a la norma de la matriz.
