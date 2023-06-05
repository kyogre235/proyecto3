package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * 
     * @return una representación en cadena de la pila.
     */
    @Override
    public String toString() {
        String cadena = "";
        Nodo n = cabeza;

        while (n != null) {
            cadena = cadena + n.elemento.toString() + "\n";
            n = n.siguiente;
        }
        return cadena;
    }

    /**
     * Agrega un elemento al tope de la pila.
     * 
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *                                  <code>null</code>.
     */
    @Override
    public void mete(T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException("el elemento es vacio");

        Nodo n = new Nodo(elemento);

        if (cabeza == null)
            cabeza = rabo = n;
        else {
            n.siguiente = cabeza;
            cabeza = n;
        }

    }
}
