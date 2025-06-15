package org.bem.procrapi.utilities.exceptions;

public class ServiceValidationException extends IllegalArgumentException {
    public ServiceValidationException(String message) {
        super(message);
    }
}
