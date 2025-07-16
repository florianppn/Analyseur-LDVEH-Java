package main.hero;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

import main.model.*;

/**
 * Représentation du traitement des informations du hero d'un LDVEH au format JSON.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class Setup {

    /**
     * Contient le LDVEH au format JSON.
     */
    private JsonReader book;

    /**
     * Constructeur par défaut.
     * 
     * @param book LDVEH au format JSON.
     */
    public Setup(JsonReader book) {

        this.book = book;

    }

    /**
     * Retourne le setup du LDVEH.
     * 
     * @return JsonNode setup.
     */
    public JsonNode getSetupNode() {
        return this.book.getSetup();
    }

    /**
     * Retourne la sequence du setup du LDVEH.
     * 
     * @return JsonNode sequence.
     */
    public JsonNode getSequenceNode() {
        return this.getSetupNode().get("sequence");
    }

    /**
     * Retourne les disciplines du setup LDVEH.
     * 
     * @return JsonNode des disciplines disponible dans le jeu.
     */
    public JsonNode getDisciplinesNode() {
        return this.getSetupNode().get("disciplines");
    }

    /**
     * Retourne une discipline du setup LDVEH.
     * IMPORTANT : D'apres le setup du livre, il faut choisir ses disciplines avec un nombre.
     * 
     * @param number de la discipline.
     * @return JsonNode d'une discipline.
     */
    public JsonNode getDisciplineNode(Integer number) {
        return this.getDisciplinesNode().get(number);
    }

    /**
     * Retourne un Map des disciplines debloquables dans le LDVEH.
     * 
     * @return un Map des disciplines debloquables dans l'histoire.
     */
    public Map<String, Boolean> createDisciplinesTree() {
        JsonNode disciplinesNode = this.getDisciplinesNode();
        Map<String, Boolean> disciplinesTree = new HashMap<>();

        for (JsonNode discipline : disciplinesNode) {
            disciplinesTree.put(discipline.toString().replace("\"", ""), false);
        }
        return disciplinesTree;
    }

    /**
     * Retourne un Map des disciplines debloques et non debloques dans l'histoire.
     * IMPORTANT : D'apres le setup du livre nous pouvons choisir exactement 5 disciplines.
     * 
     * @param d1 numero de la premiere discipline.
     * @param d2 numero de la seconde discipline.
     * @param d3 numero de la troisieme discipline.
     * @param d4 numero de la quatrieme discipline.
     * @param d5 numero de la cinquieme discipline.
     * @return un Map des disciplines debloques et non debloques dans l'histoire.
     */
    public Map<String, Boolean> updateDisciplineTree(Integer d1, Integer d2, Integer d3, Integer d4, Integer d5) {
        Map<String, Boolean> disciplinesTree = this.createDisciplinesTree();
        disciplinesTree.put(this.getDisciplineNode(d1).toString().replace("\"", ""), true);
        disciplinesTree.put(this.getDisciplineNode(d2).toString().replace("\"", ""), true);
        disciplinesTree.put(this.getDisciplineNode(d3).toString().replace("\"", ""), true);
        disciplinesTree.put(this.getDisciplineNode(d4).toString().replace("\"", ""), true);
        disciplinesTree.put(this.getDisciplineNode(d5).toString().replace("\"", ""), true);
        return disciplinesTree;
    }

    /**
     * Retourne les armes disponibles dans le setup du LDVEH sous le format JsonNode.
     * 
     * @return JsonNode des armes disponibles dans le jeu.
     */
    public JsonNode getWeaponsNode() {
        return this.getSetupNode().get("weapons");
    }

    /**
     * Retourne un Map des armes debloquable dans l'histoire.
     * 
     * @return un Map des armes debloquable dans l'histoire.
     */
    public Map<String, Boolean> createWeaponsTree() {
        JsonNode weaponsNode = this.getWeaponsNode();
        Map<String, Boolean> weaponsTree = new HashMap<>();

        for (JsonNode weapon : weaponsNode){
            weaponsTree.put(weapon.toString().replace("\"", ""), false);
        }
        return weaponsTree;
    }

    /**
     * Retourne les equipements disponibles dans le setup du LDVEH sous le format JsonNode.
     * 
     * @return JsonNode des equipements disponibles sur la table a votre arrivé.
     */
    public JsonNode getEquipmentNode() {
        return this.getSetupNode().get("equipment");
    }

    /**
     * Retourne un JsonNode de l'équipement que vous avez sélectionné.
     * IMPORTANT : D'apres le setup du livre, il faut choisir son equipement avec un nombre.
     * 
     * @param number le numero de l'equipement.
     * @return JsonNode de l'equipement selectionné.
     */
    public JsonNode getEquipmentNode(Integer number) {
        return this.getEquipmentNode().get(number);
    }

    /**
     * Retourne une liste contenant un équipement du setup LDVEH.
     *
     * @param number le numero de l'equipement.
     * @return une liste contenant l'equipement ou les sous-equipements.
     */
    public List<Equipment> getEquipmentList(Integer number) {
        List<Equipment> equipmentList = new ArrayList<>();
        JsonNode equipmentNode = this.getEquipmentNode(number);

        if (equipmentNode.get("name") == null){
            for (JsonNode subEquipment : equipmentNode){
                Equipment newEquipement = new Equipment(subEquipment.get("name"), subEquipment.get("ac_section"), subEquipment.get("endurance"), subEquipment.get("is_permanent"), subEquipment.get("is_consumable"), subEquipment.get("combat_skill"));
                equipmentList.add(newEquipement);
            }
        } else {
            Equipment newEquipement = new Equipment(equipmentNode.get("name"), equipmentNode.get("ac_section"), equipmentNode.get("endurance"), equipmentNode.get("is_permanent"), equipmentNode.get("is_consumable"), equipmentNode.get("combat_skill"));
            equipmentList.add(newEquipement);
        }
        return equipmentList;
    }

    /**
     * Retourne une liste contenant les deux equipements de depart.
     * 
     * @param e1 le premier equipement de depart.
     * @param e2 le deuxieme equipement de depart.
     * @return une liste contenant les deux equipements de depart.
     */
    public List<Equipment> getEquipmentList(Integer e1, Integer e2) {
        List<Equipment> equipmentList = this.getEquipmentList(e1);
        equipmentList.addAll(this.getEquipmentList(e2));
        return equipmentList;
    }

    /**
     * Retourne un Map des armes debloques et non debloques dans l'histoire.
     * 
     * @param equipmentList liste des equipements du hero.
     * @return un Map des armes debloques et non debloques dans l'histoire.
     */
    public Map<String, Boolean> updateWeaponsTree(List<Equipment> equipmentList) {
        Map<String, Boolean> weaponsTree = this.createWeaponsTree();
        for(Equipment equipment : equipmentList){
            if(equipment.getAcSection().equals("weapons")) {
                weaponsTree.put(equipment.getName(), true);
            }
        }
        return weaponsTree;
    }

}
