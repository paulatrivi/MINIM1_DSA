package edu.upc.dsa;

import edu.upc.dsa.models.*;
import edu.upc.dsa.exceptions.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DronManagerTest {

    private DronManager manager;

    @Before
    public void setUp() {

        manager = DronManagerImpl.getInstance();
        manager.clear();

        manager.addDron("D1", "Uno", "DJI", "M1");
        manager.addDron("D2", "Dos", "DJI", "M2");
        manager.addDron("D3", "Tres", "DJI", "M3");


        manager.addPiloto("P1", "Ana", "Garcia");
        manager.addPiloto("P2", "Luis", "Lopez");
        manager.addPiloto("P3", "Paula", "Triviño");

    }

    @After
    public void tearDown() {
        manager.clear();
    }

    @Test
    public void AddTest() {

        assertEquals(3, manager.numDrones());
        assertEquals(3, manager.numPilotos());

        assertEquals("DJI", manager.getDron("D1").getFabricante());
        assertEquals("Garcia", manager.getPiloto("P1").getSurname());

        manager.addDron("D4","Cuatro", "DJI", "M4");
        manager.addPiloto("P4", "Leire", "Goma");

        assertEquals(4, manager.numDrones());
        assertEquals(4, manager.numPilotos());

        assertEquals("M4", manager.getDron("D4").getModelo());
        assertEquals("Goma", manager.getPiloto("P4").getSurname());


        assertSame(manager, DronManagerImpl.getInstance());
    }

    @Test(expected = DronNotFoundException.class)
    public void dronNotFoundTest() {
        manager.getDron("D99");
    }

    @Test(expected = PilotoNotFoundException.class)
    public void pilotoNotFoundTest() {
        manager.getPiloto("P99");
    }

    @Test
    public void ListasOrdenadasTest() {

        manager.getDron("D1").setHorasVuelo(10);
        manager.getDron("D2").setHorasVuelo(20);
        manager.getDron("D3").setHorasVuelo(15);


        manager.getPiloto("P1").setHorasVuelo(5);
        manager.getPiloto("P2").setHorasVuelo(15);
        manager.getPiloto("P3").setHorasVuelo(2);


        List<Dron> drones = manager.getDronesByHorasVuelo();
        List<Piloto> pilotos = manager.getPilotosByHorasVuelo();

        assertEquals("D2", drones.get(0).getId());
        assertEquals("D3", drones.get(1).getId());
        assertEquals("D1", drones.get(2).getId());

        assertEquals("P2", pilotos.get(0).getId());
        assertEquals("P1", pilotos.get(1).getId());
        assertEquals("P3", pilotos.get(2).getId());
    }

    @Test
    public void mantenimientoTest() {

        manager.guardarDron("D1");
        manager.guardarDron("D2");

        //Comprobar que está en mantenimiento.
        assertTrue(manager.getDron("D1").isMantenimiento());
        assertTrue(manager.getDron("D2").isMantenimiento());
        assertFalse(manager.getDron("D3").isMantenimiento());

        Dron primero = manager.arreglarDron();

        assertEquals("D1", primero.getId());
        assertFalse(primero.isMantenimiento());

        Dron segundo = manager.arreglarDron();

        assertEquals("D2", segundo.getId());
        assertFalse(segundo.isMantenimiento());
    }

    @Test
    public void planesVueloTest() {

        assertEquals(0, manager.numPlanesVuelo());
        assertEquals(0, manager.getPlanVueloByDron("D1").size());
        assertEquals(0, manager.getPlanVueloByPiloto("P1").size());

        LocalDateTime fecha = LocalDateTime.of(2026, 11, 10, 10, 0);

        List<Position> posiciones = new ArrayList<>();
        posiciones.add(new Position(41.28, 2.07));
        posiciones.add(new Position(41.35, 2.12));

        PlanVuelo plan1 = new PlanVuelo("D1", "P1", fecha, 2, posiciones);

        manager.addPlanVuelo(plan1);

        assertEquals(1, manager.numPlanesVuelo());
        assertEquals(1, manager.getPlanVueloByDron("D1").size());
        assertEquals(1, manager.getPlanVueloByPiloto("P1").size());

        // Segundo plan que no coincide con el primero
        PlanVuelo plan2 = new PlanVuelo("D1", "P1", fecha.plusHours(3), 1, posiciones);

        manager.addPlanVuelo(plan2);

        assertEquals(2, manager.numPlanesVuelo());
        assertEquals(2, manager.getPlanVueloByDron("D1").size());
        assertEquals(2, manager.getPlanVueloByPiloto("P1").size());
    }

    public void dronOcupadoTest() {

        LocalDateTime fecha = LocalDateTime.of(2026, 11, 10, 10, 0);

        List<Position> posiciones = new ArrayList<>();
        posiciones.add(new Position(41.28, 2.07));

        PlanVuelo plan1 = new PlanVuelo("D1", "P1", fecha, 2, posiciones);
        manager.addPlanVuelo(plan1);

        PlanVuelo plan2 = new PlanVuelo("D1", "P2", fecha.plusHours(1), 1, posiciones);

        manager.addPlanVuelo(plan2);
    }

    @Test(expected = PilotoOcupadoException.class)
    public void pilotoOcupadoTest() {

        LocalDateTime fecha = LocalDateTime.of(2026, 11, 10, 10, 0);

        List<Position> posiciones = new ArrayList<>();
        posiciones.add(new Position(41.28, 2.07));

        PlanVuelo plan1 = new PlanVuelo("D1", "P1", fecha, 2, posiciones);
        manager.addPlanVuelo(plan1);

        PlanVuelo plan2 = new PlanVuelo("D2", "P1", fecha.plusHours(1), 1, posiciones);

        manager.addPlanVuelo(plan2);
    }
}

