package iFace.resources;

import iFace.dao.CommunityDAO;
import iFace.dao.UserDAO;
import iFace.model.Community;
import iFace.model.User;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.Hibernate;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/community")
@Produces(MediaType.APPLICATION_JSON)
public class CommunityResource {

    private final CommunityDAO communityDAO;

    public CommunityResource(CommunityDAO communityDAO) {
        this.communityDAO = communityDAO;
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getUser(@PathParam("id") Long id) {
        Community community = communityDAO.retrive(id);

        return Response.ok(community).build();
    }

    @POST
    @UnitOfWork
    public Response createCommunity(Community community) {
        Community communityIn = communityDAO.create(community);
        return Response.ok(communityIn).build();
    }

    @GET
    @Path("/all")
    @UnitOfWork
    public Response getAllUser() {
        List<Community> communities = communityDAO.retrieveAll();
        return Response.ok(communities).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response deleteUser(@PathParam("id") Long id) {

        Community community = communityDAO.retrive(id);

        if (community != null) {
            communityDAO.delete(community);
            return Response.ok().build();
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

    }
//
//    @POST
//    @Path("/{id}/add/{id2}")
//    @UnitOfWork
//    public Response addUser(@PathParam("id") Long id, @PathParam("id2") Long id2) {
//        Community community = communityDAO.retrive(id);
//        return Response.ok(community).build();
//    }
}
