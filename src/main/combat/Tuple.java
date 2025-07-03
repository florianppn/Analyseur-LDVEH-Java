package main.combat;

import main.hero.Hero;
import main.point.Point;

/**
 * Représentation d'un tuple contenant un choix et un hero.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class Tuple {

    /**
     * Contient un choix.
     */
    private Integer choice;
    /**
     * Contient un hero.
     */
    private Hero hero;

    private Point point;

    /**
     * Constructeur par défaut.
     * 
     * @param choice choix.
     * @param hero hero du LDVEH.
     */
    public Tuple(Integer choice, Hero hero) {

        this.choice = choice;
        this.hero = hero;

    }

    public Tuple(Point choice, Hero hero) {

        this.point = choice;
        this.hero = hero;

    }

    /**
     * Renvoie un choix.
     * 
     * @return un choix.
     */
    public Integer getChoice() {

        return this.choice;

    }

    public Point getPoint() {
        return this.point;

    }

    /**
     * Modifie le choix.
     */
    public void setChoice(Integer newChoice) {

        this.choice = newChoice;

    }

    public void setPoint(Point newChoice) {

        this.point = newChoice;

    }

    /**
     * Renvoie un hero.
     * 
     * @return un hero.
     */
    public Hero getHero() {

        return this.hero;

    }

    /**
     * Renvoie une chaine de caractère contenant les informations du tuple.
     * 
     * @return une chaine de caractère contenant les informations du tuple.
     */
    @Override
    public String toString() {

        return "Choix : " + this.choice + " | Hero : " + this.hero;

    }

}