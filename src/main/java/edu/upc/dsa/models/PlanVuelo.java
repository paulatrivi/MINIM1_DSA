package edu.upc.dsa.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class PlanVuelo {
    private String IDdron;
    private String IDpiloto;
    private LocalDateTime fecha;
    private double duracion;
    private List<Position> coordenadas;

    public PlanVuelo() {
        this.coordenadas = new ArrayList<>();
    }

    public PlanVuelo(String droneId, String pilotId, LocalDateTime fecha, double durationHours, List<Position> positions) {
        this.IDdron = droneId;
        this.IDpiloto = pilotId;
        this.fecha = fecha;
        this.duracion = durationHours;
        this.coordenadas = new ArrayList<>(positions);
    }

    public String getIDdron() {
        return IDdron;
    }

    public void setIDdron(String IDdron) {
        this.IDdron = IDdron;
    }

    public String getIDpiloto() {
        return IDpiloto;
    }

    public void setIDpiloto(String IDpiloto) {
        this.IDpiloto = IDpiloto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    public List<Position> getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(List<Position> coordenadas) {
        this.coordenadas = coordenadas;
    }

    public void addPosition(double latitud, double longitud) {
        this.coordenadas.add(new Position(latitud, longitud));
    }

    public LocalDateTime getEnd() {
        return fecha.plusMinutes(Math.round(duracion * 60));
    }


    @Override
    public String toString() {
        return "FlightPlan{droneId=" + IDdron + ", pilotId=" + IDpiloto
                + ", start=" + fecha + ", durationHours=" + duracion
                + ", positions=" + coordenadas + "}";
    }
}