package main.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

import main.choicesection.ChoiceSection;
import main.combat.CombatNode;
import main.item.Item;
import main.hero.Hero;
import main.utils.Pair;

/**
 * Représentation d'un point (une section) du LDVEH.
 * 
 * @author Tom David et Florian Pépin
 * @version 2.0
 */
public class Point {

    public static int idCounter = 0;
    private int id;
    private JsonNode pointNode;
    private List<Integer> childsID;
    private List<Point> childs;
    private String text;

    public Point(int id ,JsonNode pointNode) {
        Point.idCounter++;
        this.id = id;
        this.pointNode = pointNode;
        this.childsID = this.initChilds();
        this.childs = new ArrayList<>();
        this.text = this.pointNode.get("text") != null ? this.pointNode.get("text").asText() : "";
    }

    public int getID() {
        return this.id;
    }

    public JsonNode getPointNode() {
        return this.pointNode;
    }

    public List<Integer> getChildsID() {
        return this.childsID;
    }

    public List<Point> getChilds() {
        return this.childs;
    }

    public String getText(){
        return this.text;
    }

    public JsonNode getCombatNode() {
        return this.pointNode.get("combat");
    }

    public JsonNode getItemsNode() {
        return this.pointNode.get("items");
    }

    public JsonNode getAllChoice() {
        return this.pointNode.get("choices");
    }

    public void addChild(Point child) {
        this.childs.add(child);
    }

    /**
     * Renvoie une liste d'entiers représentant les identifiants des sections enfants.
     *
     * @return Une liste d'entiers représentant les identifiants des sections enfants.
     */
    public List<Integer> initChilds() {
        List<Integer> liste = new ArrayList<>();
        for (JsonNode choice : getAllChoice()) {
            liste.add(choice.get("section").asInt());
        }
        return liste;
    }

    /**
     * Simule l'avancée du Héro dans le LDVEH.
     * Renvoie -1 si le hero est mort et 0 si le hero a fini le LDVEH.
     *
     * @param hero du ldveh.
     * @return la prochaine section et ajoute les items.
     */
    public int nextSection(Hero hero) {
        hero.useBackpackItems();
        JsonNode items = this.getItemsNode();
        if (items != null) {
            for(int i = 0; i<items.size(); i++) {
                Item item = new Item(items.get(i));
                item.addItem(hero);
            }
        }
        JsonNode combat = this.getCombatNode();
        if (combat != null) {
            CombatNode combatNode = new CombatNode(combat);
            int res = combatNode.getCombat().useRandomChoice(hero);
            if (res == -1) return res;
            int child = this.getChildsID().get(res);
            if (child == 350) return 0;
            return child;
        } else {
            ChoiceSection newChoiceSection = new ChoiceSection(this.getAllChoice());
            int child = newChoiceSection.useRandomChoice(hero);
            if (child == 350) return 0;
            return child;
        }
    }

    /**
     * Simule l'avancée du Héro dans le LDVEH.
     * Renvoie -1 si le hero est mort et 0 si le hero a fini le LDVEH.
     *
     * @param hero du LDVEH.
     * @param point suivant du LDVEH.
     * @return la prochaine section et ajoute les items.
     */
    public List<Pair<Integer, Hero>> nextSectionHero(Hero hero, Point point) {
        hero.useBackpackItems();
        JsonNode items = this.getItemsNode();
        if (items != null) {
            for(int i = 0; i<items.size(); i++) {
                Item item = new Item(items.get(i));
                item.addItem(hero);
            }
        }
        JsonNode combat = this.getCombatNode();
        if(combat != null) {
            CombatNode combatNode = new CombatNode(combat);
            List<Pair<Integer, Hero>> res = combatNode.getCombat().useAllChoice(hero);
            for (Pair<Integer, Hero> p : res) {
                if (p.getFirst().equals(-1)) {
                    p.setFirst(point.getChildsID().get(p.getFirst()));
                }
            }
            return res;
        } else {
            ChoiceSection newChoiceSection = new ChoiceSection(this.getAllChoice());
            List<Integer> res = newChoiceSection.useAllChoice(hero);
            List<Pair<Integer, Hero>> childs = new ArrayList<>();
            for (Integer i : res) {
                Pair<Integer, Hero> pair = new Pair<Integer, Hero>( i, hero);
                childs.add(pair);
            }
            return childs;
        }
    }

}