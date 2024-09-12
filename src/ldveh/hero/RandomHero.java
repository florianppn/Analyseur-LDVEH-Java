package ldveh.hero;

import ldveh.json.JsonReader;

import java.util.*;

/**
 * Représentation d'un hero initialisé aleatoirement.
 * 
 * @author Florian Pépin
 * @version 1.0 
 */
public class RandomHero {

    /**
     * Contient le setup du LDVEH.
     */
    private Setup setup;
    /**
     * Contient une instance aléatoire.
     */
    private Random random;

    /**
     * Constructeur par défaut.
     * 
     * @param setup du LDVEH.
     */
    public RandomHero(Setup setup) {

        this.setup = setup;
        this.random = new Random();

    }

    /**
     * Creer une liste aleatoire de n nombre(s).
     * 
     * @param n est la taille de la liste.
     * @return une List contenant n chiffres aleatoirement melanges.
     */
    public List<Integer> getRandomNumberList(int n) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        List<Integer> fiveNumbers = numbers.subList(0, 5);
        return fiveNumbers;
    }

    /**
     * Genere un hero aleatoirement.
     * 
     * @return un hero.
     */
    public Hero generateRandomHero() {
        int nbEquipments = this.setup.getEquipmentNode().size();
        int nbDisciplines = this.setup.getDisciplinesNode().size();
        List<Integer> numbers = this.getRandomNumberList(nbDisciplines);
        int eqOne;
        int eqTwo;
        do {
            eqOne = this.random.nextInt(nbEquipments);
            eqTwo = this.random.nextInt(nbEquipments);
        } while (eqOne == eqTwo);

        List<Equipment> equipmentList = this.setup.getEquipmentList(eqOne, eqTwo);
        Random random = new Random();
        Hero hero = new Hero(equipmentList, setup.updateDisciplineTree(numbers.get(0), numbers.get(1), numbers.get(2), numbers.get(3), numbers.get(4)), setup.updateWeaponsTree(equipmentList), random.nextInt(21) + 5, random.nextInt(21) + 1, random.nextInt(50) + 1);

        return hero;
    }

}