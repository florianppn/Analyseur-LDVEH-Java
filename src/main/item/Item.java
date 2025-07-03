package main.item;

import main.hero.*;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Représentation d'un item de ldveh.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class Item {

    private JsonNode item;
    private Equipment equipment;

    public Item(JsonNode item) {
        this.item = item;
        this.equipment = this.createEquipment();
    }

    public Equipment getEquipment() {
        return this.equipment;
    }

    public JsonNode getItem() {
        return this.item;
    }

    /**
     * Créer un Equipment à partir de l'item.
     *
     * @return l'Equipment créé.
     */
    private Equipment createEquipment() {
        JsonNode name = this.item.get("name");
        JsonNode acSection = this.item.get("ac_section");
        JsonNode endurance = this.item.get("endurance");
        JsonNode permanent = this.item.get("permanent");
        JsonNode consumable = this.item.get("is_consumable");
        JsonNode combatSkill = this.item.get("combat_skill");
        return new Equipment(name, acSection, endurance, permanent, consumable, combatSkill);
    }

    /**
     * Créer de l'or à partir de l'item.
     *
     * @return la quantité d'or créée.
     */
    private Integer createGold() {
        return this.item.get("value").asInt();
    }

    /**
     * Ajouter un item au Hero.
     * 
     * @param hero qui reçoit l'item.
     */
    public void addItem(Hero hero) {
        if(this.item.get("ac_section").asText().equals("gold")) {
            hero.addGold(this.createGold());
        } else if (this.item.get("ac_section").asText().equals("backpack_items")) {
            hero.addBackpackItems(this.createEquipment());
        } else if (this.item.get("ac_section").asText().equals("special_items")) {
            hero.addSpecialItems(this.createEquipment());
        } else if (this.item.get("ac_section").asText().equals("weapons")) {
            if(!hero.atLeastOneWeapon()) {
                hero.addWeaponsTree(this.item.get("name").asText());
            }
        }
    }

}