package edu.upc.dsa.models;
import java.util.ArrayList;
import java.util.List;

public class Piloto {
    private String id;
    private String name;
    private String surname;
    private double HorasVuelo;
    private List<PlanVuelo> planesVuelo;

    public Piloto() {
        this.planesVuelo = new ArrayList<>();
    }

    public Piloto(String id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.HorasVuelo = 0;
        this.planesVuelo = new ArrayList<>();
    }

    public String getId() {
        return id; }

    public void setId(String id) {
        this.id = id; }

    public String getName() {
        return name; }

    public void setName(String name) {
        this.name = name; }

    public String getSurname() {
        return surname; }

    public void setSurname(String surname) {
        this.surname = surname; }

    public double getHorasVuelo() {
        return HorasVuelo; }

    public void setHorasVuelo(double horasVuelo) {
        this.HorasVuelo = horasVuelo; }

    public List<PlanVuelo> getPlanesVuelo() {
        return planesVuelo;
    }

    public void setPlanesVuelo(List<PlanVuelo> planesVuelo) {
        this.planesVuelo = planesVuelo;
    }

    public void addPlanVuelo(PlanVuelo planVuelo) {
        this.planesVuelo.add(planVuelo);
    }

    @Override
    public String toString() {
        return "Piloto{id=" + id + ", name=" + name + ", surname=" + surname
                + ", HorasVuelo=" + HorasVuelo + "}";
    }
}
