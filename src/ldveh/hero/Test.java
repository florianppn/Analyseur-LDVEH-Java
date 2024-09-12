package ldveh.hero;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

import ldveh.json.JsonReader;
import ldveh.combat.*;
import ldveh.point.*;

/**
 * Representation des tests du package ldveh.hero.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class Test {

    /**
     * Vérifie que le hero est capable d'enlever des points de vie à un ennemi.
     * 
     * @param pointManager gère les points d'un LDVEH.
     */
    public void heroAttack(PointManager pointManager) {

        Point point = pointManager.getPoint(268);
        CombatNode combatNode = new CombatNode(point.getCombatNode());
        List<Enemie> enemies = combatNode.getListEnemie();
        Setup setup = new Setup(new JsonReader("livre"));
        List<Equipment> equipmentList = setup.getEquipmentList(0,2);
        Hero hero = new Hero(equipmentList, setup.updateDisciplineTree(0,2,1,4,8), setup.updateWeaponsTree(equipmentList), 10, 60, 28);
        hero.toAttack(enemies.get(0));

        assert hero.getEndurance() == 60 : "endurance de l'ennemi différente de 60";

    }

    /**
     * Vérifie que les equipements sont bien à leur place.
     */
    public void isEquipement() {

        Setup setup = new Setup(new JsonReader("livre"));
        List<Equipment> equipmentList = setup.getEquipmentList(2, 3);
        Hero hero = new Hero(equipmentList, setup.updateDisciplineTree(0,2,1,4,8), setup.updateWeaponsTree(equipmentList), 10, 60, 28);

        assert hero.isSpecialItems("Chainmail Waistcoat") == true : "L'item special n'y est pas, ce n'est pas normal";
        assert hero.isBackpackItems("Meal") == true : "L'item sac à dos n'y est pas, ce n'est pas normal";
        assert hero.isSpecialItems("Chevre") == false : "L'item special est présent, ce n'est pas normal";
        assert hero.isBackpackItems("Chevre") == false : "L'item special est présent, ce n'est pas normal";

    }

    /**
     * Vérifie si le hero a une arme ou non.
     */
    public void isWeapon() {

        Setup setup = new Setup(new JsonReader("livre"));
        List<Equipment> equipmentListA = setup.getEquipmentList(2, 3);
        List<Equipment> equipmentListB = setup.getEquipmentList(0, 1);
        Hero heroA = new Hero(equipmentListA, setup.updateDisciplineTree(0,2,1,4,8), setup.updateWeaponsTree(equipmentListA), 10, 60, 28);
        Hero heroB = new Hero(equipmentListB, setup.updateDisciplineTree(0,2,1,4,8), setup.updateWeaponsTree(equipmentListB), 10, 60, 28);

        assert heroA.atLeastOneWeapon() == false : "Le hero a une arme, ce n'est pas normal.";
        assert heroB.atLeastOneWeapon() == true : "Le hero n'a pas d'arme, ce n'est pas normal.";

    }

    /**
     * Tests des fonctionnalités du package ldveh.hero.
     */
    public static void main(String[] args) {

        PointManager pointManager = new PointManager(new JsonReader("livre"));

        Test test = new Test();
        test.heroAttack(pointManager);
        test.isEquipement();
        test.isWeapon();

    }

}
