package main.combat;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

import main.hero.Hero;

/**
 * Représentation d'un ennemi lors d'un combat.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class Enemie {

    /**
     * Contient le nom de l'ennemi.
     */
    private String name;
    /**
     * Contient le nombre de points de vie de l'ennemi.
     */
    private Integer endurance;
    /**
     * Contient le nombre de dégats que l'ennemi peut faire.
     */
    private Integer combatSkill;
    /**
     * Contient un possible malus pour l'ennemi.
     */
    private Boolean doubleDommage;
    /**
     * Contient un possible bonus pour l'ennemi. 
     */
    private String immune;
    /**
     * Contient un possible bonus pour l'ennemi.
     */
    private Boolean hasMindforce;

    /**
     * Constructeur par défaut.
     * 
     * @param name nom de l'ennemi.
     * @param endurance vie de l'ennemi.
     * @param combatSkill competence de combat de l'ennemi.
     * @param doubleDommage malus double dommage.
     * @param immune bonus immunité.
     * @param hasMindforce bonus force mentale. Elle ajoute 2 points supplementaires a combatSkill (sauf si le hero est immunisé).
     */
    public Enemie(JsonNode name, JsonNode endurance, JsonNode combatSkill, JsonNode doubleDommage, JsonNode immune, JsonNode hasMindforce) {

        this.name = name.asText();
        this.endurance = endurance != null ? endurance.asInt() : 0;
        this.combatSkill = combatSkill != null ? combatSkill.asInt() : 0;
        this.doubleDommage = doubleDommage != null ? doubleDommage.asBoolean() : false;
        this.immune = immune != null ? immune.asText() : "null";
        this.hasMindforce = hasMindforce != null ? hasMindforce.asBoolean() : false;

    }

    /** 
     * Retourne le nom de l'ennemi.
     * 
     * @return nom de l'ennemi.
     */
    public String getName(){
        return this.name;
    }

    /** 
     * Retourne la vie de l'ennemi.
     * 
     * @return endurance de l'ennemi.
    */
    public Integer getEndurance() {
        return this.endurance;
    }

    /**
     * Retirer de l'endurance a l'ennemi.
     */
    public void removeEndurance(Integer endurance) {
        this.endurance -= endurance;
    }

    /**
     * Renvoie le niveau de la competence de combat de l'ennemi.
     * 
     * @return niveau de la competence de combat de l'ennemi.
     */
    public Integer getCombatSkill() {
        return this.combatSkill;
    }

    /**
     * Renvoie le possible malus de l'ennemi.
     * 
     * @return le double dommage de l'ennemi.
     */
    public Boolean getDoubleDommage() {
        return this.doubleDommage;
    }

    /**
     * Renvoie la possible immunité de l'ennemi.
     * 
     * @return l'immunité de l'ennemi.
     */
    public String getImmune() {
        return this.immune;
    }

    /**
     * Renvoie le possible bonus "force mentale" de l'ennemi.
     * 
     * @return la force mentale de l'ennemi.
     */
    public Boolean getHasMindforce() {
        return this.hasMindforce;
    }

    /**
     * Renvoie une chaine de caractère contenant les information de l'ennemi.
     * 
     * @return une chaine de caractere contenant le nom de l'ennemi.
     */
    @Override
    public String toString() {
        return "ENEMIE : "+"endurance -> "+this.endurance+" and combatSkill -> "+this.combatSkill;
    }

    /**
     * L'ennemi attaque le hero et lui enleve de l'endurance.
     * 
     * @param hero du LDVEH.
     */
    public void toAttack(Hero hero) {
        if(hero.getDisciplinesTree().get("Mindshield") == false && this.hasMindforce == true) {
            hero.removeEndurance(((this.combatSkill)+2));
        } else {
            hero.removeEndurance(this.combatSkill);
        }
    }

}