
class Nodo {
    int dato;
    Nodo siguiente;
    Nodo anterior;

    public Nodo(int dato) {
        this.dato = dato;
        this.siguiente = null;
        this.anterior = null;
    }
}

class DoubleLinkedList {
    Nodo cabeza;
    Nodo cola;
    Nodo current; // Referencia al nodo actual

    // Método para insertar al final de la lista
    public void insertar(int dato) {
        Nodo nuevoNodo = new Nodo(dato);
        if (cabeza == null) {
            cabeza = cola = nuevoNodo;
        } else {
            cola.siguiente = nuevoNodo;
            nuevoNodo.anterior = cola;
            cola = nuevoNodo;
        }
    }

    // Método para establecer el nodo 'current' buscando un valor específico
    public void setCurrent(int valor) {
        Nodo temp = cabeza;
        while (temp != null) {
            if (temp.dato == valor) {
                this.current = temp;
                return;
            }
            temp = temp.siguiente;
        }
        System.out.println("Valor no encontrado en la lista.");
    }

    // Método para impriestá 'current'
    public void mostrarLista() {
        Nodo temp = cabeza;
        System.out.println("Lista Doblemente Enlazada:");
        while (temp != null) {
            if (temp == current) {
                System.out.print("[ " + temp.dato + " ] <--- current");
            } else {
                System.out.print("[ " + temp.dato + " ]");
            }
            if (temp.siguiente != null) {
                System.out.print(" <--> ");
            }
            temp = temp.siguiente;
        }
        System.out.println();
    }
}

public class Main {
    public static void main(String[] args) {
        DoubleLinkedList lista = new DoubleLinkedList();

        // 1. Insertar los 7 elementos en el orden solicitado
        int[] elementos = {12, 24, 36, 48, 60, 72, 84};
        for (int e : elementos) {
            lista.insertar(e);
        }

        // 2. Establecer el nodo 36 como referencia (current)
        lista.setCurrent(36);

        // 3. Mostrar el
        lista.mostrarLista();
    }
}
