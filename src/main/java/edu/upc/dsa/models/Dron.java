package edu.upc.dsa.models;
import java.util.ArrayList;
import java.util.List;

public class Dron {
    private String id;
    private String nombre;
    private String fabricante;
    private String modelo;
    private double HorasVuelo;
    private boolean Mantenimiento;
    private List<PlanVuelo> planesVuelo;


    public Dron() {
        this.planesVuelo = new ArrayList<>();
    }

    public Dron(String id, String nombre, String fabricante, String modelo) {
        this.id = id;
        this.nombre = nombre;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.HorasVuelo = 0;
        this.Mantenimiento = false;
        this.planesVuelo = new ArrayList<>();

    }

    public String getId() {
        return id; }

    public void setId(String id) {
        this.id = id; }

    public String getNombre() {
        return nombre; }

    public void setNombre(String nombre) {
        this.nombre = nombre; }

    public String getFabricante() {
        return fabricante; }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante; }

    public String getModelo() {
        return modelo; }

    public void setModelo(String modelo) {
        this.modelo = modelo; }

    public double getHorasVuelo() {
        return HorasVuelo; }

    public void setHorasVuelo(double horasVuelo) {
        this.HorasVuelo = horasVuelo; }

    public boolean isMantenimiento() {
        return Mantenimiento; }

    public void setMantenimiento(boolean mantenimiento) {
        this.Mantenimiento = mantenimiento; }

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
        return "Drone{id=" + id + ", nombre=" + nombre + ", fabricante=" + fabricante
                + ", modelo=" + modelo + ", HorasVuelo=" + HorasVuelo
                + ", Mantenimiento=" + Mantenimiento + "}";
    }
}
