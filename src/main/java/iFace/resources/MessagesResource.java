package iFace.resources;

import iFace.dao.MessagesDAO;
import iFace.model.Community;
import iFace.model.Messages;
import iFace.model.User;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.Hibernate;

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
    public Response getUser(@PathParam("id") Long id) {
        Messages messages = messagesDAO.retrive(id);

        return Response.ok(messages).build();
    }

    @GET
    @Path("/all")
    @UnitOfWork
    public Response getAllUser() {
        List<Messages> messages = messagesDAO.retrieveAll();
        return Response.ok(messages).build();
    }

    @POST
    @Path("/{id}/to/{id2}")
    @UnitOfWork
    public Response sendMessageToUser(@PathParam("id") Long id, @PathParam("id2") Long id2,String message) {
        Messages messages = new Messages();
        User user1 = new User();
        User user2 = new User();
        user1.setId(id);
        user2.setId(id2);
        messages.setSender(user1);
        messages.setReceiver(user2);
        messages.setMessage(message);

        Date date = new Date();
        messages.setDate(date);

        Messages messagesIn = messagesDAO.create(messages);
        return Response.ok(messagesIn).build();
    }

    @POST
    @Path("/{id}/toc/{id2}")
    @UnitOfWork
    public Response sendMessageToCommunity(@PathParam("id") Long id, @PathParam("id2") Long id2,String message) {
        Messages messages = new Messages();

        User user1 = new User();
        user1.setId(id);
        messages.setSender(user1);

        Community community = new Community();
        community.setId(id2);
        messages.setCommunity(community);

        messages.setMessage(message);
        Messages messagesIn = messagesDAO.create(messages);
        return Response.ok(messagesIn).build();
    }
}
