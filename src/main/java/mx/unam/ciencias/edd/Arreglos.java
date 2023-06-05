package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        quickSort(arreglo, comparador, 0, arreglo.length-1 );
    }

    private static <T> void quickSort (T[] arreglo, Comparator<T> comparador, int a, int b){
        if (b <= a)
            return;
        
        int midleft = a + 1;
        int midright = b;
        int resultado;
        while (midleft < midright){
            if ((resultado =(comparador.compare(arreglo[midleft],arreglo[a]))) > 0 && (comparador.compare(arreglo[midright], arreglo[a]))<=0)

                intercambio(arreglo, midleft++, midright --);
            else if (resultado <= 0)
                midleft++;
            else 
                midright--;
        }

        if (comparador.compare(arreglo[midleft], arreglo[a]) > 0)
            midleft--;
        
        intercambio(arreglo, a, midleft);
        quickSort(arreglo, comparador, a, midleft-1);
        quickSort(arreglo, comparador, midleft+1, b);

    }
    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        int i,j;
        for (i=0; i<arreglo.length-1; i++){
            int min = i;
            for (j = i+1; j < arreglo.length; j++){
                if(comparador.compare(arreglo[j], arreglo[min]) < 0)
                    min = j;
            }
            intercambio (arreglo, i, min);
        }
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
      return busquedaBinaria(arreglo, elemento,comparador,0,arreglo.length-1);
    }

    private static <T> int busquedaBinaria (T[] arreglo, T elemento, Comparator<T> comparador, int a, int b){
        if(b<a)
            return -1;
        
        int mitad = ((b-a)/2)+ a; 
        int dif = comparador.compare(elemento, arreglo[mitad]);

        if (dif == 0)
            return mitad;
        else if (dif < 0)
            return busquedaBinaria(arreglo, elemento, comparador, a, mitad-1);
        else 
            return busquedaBinaria(arreglo, elemento, comparador, mitad+1, b);

    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }

    private static <T> void intercambio (T[] arreglo, int pos1, int pos2){
        T aux = arreglo[pos1];

        arreglo[pos1] = arreglo[pos2];
        arreglo[pos2] = aux;
    }
}
