package edu.upc.dsa.exceptions;

public class PilotoNotFoundException extends RuntimeException {

    public PilotoNotFoundException(String mensaje) {
        super(mensaje);
    }
}
