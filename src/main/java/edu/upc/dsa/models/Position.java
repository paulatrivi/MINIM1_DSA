package edu.upc.dsa.models;

public class Position {
    private double latitud;
    private double longitud;

    public Position() {
    }

    public Position(double latitud, double longitud) {
        setLatitud(latitud);
        setLongitud(longitud);
    }

    public double getLatitud() {
        return latitud; }

    public void setLatitud(double latitud) {
        if (!Double.isFinite(latitud) || latitud < -90 || latitud > 90) {
            throw new IllegalArgumentException("La latitud debe estar entre -90 y 90");
        }
        this.latitud = latitud;
    }
    public double getLongitud() {
        return longitud; }

    public void setLongitud(double longitud) {
        if (!Double.isFinite(longitud) || longitud < -180 || longitud > 180) {
            throw new IllegalArgumentException("La longitud debe estar entre -180 y 180");
        }
        this.longitud = longitud;
    }

    @Override
    public String toString() { return "(" + latitud + ", " + longitud + ")"; }
}


