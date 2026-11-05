package edu.upc.dsa;

import edu.upc.dsa.models.Dron;
import edu.upc.dsa.models.Piloto;
import edu.upc.dsa.models.PlanVuelo;

import org.apache.log4j.Logger;
import java.util.*;
import java.util.function.Supplier;

public final class DronManagerImpl implements DronManager {

    private static final Logger logger = Logger.getLogger(DronManagerImpl.class);

    // SINGLETON
    private static final DronManagerImpl INSTANCE = new DronManagerImpl();

    // ESTRUCTURAS DE DATOS
    private final Map<String, Dron> drones = new HashMap<>();
    private final Map<String, Piloto> pilotos = new HashMap<>();
    private final Queue<Dron> almacen = new LinkedList<>();

    private int numPlanes = 0;

    private DronManagerImpl() {
    }

    public static DronManagerImpl getInstance() {
        logger.info("Inicio getInstance()");
        logger.info("Fin getInstance()");
        return INSTANCE;
    }

    // METODO AUXILIAR PARA LOG4J
    private <T> T trace(String operacion, Supplier<T> accion) {
        logger.info("Inicio " + operacion);

        try {
            T resultado = accion.get();
            logger.info("Fin " + operacion + ": " + resultado);
            return resultado;
        }
        catch (RuntimeException e) {
            logger.error("Error en " + operacion + ": " + e.getMessage(), e);
            throw e;
        }
    }

    // AÑADIR DRON
    @Override
    public  void addDron(String id, String name,
                                     String manufacturer, String model) {

        trace("addDron: " + id + ", " + name + ", " + manufacturer + ", " + model, () -> {

            if (id == null || id.trim().isEmpty() ||
                    name == null || manufacturer == null || model == null) {
                throw new IllegalArgumentException("Datos del dron incorrectos");
            }

            if (drones.containsKey(id)) {
                throw new IllegalArgumentException("El dron ya existe");
            }

            Dron dron = new Dron(id, name, manufacturer, model);
            drones.put(id, dron);

            return dron;
        });
    }

    // AÑADIR PILOTO
    @Override
    public synchronized void addPiloto(String id, String name, String surname) {

        trace("addPiloto: " + id + ", " + name + ", " + surname, () -> {

            if (id == null || id.trim().isEmpty() ||
                    name == null || surname == null) {
                throw new IllegalArgumentException("Datos del piloto incorrectos");
            }

            if (pilotos.containsKey(id)) {
                throw new IllegalArgumentException("El piloto ya existe");
            }

            Piloto piloto = new Piloto(id, name, surname);
            pilotos.put(id, piloto);

            return piloto;
        });
    }

    // BUSCAR DRON
    @Override
    public synchronized Dron getDron(String id) {

        return trace("getDron: " + id, () -> {

            Dron dron = drones.get(id);

            if (dron == null) {
                throw new NoSuchElementException("Dron no encontrado");
            }

            return dron;
        });
    }

    // BUSCAR PILOTO
    @Override
    public synchronized Piloto getPiloto(String id) {

        return trace("getPiloto: " + id, () -> {

            Piloto piloto = pilotos.get(id);

            if (piloto == null) {
                throw new NoSuchElementException("Piloto no encontrado");
            }

            return piloto;
        });
    }

    // DRONES ORDENADOS POR HORAS DE VUELO
    @Override
    public synchronized List<Dron> getDronesByHorasVuelo() {

        return trace("getDronesByHorasVuelo()", () -> {

            List<Dron> lista = new ArrayList<>(drones.values());

            lista.sort((d1, d2) ->
                    Double.compare(d2.getHorasVuelo(), d1.getHorasVuelo()));

            return lista;
        });
    }

    // PILOTOS ORDENADOS POR HORAS DE VUELO
    @Override
    public synchronized List<Piloto> getPilotosByHorasVuelo() {

        return trace("getPilotosByHorasVuelo()", () -> {

            List<Piloto> lista = new ArrayList<>(pilotos.values());

            lista.sort((p1, p2) ->
                    Double.compare(p2.getHorasVuelo(), p1.getHorasVuelo()));

            return lista;
        });
    }

    // GUARDAR DRON EN EL ALMACEN
    @Override
    public synchronized void guardarDron(String droneId) {

        trace("guardarDron: " + droneId, () -> {

            Dron dron = getDron(droneId);

            if (dron.isMantenimiento()) {
                throw new IllegalStateException("El dron ya esta en mantenimiento");
            }

            almacen.offer(dron);
            dron.setMantenimiento(true);

            return dron;
        });
    }

    // REPARAR PRIMER DRON DEL ALMACEN
    @Override
    public synchronized Dron arreglarDron() {

        return trace("arreglarDron()", () -> {

            if (almacen.isEmpty()) {
                throw new IllegalStateException("El almacen esta vacio");
            }

            Dron dron = almacen.poll();
            dron.setMantenimiento(false);

            return dron;
        });
    }

    // CREAR PLAN DE VUELO
    @Override
    public synchronized void addPlanVuelo(PlanVuelo plan) {

        trace("addPlanVuelo: " + plan, () -> {

            if (plan == null ||
                    plan.getFecha() == null ||
                    plan.getCoordenadas() == null ||
                    plan.getCoordenadas().isEmpty() ||
                    !Double.isFinite(plan.getDuracion()) ||
                    plan.getDuracion() <= 0) {

                throw new IllegalArgumentException("Datos del plan incorrectos");
            }

            Dron dron = getDron(plan.getIDdron());
            Piloto piloto = getPiloto(plan.getIDpiloto());

            if (dron.isMantenimiento()) {
                throw new IllegalStateException("Dron en mantenimiento");
            }

            // Comprobar planes del dron
            for (PlanVuelo actual : dron.getPlanesVuelo()) {

                if (haySolapamiento(plan, actual)) {
                    throw new IllegalStateException("Dron ocupado");
                }
            }

            // Comprobar planes del piloto
            for (PlanVuelo actual : piloto.getPlanesVuelo()) {

                if (haySolapamiento(plan, actual)) {
                    throw new IllegalStateException("Piloto ocupado");
                }
            }

            // Guardar el mismo plan en las dos listas
            dron.addPlanVuelo(plan);
            piloto.addPlanVuelo(plan);

            numPlanes++;

            return plan;
        });
    }

    // COMPROBAR SOLAPAMIENTO ENTRE DOS PLANES
    private boolean haySolapamiento(PlanVuelo nuevo, PlanVuelo actual) {

        return nuevo.getFecha().isBefore(actual.getEnd()) &&
                actual.getFecha().isBefore(nuevo.getEnd());
    }

    // CONSULTAR PLANES DE UN PILOTO
    @Override
    public synchronized List<PlanVuelo> getPlanVueloByPiloto(String pilotId) {

        return trace("getPlanVueloByPiloto: " + pilotId, () -> {

            Piloto piloto = getPiloto(pilotId);

            return piloto.getPlanesVuelo();
        });
    }

    // CONSULTAR PLANES DE UN DRON
    @Override
    public synchronized List<PlanVuelo> getPlanVueloByDron(String droneId) {

        return trace("getPlanVueloByDron: " + droneId, () -> {

            Dron dron = getDron(droneId);

            return dron.getPlanesVuelo();
        });
    }

    // CONTADORES
    @Override
    public synchronized int numDrones() {
        return trace("numDrones()", () -> drones.size());
    }

    @Override
    public synchronized int numPilotos() {
        return trace("numPilotos()", () -> pilotos.size());
    }

    @Override
    public synchronized int numPlanesVuelo() {
        return trace("numPlanesVuelo()", () -> numPlanes);
    }

    // LIMPIAR DATOS PARA LOS TESTS
    @Override
    public synchronized void clear() {

        trace("clear()", () -> {

            drones.clear();
            pilotos.clear();
            almacen.clear();
            numPlanes = 0;

            return "Datos eliminados";
        });
    }
}

