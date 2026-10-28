

// Implementación de una Pila Enlazada para las tareas terminadas
class LinkedStack<T> {
    private class Node {
        T data;
        Node next;
        Node(T data) { this.data = data; }
    }

    private Node head = null;
    private int n = 0;

    public void push(T item) {
        Node newNode = new Node(item);
        newNode.next = head;
        head = newNode;
        n++;
    }

    public T pop() {
        if (head == null) return null;
        T item = head.data;
        head = head.next;
        n--;
        return item;
    }

    public void mostrarPila() {
        System.out.println("Pila Finished (LinkedStack):");
        Node temp = head;
        if (temp == null) System.out.print("Vacía");
        while (temp != null) {
            System.out.print("[" + temp.data + "] -> ");
            temp = temp.next;
        }
        System.out.println("null (n=" + n + ")");
    }
}

// Implementación de una Cola Circular basada en arreglo
class ArrayQueue {
    private String[] a;
    private int j; // Frente de la cola (Front)
    private int n; // Número de elementos

    public ArrayQueue(int capacity, int front, int size) {
        this.a = new String[capacity];
        this.j = front;
        this.n = size;
    }

    // Método para forzar la posición de los elementos según el ejercicio
    public void setElement(int index, String task) {
        a[index] = task;
    }

    public String dequeue() {
        if (n == 0) return null;
        String task = a[j];
        a[j] = null; // Marcamos como null según el ejercicio
        j = (j + 1) % a.length; // Movimiento circular
        n--;
        return task;
    }

    public void mostrarCola() {
        System.out.println("Cola Ready (ArrayQueue) - Estado del Arreglo:");
        for (int i = 0; i < a.length; i++) {
            String mark = (i == j) ? " <--- Frente(j)" : "";
            System.out.println("a[" + i + "] = " + a[i] + mark);
        }
        System.out.println("n = " + n + ", j = " + j);
    }
}

public class main {
    public static void main(String[] args) {
        // 1. Configuración Inicial de la Cola (Ready)
        // length=8, j=5, n=5
        ArrayQueue ready = new ArrayQueue(8, 5, 5);
        ready.setElement(5, "T1");
        ready.setElement(6, "T2");
        ready.setElement(7, "T3");
        ready.setElement(0, "T4");
        ready.setElement(1, "T5");

        // 2. Configuración Inicial de la Pila (Finished)
        // head=null, n=0
        LinkedStack<String> finished = new LinkedStack<>();

        // --- MOSTRAR ESTADO INICIAL ---
        System.out.println("=== ESTADO INICIAL ===");
        ready.mostrarCola();
        System.out.println();
        finished.mostrarPila();
        System.out.println("======================\n");

        // --- SIMULACIÓN DE EJECUCIÓN (FIFO) ---
        // Vamos a procesar 3 tareas para ver cómo funciona el sistema
        for (int i = 1; i <= 3; i++) {
            System.out.println("Ejecutando tarea " + i + "...");
            String task = ready.dequeue(); // Sale de la cola
            finished.push(task);           // Entra a la pila

            ready.mostrarCola();
            finished.mostrarPila();
            System.out.println("----------------------");
        }
    }
}