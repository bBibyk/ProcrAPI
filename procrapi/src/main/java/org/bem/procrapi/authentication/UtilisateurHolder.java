package org.bem.procrapi.authentication;

import org.bem.procrapi.entities.Utilisateur;

import java.util.Optional;

/* Classe minimaliste pour pouvoir conserver l'utilisateur
 * qui est en train d'utiliser l'API. */

public class UtilisateurHolder {

    private static final ThreadLocal<Utilisateur> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(Utilisateur utilisateur) {
        currentUser.set(utilisateur);
    }

    public static Utilisateur getCurrentUser() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }
}