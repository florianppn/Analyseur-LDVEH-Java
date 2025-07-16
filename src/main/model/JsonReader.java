package main.model;

import com.fasterxml.jackson.databind.*;

import java.io.*;

/**
 * Représentation la lecture et le stockage des données d'un LDVEH en format JSON.
 * 
 * @author Florian Pépin
 * @version 2.0
 */
public class JsonReader {

    private JsonNode rootNode;

    public JsonReader(String name) {
        String resourcePath = "./resources/"+name+".json";
        try (InputStream inputStream = new FileInputStream(resourcePath)) {
            ObjectMapper objectMapper = new ObjectMapper();
            this.rootNode = objectMapper.readTree(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error opening json file :" + e);
        }
    }

    /**
     * Retourne le nœud root représentant l'ensemble du JSON.
     * 
     * @return Le nœud root représentant l'ensemble du JSON.
     */
    public JsonNode getRootNode() {
        return this.rootNode;
    }

    /**
     * Retourne le nœud JSON d'une section.
     * 
     * @param id Le numéro de la section à récupérer.
     * @return Le nœud JSON d'une section.
     */
    public JsonNode getSection(int id) {
         return this.rootNode.get("sections").get(String.valueOf(id));
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
        if (this.rootNode != null && this.rootNode.has("sections")) {
            return this.rootNode.get("sections").size();
        } else {
            return 0;
        }
    }

    /**
     * Modifier le nœud root avec un nouveau fichier JSON.
     *
     * @param name Le nom du fichier JSON sans l'extension.
     */
    public void setRootNode(String name) {
        String resourcePath = "./resources/"+name+".json";
        try (InputStream inputStream = new FileInputStream(resourcePath)) {
            ObjectMapper objectMapper = new ObjectMapper();
            this.rootNode = objectMapper.readTree(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error opening json file :" + e);
        }
    }

}
