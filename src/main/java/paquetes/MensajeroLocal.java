package paquetes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Scanner;


public class MensajeroLocal {

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
        System.out.println("1. Listar los paquetes enviados a un CP");
        System.out.println("2. Recoger un paquete");
        do {
            System.out.print("\nElige una opcion (0..2): ");
            opcion = teclado.nextInt();
        } while ( (opcion<0) || (opcion>2) );
        teclado.nextLine(); // Elimina retorno de carro del buffer de entrada
        return opcion;
    }

    /**
     * Programa principal. Muestra el menú repetidamente y atiende las peticiones del mensajero.
     *
     * @param args	no se usan argumentos de entrada al programa principal
     */
    public static void main(String[] args)  {

        Scanner teclado = new Scanner(System.in);

        // Crea un gestor de valoraciones
        GestorPaquetes gestor = new GestorPaquetes();


        System.out.print("Introduce tu código de mensajero: ");
        String codMensajero = teclado.nextLine();


        int opcion;
        do {
            opcion = menu(teclado);
            switch (opcion) {
                case 0 -> { // Guardar los datos en el fichero y salir del programa
                    gestor.guardaDatos();
                    System.out.println("Saliendo del programa...");
                }
                case 1 -> { // Listar los paquetes enviados a un CP
                    System.out.printf("Introduce un CPDestino valido: ");
                    String cpDestino = teclado.nextLine();
                    JSONArray imprimir= gestor.listaPaquetesCP(cpDestino);
                    System.out.println(imprimir);
                }
                case 2 -> { // Recoger un paquete con un código dado
                    System.out.printf("Introduce un codigo de paquete valido: ");
                    long paquete = teclado.nextLong();
                    teclado.nextLine();
//                    System.out.printf("Introduce un codigo de mensajero: ");
//                    String cod_mensajero = teclado.nextLine();
                    gestor.recogePaquete(paquete,codMensajero);
                    gestor.guardaDatos();
                }

            } // fin switch

        } while (opcion != 0);

    } // fin de main

} // fin class
