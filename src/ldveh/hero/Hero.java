package ldveh.hero;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

import ldveh.combat.Enemie;

/**
 * Représentation d'un hero pour le LDVEH.
 */
public class Hero {
    
    /**
     * Contient la liste des items "sac à dos".
     */
    private List<Equipment> backpackItems;
    /**
     * Contient la liste des items "spéciaux".
     */
    private List<Equipment> specialItems;
    /**
     * Contient l'arbre des disciplines du héro.
     */
    private Map<String, Boolean> disciplinesTree;
    /**
     * Contient l'arbre des armes du héro. 
     */    
    private Map<String, Boolean> weaponsTree;
    /**
     * Contient les pièces d'or du héro.
     */
    private Integer gold;
    /**
     * Contient les points de vie du héro.
     */
    private Integer endurance;
    /**
     * Contient les points d'attaque du héro.
     */
    private Integer combatSkill;

    /**
     * Constructeur par défaut.
     * 
     * @param disciplinesTree Arbre de deblocage des disciplines. Debloquer exactement 5 disciplines lors du setup.
     * @param weaponsTree Arbre de deblocage des armes. Debloquer maximum 2 armes lors du setup.
     * @param gold piece d'or du hero.
     * @param endurance représente la vie du hero.
     * @param combatSkill représente les degats que le hero inflige aux ennemis.
     */
    public Hero(List<Equipment> equipmentList, Map<String, Boolean> disciplinesTree, Map<String, Boolean> weaponsTree, Integer gold, Integer endurance, Integer combatSkill) {

        this.backpackItems = this.takeBackpackItems(equipmentList);
        this.specialItems = this.takeSpecialItems(equipmentList);
        this.disciplinesTree = disciplinesTree;
        this.weaponsTree = weaponsTree;
        this.gold = gold;
        this.endurance = endurance;
        this.combatSkill = combatSkill;

    }

    /**
     * Surcharge du constructeur.
     * IMPORTANT : A utiliser lors de la copie d'un héro.
     * 
     * @param hero du LDVEH.
     */
    public Hero(Hero hero) {

        this.backpackItems = hero.getBackpackItems();
        this.specialItems = hero.getSpecialItems();
        this.disciplinesTree = hero.getDisciplinesTree();
        this.weaponsTree = hero.getWeaponsTree();
        this.gold = hero.getGold();
        this.endurance = hero.getEndurance();
        this.combatSkill = hero.getCombatSkill();

    }



    /**
     * Retourne la liste des items "sac a dos"
     * 
     * @return liste des items "sac a dos".
     */
    public List<Equipment> getBackpackItems() {
        return this.backpackItems;
    }

    /**
     * Retourne la liste des items "scpeciaux".
     * 
     * @return liste des items "speciaux".
     */
    public List<Equipment> getSpecialItems() {
        return this.specialItems;
    }

    /**
     * Retourne l'arbre de déblocage des armes.
     * 
     * @return Map des disciplines deblocable/debloque du hero.
     */
    public Map<String, Boolean> getDisciplinesTree() {
        return this.disciplinesTree;
    }

    /**
     * Modifie l'état d'une discipline dans l'arbre des disciplines.
     * 
     * @param discipline est la discipline qui veut etre modifiee.
     * @param status est le bool qui representera l'etat de la discipline (verouille/deverouille).
     */
    public void setDisciplinesTree(String discipline, Boolean status) {
        this.disciplinesTree.put(discipline, status);
    }

    /**
     * Retourne une liste des disciplines débloqués.
     *
     * @return une liste des disciplines debloques.
     */
    public List<String> unlockDisciplinesList() {
        List<String> unlock = new ArrayList<>();

        for (Map.Entry discipline : this.disciplinesTree.entrySet()) {
            if ((boolean)discipline.getValue()) {
                unlock.add((String)discipline.getKey());
            }
        }

        return unlock;
    }

    /**
     * Retourne l'arbre des armes du hero.
     * 
     * @return Map des armes deblocable/debloque du hero.
     */
    public Map<String, Boolean> getWeaponsTree() {
        return this.weaponsTree;
    }

    /**
     * Ajoute une arme à l'arbre des armes du héro.
     * 
     * @param weapon à ajouter.
     */
    public void addWeaponsTree(String weapon) {
        this.weaponsTree.put(weapon, true);
    }

    /**
     * Vérifie si le héro possède au moins une arme.
     * 
     * @return true s'il y a au moins une arme debloqué par le hero sinon false.
     */
    public boolean atLeastOneWeapon() {
        for(Map.Entry weapon : this.weaponsTree.entrySet()) {
            if((boolean)weapon.getValue()){
                return true;
            }
        }
        return false;
    }

    /**
     * Modifie l'état d'une arme dans l'arbre des armes du hero.
     * 
     * @param weapon est l'arme qui veut etre modifiee.
     * @param status est le bool qui representera l'etat de l'arme (verrouille/deverouille).
     */
    public void setWeaponsTree(String weapon, Boolean status) {
        this.weaponsTree.put(weapon, status);
    }

    /**
     * Retourne une liste des armes debloques.
     * 
     * @return une liste des armes debloques.
     */
    public List<String> unlockWeaponsList() {
        List<String> unlock = new ArrayList<>();

        for (Map.Entry weapon : this.weaponsTree.entrySet()) {
            if ((Boolean)weapon.getValue()) {
                unlock.add((String)weapon.getKey());
            }
        }

        return unlock;
    }

    /**
     * Retourne le nombre de pièces d'or du héro.
     * 
     * @return piece d'or que possede le hero.
     */
    public Integer getGold() {
        return this.gold;
    }

    /**
     * Ajoute des pièces d'or au héro.
     * 
     * @param value est le nombre de pieces d'or a ajouter.
     */
    public void addGold(Integer value) {
        this.gold += value;
    }

    /**
     * Retourne l'endurance du héro.
     * 
     * @return endurance du hero.
     */
    public Integer getEndurance() {
        return this.endurance;
    }

    /**
     * Pour retirer de l'endurance au héro.
     * 
     * @param endurance du hero.
     */
    public void removeEndurance(Integer endurance) {
        this.endurance -= endurance;
    }

    /**
     * Retourne la compétence de combat du hero.
     * 
     * @return competence de combat du hero.
     */
    public Integer getCombatSkill() {
        return this.combatSkill;
    }

    /**
     * Ajoute un nouvel item dans la liste des items "sac a dos" du hero.
     * 
     * @param equipment du hero.
     */
    public void addBackpackItems(Equipment equipment) {
        this.backpackItems.add(equipment);
    }

    /**
     * Vérifie si l'équipement "sac à dos" est dans la liste ou non.
     * 
     * @param name est le nom de l'equipement.
     * @return true si l'equipement est dans la liste, sinon false.
     */
    public boolean isBackpackItems(String name) {
        for(Equipment e : this.backpackItems) {
            if(e.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Utiliser les items sac à dos (potions de soin, etc...).
     */
    public void useBackpackItems() {
        for(Equipment e : this.backpackItems) {
            if(e.getConsumable()) {
                this.endurance += e.getEndurance();
                this.combatSkill += e.getCombatSkill();
            }
        }
        this.deleteConsumables();
    }

    /**
     * Supprime les consommables des items "sac à dos".
     */
    public void deleteConsumables() {
        for(int i = 0 ; i < this.backpackItems.size() ; i++) {
            if(this.backpackItems.get(i).getConsumable()) {
                this.backpackItems.remove(i);
                i--;
            }
        }
    }

    /**
     * Ajoute un nouvel item dans la liste des items speciaux du hero.
     * 
     * @param e est l'équipement du hero.
     */
    public void addSpecialItems(Equipment e) {
        this.specialItems.add(e);
    }

    /**
     * Supprimer un equipement special du hero.
     * 
     * @param items est l'équipement du hero a supprimer.
     */
    public void removeSpecialItems(List<String> items) {
        for(String i : items) {
            for(Equipment e : this.specialItems) {
                if(e.getName().equals(i)) {
                    this.specialItems.remove(e);
                }
            }
        }
    }

    /**
     * Vérifie si l'équipement "spécial" est dans la liste ou non.
     * 
     * @param name est le nom de l'equipement.
     * @return true si l'equipement est dans la liste, sinon false.
     */
    public boolean isSpecialItems(String name) {
        for(Equipment e : this.specialItems) {
            if(e.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Renvoie la liste des items "sac à dos".
     * IMPORTANT : à utiliser quand on veut extraire d'une liste, contenant plusieurs types d'items, les items "sac à dos".
     * 
     * @param equipmentList est la liste contenant les equipements de depart du hero.
     * @return la liste des items "sac a dos".
     */
    public List<Equipment> takeBackpackItems(List<Equipment> equipmentList) {
        List<Equipment> backpackItemsList = new ArrayList<>();
        for(Equipment e : equipmentList) {
            if(e.getAcSection().equals("backpack_items")) {
                backpackItemsList.add(e);
            }
        }
        return backpackItemsList;
    }

    /**
     * Renvoie la liste des items "spéciaux".
     * IMPORTANT : à utiliser quand on veut extraire d'une liste, contenant plusieurs types d'items, les items "spéciaux".
     * 
     * @param equipmentList est la liste contenant les equipements de depart du hero.
     * @return la liste des items speciaux.
     */
    public List<Equipment> takeSpecialItems(List<Equipment> equipmentList) {
        List<Equipment> specialItemsList = new ArrayList<>();
        for(Equipment e : equipmentList) {
            if(e.getAcSection().equals("special_items")) {
                specialItemsList.add(e);
            }
        }
        return specialItemsList;
    }

    /**
     * Le hero attaque l'ennemi et lui enleve de l'endurance.
     * 
     * @param enemie attaqué.
     */
    public void toAttack(Enemie enemie) {
        if(this.atLeastOneWeapon()) {
            if(enemie.getImmune() == "Mindblast" && enemie.getDoubleDommage()) {
                enemie.removeEndurance((this.combatSkill)*2);
            } else if(enemie.getImmune() == "Mindblast" && !enemie.getDoubleDommage()) {
                enemie.removeEndurance(this.combatSkill);
            } else if(this.disciplinesTree.get("Mindblast") && enemie.getDoubleDommage()) {
                enemie.removeEndurance((((this.combatSkill)+2)*2));
            } else if(this.disciplinesTree.get("Mindblast") && !enemie.getDoubleDommage()) {
                enemie.removeEndurance(((this.combatSkill)+2));
            } else if(enemie.getDoubleDommage()) {
                enemie.removeEndurance(((this.combatSkill)*2));
            } else {
                enemie.removeEndurance(this.combatSkill);
            }
        }
    }

    /**
     * Renvoie les informations principales du hero.
     * 
     * @return une chaine de caractere courte contenant des infos sur le hero.
     */
    public String shortString() {
        return "• HERO : endurance -> "+this.endurance+" combatSkill -> "+this.combatSkill+"\n";
    }

    /**
     * Renvoie toutes les informations concernant le héro.
     * 
     * @return une chaine de caractere contenant des infos sur le héro.
     */
    @Override
    public String toString() {
        String backpackItemsText = "- ";
        String specialItemsText = "- ";
        String disciplinesText = "- ";
        String weaponsText = "- ";
        for (Equipment e : this.backpackItems){
            backpackItemsText += e.getName()+" - ";
        }
        for (Equipment e : this.specialItems){
            specialItemsText += e.getName()+" - ";
        }
        for (String d : this.unlockDisciplinesList()){
            disciplinesText += d+" - ";
        }
        for (String w : this.unlockWeaponsList()){
            weaponsText += w+" - ";
        }
        return "######### INVENTORY #########\n\n"+"- Your equipments : "+backpackItemsText+specialItemsText+"\n- Your disciplines : "+disciplinesText+"\n- Your weapons : "+weaponsText+"\n- You possess "+this.gold+" gold.\n"+"- You possess "+this.endurance+" endurance.\n"+"- You possess "+this.combatSkill+" combat skill.\n";

    }

}
