package edu.upc.dsa.models;
import edu.upc.dsa.exceptions.*;

public class Position {
    private double latitud;
    private double longitud;

    public Position() {
    }

    //con set pq asi ya se comprueba q sean validos los datos.
    public Position(double latitud, double longitud) {
        setLatitud(latitud);
        setLongitud(longitud);
    }

    //getters y setters
    public double getLatitud() {
        return latitud; }

    public void setLatitud(double latitud) {
        if (!Double.isFinite(latitud) || latitud < -90 || latitud > 90) {
            throw new PositionInvalidException("La latitud debe estar entre -90 y 90");
        }
        this.latitud = latitud;
    }
    public double getLongitud() {
        return longitud; }

    public void setLongitud(double longitud) {
        if (!Double.isFinite(longitud) || longitud < -180 || longitud > 180) {
            throw new PositionInvalidException("La longitud debe estar entre -180 y 180");
        }
        this.longitud = longitud;
    }

    @Override
    public String toString() { return "(" + latitud + ", " + longitud + ")"; }
}


