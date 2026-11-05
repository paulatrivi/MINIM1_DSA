package edu.upc.dsa;

import edu.upc.dsa.models.Dron;
import edu.upc.dsa.models.Piloto;
import edu.upc.dsa.models.PlanVuelo;
import edu.upc.dsa.models.Position;
import org.junit.*;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.Assert.*;

public class DroneManagerTest {
    private DronManager manager;
    private final LocalDateTime start = LocalDateTime.of(2026, 11, 10, 10, 0);

    @Before
    public void setUp() {
        manager = DronManagerImpl.getInstance();
        manager.clear();
        manager.addDron("D1", "Uno", "DJI", "M1");
        manager.addDron("D2", "Dos", "DJI", "M2");
        manager.addPiloto("P1", "Ana", "Garcia");
        manager.addPiloto("P2", "Luis", "Lopez");
    }

    @After
    public void tearDown() { manager.clear(); }

    @Test
    public void registrationAndSingletonTest() {
        assertSame(manager, DronManagerImpl.getInstance());
        assertEquals(2, manager.numDrones());
        assertEquals(2, manager.numPilotos());
        assertEquals("DJI", manager.getDron("D1").getFabricante());
        assertEquals("M1", manager.getDron("D1").getModelo());
        assertEquals("Garcia", manager.getPiloto("P1").getSurname());
        assertThrows(IllegalArgumentException.class,
                () -> manager.addDron("D1", "Otro", "DJI", "M3"));
        assertThrows(IllegalArgumentException.class,
                () -> manager.addPiloto("P1", "Otra", "Garcia"));
        assertThrows(IllegalArgumentException.class,
                () -> manager.addDron("", "Otro", "DJI", "M3"));
        assertThrows(NoSuchElementException.class, () -> manager.getDron("NO"));
        assertThrows(NoSuchElementException.class, () -> manager.getPiloto("NO"));
        assertEquals(2, manager.numDrones());
        assertEquals(2, manager.numPilotos());
    }

    @Test
    public void descendingFlightHoursTest() {
        manager.getDron("D1").setHorasVuelo(1.5);
        manager.getDron("D2").setHorasVuelo(8.25);
        manager.getPiloto("P1").setHorasVuelo(12.5);
        manager.getPiloto("P2").setHorasVuelo(3.75);
        List<Dron> drones = manager.getDronesByHorasVuelo();
        List<Piloto> pilots = manager.getPilotosByHorasVuelo();
        assertEquals("D2", drones.get(0).getId());
        assertEquals("D1", drones.get(1).getId());
        assertEquals("P1", pilots.get(0).getId());
        assertEquals("P2", pilots.get(1).getId());
        drones.clear();
        pilots.clear();
        assertEquals(2, manager.numDrones());
        assertEquals(2, manager.numPilotos());
    }

    @Test
    public void warehouseMaintenanceTest() {
        manager.guardarDron("D1");
        manager.guardarDron("D2");
        assertTrue(manager.getDron("D1").isMantenimiento());
        assertThrows(IllegalStateException.class, () -> manager.guardarDron("D1"));
        assertThrows(IllegalStateException.class,
                () -> manager.addPlanVuelo(plan("D1", "P1", start, 1)));
        assertEquals("D2", manager.arreglarDron().getId());
        assertFalse(manager.getDron("D2").isMantenimiento());
        assertTrue(manager.getDron("D1").isMantenimiento());
        manager.addPlanVuelo(plan("D2", "P1", start, 1));
        assertEquals("D1", manager.arreglarDron().getId());
        assertFalse(manager.getDron("D1").isMantenimiento());
        assertThrows(IllegalStateException.class, () -> manager.arreglarDron());
        assertThrows(NoSuchElementException.class, () -> manager.guardarDron("NO"));
    }

    @Test
    public void flightPlansAndAvailabilityTest() {
        PlanVuelo first = plan("D1", "P1", start, 2);
        manager.addPlanVuelo(first);
        // Conflicto solo de dron.
        assertThrows(IllegalStateException.class,
                () -> manager.addPlanVuelo(plan("D1", "P2", start.plusHours(1), 1)));
        // Conflicto solo de piloto.
        assertThrows(IllegalStateException.class,
                () -> manager.addPlanVuelo(plan("D2", "P1", start.plusHours(1), 1)));
        // Un intervalo que contiene al existente tambien se solapa.
        assertThrows(IllegalStateException.class,
                () -> manager.addPlanVuelo(plan("D1", "P2", start.minusHours(1), 4)));
        // Recursos distintos pueden volar a la vez.
        manager.addPlanVuelo(plan("D2", "P2", start, 2));
        // Los intervalos consecutivos estan permitidos.
        PlanVuelo next = plan("D1", "P1", start.plusHours(2), 0.5);
        manager.addPlanVuelo(next);
        assertEquals(Arrays.asList(first, next), manager.getPlanVueloByDron("D1"));
        assertEquals(Arrays.asList(first, next), manager.getPlanVueloByPiloto("P1"));
        assertEquals(3, manager.numPlanesVuelo());
        assertThrows(NoSuchElementException.class,
                () -> manager.addPlanVuelo(plan("NO", "P1", start, 1)));
        assertThrows(NoSuchElementException.class,
                () -> manager.addPlanVuelo(plan("D1", "NO", start, 1)));
        assertThrows(IllegalArgumentException.class,
                () -> plan("D1", "P1", start, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Position(91, 0));
        assertEquals(0, manager.getDron("D1").getHorasVuelo(), 0.001);
        assertEquals(0, manager.getPiloto("P1").getHorasVuelo(), 0.001);
        manager.getPlanVueloByDron("D1").clear();
        assertEquals(2, manager.getPlanVueloByDron("D1").size());
    }

    private PlanVuelo plan(String drone, String pilot, LocalDateTime date, double hours) {
        return new PlanVuelo(drone, pilot, date, hours,
                Arrays.asList(new Position(41.275, 1.987),
                        new Position(41.280, 1.990)));
    }
}
