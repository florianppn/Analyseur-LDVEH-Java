package main.choicesection;

import java.util.*;

import main.hero.Hero;

/**
 * Representation d'un choix par exigence dans un LDVEH.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class TrimChoice extends AlternateChoice {

    /**
     * Map des exigences nécessaires pour déverouiller ce choix.
     */
    private Map<String, String> requires;

    /**
     * Constructeur par défaut.
     * 
     * @param text du choix.
     * @param section du choix.
     * @param requires du choix.
     */
    public TrimChoice(String text, Integer section, Map<String, String> requires) {

        super(text, section);
        this.requires = requires;


    }

    /**
     * Renvoie un Map contenant les exigences du choix.
     * 
     * @return un Map représentant les exigences du choix.
     */
    public Map<String, String> getRequires() {
        return this.requires;
    }

    /**
     * Renvoie une liste contenant les items spéciaux du Map requires de la classe.
     * 
     * @return un liste représentant les items speciaux du Map requires de la classe.
     */
    public List<String> getSpecialItems() {
        List<String> specialItems = new ArrayList<>();
        for(Map.Entry require : this.requires.entrySet()) {
            if(require.getKey() == "specialItems") {
                specialItems.add((String)require.getValue());
            }
        }
        return specialItems;
    }

    /**
     * Détermine si la condition est satisfaite.
     * 
     * @param hero de l'histoire.
     * @return true si le hero possede toutes les exigences necessaires pour deverouiller la section sinon false.
     */
    public boolean isSatisfied(Hero hero) {
        boolean ok = true;
        for(Map.Entry require : this.requires.entrySet()) {
            if(require.getKey().equals("kaiDisciplines")) {
                ok = hero.getDisciplinesTree().get((String)require.getValue());
            } else if (require.getKey() == "gold") {
                ok = hero.getGold().equals(Integer.valueOf((String)require.getValue()));
            } else if (require.getKey() == "backpackItems") {
                ok = hero.isBackpackItems((String)require.getValue());
            } else if (require.getKey() == "specialItems") {
                ok = hero.isSpecialItems((String)require.getValue());
            }
        }
        return ok;
    }

    /**
     * Renvoie une chaine de caractere contenant les informations de choix par exigences.
     * 
     * @return une chaine de caractere contenant les infos de l'instance TrimChoice.
     */
    @Override
    public String toString() {
        String requiresString = "";
        for(Map.Entry require : this.requires.entrySet()) {
            requiresString += (String)require.getKey() + " : " + (String)require.getValue() + "\n";
        }
        return super.toString() + "• " + requiresString;
    }

}