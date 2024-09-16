package paquetes;

import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class ClienteLocal {

    /**
     * Muestra el menu de opciones y lee repetidamente de teclado hasta obtener una opción válida
     *
     * @param teclado	stream para leer la opción elegida de teclado
     * @return			opción elegida
     */
    public static int menu(Scanner teclado) {
        int opcion;
        System.out.println("\n\n");
        System.out.println("=====================================================");
        System.out.println("============            MENU        =================");
        System.out.println("=====================================================");
        System.out.println("0. Salir");
        System.out.println("1. Listar los paquetes enviados");
        System.out.println("2. Enviar un paquete");
        System.out.println("3. Modificar un paquete");
        System.out.println("4. Retirar un paquete");
        do {
            System.out.print("\nElige una opcion (0..4): ");
            opcion = teclado.nextInt();
        } while ( (opcion<0) || (opcion>4) );
        teclado.nextLine(); // Elimina retorno de carro del buffer de entrada
        return opcion;
    }

    /**
     * Programa principal. Muestra el menú repetidamente y atiende las peticiones del cliente.
     *
     * @param args	no se usan argumentos de entrada al programa principal
     */
    public static void main(String[] args)  {

        Scanner teclado = new Scanner(System.in);

        // Crea un gestor de valoraciones
        GestorPaquetes gestor = new GestorPaquetes();

        System.out.print("Introduce tu código de cliente: ");
        String codCliente = teclado.nextLine();

        int opcion;
        do {
            opcion = menu(teclado);
            switch (opcion) {
                case 0 -> { // Guardar los datos en el fichero y salir del programa

                    // POR IMPLEMENTAR

                }
                case 1 -> { // Listar los paquetes enviados por el cliente

                    // POR IMPLEMENTAR

                }
                case 2 -> { // Hacer un envío

                    // POR IMPLEMENTAR


                }
                case 3 -> { // Modificar un paquete enviado por ti y no recogido todavía

                    // POR IMPLEMENTAR

                }
                case 4 -> { // Retira un paquete envíado por ti y no recogido todavía

                    // POR IMPLEMENTAR

                }

            } // fin switch

        } while (opcion != 0);

    } // fin de main

} // fin class
