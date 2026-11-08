package edu.upc.dsa;

import edu.upc.dsa.models.*;

import java.util.List;

public interface DronManager {

    void addDron(String id, String name, String manufacturer, String model);

    void addPiloto(String id, String name, String surname);

    Dron getDron(String id);

    Piloto getPiloto(String id);

    List<Dron> getDronesByHorasVuelo();

    List<Piloto> getPilotosByHorasVuelo();

    void guardarDron(String droneId);

    Dron arreglarDron();

    void addPlanVuelo(PlanVuelo plan);

    List<PlanVuelo> getPlanVueloByPiloto(String pilotId);

    List<PlanVuelo> getPlanVueloByDron(String droneId);

    int numDrones();

    int numPilotos();

    int numPlanesVuelo();

    void clear();
}

