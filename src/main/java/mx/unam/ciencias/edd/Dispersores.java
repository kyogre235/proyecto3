package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        int regreso = 0;
        int i = 0;
        while(i < llave.length){
            regreso ^= combina(valor(llave, i),valor(llave, i+1),valor(llave, i+2), valor(llave, i+3));
            i += 4;
        }
        return regreso;
    }

    /**metodo que combina 4 bytes en un entero de 32 bits */
    private static int combina(byte a, byte b, byte c, byte d){
        return ((a & 0xFF) << 24)|((b & 0xFF) << 16) | ((c & 0xFF) << 8) | ((d & 0xFF));
    }

    /**
     * metodo que dado un i, regresa el byte que esta en datos[i],
     * si i se sale del arreglo, solamente regresa 0 que es como agregar los 0 que dice canek, jaja
     */
    private static byte valor(byte[] datos, int i){
        if(i<datos.length)
            return  datos[i];
        
        return 0;
    }
    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        int i = 0;
        int a = 0x9E3779B9;
        int b = 0x9E3779B9;
        int c = 0xFFFFFFFF;

        boolean sigue = true;
        while (sigue){
            a += combina(valor(llave, i+3), valor(llave, i+2), valor(llave, i+1), valor(llave, i));

            i += 4;

            b += combina(valor(llave, i+3), valor(llave, i+2), valor(llave, i+1), valor(llave, i));

            i += 4;

            if(llave.length - i >= 4){
                c+= combina(valor(llave, i+3), valor(llave, i+2), valor(llave, i+1), valor(llave, i));
            } else{
                sigue = false;
                c += llave.length;
                c += combina(valor(llave, i + 2), valor(llave, i + 1), valor(llave, i),(byte) 0);
            }
            i += 4;

            a -= b + c;
            a ^= (c >>> 13);
            b -= c + a;
            b ^= (a << 8);
            c -= a + b;
            c ^= (b >>> 13);

            a -= b + c;
            a ^= (c >>> 12);
            b -= c + a;
            b ^= (a << 16);
            c -= a + b;
            c ^= (b >>> 5);

            a -= b + c;
            a ^= (c >>> 3);
            b -= c + a;
            b ^= (a << 10);
            c -= a + b;
            c ^= (b >>> 15);

        }


        return c;
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        int regreso = 5381;

        for(int i = 0; i < llave.length; i++){
            regreso +=(regreso << 5) + (0xFF & valor(llave, i)) ; 
        }

        return regreso;
    }
}
