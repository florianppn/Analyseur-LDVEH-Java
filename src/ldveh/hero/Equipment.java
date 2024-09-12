package ldveh.hero;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;

/**
 * Représentation d'un equipement de héro.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class Equipment {

    /**
     * Contient le nom de l'équipement.
     */
    private String name;
    /**
     * Contient le nom de la section de l'équipement.
     */
    private String acSection;
    /**
     * Contient les points de vie bonus de l'équipement.
     */
    private Integer endurance;
    /**
     * Contient la possibilité que l'équiepement soit permanent.
     */
    private Boolean permanent;
    /**
     * Contient la possibilité que l'équipement soit un consommable.
     */
    private Boolean consumable;
    /**
     * Contient les points de d'attaques bonus de l'équipement.
     */
    private Integer combatSkill;

    /**
     * Constructeur par défaut.
     * 
     * @param name nom de l'equipement.
     * @param acSection section de l'equipement.
     * @param endurance bonus endurance de l'equipement.
     * @param permanent équipement permanent.
     * @param consumable équipement consommable.
     * @param combatSkill competence de combat bonus de l'equipement.
     */
    public Equipment(JsonNode name, JsonNode acSection, JsonNode endurance, JsonNode permanent, JsonNode consumable, JsonNode combatSkill) {

        this.name = name.asText();
        this.acSection = acSection.asText();
        this.endurance = endurance != null ? endurance.asInt() : 0;
        this.permanent = permanent != null ? permanent.asBoolean() : false;
        this.consumable = consumable != null ? consumable.asBoolean() : false;
        this.combatSkill = combatSkill != null ? combatSkill.asInt() : 0;

    }

    /**
     * Retourne le nom de l'équipement.
     * 
     * @return nom de l'equipement.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retourne la section de l'équipement.
     * 
     * @return section a laquelle appartient l'equipement.
     */
    public String getAcSection() {
        return this.acSection;
    }

    /**
     * Retourne le possible bonus d'endurance.
     * 
     * @return bonus endurance lors de l'utilisation de l'equipement.
     */
    public Integer getEndurance() {
        return this.endurance;
    }

    /**
     * Retourne un boolean pour savoir si l'équipement est permanent ou non.
     * 
     * @return true si les bonus sont permanents, false sinon.
     */
    public Boolean getPermanent() {
        return this.permanent;
    }

    /**
     * Retourne un boolean pour savoir si l'équipement est un consommable ou non.
     * 
     * @return true si on peut consommer l'equipement,false sinon.
     */
    public Boolean getConsumable() {
        return this.consumable;
    }

    /**
     * Retourne le niveau de compétence de combat que l'équipement octroie.
     * 
     * @return niveau de competence de combat de l'equipement.
     */
    public Integer getCombatSkill() {
        return this.combatSkill;
    }

}