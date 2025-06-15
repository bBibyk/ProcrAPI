package org.bem.procrapi.utilities.exceptions;

// Exception générique pour filtrer les cas de requêtes mal formées
public class ServiceValidationException extends IllegalArgumentException {
    public ServiceValidationException(String message) {
        super(message);
    }
}
