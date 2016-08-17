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
    public Response getCommunity(@PathParam("id") Long id) {
        Community community = communityDAO.retrive(id);

        Hibernate.initialize(community.getMembers());
        Hibernate.initialize(community.getOwner());
        Hibernate.initialize(community.getMessages());

        return Response.ok(community).build();
    }

    @POST
    @Path("/owner/{id}")
    @UnitOfWork
    public Response createCommunity(@PathParam("id") Long id, Community community) {
        Community communityIn = communityDAO.create(community);
        User user = new User();
        user.setId(id);
        community.setOwner(user);
        return Response.ok(communityIn).build();
    }

    @POST
    @Path("/{id}")
    @UnitOfWork
    public Response updateCommunity(@PathParam("id") Long id,Community community) {
        Community communityIn = communityDAO.retrive(id);

        communityIn.setName(community.getName());
        communityIn.setDescription(community.getDescription());
        communityIn.setOwner(community.getOwner());

        communityIn = communityDAO.update(communityIn);
        return Response.ok(communityIn).build();
    }

    @GET
    @Path("/all")
    @UnitOfWork
    public Response getAllCommunities() {
        List<Community> communities = communityDAO.retrieveAll();
        return Response.ok(communities).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response deleteCommunity(@PathParam("id") Long id) {

        Community community = communityDAO.retrive(id);

        if (community != null) {
            communityDAO.delete(community);
            return Response.ok().build();
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

    }

    @POST
    @Path("/{id}/add/{id2}")
    @UnitOfWork
    public Response addUser(@PathParam("id") Long id, @PathParam("id2") Long id2) {
        Community community = communityDAO.retrive(id);

        List<User> members = community.getMembers();
        User user = new User();
        user.setId(id2);
        members.add(user);

        community = communityDAO.update(community);

        return Response.ok(community).build();
    }
}
