package main.combat;

import java.util.*;

import main.hero.Hero;
import main.utils.*;

/**
 * Représentation d'un combat dans un LDVEH.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class Combat {

    /**
     * Contient la position de la section gagnante.
     */
    private Integer win;
    /**
     * Contient les points d'attaques que le Hero perdra lors du combat.
     */
    private Integer combatSkill;
    /**
     * Contient un possible bonus.
     */
    private Boolean special;
    /**
     * Contient une possible Evasion lors du combat.
     */
    private Evasion evasion;
    /**
     * Contient la liste des ennemies que le hero devra affronter.
     */
    private List<Enemie> enemies;

    /**
     * Constructeur par défaut.
     * 
     * @param win la position de la section gagante.
     * @param combatSkill points d'attaques que le hero perd.
     * @param special possible bonus.
     * @param evasion possible evasion.
     * @param enemies liste des ennemies.
     */
    public Combat(Integer win, Integer combatSkill, Boolean special, Evasion evasion, List<Enemie> enemies){
        this.win = win;
        this.combatSkill = combatSkill;
        this.special = special;
        this.evasion = evasion;
        this.enemies = enemies;
    }

    /**
     * Renvoie la position de la section gagnante.
     * 
     * @return la position de la section gagnante.
     */
    public Integer getWin() {
        return this.win;
    }

    /**
     * Renvoie les points d'attaques que le hero perd.
     * 
     * @return les points d'attaques que le hero perd..
     */
    public Integer getCombatSkill() {
        return this.combatSkill;
    }

    /**
     * Renvoie le possible bonus.
     * 
     * @return le possible bonus.
     */
    public Boolean getSpecial() {
        return this.special;
    }

    /**
     * Renvoie la possible evasion.
     * 
     * @return possible evasion.
     */
    public Evasion getEvasion() {
        return this.evasion;
    }

    /**
     * Renvoie la liste des ennemies.
     * 
     * @return liste d'ennemies.
     */
    public List<Enemie> getEnemies() {
        return this.enemies;
    } 

    /**
     * Permet de simuler un hero qui prend la fuite.
     * 
     * @param hero qui provoque une fuite.
     * @return l'index de la section de fuite ou -1 si le hero est mort avant d'avoir pu fuir.
     * @throws NullPointerException si this.evasion = null.
     */
    public Integer useEscape(Hero hero) {
        if (this.evasion.getNRounds() == 0) {
            return this.evasion.getChoice();
        } else if (this.evasion.getNRounds() == -1) {
            return this.useFight(hero, true);
        } else {
            this.evasion.substractNRounds();
            return this.useFight(hero, false);
        }
    }

    /**
     * Permet de simuler un hero qui prend la fuite tout en gardant les informations du hero.
     * 
     * @param hero qui provoque une fuite.
     * @return un tuple contenant l'index de la section de fuite et une instance du hero.
     */
    public Pair<Integer, Hero> useEscapeHero(Hero hero) {
        if (this.evasion.getNRounds() == 0) {
            return new Pair<Integer, Hero>(this.evasion.getChoice(), hero);
        } else if (this.evasion.getNRounds() == -1) {
            return this.useFightHero(hero, true);
        } else {
            this.evasion.substractNRounds();
            return this.useFightHero(hero, false);
        }
    }

    /**
     * Permet de simuler un combat entre un hero et un/des ennemie(s).
     * ATTENTION : le parametre road doit etre sur true tout le temps, il est sur false uniquement dans le cadre de la methode useEscape.
     * 
     * @param hero est le hero qui provoque un combat.
     * @param road est la route a suivre. True si le hero veut se battre jusqu'a la mort, False si il souhaite fuir dès que possible.
     * @return l'index de la section quand le hero gagne le combat ou -1 si le hero est mort.
     */
    public Integer useFight(Hero hero, boolean road) {
        int attacker = 0;
        while(hero.getEndurance() > 0 && !this.enemies.isEmpty()) {
            if(attacker == 0) {
                hero.toAttack(this.enemies.get(0));
                attacker = 1;
                if(this.enemies.get(0).getEndurance() <= 0) {
                    this.enemies.remove(0);
                }
            } else {
                this.enemies.get(0).toAttack(hero);
                attacker = 0;
                if(road == false) {
                    this.evasion.substractNRounds();
                    if(this.evasion.getNRounds() == 0 && hero.getEndurance() > 0) {
                        this.useEscape(hero);
                    }
                }
            }
        }

        if (!road) {
            return this.evasion.getChoice();
        } else if(hero.getEndurance() > 0) {
            return this.win;
        } else {
            return -1;
        }
    }

    /**
     * Permet de simuler un combat entre un hero et un/des ennemie(s) tout en gardant les informations du hero.
     * 
     * @param hero est le hero qui provoque un combat.
     * @param road est la route a suivre. True si le hero veut se battre jusqu'a la mort, False si il souhaite fuir dès que possible.
     * @return une tuple contenant l'index de la section (ou -1 si le hero est mort) et une instance hero.
     */
    public Pair<Integer, Hero> useFightHero(Hero hero, boolean road) {
        Hero newHero = new Hero(hero);
        int attacker = 0;
        while(newHero.getEndurance() > 0 && !this.enemies.isEmpty()) {
            if(attacker == 0) {
                newHero.toAttack(this.enemies.get(0));
                attacker = 1;
                if(this.enemies.get(0).getEndurance() <= 0) {
                    this.enemies.remove(0);
                }
            } else {
                this.enemies.get(0).toAttack(newHero);
                attacker = 0;
                if(road == false) {
                    this.evasion.substractNRounds();
                    if(this.evasion.getNRounds() == 0 && newHero.getEndurance() > 0) {
                        this.useEscapeHero(newHero);
                    }
                }
            }
        }
        if (!road) {
            return new Pair<Integer, Hero>(this.evasion.getChoice(), newHero);
        } else if(newHero.getEndurance() > 0) {
            return new Pair<Integer, Hero>(this.win, newHero);
        } else {
            return new Pair<Integer, Hero>(-1, newHero);
        }
    }

    /**
     * Le choix est aleatoire, soit le hero combat, soit le hero fuit.
     * 
     * @param hero est le hero qui provoque un combat ou une fuite.
     * @return soit le numero de la section pour fuir le combat, soit le numero de la section si le hero gagne le combat, soit -1 si le hero meurt.
     */
    public Integer useRandomChoice(Hero hero) {
        Random random = new Random();
        if (random.nextInt(21) <= 10) {
            return useEscape(hero);
        } else {
            return useFight(hero, true);
        }
    }

    /**
     * Renvoie une liste de tuples contenant tous les choix et une instance du hero contenant ses informations à ce moment là.
     * 
     * @param hero est le hero qui provoque un combat et/ou une fuite.
     * @return une liste de tuples contenant tous les choix et une instance du hero contenant ses informations à ce moment là.
     */
    public List<Pair<Integer, Hero>> useAllChoice(Hero hero) {
        List<Pair<Integer, Hero>> list = new ArrayList<>();
        list.add(useEscapeHero(hero));
        list.add(useFightHero(hero, true));
        return list;
    }

    /**
     * Renvoie une chaine de caractères contenant des informations sur les ennemies.
     * 
     * @return une chaine de caractères contenant des infos sur les ennemies du combat.
     */
    public String enemiesString() {
        String stringEnemies = "";
        for(Enemie enemie : this.enemies){
            stringEnemies += "• "+enemie+"\n";
        }
        return stringEnemies;
    }

    /**
     * Renvoie une chaine de caracteres contenant des informations sur le combat.
     * 
     * @return une chaine de caractere contenant des infos sur le combat.
     */
    @Override
    public String toString() {
        String enemiesText = "";
        for (Enemie e : this.enemies){
            enemiesText += e+" ";
        }
        if (this.combatSkill == null) {
            return "######## INFOS : combat ########\n"+"Section if you win : "+this.win+"\nYou lose "+"0"+" combat skill\n"+"Evasion : "+this.evasion+"\nYour ennemies : "+enemiesText;
        } else {
            return "######## INFOS : combat ########\n"+"Section if you win : "+this.win+"\nYou lose "+(-1*this.combatSkill)+" combat skill\n"+"Evasion : "+this.evasion+"\nYour ennemies : "+enemiesText;
        }
    }

}