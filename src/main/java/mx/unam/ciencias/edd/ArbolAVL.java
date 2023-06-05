package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
            altura = 0;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            return elemento.toString()+ " " + altura+ "/" + balance(this);
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            return (altura == vertice.altura && super.equals(objeto));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    private VerticeAVL casteo (VerticeArbolBinario<T> vertice){
        return (VerticeAVL) vertice;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        
        rebalanceo(casteo(ultimoAgregado.padre));
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeAVL vertice = casteo(busca(elemento));

        if(vertice == null)
            return;
        
        elementos --;

        if(vertice.izquierdo != null && vertice.derecho != null)
            vertice = casteo(intercambiaEliminable(vertice));

        eliminaVertice(vertice);

        rebalanceo(casteo(vertice));
        
    }

    private void rebalanceo (VerticeAVL vertice){
        
        if(vertice == null)
            return;
        
        vertice.altura = alturaAc(vertice);
        int balance = balance(vertice);

        if(balance == -2){
            VerticeAVL derecho = casteo(vertice.derecho);
            
            if(balance(derecho) == 1){
                super.giraDerecha(derecho);
                derecho.altura = alturaAc(derecho);
            }
            
            super.giraIzquierda(vertice);
            derecho.altura = alturaAc(derecho);
            vertice.altura = alturaAc(vertice);
                 
        }

        if(balance == 2){
            VerticeAVL izquierdo = casteo(vertice.izquierdo);
            if(balance(izquierdo)== -1){
                super.giraIzquierda(izquierdo);
                izquierdo.altura = altura(izquierdo);
            }
            
            super.giraDerecha(vertice);
            izquierdo.altura = alturaAc(izquierdo);
            vertice.altura = alturaAc(vertice);
                 
        }
        
        rebalanceo(casteo(vertice.padre));
    }
    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }

    private int balance(VerticeAVL v){
        if(v.izquierdo == null && v.derecho == null)
            return 0;
        else if(v.izquierdo != null && v.derecho != null )
            return altura(casteo(v.izquierdo)) - altura(casteo(v.derecho));
        else if(v.izquierdo != null && v.derecho == null )
            return altura(casteo(v.izquierdo)) - (-1);
        else
            return (-1) - altura(casteo(v.derecho));
        
        
    }

    private int altura(VerticeAVL vertice){
        return vertice == null ? -1 : vertice.altura;
    }

    private int alturaAc(VerticeAVL v){
        
        return 1 + Math.max(altura(casteo(v.izquierdo)),altura(casteo(v.derecho)));
    }
}
