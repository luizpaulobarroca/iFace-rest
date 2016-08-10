package iFace.resources;

import iFace.dao.CommunityDAO;
import iFace.dao.UserDAO;
import iFace.model.Community;
import iFace.model.User;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.Hibernate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
}
