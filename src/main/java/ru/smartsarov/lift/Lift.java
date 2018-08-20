package ru.smartsarov.lift;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ru.smartsarov.lift.db.Queries;

@Path("/")
public class Lift
{
	@GET
    @Path("/sql1")
    @Produces(MediaType.APPLICATION_JSON)
    public String sql1()
    {
    	String ret = null;
    	try {
    		ret = Queries.sql1();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return ret;
    }
	
    @GET
    @Path("/addresses")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAddresses()
    {
    	String ret = null;
    	try {
    		ret = Queries.getAddresses();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return ret;
    }
	
	@GET
    @Path("/lifts")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLifts()
    {
    	String ret = null;
    	try {
    		ret = Queries.getLifts();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return ret;
    }
	
	@GET
    @Path("/states")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLiftsStates()
    {
    	String ret = null;
    	try {
    		ret = Queries.getLiftsStates();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return ret;
    }
	
	@GET
    @Path("/states/errors")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLiftsErrorsStates()
    {
    	String ret = null;
    	try {
    		ret = Queries.getLiftsErrorsStates();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return ret;
    }
	
	@GET
    @Path("/events")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLiftsEvents()
    {
    	String ret = null;
    	try {
    		ret = Queries.getLiftsEvents();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return ret;
    }
	
	@GET
    @Path("/events/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	public String getLiftEvents(@PathParam("id") int id)
    {
    	String ret = null;
    	try {
    		ret = Queries.getLiftEvents(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return ret;
    }

	@GET
    @Path("/events/{id}/last")
    @Produces(MediaType.APPLICATION_JSON)
	public String getLiftLastEvent(@PathParam("id") int id)
    {
    	String ret = null;
    	try {
    		ret = Queries.getLiftLastEvent(id, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return ret;
    }

	@GET
    @Path("/events/{id}/{count}")
    @Produces(MediaType.APPLICATION_JSON)
	public String getLiftEvent(@PathParam("id") int id, @PathParam("count") Integer count)
    {
    	String ret = null;
    	try {
    		ret = Queries.getLiftLastEvent(id, count);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return ret;
    }
}