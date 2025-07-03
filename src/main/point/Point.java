package main.point;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

import main.choicesection.ChoiceSection;
import main.combat.CombatNode;
import main.item.Item;
import main.hero.Hero;
import main.combat.Tuple;

/**
 * Représentation d'un point (=une section) du LDVEH.
 * 
 * @author Tom David et Florian Pépin
 * @version 1.0
 */
public class Point {

    private static int idCounter = 0;
    private int id;
    private JsonNode pointNode;
    public List<Integer> childsID = new ArrayList<>();
    public List<Point> childsPoint;
    private String text;

    public Point(int id ,JsonNode pointNode) {
        this.id = id;
        this.pointNode = pointNode;
        this.childsID = this.getChildsID();
        this.childsPoint = new ArrayList<>();
        this.text = this.getText();
    }

    /**
     * Ajoute un point à la liste des points enfants de ce point.
     * 
     * @param point à ajouter.
     */
    public void addChildList(Point point){
        childsPoint.add(point);
    }

    /**
     * Retourne l'identifiant unique du point.
     * 
     * @return L'identifiant unique du point.
     */
    public int getID() {
        return id;
    }

    /**
     * Retourne le point en JsonNode.
     * 
     * @return le point en JsonNode.
     */
    public JsonNode getPointNode() {
        return this.pointNode;
    }

    /**
     * Retourne le texte lié au point. 
     *
     * @return le texte lié au point.
     */
    public String getText(){
        if(this.pointNode.get("text")!=null)
            return this.pointNode.get("text").asText();
        return null;
    }

    /**
     * Retourne le noeud JSON contenant les données de combat du point.
     * 
     * @return Le noeud JSON contenant les données de combat du point.
     */
    public JsonNode getCombatNode() {
        return this.pointNode.get("combat");
    }

    /**
     * Retourne le noeud JSON contenant les données items du point.
     * 
     * @return le noeud JSON contenant les données items du point.
     */
    public JsonNode getItemsNode() {
        return this.pointNode.get("items");
    }
    
    /**
     * Renvoie une liste d'entiers représentant les identifiants des sections enfants.
     * 
     * @return Une liste d'entiers représentant les identifiants des sections enfants.
     */
    public List<Integer> getChildsID() {
        List<Integer> liste = new ArrayList<>();
        for (JsonNode choice : getAllChoice()) {
            liste.add(choice.get("section").asInt());
        }
        return liste;
    }

    /**
     * Renvoie une liste de points représentant les enfants de ce point.
     * 
     * @return Une liste de points représentant les enfants de ce point.
     */
    public List<Point> getChildsPoint(){
        return childsPoint;
    }

    /**
     * Renvoie nn objet JsonNode représentant tous les choix disponibles pour ce point.
     * 
     * @return Un objet JsonNode représentant tous les choix disponibles pour ce point.
     */
    public JsonNode getAllChoice(){
        return this.pointNode.get("choices");
    }

    /**
     * Simule l'avancé du Héro dans le LDVEH.
     * Renvoie -1 si le hero est mort et 0 si le hero a fini le ldveh.
     * 
     * @param hero du ldveh.
     * @return la prochaine section et ajoute les items.
     */
    public int nextSection(Hero hero) {

        hero.useBackpackItems();
        JsonNode items = this.getItemsNode();
        if(items != null) {
            for(int i = 0; i<items.size(); i++) {
                Item item = new Item(items.get(i));
                item.addItem(hero);
            }
        }
        JsonNode combat = this.getCombatNode();
        if(combat != null) {

            CombatNode combatNode = new CombatNode(combat);
            int res = combatNode.getCombat().useRandomChoice(hero);

            if (res == -1){
                return res;
            } 
            int child = this.getChildsID().get(res);
            if (child == 350) {
                return 0;
            }
            return child;

        } else {

            ChoiceSection newChoiceSection = new ChoiceSection(this.getAllChoice());
            int child = newChoiceSection.useRandomChoice(hero);

            if (child == 350) {
                return 0;
            }
            return child;
        }
    }


    /**
     * Simule l'avancé du Héro dans le LDVEH.
     * Renvoie -1 si le hero est mort et 0 si le hero a fini le ldveh.
     *
     * @param hero du ldveh.
     * @param point du ldveh.
     * @return la prochaine section et ajoute les items.
     */
    public List<Tuple> nextSectionHero(Hero hero, Point point) {
        hero.useBackpackItems();
        JsonNode items = this.getItemsNode();
    
        if(items != null) {
            for(int i = 0; i<items.size(); i++) {
                Item item = new Item(items.get(i));
                item.addItem(hero);
            }
        }
        JsonNode combat = this.getCombatNode();
        if(combat != null) {
            CombatNode combatNode = new CombatNode(combat);
            //Une liste de liste sous cette forme [[prochaine_section,hero_modifier], [prochaine_section,hero_modifier]]
            List<Tuple> res = combatNode.getCombat().useAllChoice(hero);
            //remplace 350 par 0

            for (Tuple t : res) {
                if(t.getChoice()!=-1){
                    t.setChoice(point.getChildsID().get(t.getChoice()));
                }

            }
            return res;

        } else {
            ChoiceSection newChoiceSection = new ChoiceSection(this.getAllChoice());
            List<Integer> Lis = newChoiceSection.useAllChoice(hero);

            List<Tuple> childs = new ArrayList<>();

            for (Integer i : Lis) {
                Tuple tuple = new Tuple( i, hero);
                childs.add(tuple);
            }

            return childs;
        }
    }

}