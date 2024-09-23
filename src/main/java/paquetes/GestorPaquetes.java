package paquetes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;

public class GestorPaquetes {

    private FileWriter os;            // stream para escribir los datos en el fichero

    /**
     * Diccionario para manejar los datos en memoria.
     * La clave es el codigo único de los clientes.
     * El valor es un vector con todos los paquetes enviados por el cliente
     */
    private HashMap<String, Vector<Paquete>> mapa = new HashMap<>();//codigo_cliente<-->vector de paquetes


    /**
     * Constructor del gestor de paquetes
     * Crea o Lee un fichero con datos de prueba
     */
    public GestorPaquetes() {
        this.mapa = new HashMap<String, Vector<Paquete>>();
        File file = new File("paquetes.json");
        try {
            // Si no existe el fichero de datos, los genera, rellena el diccionario y los escribe en el fichero
            if (!file.exists()) {
                os = new FileWriter(file);
                generaDatos();
                escribeFichero(os);
                os.close();
            } else {
                // Si existe el fichero o lo acaba de crear, lo lee y rellena el diccionario con los datos
                FileReader is = new FileReader(file);
                leeFichero(is);
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Cuando cada cliente cierra su sesión volcamos los datos en el fichero para mantenerlos actualizados
     */
    public void guardaDatos() {
        File file = new File("paquetes.json");//creamos el fichero json y posteriormente guardamos en el los datos
        try {
            os = new FileWriter(file);
            escribeFichero(os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Copia en el fichero un array JSON con los datos de los paquetes guardadas en el diccionario
     *
     * @param os stream de escritura asociado al fichero de datos
     */
    @SuppressWarnings("unchecked")
    private void escribeFichero(FileWriter os) {
        JSONArray jsonArray = new JSONArray();//array de objetos json
        for (String codCliente : mapa.keySet()) {//Añadimos los paquetes como objetos json al arrayjson
            Vector<Paquete> paquetes = mapa.get(codCliente);
            for (Paquete paquete : paquetes) {
                jsonArray.add(paquete.toJSON());
            }
        }
        try {
            os.write(jsonArray.toJSONString());//Añadimos el arrayjson al fichero
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Almacena un paquete en el diccionario
     *
     * @param paquete paquete a almacenar
     */
    private void almacenaPaquete(Paquete paquete) throws Exception {
        if (paquete == null) throw new Exception("Paquete vacio o nulo");
        //Buscamos el cliente en el mapa y le añadimos ese paquete
        String cliente = paquete.getCodCliente();
        if (mapa.containsKey(cliente)) {//cliente existente
            if (mapa.get(cliente).isEmpty()) {//sin paquetes
                mapa.put(cliente, new Vector<>());
            }
        } else {//cliente nuevo
            mapa.put(cliente, new Vector<>());

        }
        mapa.get(cliente).add(paquete);
    }


    /**
     * Genera los datos iniciales y los guarda en el diccionario
     */
    private void generaDatos() throws Exception {
        almacenaPaquete(new Paquete("cli01", "12001", "12006", 0.7));
        almacenaPaquete(new Paquete("cli02", "12005", "12002", 1.2));
        almacenaPaquete(new Paquete("cli02", "12002", "12006", 15.2));
        almacenaPaquete(new Paquete("cli03", "12003", "12001", 3));
        almacenaPaquete(new Paquete("cli04", "12004", "12002", 2.6));
    }


    /**
     * Lee los datos del fichero en formato JSON y los añade al diccionario en memoria
     *
     * @param is stream de lectura de los datos del fichero
     */
    private void leeFichero(FileReader is) {
        JSONParser parser = new JSONParser();
        try {
            // Leemos toda la información del fichero en un array de objetos JSON
            JSONArray array = (JSONArray) parser.parse(is);
            // Rellena los datos del diccionario en memoria a partir del JSONArray
            rellenaDiccionario(array);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * Rellena el diccionario a partir de los datos en un JSONArray
     *
     * @param array JSONArray con los datos de los paquetes
     */
    private void rellenaDiccionario(JSONArray array) {
        for (int i = 0; i < array.size(); i++) {
            Paquete paquete = new Paquete((JSONObject) array.get(i));
            if (mapa.containsKey(paquete.getCodCliente())) //cliente existente
                mapa.get(paquete.getCodCliente()).add(paquete);//le añadimos el paquete
            else {//cliente nuevo, lo añadimos y añadimos su paquete
                mapa.put(paquete.getCodCliente(), new Vector<>());
                mapa.get(paquete.getCodCliente()).add(paquete);
            }
			/*
			Podemos usar esto en lugar de lo de arriba
			JSONObject jsonObject = (JSONObject) array.get(i);
			String codigo_cliente = jsonObject.get("codCliente").toString();
			String cp_origen = jsonObject.get("CPOrigen").toString();
			Double peso = (Double) jsonObject.get("peso");
			String cp_destino = jsonObject.get("CPDestino").toString();
			*/
        }
    }


    /**
     * Obtiene una lista de todos los paquetes asociados con un cliente específico.
     * Devuelve un `JSONArray` con la representación JSON de cada paquete.
     *
     * @param codCliente El código del cliente cuyos paquetes se desean listar.
     * @return Un `JSONArray` que contiene la representación JSON de cada paquete del cliente.
     */
    public JSONArray listaPaquetesCliente(String codCliente) throws Exception {
        if (codCliente == null || !mapa.containsKey(codCliente)) {
            throw new Exception("Codigo de cliente no valido");
        }
        //En un arrayjson devolvemos todos los paquetes en formato json
        JSONArray jsonArray = new JSONArray();
        for (Paquete paq : mapa.get(codCliente)) {
            jsonArray.add(paq.toJSON());
        }
        return jsonArray;
        // }
    }

    /**
     * Envía un paquete creando un objeto `Paquete` y almacenándolo.
     * Luego, retorna un objeto `JSONObject` con el código del paquete.
     *
     * @param codCliente El código del cliente que envía el paquete.
     * @param CPOrigen   El código postal de origen del paquete.
     * @param CPDestino  El código postal de destino del paquete.
     * @param peso       El peso del paquete en kilogramos.
     * @return Un objeto `JSONObject` que contiene el código del paquete bajo la clave "codPaquete".
     */
    public JSONObject enviaPaquete(String codCliente, String CPOrigen, String CPDestino, double peso) throws Exception {
        Paquete paquete = new Paquete(codCliente, CPOrigen, CPDestino, peso);
        almacenaPaquete(paquete);
        return paquete.toJSON();
    }


    /**
     * Busca un paquete con un código dado
     *
     * @param vector     vector de paquetes enviados por un cliente
     * @param codPaquete código del paquete buscado
     * @return Referencia al paquete. Si no la encuentra, null
     * <p>
     * Devolvemos una referencia al paquete para poder borrarlo o modificarlo
     */
    private Paquete buscaPaquete(Vector<Paquete> vector, long codPaquete) throws Exception {
        Paquete paquete = null;
        for (Paquete p : vector) {
            if (p.getCodPaquete() == codPaquete) {
                paquete = p;
            }
        }
        return paquete;
    }


    /**
     * Modifica un paquete específico de un cliente dado, actualizando sus datos si no ha sido recogido.
     * Retorna un `JSONObject` con la representación JSON del paquete modificado.
     *
     * @param codCliente El código del cliente que posee el paquete.
     * @param codPaquete El código del paquete a modificar.
     * @param CPOrigen   El nuevo código postal de origen del paquete. Si está vacío, no se modifica.
     * @param CPDestino  El nuevo código postal de destino del paquete. Si está vacío, no se modifica.
     * @param peso       El nuevo peso del paquete. Si es 0, no se modifica.
     * @return Un objeto `JSONObject` con la representación del paquete modificado, o un objeto vacío si no se encontró el paquete o ya fue recogido.
     */
    public JSONObject modificaPaquete(String codCliente, long codPaquete, String CPOrigen, String CPDestino, double peso) throws Exception {
        if (codCliente == null || !mapa.containsKey(codCliente)) {
            throw new Exception("Cliente inexistente");
        }
        JSONObject res = null;
        for (Paquete paquete : mapa.get(codCliente)) {
            if (paquete.getCodPaquete() == codPaquete && paquete.getFechaRecogida().isEmpty()) {//si es el paq buscado y no ha sido recogido
                //si procede--> atributo no vacio y parametro pasado no vacio
                if (!paquete.getCPOrigen().isEmpty() && !CPOrigen.isEmpty()) {//Actualizamos cporigen si procede
                    paquete.setCPOrigen(CPOrigen);
                }
                if (!paquete.getCPDestino().isEmpty() && !CPDestino.isEmpty()) {//Actualizamos cpdestino si procede
                    paquete.setCPDestino(CPDestino);
                }
                if (paquete.getPeso() != 0.0) paquete.setPeso(peso);//Actualizamos peso si procede

                res = paquete.toJSON();
            }
        }
        return res;
    }


    /**
     * Retira un paquete específico de un cliente dado, si no ha sido recogido.
     * El paquete retirado se elimina del sistema y se retorna su representación JSON.
     *
     * @param codCliente El código del cliente que posee el paquete.
     * @param codPaquete El código del paquete a retirar.
     * @return Un objeto `JSONObject` con la representación del paquete retirado, o un objeto vacío si no se encontró el paquete o ya fue recogido.
     */
    public JSONObject retiraPaquete(String codCliente, long codPaquete) throws Exception {
        if (codCliente == null || !mapa.containsKey(codCliente)) {
            throw new Exception("Cliente inexsistente");
        }
        Paquete paq_eliminar = null;
        JSONObject res = new JSONObject();
        for (Paquete paquete : mapa.get(codCliente)) {
            if (paquete.getCodPaquete() == codPaquete && paquete.getFechaRecogida().isEmpty()) {//eliminamos el paquete si no ha sido recogido
                res = paquete.toJSON();
                paq_eliminar = paquete;
            }
        }
        if (paq_eliminar != null) {
            mapa.get(codCliente).remove(paq_eliminar);
        }
        else return null;
        return res;
    }

    /* METODOS USADOS POR LOS MENSAJEROS */

    /**
     * Devuelve la lista de paquetes destinados a un código postal dado que no han sido recogidos todavía.
     *
     * @return JSONArray con la lista de paquetes. Vacío si no hay paquetes destinados a ese código postal
     */
    public JSONArray listaPaquetesCP(String CPDestino) {
        JSONArray jsonArray = new JSONArray();
        for (String codigoCli : mapa.keySet()) {
            for (Paquete paquete : mapa.get(codigoCli)) {
                if (paquete.getFechaRecogida().isEmpty() && paquete.getCPDestino().equals(CPDestino)) {//si paquete no recogido y cpdestino coincide
                    jsonArray.add(paquete.toJSON());
                }
            }
        }
        return jsonArray;
    }


    /**
     * Método para que un mensajero busque y recoja un paquete con el código dado.
     *
     * @param codPaquete   el código único del paquete a recoger
     * @param codMensajero el código único del mensajero que recoge el paquete
     * @return un objeto JSON con la información del paquete recogido
     */
    public JSONObject recogePaquete(long codPaquete, String codMensajero) {
        JSONObject res = null;
        for (String codigoCli : mapa.keySet()) {
            for (Paquete paquete : mapa.get(codigoCli)) {
                if (paquete.getCodPaquete() == codPaquete) {//si es el paquete a recoger-->añadimos codmensajero y fecha de recogida
                    paquete.setCodMensajero(codMensajero);
                    paquete.setFechaRecogida(Paquete.fechaHoy());
                    res = paquete.toJSON();
                }
            }
        }
        return res;
    }


}
