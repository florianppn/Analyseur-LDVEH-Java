package main.combat;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;

/**
 * Représentation du traitement des informations d'un combat au format JsonNode.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class CombatNode {

    /**
     * Contient le combat dans la section au format JsonNode.
     */
    private JsonNode combatNode;

    /**
     * Constructeur par défaut.
     * 
     * @param combatNode contient le combat present dans une section.
     */
    public CombatNode(JsonNode combatNode) {

        this.combatNode = combatNode;

    }

    /**
     * Renvoie la section deverouillable si le joueur gagne le combat.
     * 
     * @return la section deverouillable si le joueur gagne le combat.
     */
    public Integer getWin() {
        return this.combatNode.get("win").get("choice").intValue();
    }

    /**
     * Renvoie un boolean pour savoir si l'ennemi est un boss ou non.
     * 
     * @return true si l'ennemi est un boss sinon false.
     */
    public Boolean getSpecial() {
        JsonNode special = this.combatNode.get("is_special");
        return special != null ? special.asBoolean() : false;
    }

    /**
     * Renvoie un possible niveau de competence de combat que le hero perdra lors du combat.
     * 
     * @return le niveau de la competence de combat s'il y en a une sinon null.
     */
    public Integer getCombatSkill() {
        JsonNode combatSkill = this.combatNode.get("combat_skill");
        return combatSkill != null ? combatSkill.intValue() : null;
    }

    /**
     * Renvoie une possible evasion.
     * 
     * @return une possible evasion, si il n'y en a pas alors null.
     */
    public Evasion getEvasion() {
        JsonNode evasionNode = this.combatNode.get("evasion");
        if (evasionNode == null) {
            Evasion evasion = new Evasion(null, null);
            return evasion;
        } else {
            Evasion evasion = new Evasion(evasionNode.get("n_rounds"), evasionNode.get("choice"));
            return evasion;
        }
    }

    /**
     * Renvoie une liste contenant des ennemies.
     * 
     * @return une liste d'ennemis.
     */
    public List<Enemie> getListEnemie() {
        JsonNode enemiesNode = this.combatNode.get("enemies");
        List<Enemie> enemiesList = new ArrayList<>();

        for (JsonNode e : enemiesNode) {
            Enemie enemie = new Enemie(e.get("name"), e.get("endurance"), e.get("combat_skill"), e.get("double_dommage"), e.get("immune"), e.get("has_mindforce"));
            enemiesList.add(enemie);
        }
        return enemiesList;

    }

    /**
     * Génère une classe Combat.
     * 
     * @return une classe Combat.
     */
    public Combat getCombat() {
        Combat combat = new Combat(this.getWin(), this.getCombatSkill(), this.getSpecial(), this.getEvasion(), this.getListEnemie());
        return combat;
    }


}