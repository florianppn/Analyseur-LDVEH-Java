package main.combat;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Représentation d'une evasion lors d'un combat.
 * 
 * @author Florian Pépin
 * @version 1.0
 */
public class Evasion {

    /** 
     * Contient le nombre de manche avant de pouvoir s'évader.
     */
    private Integer nRounds;
    /**
     * Contient la position de la section pour s'enfuir du combat.
    */
    private Integer choice;

    /** 
     * Constructeur par défaut.
     * INFO : Si les attributs sont à -1, il n'y a pas d'évasion possible.
     * 
     * @param nRounds nombre de rounds avant lequel vous pouvez vous echapper.
     * @param choice position de la section de fuite dans "choices".
     */
    public Evasion (JsonNode nRounds, JsonNode choice) {

        this.nRounds = nRounds != null ? nRounds.asInt() : -1;
        this.choice = choice != null ? choice.asInt() : -1;

    }

    /** 
     * Retourne le nombre de rounds avant de pouvoir s'enfuir.
     * 
     * @return nombre de rounds.
     */
    public Integer getNRounds() {
        return this.nRounds;
    }

    /**
     * Soustrait 1 round a nRounds.
     */
    public void substractNRounds() {
        if (this.nRounds >= 1) {
            this.nRounds -= 1;
        }
    }

    /**
     * Renvoie la position de la section de fuite.
     * 
     * @return position de la section de fuite.
     */
    public Integer getChoice() {
        return this.choice;
    }

    /**
     * Renvoie une chaine de caractère contenant les informations de l'évasion.
     * 
     * @return une chaine de caractere contenant la section d'evasion.
     */
    @Override
    public String toString() {
        return "Section if you want to escape : "+this.choice+"\n";
    }

}