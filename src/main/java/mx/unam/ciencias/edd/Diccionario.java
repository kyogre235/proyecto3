package mx.unam.ciencias.edd;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, mapeando un conjunto de <em>llaves</em> a una colección
 * de <em>valores</em>.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /* Clase interna privada para entradas. */
    private class Entrada {

        /* La llave. */
        public K llave;
        /* El valor. */
        public V valor;

        /* Construye una nueva entrada. */
        public Entrada(K llave, V valor) {
            this.llave = llave;
            this.valor = valor;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador() {
            indice = -1;
            buscarProximo();
        }

        private void buscarProximo(){
            indice++;
            while(indice<entradas.length){
                if(entradas[indice]!= null){
                    iterador = entradas[indice].iterator();
                    return;
                }
            indice++;
            }
            iterador = null;
        }
        /* Nos dice si hay una siguiente entrada. */
        public boolean hasNext() {
            return iterador != null;
        }

        /* Regresa la siguiente entrada. */
        public Entrada siguiente() {
            if(iterador == null)
                throw new NoSuchElementException("no hay elemento siguiente");
            
            Entrada sig = iterador.next();

            if(!iterador.hasNext())
                buscarProximo();
            
            return sig;
        }
    }

    /* Clase interna privada para iteradores de llaves. */
    private class IteradorLlaves extends Iterador
        implements Iterator<K> {

        /* Regresa el siguiente elemento. */
        @Override public K next() {
            return siguiente().llave;
        }
    }

    /* Clase interna privada para iteradores de valores. */
    private class IteradorValores extends Iterador
        implements Iterator<V> {

        /* Regresa el siguiente elemento. */
        @Override public V next() {
            return siguiente().valor;
        }
    }

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Capacidad mínima; decidida arbitrariamente a 2^6. */
    private static final int MINIMA_CAPACIDAD = 64;

    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores. */
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked")
    private Lista<Entrada>[] nuevoArreglo(int n) {
        return (Lista<Entrada>[])Array.newInstance(Lista.class, n);
    }

    /**
     * Construye un diccionario con una capacidad inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this(MINIMA_CAPACIDAD, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial definida por el
     * usuario, y un dispersor predeterminado.
     * @param capacidad la capacidad a utilizar.
     */
    public Diccionario(int capacidad) {
        this(capacidad, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial predeterminada, y un
     * dispersor definido por el usuario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this(MINIMA_CAPACIDAD, dispersor);
    }

    /**
     * Construye un diccionario con una capacidad inicial y un método de
     * dispersor definidos por el usuario.
     * @param capacidad la capacidad inicial del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int capacidad, Dispersor<K> dispersor) {
        this.dispersor = dispersor; 
        entradas = nuevoArreglo(buscarPotencia(capacidad));
    }
    // metodo que busca la potencia de 2 en caso de ser nesesario
    private int buscarPotencia(int capacidad){
        int regreso = 1;

        capacidad = (capacidad < 64) ? 64 : capacidad;
        while(regreso < capacidad * 2)
            regreso *= 2;

        return regreso;
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
        if(llave == null || valor == null)
            throw new IllegalArgumentException("la llave o el valor son null");

        int indice = mascara(llave);
        Entrada entrada = new Entrada(llave, valor);
        if(entradas[indice]== null){
            entradas[indice] = new Lista<Entrada>();
            entradas[indice].agrega(entrada);
            elementos++;
        }
           
        Entrada colicion = recorreIndice(indice, llave);
        if(colicion == null){
            entradas[indice].agrega(entrada);
            elementos++;
        } else{
            colicion.valor = valor; 
        }
        

        if(carga()>= MAXIMA_CARGA)
            duplicar();  
    }

    // metodo que le aplica la mascara a la llave
    private int mascara(K llave){
        return dispersor.dispersa(llave) & (entradas.length - 1);
    }

    // metodo que duplica el arreglo si se supera la carga maxima
    private void duplicar(){
        Lista<Entrada>[] duplicada = nuevoArreglo(entradas.length*2);
        Iterador iterador = new Iterador();
        while(iterador.hasNext()){
            Entrada entrada = iterador.siguiente();
            int indice = dispersor.dispersa(entrada.llave) & (duplicada.length - 1);

            if(duplicada[indice] == null)
                duplicada[indice] = new Lista<Entrada>();
            
            duplicada[indice].agrega(entrada);
        }

        entradas = duplicada; 
    }

    private Entrada recorreIndice(int indice, K llave){
        if(entradas[indice] == null)
            return null;
        for (Entrada entrada : entradas[indice]) {
            if(entrada.llave.equals(llave))
                return entrada;
        }
        return null;
    }
    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
        if(llave == null)
            throw new IllegalArgumentException("la llave es nula");
        int indice = mascara(llave);
        Entrada regreso = recorreIndice(indice, llave);

        if(regreso == null)
            throw new NoSuchElementException("la llave no esta en el diccionario");
        
        return regreso.valor;

    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <code>true</code> si la llave está en el diccionario,
     *         <code>false</code> en otro caso.
     */
    public boolean contiene(K llave) {
        if(llave == null){
            return false;
        }
        return recorreIndice(mascara(llave), llave)!= null;
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
        if (llave == null)
            throw new IllegalArgumentException("la llave es nula");
        int indice = mascara(llave);
        Entrada regreso = recorreIndice(indice, llave);

        if (regreso == null)
            throw new NoSuchElementException("la llave no esta en el diccionario");

        entradas[indice].elimina(regreso);
        if(entradas[indice].getLongitud()== 0)
            entradas[indice] = null;
        elementos--;
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
       int coliciones=0;

       for(Lista<Entrada> entrada : entradas){
        if(entrada!= null)
            coliciones += entrada.getElementos()-1;
        
       }
       return coliciones;
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        int coliciones = 0;
        int max = 0;

        for (Lista<Entrada> entrada : entradas) {
            if (entrada != null){
                coliciones = entrada.getElementos() - 1;
            }
            
            if(coliciones > max)
                max = coliciones;
                

        }
        return max;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
        return Double.valueOf(elementos)/entradas.length;
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        return elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacia() {
        return elementos == 0;
    }

    /**
     * Limpia el diccionario de elementos, dejándolo vacío.
     */
    public void limpia() {
       entradas = nuevoArreglo(entradas.length);
       elementos = 0;
    }

    /**
     * Regresa una representación en cadena del diccionario.
     * @return una representación en cadena del diccionario.
     */
    @Override public String toString() {
        String regreso ="{ ";
        if(elementos == 0)
            return "{}";
        Iterador iterador = new Iterador();
        while(iterador.hasNext()){
            Entrada entrada = iterador.siguiente();
            regreso += String.format("'%s': '%s', ", entrada.llave, entrada.valor);
        }
        
        return regreso + "}";
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d =
            (Diccionario<K, V>)o;
        
        if(d.elementos != elementos)
            return false;
        
        Iterador iterador = new Iterador();
        while(iterador.hasNext()){
            Entrada entrada = iterador.siguiente();
            if(!d.contiene(entrada.llave))
                return false;
            if(!entrada.valor.equals(d.get(entrada.llave)))
                return false;
            
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar las llaves del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar las llaves del diccionario.
     */
    public Iterator<K> iteradorLlaves() {
        return new IteradorLlaves();
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar los valores del diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new IteradorValores();
    }
}
