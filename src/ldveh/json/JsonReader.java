package ldveh.json;

import com.fasterxml.jackson.databind.*;

import java.io.*;
import java.util.*;

/**
 * Représentation de la lecture et du stockage des données JSON.
 * Elle ouvre un fichier JSON specifie et stocke l'ensemble du JSON dans un noeud root.
 * 
 * @author Tom David
 * @version 1.0
 */
public class JsonReader {
    
    /**
     * Contient le noeud qui stocke l'ensemble du JSON.
     */
    private JsonNode rootNode;
    /**
     * Contient une instance Properties.
     */
    private Properties properties;

    /**
     * Constructeur par défaut.
     * 
     * @param name du fichier JSON à ouvrir.
     */
    public JsonReader(String name) {

        this.properties = new Properties();
        this.rootNode = JsonOpener(name);

        try {
            this.properties.load(JsonReader.class.getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(this.properties.getProperty("InitJsonReader"));

    }

    /**
     * Retourne le noeud root représentant l'ensemble du JSON.
     * 
     * @return Le noeud root représentant l'ensemble du JSON.
     */
    public JsonNode getRootNode() {
        return this.rootNode;
    }

    /**
     * Retourne le noeud JSON de la section spétifiée.
     * 
     * @param start Le numéro de la section à récupérer.
     * @return Le noeud JSON de la section spétifiée.
     */
    public JsonNode getSection(int start) {
         return this.rootNode.get(this.properties.getProperty("SectionName")).get(String.valueOf(start));
    }

    /**
     * Retourne le noeud JSON représentant la section setup.
     *
     * @return Le noeud JSON représentant la section setup.
     */
    public JsonNode getSetup() {
        return this.rootNode.get(this.properties.getProperty("SetupName"));
    }

    /**
     * Retourne le nombre total de sections dans le fichier JSON.
     * 
     * @return Le nombre total de sections dans le fichier JSON.
     */
    public int getSectionLength(){

        if (rootNode != null && rootNode.has(this.properties.getProperty("SectionName"))) {
            return rootNode.get(this.properties.getProperty("SectionName")).size();
        } else {
            return 0;
        }

    }
    
    /**
     * Retourne le noeud racine représentant l'ensemble du JSON.
     * 
     * @param name Le nom du fichier JSON à ouvrir.
     * @return Le noeud racine représentant l'ensemble du JSON.
     */
    private JsonNode JsonOpener(String name) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(JsonReader.class.getResourceAsStream(name + ".json"));
            return rootNode;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
