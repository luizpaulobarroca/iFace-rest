package iFace.resources;

import iFace.IFaceErrors;
import iFace.dao.MessagesDAO;
import iFace.model.Community;
import iFace.model.Messages;
import iFace.model.User;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
public class MessagesResource {

    private final MessagesDAO messagesDAO;

    public MessagesResource(MessagesDAO messagesDAO) {
        this.messagesDAO = messagesDAO;
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getMessage(@PathParam("id") Long id) {
        Messages messages;

        try {
            messages = messagesDAO.retrive(id);
        } catch (Exception e) {
            String error = IFaceErrors.response("Message not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        return Response.ok(messages).build();
    }

    @GET
    @Path("/all")
    @UnitOfWork
    public Response getAllMessages() {
        List<Messages> messages = messagesDAO.retrieveAll();
        return Response.ok(messages).build();
    }

    @POST
    @Path("/{id}/to/{id2}")
    @UnitOfWork
    public Response sendMessageToUser(@PathParam("id") Long id, @PathParam("id2") Long id2,String message) {
        Messages messages = new Messages();
        User user1;
        User user2;
        Messages messagesIn;
        Session session = messagesDAO.factory.openSession();

        Query query = session.createQuery("from User where id = :id");
        query.setParameter("id", id);

        user1 = (User) query.uniqueResult();

        if(user1 == null) {
            String error = IFaceErrors.response("User id = " + id + " not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }


        query = session.createQuery("from User where id = :id");
        query.setParameter("id", id2);

        user2 = (User) query.uniqueResult();

        if(user2 == null) {
            String error = IFaceErrors.response("User id = " + id2 + " not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        session.close();

        try {
            messages.setSender(user1);
            messages.setReceiver(user2);
            messages.setMessage(message);

            Date date = new Date();
            messages.setDate(date);

            messagesIn = messagesDAO.create(messages);
        } catch (Exception e) {
            String error = IFaceErrors.response("Couldn't send message.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.ok(messagesIn).build();
    }

    @POST
    @Path("/{id}/toc/{id2}")
    @UnitOfWork
    public Response sendMessageToCommunity(@PathParam("id") Long id, @PathParam("id2") Long id2,String message) {
        Messages messages = new Messages();
        Community community;
        User user;
        Messages messagesIn;

        Session session = messagesDAO.factory.openSession();

        Query query = session.createQuery("from User where id = :id");
        query.setParameter("id", id);

        user = (User) query.uniqueResult();

        if(user == null) {
            String error = IFaceErrors.response("User not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        query = session.createQuery("from Community where id = :id");
        query.setParameter("id", id2);

        community = (Community) query.uniqueResult();

        if(community == null) {
            String error = IFaceErrors.response("Community not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        session.close();

        try {
            messages.setSender(user);
            messages.setCommunity(community);

            messages.setMessage(message);
            messagesIn = messagesDAO.create(messages);
        } catch (Exception e) {
            String error = IFaceErrors.response("Couldn't send message.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.ok(messagesIn).build();
    }
}
