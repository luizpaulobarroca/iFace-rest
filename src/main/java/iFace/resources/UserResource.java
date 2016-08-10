package iFace.resources;

import iFace.dao.UserDAO;
import iFace.model.User;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.Hibernate;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserDAO userDao;

    public UserResource(UserDAO userDao) {
        this.userDao = userDao;
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getUser(@PathParam("id") Long id) {
        User user = userDao.retrive(id);

        Hibernate.initialize(user.getFriends());

        return Response.ok(user).build();
    }

    @GET
    @Path("/all")
    @UnitOfWork
    public Response getAllUser() {
        List<User> users = userDao.retrieveAll();
        return Response.ok(users).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response deleteUser(@PathParam("id") Long id) {

        User user = userDao.retrive(id);

        if (user != null) {
            userDao.delete(user);
            return Response.ok().build();
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

    }

    @POST
    @UnitOfWork
    public Response createUser(User user) {
        User userIn = userDao.create(user);
        return Response.ok(userIn).build();
    }
}
