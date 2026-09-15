# Resoluciones de la Práctica Dirigida 2 - Análisis y Modelamiento Numérico I

Este documento contiene las resoluciones detalladas de los problemas 1, 7 y 9 de la Práctica Dirigida 2.

---

## Problema 1: Análisis de Estabilidad de una Sucesión

**Enunciado:**
Muestre que la siguiente sucesión:
$I_0 = \log\left(\frac{6}{\pi}\right), \quad I_k + 5I_{k-1} = \frac{1}{k}, \quad k=1, 2, 3, \dots$
no es apropiada para aproximar la integral $I_\infty = \int_0^\infty \frac{x^n}{5+x} dx$ (considerando el contexto de estabilidad numérica), a pesar de que funcione en aritmética infinita.

**Resolución:**

1. **Análisis de la Recurrencia:**
   La sucesión está definida por una relación de recurrencia lineal de primer orden:
   $$I_k = \frac{1}{k} - 5I_{k-1}$$

2. **Solución General:**
   La solución de esta recurrencia se compone de una solución particular ($I_k^p$) y una solución a la ecuación homogénea asociada ($I_k^h$):
   - Ecuación homogénea: $I_k + 5I_{k-1} = 0 \implies I_k^h = C(-5)^k$.
   - Solución general: $I_k = I_k^p + C(-5)^k$.

3. **Inestabilidad Numérica:**
   En aritmética infinita (precisión exacta), si el valor inicial $I_0$ es exacto, la constante $C$ sería tal que el término $(-5)^k$ no domine o se cancele con la solución particular para converger a $I_\infty$. Sin embargo, en computación real:
   - Cualquier error de redondeo $\epsilon$ en el cálculo de $I_0$ o en las iteraciones sucesivas introduce un componente en la solución homogénea.
   - El término de error crece exponencialmente como $(-5)^k$. Por ejemplo, para $k=10$, el error se amplifica por $(-5)^{10} \approx 9.7 \times 10^6$.

4. **Conclusión:**
   Debido a que el factor de la solución homogénea $|-5| > 1$, la recurrencia es **numéricamente inestable**. Cualquier pequeña perturbación inicial crece rápidamente, haciendo que la sucesión diverja del valor real de la integral. Por lo tanto, no es apropiada para el cálculo numérico.

---

## Problema 7: Sistemas con Matrices de Hilbert

**Enunciado:**
Considere resolver el sistema $Hx = b$, donde $H(i,j) = 1/(i+j-1)$ y $b(j) = 1$ para todo $j$. Compare la resolución mediante:
a) Eliminación gaussiana sin pivoteo.
b) Eliminación gaussiana con pivoteo parcial.
c) Eliminación gaussiana con pivoteo total.
Utilice precisión simple y doble.

**Resolución:**

1. **Naturaleza de la Matriz de Hilbert ($H$):**
   La matriz de Hilbert es el ejemplo clásico de una matriz **mal condicionada**. El número de condición $\text{cond}(H)$ crece exponencialmente con el tamaño de la matriz $n$. Esto significa que pequeños cambios en $b$ o errores de redondeo durante la eliminación producen cambios masivos en la solución $x$.

2. **Comparativa de Métodos:**
   - **Sin pivoteo:** Es el método más propenso a errores. Si un elemento de la diagonal es muy pequeño (algo común en $H$), el divisor en la eliminación es pequeño, amplificando drásticamente el error de redondeo.
   - **Pivoteo Parcial:** Al elegir el elemento más grande de la columna actual como pivote, se reduce la magnitud de los multiplicadores ($\le 1$), lo que ayuda a mitigar la propagación del error, aunque no elimina la mal condición inherente de la matriz.
   - **Pivoteo Total:** Es el más robusto, ya que busca el elemento máximo en toda la submatriz restante. Ofrece la mayor estabilidad numérica posible para la eliminación gaussiana.

3. **Impacto de la Precisión:**
   - **Precisión Simple (float32):** Debido a que la matriz es mal condicionada, la precisión simple suele ser insuficiente. Los errores de redondeo consumen rápidamente los dígitos significativos, resultando en soluciones que pueden ser completamente erróneas incluso con pivoteo total.
   - **Precisión Doble (float64):** Permite manejar un rango mucho mayor de precisión, retrasando la divergencia. Se obtienen resultados mucho más cercanos a la solución exacta, aunque para $n$ suficientemente grande (ej. $n > 12$), incluso la precisión doble falla.

---

## Problema 9: Análisis de Error y Número de Condición

**Enunciado:**
El sistema lineal $Ax = b$:
$$ \begin{pmatrix} 0.03 & 58.9 \\ 5.31 & -6.10 \end{pmatrix} \begin{pmatrix} x_1 \\ x_2 \end{pmatrix} = \begin{pmatrix} 59.2 \\ 47.0 \end{pmatrix} $$
tiene solución exacta $x = (10, 1)^T$ y solución aproximada $\tilde{x} = (30, 0.990)^T$. Calcule $\|x - \tilde{x}\|_\infty$ y el límite superior del error usando $K_\infty(A)$.

**Resolución:**

1. **Cálculo del Error Absoluto ($\|x - \tilde{x}\|_\infty$):**
   $$ e = x - \tilde{x} = \begin{pmatrix} 10 \\ 1 \end{pmatrix} - \begin{pmatrix} 30 \\ 0.990 \end{pmatrix} = \begin{pmatrix} -20 \\ 0.01 \end{pmatrix} $$
   La norma infinito es el máximo valor absoluto:
   $$ \|x - \tilde{x}\|_\infty = \max(|-20|, |0.01|) = \mathbf{20} $$

2. **Cálculo de la Norma de la Matriz $\|A\|_\infty$:**
   Suma máxima de filas:
   - Fila 1: $|0.03| + |58.9| = 58.93$
   - Fila 2: $|5.31| + |-6.10| = 11.41$
   $$ \|A\|_\infty = \mathbf{58.93} $$

3. **Cálculo de la Matriz Inversa $A^{-1}$ y $\|A^{-1}\|_\infty$:**
   $\det(A) = (0.03)(-6.10) - (58.9)(5.31) = -0.183 - 312.759 = -312.942$
   $$ A^{-1} = \frac{1}{-312.942} \begin{pmatrix} -6.10 & -58.9 \\ -5.31 & 0.03 \end{pmatrix} \approx \begin{pmatrix} 0.0195 & 0.1882 \\ 0.0170 & -0.0001 \end{pmatrix} $$
   Norma infinito de $A^{-1}$:
   - Fila 1: $0.0195 + 0.1882 = 0.2077$
   - Fila 2: $0.0170 + 0.0001 = 0.0171$
   $$ \|A^{-1}\|_\infty = \mathbf{0.2077} $$

4. **Número de Condición $K_\infty(A)$:**
   $$ K_\infty(A) = \|A\|_\infty \cdot \|A^{-1}\|_\infty = 58.93 \times 0.2077 \approx \mathbf{12.24} $$

5. **Cálculo del Residuo $r = b - A\tilde{x}$:**
   $$ A\tilde{x} = \begin{pmatrix} 0.03(30) + 58.9(0.990) \\ 5.31(30) - 6.10(0.990) \end{pmatrix} = \begin{pmatrix} 0.9 + 58.311 \\ 159.3 - 6.039 \end{pmatrix} = \begin{pmatrix} 59.211 \\ 153.261 \end{pmatrix} $$
   $$ r = \begin{pmatrix} 59.2 \\ 47.0 \end{pmatrix} - \begin{pmatrix} 59.211 \\ 153.261 \end{pmatrix} = \begin{pmatrix} -0.011 \\ -106.261 \end{pmatrix} $$
   $$ \|r\|_\infty = \mathbf{106.261} $$

6. **Límite Superior del Error:**
   Utilizando la fórmula solicitada:
   $$ \text{Límite} = K_\infty(A) \frac{\|r\|_\infty}{\|A\|_\infty} = 12.24 \times \frac{106.261}{58.93} \approx 12.24 \times 1.803 \approx \mathbf{22.07} $$

**Comparación Final:**
El error real ($\|x - \tilde{x}\|_\infty = 20$) es menor que el límite teórico calculado ($\approx 22.07$), lo cual es consistente con la teoría de análisis numérico.
