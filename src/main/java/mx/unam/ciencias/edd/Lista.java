package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;



/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
            anterior = null;
            siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if (siguiente == null)
            throw new NoSuchElementException("no hay siguiente elemento");

            anterior = siguiente;
            siguiente = siguiente.siguiente;

            return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if (anterior == null)
                throw new NoSuchElementException("no hay elemento anterior");

            siguiente = anterior;
            anterior = anterior.anterior;

            return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            anterior = null;
            siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            anterior = rabo;
            siguiente = null;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
       return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return rabo == null;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("elemento no puede ser null");
        }
        Nodo n = new Nodo(elemento);
        if (rabo == null) {
            cabeza = rabo = n;
        } else {
            rabo.siguiente = n;
            n.anterior = rabo;
            rabo = n;
        }
        longitud++;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        this.agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("elemento no puede ser null");
        }

        Nodo n = new Nodo(elemento);

        if (rabo == null) {
            cabeza = rabo = n;
        } else {
            cabeza.anterior = n;
            n.siguiente = cabeza;
            cabeza = n;
        }

        longitud++;
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException("el elemento es null");

        if (i < 1) {
            agregaInicio(elemento);
            return;
        }

        if (i >= longitud) {
            agregaFinal(elemento);
            return;
        }
        Nodo nuevo = new Nodo(elemento);
        Nodo nodoaux = buscaNodo(i);
        
        nuevo.anterior = nodoaux.anterior;
        nuevo.siguiente = nodoaux;
        nodoaux.anterior.siguiente = nuevo;
        nodoaux.anterior = nuevo;

        longitud++;
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Nodo n = cabeza;

        while (n != null) {
            if (n.elemento.equals(elemento))
                break;

            n = n.siguiente;
        }

        if (n == null)
            return;

        if (n.anterior == null)
            cabeza = n.siguiente;
        else
            n.anterior.siguiente = n.siguiente;

        if (n.siguiente == null)
            rabo = n.anterior;
        else
            n.siguiente.anterior = n.anterior;

        longitud--;
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if (cabeza == null)
            throw new NoSuchElementException("esta lista esta vacia");

        T eliminado = cabeza.elemento;
        cabeza = cabeza.siguiente;
        if (cabeza != null)
            cabeza.anterior = null;
        else
            rabo = null;

        longitud--;

        return eliminado;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if (rabo == null)
            throw new NoSuchElementException("esta lista esta vacia");

        T eliminado = rabo.elemento;
        rabo = rabo.anterior;

        if (rabo != null)
            rabo.siguiente = null;
        else
            cabeza = null;

        longitud--;
        return eliminado;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        Nodo n = cabeza;
        while (n != null) {
            if (n.elemento.equals(elemento))
                break;

            n = n.siguiente;
        }

        return n != null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> newlista = new Lista<>();

        Nodo n = cabeza;
        while (n != null) {
            newlista.agregaInicio(n.elemento);
            n = n.siguiente;
        }

        return newlista;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> newlista = new Lista<>();

        Nodo n = cabeza;
        while (n != null) {
            newlista.agregaFinal(n.elemento);
            n = n.siguiente;
        }

        return newlista;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        rabo = cabeza = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if (cabeza == null)
        throw new NoSuchElementException("no hay elementos en esta lista");

        T elemento = cabeza.elemento;
        return elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if (rabo == null)
            throw new NoSuchElementException("no hay elementos en esta lista");

        T elemento = rabo.elemento;
        return elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if (i < 0 || i >= longitud)
            throw new ExcepcionIndiceInvalido("indice fuera del rango");


        return buscaNodo(i).elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        int contador = 0;
        Nodo n = cabeza;

        while (n != null) {
            if (n.elemento.equals(elemento)) {
                break;
            }
            contador++;
            n = n.siguiente;
        }
        if (n == null) {
            contador = -1;
        }
        return contador;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        Nodo nodo = cabeza;
        String texto = "[";

        if (nodo != null) {
            while (nodo.siguiente != null) {
                texto += String.format("%s, ", nodo.elemento);
                nodo = nodo.siguiente;
            }

            texto += String.format("%s", nodo.elemento);
        }

        texto += "]";
        return texto;
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        
        if (longitud != lista.getLongitud())
            return false;
        Nodo n1 = cabeza;
        Nodo n2 = lista.cabeza;

        while (n1 != null) {
            if (!n1.elemento.equals(n2.elemento))
                return false;

            n1 = n1.siguiente;
            n2 = n2.siguiente;
        }
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        if(longitud <= 1)
            return copia();
        
        int medio = longitud/2;

        Lista<T> mitad1 = partirLista(0, medio).mergeSort(comparador);
        Lista<T> mitad2 = partirLista(medio, longitud).mergeSort(comparador);

        Lista<T> regreso = new Lista<T>();

        Nodo n1 = mitad1.cabeza;
        Nodo n2 = mitad2.cabeza;

        while (n1 != null && n2 != null){
            if (comparador.compare(n1.elemento, n2.elemento) <= 0){
                regreso.agregaFinal(n1.elemento);
                n1 = n1.siguiente;
            } else {
                regreso.agregaFinal(n2.elemento);
                n2 = n2.siguiente;
            }
        }

        while (n1 != null){
            regreso.agregaFinal(n1.elemento);
            n1 = n1.siguiente;
        }

        while (n2 != null){
            regreso.agregaFinal(n2.elemento);
            n2 = n2.siguiente;
        }

        return regreso;
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        Nodo n = cabeza;
        int resultado = -1;

        while (n != null && (resultado = comparador.compare(n.elemento, elemento)) < 0){
            n = n.siguiente;
        }

        return n != null && resultado == 0;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }

    private Lista<T> partirLista (int i, int j){
        Lista<T> segmento = new Lista<T>();
        Nodo n = buscaNodo(i);

        while (n != null && i < j){
            segmento.agrega(n.elemento);
            n = n.siguiente;
            i++;
        }
        return segmento;
    }

    private Nodo buscaNodo(int i) {
        int indice = 0;
        Nodo nodo = cabeza;

        while (indice++ != i)
            nodo = nodo.siguiente;

        return nodo;
    }
}
