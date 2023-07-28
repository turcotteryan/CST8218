/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cst8218.turc0160leve0282.Assignment2.service;

import cst8218.turc0160leve0282.Assignment2.entity.Bouncer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Ryan Turcotte
 * 
 * This class has the same purpose as the BouncerFacade, but with the addition of RESTful API. It represents interactions with the Bouncer entity using REST methods. It is injected with an Entity Manager
 * which gives it access to the database. It inherits from AbstractFacade giving it CRUD capabilities. It is stateless and can handle concurrent client connections due to the lack of conversational state.
 * The @Path annotation specifies the endpoint to the class as a resource. When entered into a URI following the host domain and resources, users gain access to this class and its methods through HTTP protocol.
 */
@Stateless
@Path("cst8218.turc0160.bouncer.entity.bouncer")
public class BouncerFacadeREST extends AbstractFacade<Bouncer> {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public BouncerFacadeREST() {
        super(Bouncer.class);
    }
    
    
    
    
    /*
      Returns the count of Bouncer entities in the Bouncer table when /count appended to URI following the path on a GET request.
    */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response countBouncers() {
        return Response.ok(String.valueOf(super.count())).build();
    }

    /*
      Accepts a Bouncer entity in the body of a post request to the root resource. If bouncers Id is null, sets default values for fields, creates a new entity and returns it in the body of the response. Returning HTTP CREATED.
      If Id not null, returns a HTTP bad request.
    */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createBouncerNullId(Bouncer entity, @Context UriInfo uriInfo) throws URISyntaxException {
        
        if(entity.getId() == null){
            entity.setxPosition(0);
            entity.setyPosition(0);
            entity.setySpeed(0);
            
            super.create(entity);
            URI location = new URI(uriInfo.getRequestUri().getPath() + "/" + entity.getId());
            
            return Response.status(Response.Status.CREATED).location(location).entity(entity).build();
        } 
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();         
        }
    }
    
    /*
      Accepts an Id of a Bouncer appended to end of root resource on a POST request, and a Bouncer entity in the request body. If the Id is not in the database returns HTTP NOT_FOUND. 
      If the Bouncer returned from the initial fetch using the supplied Id does not have a matching Id with the one supplied in the body, returns HTTP CONFLICT.
      If the correct bouncer is fetched with the supplied Id matching the one in the body, update the bouncer from the database with any non-null fields from the bouncer in the body while retaining any data not being overwritten.
      Upon correct action, return HTTP CREATED.
    */
    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateBouncer(Bouncer newBouncer, @Context UriInfo uriInfo) throws URISyntaxException {
        
        //get bouncer from database with matching id from incoming entity
        Bouncer oldBouncer = super.find(newBouncer.getId());
        
        //Incoming entity id does not exist in db
        if(oldBouncer == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        } 
        //Returned entity from db does not match the id we searched for
        else if(oldBouncer.getId() != newBouncer.getId()) {
            return Response.status(Response.Status.CONFLICT).build();        
        }
        //Correct id matched
        else { 
            newBouncer.updates(oldBouncer);
            super.edit(oldBouncer);
            
            URI location = new URI(uriInfo.getRequestUri().getPath() + "/" + oldBouncer.getId());
            
            return Response.status(Response.Status.CREATED).location(location).entity(oldBouncer).build();
        }
    }

    
    /*
       Accepts a Bouncer Id appended to the root resource of a PUT request. Takes a bouncer in the body of the request. If the Id doesnt exist in the database, return an HTTP NOT_FOUND response.
       If the fetched Bouncer Id does not match the supplied bouncer Id, return HTTP CONFLICT.
       If the fetched bouncer Id matches the supplied bouncer, completely replace the bouncer in the database with the supplied bouncer. Return HTTP CREATED.
    */
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response replaceBouncer(Bouncer newBouncer, @Context UriInfo uriInfo) throws URISyntaxException {
        
        //get bouncer from database with matching id from incoming entity
        Bouncer oldBouncer = super.find(newBouncer.getId());
        
        //Incoming entity id does not exist in db
        if(oldBouncer == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        } 
        //Returned entity from db does not match the id we searched for
        else if(oldBouncer.getId() != newBouncer.getId()) {
            return Response.status(Response.Status.CONFLICT).build();        
        }
        //Correct id matched
        else { 
            super.edit(newBouncer);
            
            URI location = new URI(uriInfo.getRequestUri().getPath() + "/" + newBouncer.getId());
            
            return Response.status(Response.Status.CREATED).location(location).entity(newBouncer).build();
        }
    }

    /*
        Restricts the ability to make a PUT request on the root resource. Return HTTP FORBIDDEN.
    */
    @PUT
    public Response putOnRoot() {
        return Response.status(Response.Status.FORBIDDEN).build();
    }
    
    
    
    
    
  
   
    
    
    
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Bouncer find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Bouncer> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Bouncer> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }
    

    

    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
