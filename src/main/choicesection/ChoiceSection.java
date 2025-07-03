package main.choicesection;

import main.hero.Hero;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

/**
 * Representation de tous les choix possibles dans une section LDVEH.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class ChoiceSection {

    /**
     * Contient tous les choix disponibles dans cette section au format JsonNode.
     */
    private JsonNode choices;
    /**
     * Liste des choix alternatifs configurables dans cette section.
     */
    private List<AlternateChoice> alternateChoiceList;
    /**
     * Liste des choix aleatoires configurables dans cette section.
     */
    private List<RandomPick> randomPickList;
    /**
     * Liste des choix par exigences configurables dans cette section.
     */
    private List<TrimChoice> trimChoiceList;
    /**
     * Propriétés de configuration de la classe.
     */

    /**
     * Constructeur par défaut.
     * 
     * @param choices est le JsonNode contenant tous les choix du Point.
     */
    public ChoiceSection(JsonNode choices) {

        this.choices = choices;
        this.alternateChoiceList = this.initAlternateChoice();
        this.randomPickList = this.initRandomPick();
        this.trimChoiceList = this.initTrimChoice();

    }

    /**
     * Renvoie un JsonNode contenant les choix disponibles pour le Point actuel.
     * 
     * @return un JsonNode représentant les choix du Point.
     */
    public JsonNode getChoices() {
        return this.choices;
    }

    /**
     * Renvoie une liste contenant les choix alternatifs disponibles pour le Point actuel.
     * 
     * @return une liste représentant les choix alternatifs du Point.
     */
    public List<AlternateChoice> getAlternateChoiceList() {
        return this.alternateChoiceList;
    }

    /**
     * Renvoie une liste contenant les sections des choix alternatifs disponibles pour le Point actuel.
     * 
     * @return une liste représentant les choix alternatifs du Point.
     */
    public List<Integer> getAlternateChoiceListSection() {
        List<Integer> sections = new ArrayList<>();
        for(AlternateChoice section : this.alternateChoiceList) {
            sections.add(section.getSection());
        }
        return sections;
    }

    /**
     * Renvoie une liste contenant les choix aléatoires disponibles pour le Point actuel.
     * 
     * @return une liste représentant les choix aléatoires du Point.
     */
    public List<RandomPick> getRandomPickList() {
        return this.randomPickList;
    }

    /**
     * Renvoie une liste contenant les sections des choix aléatoires disponibles pour le Point actuel.
     * 
     * @return une liste représentant les choix aléatoires du Point.
     */
    public List<Integer> getRandomPickListSection() {
        List<Integer> sections = new ArrayList<>();
        for(RandomPick section : this.randomPickList) {
            sections.add(section.getSection());
        }
        return sections;
    }

    /**
     * Renvoie une liste contenant les sections des choix par exigences disponibles pour le Point actuel.
     * 
     * @return une liste représentant les choix par exigences du Point.
     */
    public List<TrimChoice> getTrimChoiceList() {
        return this.trimChoiceList;
    }

    /**
     * Renvoie une liste contenant les sections des choix par exigences disponibles pour le Point actuel.
     * 
     * @return une liste représentant les choix par exigences du Point.
     */
    public List<Integer> getTrimChoiceListSection(List<TrimChoice> trimChoiceList) {
        List<Integer> sections = new ArrayList<>();
        for(TrimChoice section : trimChoiceList) {
            sections.add(section.getSection());
        }
        return sections;
    }

    /**
     * Initialisation d'une liste contenant les choix alternatifs disponibles pour le Point actuel.
     * 
     * @return une liste représentant les choix alternatifs du Point.
     */
    public List<AlternateChoice> initAlternateChoice() {
        List<AlternateChoice> newList = new ArrayList<>();
        for(JsonNode choice : this.choices) {
            if(choice.get("requires") == null && choice.get("range") == null) {
                AlternateChoice alternateChoice = new AlternateChoice(choice.get("text").asText(), choice.get("section").asInt());
                newList.add(alternateChoice);
            }
        }
        return newList;
    }

    /**
     * Initialisation d'une liste contenant les choix aléatoires disponibles pour le Point actuel.
     * 
     * @return une liste représentant les choix aléatoires du Point.
     */
    public List<RandomPick> initRandomPick() {
        List<RandomPick> newList = new ArrayList<>();
        for(JsonNode choice : this.choices) {
            if(choice.get("range") != null) {
                RandomPick randomPick = new RandomPick(choice.get("text").asText(), choice.get("section").asInt(), choice.get("range").get(0).asInt(), choice.get("range").get(1).asInt());
                newList.add(randomPick);
            }
        }
        return newList;
    }

    /**
     * Initialisation d'une liste contenant les choix par exigences disponibles pour le Point actuel.
     * 
     * @return une liste représentant les choix par exigences du Point.
     */
    public List<TrimChoice> initTrimChoice() {

        List<TrimChoice> newList = new ArrayList<>();

        for(JsonNode choice : this.choices) {
            JsonNode requires = choice.get("requires");
            if(requires != null) {
                 Map<String, String> result = new HashMap<>(requires.size());
                 for (JsonNode node : requires) {
                 result.put(String.valueOf(node.get("item")), String.valueOf(node.get("value")));
                 }
                TrimChoice trimChoice = new TrimChoice(choice.get("text").asText(), choice.get("section").asInt(), result);
                newList.add(trimChoice);
            }
        }
        return newList;
    }

    /**
     * Creation d'une liste contenant les choix par exigences que le hero peut débloquer pour le Point actuel.
     * 
     * @param hero du livre.
     * @return une liste de choix par exigences dont le hero possède les exigences requises.
     */
    public List<TrimChoice> unlockableTrimChoice(Hero hero) {
        List<TrimChoice> newList = new ArrayList<>();
        for(TrimChoice c : this.trimChoiceList) {
            if(c.isSatisfied(hero)) {
                newList.add(c);
            }
        }
        return newList;
    }

    /**
     * Renvoie une chaine de caractere aleatoire en fonction des choix disponibles.
     * 
     * @param hero du livre.
     * @return une chaine de caractère aleatoire.
     */
    public String randomTypeChoice(Hero hero) {
        Random random = new Random();
        List<String> typeChoiceList = new ArrayList<>();
        if(!this.alternateChoiceList.isEmpty()) {
            typeChoiceList.add("alternate_choices");
        }
        if(!this.randomPickList.isEmpty()) {
            typeChoiceList.add("is_random_pick");
        }
        if(!this.unlockableTrimChoice(hero).isEmpty()) {
            typeChoiceList.add("trim_choices");
        }
        if(typeChoiceList.isEmpty()) {
            return "null";
        } else {
            int randomPick = random.nextInt(typeChoiceList.size());
            return typeChoiceList.get(randomPick);
        }
    }

    /**
     * Renvoie une liste contenant tous les choix disponibles pour la section.
     * 
     * @param hero du LDVEH.
     * @return une liste contenant tous les choix disponibles pour la section.
     */
    public List<String> typeChoice(Hero hero) {
        List<String> typeChoiceList = new ArrayList<>();
        if(!this.alternateChoiceList.isEmpty()) {
            typeChoiceList.add("alternate_choices");
        }
        if(!this.randomPickList.isEmpty()) {
            typeChoiceList.add("is_random_pick");
        }
        if(!this.unlockableTrimChoice(hero).isEmpty()) {
            typeChoiceList.add("trim_choices");
        }

        return typeChoiceList;

    }

    /**
     * Renvoie une section d'un des choix du Point aleatoirement.
     * La section renvoyé dépend des choix disponibles dans le Point.
     * 
     * @param hero du livre.
     * @return la section choisi aleatoirement s'il n'y a pas de section, alors -1.
     */
    public int useRandomChoice(Hero hero) {
        Random random = new Random();
        String result = this.randomTypeChoice(hero);
        if(result.equals("alternate_choices")) {
            int randomPick = random.nextInt(this.alternateChoiceList.size());
            return this.alternateChoiceList.get(randomPick).getSection();
        } else if (result.equals("is_random_pick")) {
            int randomPick = random.nextInt(this.randomPickList.size());
            return this.randomPickList.get(randomPick).getSection();
        } else if (result.equals("trim_choices")){
            List<TrimChoice> unlockables = this.unlockableTrimChoice(hero);
            int randomPick = random.nextInt(unlockables.size());
            hero.removeSpecialItems(unlockables.get(randomPick).getSpecialItems());
            return unlockables.get(randomPick).getSection();
        } else {
            return -1;
        }
    }

    /**
     * Renvoie une liste des sections de tous les types de choix disponibles dans le Point.
     * 
     * @param hero du LDVEH.
     * @return une liste des sections de tous les types de choix disponibles dans le Point.
     */
    public List<Integer> useAllChoice(Hero hero) {
        List<String> choices = this.typeChoice(hero);
        List<Integer> sections = new ArrayList<>();
        for (String choice : choices) {
            if(choice.equals("alternate_choices")) {
                sections.addAll(this.getAlternateChoiceListSection());
            } else if (choice.equals("is_random_pick")) {
                sections.addAll(this.getRandomPickListSection());
            } else if (choice.equals("trim_choices")){
                List<TrimChoice> unlockables = this.unlockableTrimChoice(hero);
                sections.addAll(this.getTrimChoiceListSection(unlockables));
            }
        }
        return sections;
    }

    /**
     * Renvoie une chaine de caractere contenant les informations des sections du Point.
     * 
     * @return une chaine de caractere contenant les infos de l'instance ChoiceSection.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(AlternateChoice c : this.alternateChoiceList){
            result.append(c).append("\n");
        }
        for(RandomPick c : this.randomPickList){
            result.append(c).append("\n");
        }
        for(TrimChoice c : this.trimChoiceList){
            result.append(c).append("\n");
        }
        return result.toString();
    }

}