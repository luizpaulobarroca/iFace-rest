package iFace.resources;

import iFace.IFaceErrors;
import iFace.dao.CommunityDAO;
import iFace.model.Community;
import iFace.model.User;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

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

        try {
            Hibernate.initialize(community.getMembers());
            Hibernate.initialize(community.getOwner());
            Hibernate.initialize(community.getMessages());
        } catch (Exception e) {
            String error = IFaceErrors.response("Community not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        return Response.ok(community).build();
    }

    @POST
    @Path("/owner/{id}")
    @UnitOfWork
    public Response createCommunity(@PathParam("id") Long id, Community community) {
        Community communityIn = communityDAO.create(community);
        User user;

        Session session = communityDAO.factory.openSession();

        Query query = session.createQuery("from User where id = :id");
        query.setParameter("id", id);

        user = (User) query.uniqueResult();

        session.close();

        if(user == null) {
            String error = IFaceErrors.response("User not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        community.setOwner(user);

        return Response.ok(communityIn).build();
    }

    @POST
    @Path("/{id}")
    @UnitOfWork
    public Response updateCommunity(@PathParam("id") Long id,Community community) {
        Community communityIn = communityDAO.retrive(id);

        try {
            communityIn.setName(community.getName());
            communityIn.setDescription(community.getDescription());
        } catch (Exception e) {
            String error = IFaceErrors.response("Community not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

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

    @POST
    @Path("/{id}/add/{id2}")
    @UnitOfWork
    public Response addMember(@PathParam("id") Long id, @PathParam("id2") Long id2) {
        Community community = communityDAO.retrive(id);
        List<User> members;

       try {
           members = community.getMembers();
       } catch (Exception e) {
           String error = IFaceErrors.response("Community not found.");
           return Response.status(Response.Status.NOT_FOUND).entity(error).build();
       }

        Session session = communityDAO.factory.openSession();

        Query query = session.createQuery("from User where id = :id");
        query.setParameter("id", id2);

        User user = (User) query.uniqueResult();

        session.close();

        if(user == null) {
            String error = IFaceErrors.response("User not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        if(community.getMembers().contains(user)) {
            String error = IFaceErrors.response("User already a member.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }


        members.add(user);
        community = communityDAO.update(community);

        return Response.ok(community).build();
    }
}
