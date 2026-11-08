package edu.upc.dsa.services;

import edu.upc.dsa.DronManager;
import edu.upc.dsa.DronManagerImpl;
import edu.upc.dsa.models.*;
import edu.upc.dsa.exceptions.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/drones", description = "Endpoint to Dron Service")
@Path("/drones")
public class DronService {

    private DronManager manager;

    public DronService() {
        this.manager = DronManagerImpl.getInstance();

        if (manager.numDrones()==0) {
            manager.addDron("D1", "Uno", "DJI", "M1");
            manager.addDron("D2", "Dos", "DJI", "M2");
            manager.addDron("D3", "Tres", "DJI", "M3");
            manager.getDron("D1").setHorasVuelo(10);
            manager.getDron("D2").setHorasVuelo(20);
            manager.getDron("D3").setHorasVuelo(15);
        }
        if (manager.numPilotos()==0){
            manager.addPiloto("P1", "Ana", "Garcia");
            manager.addPiloto("P2", "Luis", "Lopez");
            manager.addPiloto("P3", "Paula", "Triviño");
            manager.getPiloto("P1").setHorasVuelo(5);
            manager.getPiloto("P2").setHorasVuelo(15);
            manager.getPiloto("P3").setHorasVuelo(2);
        }
    }

    // GETs

    // Lista drones ordenados por horas de vuelo.
    @GET
    @ApiOperation(value = "Obtener lista drones", notes = "Drones ordenados por horas de vuelo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Dron.class, responseContainer = "List")
    })
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDrones() {

        List<Dron> drones = this.manager.getDronesByHorasVuelo();

        GenericEntity<List<Dron>> entity = new GenericEntity<List<Dron>>(drones) {};

        return Response.status(200).entity(entity).build();
    }


    // Lista pilotos ordenados por horas de vuelo.
    @GET
    @ApiOperation(value = "Obtener lista pilotos", notes = "Pilotos ordenados por horas")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful",
                    response = Piloto.class, responseContainer = "List")
    })
    @Path("/pilotos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPilotos() {

        List<Piloto> pilotos = this.manager.getPilotosByHorasVuelo();

        GenericEntity<List<Piloto>> entity = new GenericEntity<List<Piloto>>(pilotos) {};

        return Response.status(200).entity(entity).build();
    }

    // Obtener planes de vuelo de un dron.
    @GET
    @Path("/planes/dron/{id}")
    @ApiOperation(value = "Consultar planes de vuelo de un dron")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Planes obtenidos correctamente", response = PlanVuelo.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Dron no encontrado")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlanesDron(@PathParam("id") String id) {

        try {
            List<PlanVuelo> planes = this.manager.getPlanVueloByDron(id);
            GenericEntity<List<PlanVuelo>> entity = new GenericEntity<List<PlanVuelo>>(planes) {};
            return Response.status(200).entity(entity).build();

        } catch (DronNotFoundException e) {
            return Response.status(404).build();
        }
    }

    // Obtener planes de vuelo de un piloto.
    @GET
    @Path("/planes/piloto/{id}")
    @ApiOperation(value = "Consultar planes de vuelo de un piloto")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Planes obtenidos correctamente",
                    response = PlanVuelo.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Piloto no encontrado")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlanesPiloto(@PathParam("id") String id) {

        try {
            List<PlanVuelo> planes = this.manager.getPlanVueloByPiloto(id);

            GenericEntity<List<PlanVuelo>> entity = new GenericEntity<List<PlanVuelo>>(planes) {};
            return Response.status(200).entity(entity).build();

        } catch (PilotoNotFoundException e) {
            return Response.status(404).build();
        }
    }

    //POSTs

    // 3. Añadir Dron
    @POST
    @ApiOperation(value = "Añadir nuevo Dron", notes = "Añadir nuevo dron")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Dron creado", response = Dron.class),
            @ApiResponse(code = 400, message = "Invalid data")
    })
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDron(Dron dron) {

        if (dron == null || dron.getId() == null || dron.getNombre() == null || dron.getFabricante() == null || dron.getModelo() == null) {
            return Response.status(400).build();
        }

        try {
            this.manager.addDron(dron.getId(), dron.getNombre(), dron.getFabricante(), dron.getModelo());

            return Response.status(201).entity(this.manager.getDron(dron.getId())).build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(400).build();
        }
    }

    // 4. Añadir Piloto
    @POST
    @ApiOperation(value = "Añadir nuevo Piloto", notes = "Añadir nuevo piloto")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "PilotO creado", response = Piloto.class),
            @ApiResponse(code = 400, message = "Invalid data")
    })
    @Path("/pilotos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPiloto(Piloto piloto) {

        if (piloto == null || piloto.getId() == null || piloto.getName() == null || piloto.getSurname() == null) {
            return Response.status(400).build();
        }

        try {
            this.manager.addPiloto(piloto.getId(), piloto.getName(), piloto.getSurname());
            return Response.status(201).entity(this.manager.getPiloto(piloto.getId())).build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(400).build();
        }
    }

    // Guardar dron en mantenimiento.
    @POST
    @Path("/mantenimiento/{id}")
    @ApiOperation(value = "Guardar dron en mantenimiento")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Dron guardado correctamente"),
            @ApiResponse(code = 404, message = "Dron no encontrado"),
            @ApiResponse(code = 409, message = "Dron ya en mantenimiento")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarDron(@PathParam("id") String id) {

        try {
            this.manager.guardarDron(id);
            return Response.status(200).build();

        } catch (DronNotFoundException e) {
            return Response.status(404).build();

        } catch (DronEnMantenimientoException e) {
            return Response.status(409).build();
        }
    }


    // Añadir plan de vuelo
    @POST
    @Path("/planes")
    @ApiOperation(value = "Añadir un plan de vuelo")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Plan añadido correctamente"),
            @ApiResponse(code = 400, message = "Datos incorrectos"),
            @ApiResponse(code = 404, message = "Dron o piloto no encontrado"),
            @ApiResponse(code = 409, message = "Dron o piloto no disponible")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlanVuelo(PlanVuelo plan) {

        if (plan == null) {
            return Response.status(400).build();
        }

        try {
            this.manager.addPlanVuelo(plan);
            return Response.status(201).entity(plan).build();

        } catch (DronNotFoundException | PilotoNotFoundException e) {
            return Response.status(404).build();

        } catch (DronOcupadoException | PilotoOcupadoException
                 | DronEnMantenimientoException e) {
            return Response.status(409).build();

        } catch (IllegalArgumentException e) {
            return Response.status(400).build();
        }
    }



    // PUTs


    // Reparar Dron en mantenimiento.
    @PUT
    @Path("/mantenimiento/reparar")
    @ApiOperation(value = "Reparar primer dron en mantenimiento")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Dron reparado correctamente"),
            @ApiResponse(code = 404, message = "No hay drones en mantenimiento")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response repararDron() {

        Dron dron = this.manager.arreglarDron();

        if (dron == null) {
            return Response.status(404).build();
        }

        return Response.status(200).entity(dron).build();
    }


}


