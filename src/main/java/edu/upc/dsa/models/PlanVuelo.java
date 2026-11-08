package edu.upc.dsa.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class PlanVuelo {
    private String iddron;
    private String idpiloto;
    private LocalDateTime fecha;
    private double duracion;
    private List<Position> coordenadas;

    public PlanVuelo() {
        this.coordenadas = new ArrayList<>();
    }

    public PlanVuelo(String droneId, String pilotId, LocalDateTime fecha, double durationHours, List<Position> positions) {
        this.iddron = droneId;
        this.idpiloto = pilotId;
        this.fecha = fecha;
        this.duracion = durationHours;
        this.coordenadas = new ArrayList<>(positions);
    }

    //setters y getters
    public String getIddron() {
        return iddron;
    }

    public void setIddron(String iddron) {
        this.iddron = iddron;
    }

    public String getIdpiloto() {
        return idpiloto;
    }

    public void setIdpiloto(String idpiloto) {
        this.idpiloto = idpiloto;
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
        return "FlightPlan{droneId=" + iddron + ", pilotId=" + idpiloto
                + ", start=" + fecha + ", durationHours=" + duracion
                + ", positions=" + coordenadas + "}";
    }
}