package ldveh.hero;

import java.util.*;

import ldveh.json.JsonReader;

/**
 * Représentation d'une démonstration du package ldveh.hero.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class Demo {

    /**
     * Démonstration des fonctionnalités du package ldveh.hero.
     */
    public static void main(String[] args) {

        Setup setup = new Setup(new JsonReader("livre"));

        List<Equipment> equipmentList = setup.getEquipmentList(0,2);
        Hero hero = new Hero(equipmentList, setup.updateDisciplineTree(0,2,1,4,7), setup.updateWeaponsTree(equipmentList), 10, 10, 10);
        
        RandomHero randomHero = new RandomHero(setup);

        //hero sur mesure
        System.out.println(hero);
        //hero aléatoire
        System.out.println(randomHero.generateRandomHero());

    }

}