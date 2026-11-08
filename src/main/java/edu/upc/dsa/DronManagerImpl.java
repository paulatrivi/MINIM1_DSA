package edu.upc.dsa;

import edu.upc.dsa.models.*;
import edu.upc.dsa.exceptions.*;

import org.apache.log4j.Logger;
import java.util.*;

public final class DronManagerImpl implements DronManager {

    private static DronManager instance;
    private final Map<String, Dron> drones = new HashMap<>();
    private final Map<String, Piloto> pilotos = new HashMap<>();
    private final Queue<Dron> almacen = new LinkedList<>();
    private int numPlanes = 0;

    private static final Logger logger = Logger.getLogger(DronManagerImpl.class);

    // SINGLETON
    private DronManagerImpl() {
    }

    public static DronManager getInstance() {
        if (instance == null) {
            instance = new DronManagerImpl();
        }
        return instance;
    }

    // Añadir dron
    @Override
    public void addDron(String id, String name, String fabricante, String modelo) {

        logger.info("Inicio addDron: " + id + ", " + name + ", " + fabricante + ", " + modelo);

        Dron dron = new Dron(id, name, fabricante, modelo);
        drones.put(id, dron);

        logger.info("Fin addDron");
    }

    // Añadir piloto
    @Override
    public void addPiloto(String id, String name, String surname) {

        logger.info("Inicio addPiloto: " + id + ", " + name + ", " + surname);

        Piloto piloto = new Piloto(id, name, surname);
        pilotos.put(id, piloto);

        logger.info("Fin addPiloto");
    }

    // Buscar dron
    @Override
    public Dron getDron(String id) {

        logger.info("Inicio getDron: " + id);

        Dron dron = drones.get(id);

        if (dron == null) {
            logger.error("Dron no encontrado: " + id);
            throw new DronNotFoundException("Dron no encontrado");
        }

        logger.info("Fin getDron: " + dron);
        return dron;
    }

    // Buscar piloto
    @Override
    public Piloto getPiloto(String id) {

        logger.info("Inicio getPiloto: " + id);

        Piloto piloto = pilotos.get(id);

        if (piloto == null) {
            logger.error("Piloto no encontrado: " + id);
            throw new PilotoNotFoundException("Piloto no encontrado");
        }

        logger.info("Fin getPiloto: " + piloto);
        return piloto;
    }

    // Lista drones ordenados por horas de vuelo descendentemente.
    @Override
    public List<Dron> getDronesByHorasVuelo() {

        logger.info("Inicio getDronesByHorasVuelo");

        List<Dron> lista = new ArrayList<>(drones.values());

        lista.sort((d1, d2) -> Double.compare(d2.getHorasVuelo(), d1.getHorasVuelo()));

        logger.info("Fin getDronesByHorasVuelo: " + lista);
        return lista;
    }

    // Lista pilotos ordenador por horas de vuelo descendentemente.
    @Override
    public List<Piloto> getPilotosByHorasVuelo() {

        logger.info("Inicio getPilotosByHorasVuelo");

        List<Piloto> lista = new ArrayList<>(pilotos.values());

        lista.sort((p1, p2) -> Double.compare(p2.getHorasVuelo(), p1.getHorasVuelo()));

        logger.info("Fin getPilotosByHorasVuelo: " + lista);
        return lista;
    }

    // Guardar dron almacen.
    @Override
    public void guardarDron(String droneId) {

        logger.info("Inicio guardarDron: " + droneId);

        Dron dron = getDron(droneId);

        if (dron.isMantenimiento()) {
            logger.error("El dron ya esta en mantenimiento: " + droneId);
            throw new DronEnMantenimientoException("El dron ya esta en mantenimiento");
        }

        almacen.offer(dron);
        dron.setMantenimiento(true);

        logger.info("Fin guardarDron");
    }

    // Reparar dron.
    @Override
    public Dron arreglarDron() {

        logger.info("Inicio arreglarDron");

        Dron dron = almacen.poll();
        dron.setMantenimiento(false);

        logger.info("Fin arreglarDron: " + dron);
        return dron;
    }

    // Añadir Plan de Vuelo
    @Override
    public void addPlanVuelo(PlanVuelo plan) {

        logger.info("Inicio addPlanVuelo: " + plan);

        Dron dron = getDron(plan.getIddron());
        Piloto piloto = getPiloto(plan.getIdpiloto());

        if (dron.isMantenimiento()) {
            logger.error("Dron en mantenimiento: " + plan.getIddron());
            throw new DronEnMantenimientoException("Dron en mantenimiento");
        }

        // Comprobar planes del dron
        for (PlanVuelo actual : dron.getPlanesVuelo()) {

            //el vuelo nuevo empieza antes de q termine el actual o al revés.
            if (plan.getFecha().isBefore(actual.getEnd()) && actual.getFecha().isBefore(plan.getEnd())) {
                logger.error("El dron tiene otro vuelo en ese horario");
                throw new DronOcupadoException("Dron ocupado");
            }
        }

        // Comprobar planes del piloto
        for (PlanVuelo actual : piloto.getPlanesVuelo()) {

            if (plan.getFecha().isBefore(actual.getEnd()) && actual.getFecha().isBefore(plan.getEnd())) {

                logger.error("El piloto tiene otro vuelo en ese horario");
                throw new PilotoOcupadoException("Piloto ocupado");
            }
        }

        // Guardar el plan en las listas del dron y piloto
        dron.addPlanVuelo(plan);
        piloto.addPlanVuelo(plan);

        numPlanes++;

        logger.info("Fin addPlanVuelo");
    }

    // Planes de vuelo para un piloto
    @Override
    public List<PlanVuelo> getPlanVueloByPiloto(String pilotId) {

        logger.info("Inicio getPlanVueloByPiloto: " + pilotId);

        Piloto piloto = getPiloto(pilotId);
        List<PlanVuelo> lista = piloto.getPlanesVuelo();

        logger.info("Fin getPlanVueloByPiloto: " + lista);
        return lista;
    }

    // Planes de vuelo de un dron
    @Override
    public List<PlanVuelo> getPlanVueloByDron(String droneId) {

        logger.info("Inicio getPlanVueloByDron: " + droneId);

        Dron dron = getDron(droneId);
        List<PlanVuelo> lista = dron.getPlanesVuelo();

        logger.info("Fin getPlanVueloByDron: " + lista);
        return lista;
    }

    @Override
    public int numDrones() {

        logger.info("Inicio numDrones");

        int resultado = drones.size();

        logger.info("Fin numDrones: " + resultado);
        return resultado;
    }

    @Override
    public int numPilotos() {

        logger.info("Inicio numPilotos");

        int resultado = pilotos.size();

        logger.info("Fin numPilotos: " + resultado);
        return resultado;
    }

    @Override
    public int numPlanesVuelo() {

        logger.info("Inicio numPlanesVuelo");

        int resultado = numPlanes;

        logger.info("Fin numPlanesVuelo: " + resultado);
        return resultado;
    }

    // Limpiar datos para los test.
    @Override
    public void clear() {

        logger.info("Inicio clear");

        drones.clear();
        pilotos.clear();
        almacen.clear();
        numPlanes = 0;

        logger.info("Fin clear");
    }
}


