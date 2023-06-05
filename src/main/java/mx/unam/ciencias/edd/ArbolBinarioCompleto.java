package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        private Iterador() {
            cola = new Cola<Vertice> ();

            if (raiz != null)
                cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !cola.esVacia();
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            Vertice v = cola.saca();

            if (v.izquierdo != null)
                cola.mete(v.izquierdo);

            if (v.derecho != null)
                cola.mete(v.derecho);

            return v.elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        
        if(elemento == null)
            throw new IllegalArgumentException("el elemento no es valido");
        
        Vertice v = nuevoVertice(elemento);
        elementos++;
        
        if (raiz == null){
            raiz = v;
            return;
        }
        
        Vertice v2 = vertice(recorridoBFS1Log());
        v.padre = v2;

        if(v2.izquierdo == null)
            v2.izquierdo = v;
        else
            v2.derecho = v;

        
    }
    /* 
     * metodo que recorre el arbol de forma bfs (con O(n)) 
     * y regresa el primer vertice que le falte un hijo
     */
    
    /*
     * private VerticeArbolBinario<T> recorridoBFS1(){
        Cola<Vertice> cola = new Cola<>();
        cola.mete(raiz);
        
        
            Vertice v;
            while (!cola.esVacia()){
                v = cola.saca();
    
                if(v.izquierdo == null || v.derecho == null)
                    return v;
                
                cola.mete(v.izquierdo);
                cola.mete(v.derecho);
            }
        //este return nunca va a llegar, solo lo pongo para que compile
        return null; 
    }
     */
    
    /* 
     * metodo que recorre el arbol de forma bfs (con O(log2n)) 
     * y regresa el primer vertice que le falte un hijo
     */
    private VerticeArbolBinario<T> recorridoBFS1Log(){
        //utilizamos un long para arboles con mas de 1,000,000 de elementos
        long trayectoria = 0;
        int aux = elementos;
        Vertice regreso = raiz;
        Vertice pivote = raiz;

        while (aux > 1){
            if(aux % 2 == 0)
                trayectoria = (trayectoria * 10) + 1; 
            else
                trayectoria = trayectoria * 10;
            aux >>= 1;
        }
        
        while (pivote !=null){
            regreso = pivote;

            if(trayectoria % 10 == 0 ){
                pivote = pivote.derecho;
                trayectoria = trayectoria / 10;
            } else {
                pivote = pivote.izquierdo;
                trayectoria = trayectoria / 10;
            }
        }
        return regreso;
    }
    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice v = vertice(busca(elemento));

        if(v == null)
            return;

        elementos--;

        if(elementos == 0){
            raiz = null;
            return;
        }
            
        
        Vertice ultimo = vertice(recorridoBFS2());
        v.elemento = ultimo.elemento;
        
       
        if(ultimo.padre.izquierdo == ultimo)
            ultimo.padre.izquierdo = null;
        else
            ultimo.padre.derecho = null;
            
    }

    private VerticeArbolBinario<T> recorridoBFS2(){
        Cola <Vertice> cola = new Cola<>();
        cola.mete(raiz);
        Vertice ultimo = raiz;
        Vertice actual;

        while (!cola.esVacia()){
            actual = cola.saca();
            ultimo = actual;

            if(actual.izquierdo != null)
                cola.mete(actual.izquierdo);
            
            if(actual.derecho != null)
                cola.mete(actual.derecho);
            
        }
        return ultimo;
    }
    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        if(elementos == 0)
            return -1;
            
        return logaritmo(elementos);
    }
    // metodo que saca el piso del log2 con diviviones
    private int logaritmo (int a){
        int cont = 0;
        while (a > 1){
            a = a / 2;
        cont ++;
        }
        return cont;
    }
    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        if(raiz == null)
            return;
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(raiz);

        Vertice v;
        while (!cola.esVacia()){
            v = cola.saca();
            accion.actua(v);

            if(v.izquierdo != null)
                cola.mete(v.izquierdo);
            if(v.derecho != null)
                cola.mete(v.derecho);
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
