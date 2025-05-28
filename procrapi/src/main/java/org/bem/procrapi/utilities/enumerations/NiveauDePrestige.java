package org.bem.procrapi.utilities.enumerations;

public enum NiveauDePrestige {
    OR("or"),
    ARGENT("argent"),
    BRONZE("bronze"),
    FER("fer"),
    BOIS("bois");

    private String nom;

    NiveauDePrestige(String nom) {
        this.nom = nom;
    }

    public String toString(){
        return nom;
    }
}
