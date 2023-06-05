package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
           
            super(elemento);
            color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        @Override public String toString() {
            //return String.format("%s{%s}", color == Color.ROJO ? "R" : "N", elemento.toString());
            if(color == Color.ROJO)
                return "R{"+elemento.toString()+"}";
            else
                return "N{"+elemento.toString()+"}";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            if(this.color == vertice.color)
                return super.equals(vertice);
            else
                return false;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }
    
    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        return verticeRojinegro(vertice).color;
    }

    private VerticeRojinegro verticeRojinegro(VerticeArbolBinario<T> vertice) {
        return (VerticeRojinegro)vertice;
    }
    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeRojinegro v = verticeRojinegro(ultimoAgregado);
        v.color = Color.ROJO;
        rebalancearA(v);
        
        
    }
    private void rebalancearA(VerticeRojinegro v){
        if(v.padre == null){
            v.color = Color.NEGRO;
            return;
        }
            
        VerticeRojinegro padre = verticeRojinegro(v.padre);

        if(colorearNull(padre)== Color.NEGRO)
            return;

        VerticeRojinegro abuelo = verticeRojinegro(padre.padre);
        VerticeRojinegro tio;

        if(esIzquierdo(padre))
            tio = verticeRojinegro(abuelo.derecho);
        else
            tio = verticeRojinegro(abuelo.izquierdo);

        
        if (colorearNull(tio) == Color.ROJO) {
            padre.color = Color.NEGRO;
            tio.color = Color.NEGRO;
            abuelo.color = Color.ROJO;
            rebalancearA(abuelo);
            return;
        }

        if(!esIzquierdo(padre)&&esIzquierdo(v)){
            super.giraDerecha(padre);
            VerticeRojinegro pivote = v;
            v = padre;
            padre = pivote;
        } else if(esIzquierdo(padre) && !esIzquierdo(v)){
            super.giraIzquierda(padre);
            VerticeRojinegro pivote = v;
            v = padre;
            padre = pivote;
        }

        padre.color = Color.NEGRO;
        abuelo.color = Color.ROJO;

        if(esIzquierdo(v))
            super.giraDerecha(abuelo);
        else
            super.giraIzquierda(abuelo);
    }
    private Color colorearNull (VerticeRojinegro v){
        if(v != null)
            return v.color;
        else
            return Color.NEGRO;
    }
    

    private boolean esIzquierdo(VerticeRojinegro v){
        return v.padre.izquierdo == v;
    }
    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        //metodo por implementar el elemento
        VerticeRojinegro v = verticeRojinegro(busca(elemento));

        if(v == null)
            return;
        
        elementos--;

        if(v.izquierdo != null && v.derecho != null)
            v = verticeRojinegro(intercambiaEliminable(v));
        
        VerticeRojinegro hijo, fantasma = null;

        if(v.izquierdo == null && v.derecho == null){
            fantasma = verticeRojinegro(nuevoVertice(null));
            fantasma.color = Color.NEGRO;
            fantasma.padre = v;
            v.izquierdo = fantasma;
            hijo = fantasma;
        } else {
            if(v.izquierdo != null)
                hijo = verticeRojinegro(v.izquierdo());
            else 
                hijo = verticeRojinegro(v.derecho());
        }

        eliminaVertice(v);

        if(colorearNull(hijo) == Color.ROJO || colorearNull(v) == Color.ROJO)
            hijo.color = Color.NEGRO;
        else
            rebalancearE(hijo);
        
        if(fantasma != null)
            eliminaVertice(fantasma);
    }

    private void rebalancearE(VerticeRojinegro v){
            //caso 1
            if (v.padre == null)
                return;
        
        VerticeRojinegro padre = verticeRojinegro(v.padre);
        VerticeRojinegro hermano;

        if(esIzquierdo(v))
            hermano = verticeRojinegro(padre.derecho);
        else
            hermano = verticeRojinegro(padre.izquierdo);
        
        // caso 2
        if (colorearNull(hermano) == Color.ROJO){
            hermano.color = Color.NEGRO;
            padre.color = Color.ROJO;

            if(esIzquierdo(v))
                super.giraIzquierda(padre);
            else
                super.giraDerecha(padre);
            
            padre = verticeRojinegro(v.padre);
            
            if(esIzquierdo(v))
                hermano = verticeRojinegro(padre.derecho);
            else
                hermano = verticeRojinegro(padre.izquierdo);
        }

        VerticeRojinegro hi = verticeRojinegro(hermano.izquierdo);
        VerticeRojinegro hj = verticeRojinegro(hermano.derecho);
        
        // caso 3
        if(colorearNull(padre) == Color.NEGRO && colorearNull(hermano) == Color.NEGRO 
        && colorearNull(hi) == Color.NEGRO && colorearNull(hj) == Color.NEGRO){
            hermano.color = Color.ROJO;
            rebalancearE(padre);
            return;
        }

        // caso 4
        if(colorearNull(padre) == Color.ROJO && colorearNull(hermano) == Color.NEGRO 
        && colorearNull(hi) == Color.NEGRO && colorearNull(hj) == Color.NEGRO){
            hermano.color = Color.ROJO;
            padre.color = Color.NEGRO;
            return;
        }

        // caso 5
        if(esIzquierdo(v) && colorearNull(hi) == Color.ROJO && colorearNull(hj) == Color.NEGRO ||
        !esIzquierdo(v) && colorearNull(hi) == Color.NEGRO && colorearNull(hj) == Color.ROJO){
            hermano.color = Color.ROJO;

            if(colorearNull(hi) == Color.ROJO)
                hi.color = Color.NEGRO;

            if(colorearNull(hj) == Color.ROJO)
                hj.color = Color.NEGRO;
            
            if(esIzquierdo(v))
                super.giraDerecha(hermano);
            else
                super.giraIzquierda(hermano);

            if(esIzquierdo(v))
                hermano = verticeRojinegro(padre.derecho);
            else
                hermano = verticeRojinegro(padre.izquierdo);  
            
            hi = verticeRojinegro(hermano.izquierdo);
            hj = verticeRojinegro(hermano.derecho);
        }

        hermano.color = padre.color;
        padre.color = Color.NEGRO;

        // caso 6
        if(esIzquierdo(v)){
            hj.color = Color.NEGRO;
            super.giraIzquierda(padre);
        } else{
            hi.color = Color.NEGRO;
            super.giraDerecha(padre);
        }

       
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
