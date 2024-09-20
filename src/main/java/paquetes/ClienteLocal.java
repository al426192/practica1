package paquetes;

import java.sql.SQLOutput;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class ClienteLocal {

    /**
     * Muestra el menu de opciones y lee repetidamente de teclado hasta obtener una opción válida
     *
     * @param teclado stream para leer la opción elegida de teclado
     * @return opción elegida
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
        } while ((opcion < 0) || (opcion > 4));
        teclado.nextLine(); // Elimina retorno de carro del buffer de entrada
        return opcion;
    }

    /**
     * Programa principal. Muestra el menú repetidamente y atiende las peticiones del cliente.
     *
     * @param args no se usan argumentos de entrada al programa principal
     */
    public static void main(String[] args) /*throws Exception*/ {

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
                    gestor.guardaDatos();
                    System.out.println("Saliendo del programa...");
                }
                case 1 -> { // Listar los paquetes enviados por el cliente
                    try {
                        JSONArray lista_paquetes=gestor.listaPaquetesCliente(codCliente);
                        System.out.println(lista_paquetes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case 2 -> { // Hacer un envío
                    try {
                        System.out.println("Introduce un CP de destino valido: ");
                        String destino = teclado.nextLine();
                        System.out.println("Introduce un CP de origen valido: ");
                        String origen = teclado.nextLine();
                        System.out.println("Introduce un peso valido: ");
                        double peso = teclado.nextDouble();
                        gestor.enviaPaquete(codCliente, origen, destino, peso);
                        System.out.println("Paquete enviado correctamente");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                case 3 -> { // Modificar un paquete enviado por ti y no recogido todavía
                    try {
                        System.out.println("Introduce el paquete a modificar: ");
                        long paquete = teclado.nextLong();
                        teclado.nextLine(); //nextLine() lee la línea completa hasta el carácter de nueva línea (\n) que queda en el buffer después de usar nextLong() o nextDouble().
                        System.out.println("Introduce un CPDestino nuevo o vacio para no modificar: ");
                        String destino = teclado.nextLine();
                        System.out.println("Introduce un CPOrigen nuevo o vacio para no modificar: ");
                        String origen = teclado.nextLine();
                        System.out.println("Introduce un peso nuevo o vacio para no modificar: ");
                        double peso = teclado.nextDouble();
                        gestor.modificaPaquete(codCliente, paquete, origen, destino, peso);
                        System.out.println("Paquete modificado correctamente");
                        gestor.guardaDatos();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case 4 -> {
                    try {
                        System.out.println("Introduce el paquete a retirar: ");
                        long paquete = teclado.nextLong();
                        gestor.retiraPaquete(codCliente,paquete);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } // fin switch

        } while (opcion != 0);

    } // fin de main

} // fin class
