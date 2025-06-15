package org.bem.procrapi.components.authentication;

import org.bem.procrapi.entities.Utilisateur;

/* Classe minimaliste pour pouvoir conserver l'utilisateur
 * dans le contexte du thread actif */
public class EmailHolder {

    // champ local au thread en cours de traitement
    private static final ThreadLocal<String> currentEmail = new ThreadLocal<>();

    // méthode pour stocker l'email de l'entête
    public static void setEmail(String email) {
        currentEmail.set(email);
    }

    // méthode pour obtenir l'email de l'entête
    public static String getEmail() {
        return currentEmail.get();
    }

    // méthode pour vider le contexte d'authentification
    public static void clear() {
        currentEmail.remove();
    }
}