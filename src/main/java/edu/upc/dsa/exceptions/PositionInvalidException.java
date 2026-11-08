package edu.upc.dsa.exceptions;

public class PositionInvalidException extends RuntimeException {
    public PositionInvalidException (String mensaje){
        super (mensaje);
    }
}
