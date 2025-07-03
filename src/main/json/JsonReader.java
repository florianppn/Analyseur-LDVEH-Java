package main.json;

import com.fasterxml.jackson.databind.*;

import java.io.*;

/**
 * Représentation de la lecture et du stockage des données JSON.
 * Elle ouvre un fichier JSON specifique et stocke l'ensemble du JSON dans un nœud root.
 * 
 * @author Tom David
 * @version 1.0
 */
public class JsonReader {

    private JsonNode rootNode;

    public JsonReader(String name) {
        this.rootNode = JsonOpener(name);
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
         return this.rootNode.get("sections").get(String.valueOf(start));
    }

    /**
     * Retourne le noeud JSON représentant la section setup.
     *
     * @return Le noeud JSON représentant la section setup.
     */
    public JsonNode getSetup() {
        return this.rootNode.get("setup");
    }

    /**
     * Retourne le nombre total de sections dans le fichier JSON.
     * 
     * @return Le nombre total de sections dans le fichier JSON.
     */
    public int getSectionLength(){

        if (rootNode != null && rootNode.has("sections")) {
            return rootNode.get("sections").size();
        } else {
            return 0;
        }

    }
    
    /**
     * Retourne le noeud racine représentant l'ensemble du JSON.
     *
     * @return Le noeud racine représentant l'ensemble du JSON.
     */
    private JsonNode JsonOpener(String name) {
        String resourcePath = "./resources/"+name+".json";
        try (InputStream inputStream = new FileInputStream(resourcePath)) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'ouverture du fichier JSON : " + resourcePath, e);
        }
    }

}
