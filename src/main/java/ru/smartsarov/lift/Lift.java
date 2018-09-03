package ru.smartsarov.lift;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ru.smartsarov.lift.db.Queries;

@Path("/")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class Lift
{
	@GET
	@Path("/")
	@Produces(MediaType.TEXT_HTML)
    public Response index()
    {
		InputStream is = this.getClass().getResourceAsStream("/static/index.html");
    	return Response.status(Response.Status.OK).type(MediaType.TEXT_HTML + ";charset=utf-8").entity(is).build();
    }
	
	@GET
    @Path("/sql1")
    public Response sql1()
    {
    	String ret = null;
    	try {
    		ret = Queries.sql1();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(Response.Status.OK).entity(ret).build();
    }
	
    @GET
    @Path("/addresses")
    public Response getAddresses()
    {
    	String ret = null;
    	try {
    		ret = Queries.getAddresses();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(Response.Status.OK).entity(ret).build();
    }
	
	@GET
    @Path("/lifts")
    public Response getLifts()
    {
    	String ret = null;
    	try {
    		ret = Queries.getLifts();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(Response.Status.OK).entity(ret).build();
    }
	
	@GET
    @Path("/states")
    public Response getLiftsStates()
    {
    	String ret = null;
    	try {
    		ret = Queries.getLiftsStates();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(Response.Status.OK).entity(ret).build();
    }
	
	@GET
    @Path("/states/errors")
    public Response getLiftsErrorsStates()
    {
    	String ret = null;
    	try {
    		ret = Queries.getLiftsErrorsStates();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(Response.Status.OK).entity(ret).build();
    }
	
	@GET
    @Path("/states/problems")
    public Response getLiftsProblemsStates()
    {
    	String ret = null;
    	try {
    		ret = Queries.getLiftsErrorsStates();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(Response.Status.OK).entity(ret).build();
    }
	
	@GET
    @Path("/events")
    public Response getLiftsEvents()
    {
    	String ret = null;
    	try {
    		ret = Queries.getLiftsEvents();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(Response.Status.OK).entity(ret).build();
    }
	
	@GET
    @Path("/events/{id}")
	public Response getLiftEvents(@PathParam("id") int id)
    {
    	String ret = null;
    	try {
    		ret = Queries.getLiftEvents(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(Response.Status.OK).entity(ret).build();
    }

	@GET
    @Path("/events/{id}/last")
	public Response getLiftLastEvent(@PathParam("id") int id)
    {
    	String ret = null;
    	try {
    		ret = Queries.getLiftLastEvent(id, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(Response.Status.OK).entity(ret).build();
    }

	@GET
    @Path("/events/{id}/{count}")
	public Response getLiftEvent(@PathParam("id") int id, @PathParam("count") Integer count)
    {
    	String ret = null;
    	try {
    		ret = Queries.getLiftLastEvent(id, count);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(Response.Status.OK).entity(ret).build();
    }
	
	@GET
    @Path("/alert")
    public Response getLiftsStatesAlert()
    {
    	String ret = null;
    	try {
    		ret = Queries.getLiftsStatesAlert();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(Response.Status.OK).entity(ret).build();
    }
}