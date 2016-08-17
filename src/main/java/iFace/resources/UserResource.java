package iFace.resources;

import iFace.IFaceErrors;
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
        Hibernate.initialize(user.getFriendRequest());
        Hibernate.initialize(user.getCommunities());
        Hibernate.initialize(user.getManagedCommunities());
        Hibernate.initialize(user.getMessagesReceived());
        Hibernate.initialize(user.getMessagesSent());

        return Response.ok(user).build();
    }

    @GET
    @Path("/all")
    @UnitOfWork
    public Response getAllUser() {
        List<User> users = userDao.retrieveAll();
        return Response.ok(users).build();
    }

    @POST
    @Path("/{id}/accept/{id2}")
    @UnitOfWork
    public Response acceptFriend(@PathParam("id") Long id, @PathParam("id2") Long id2) {
        User user1 = userDao.retrive(id);
        User user2 = userDao.retrive(id2);

        List<User> friendslist = user1.getFriends();
        friendslist.add(user2);
        user1.setFriends(friendslist);

        friendslist = user1.getFriendRequest();
        friendslist.remove(user2);
        user1.setFriendRequest(friendslist);

        friendslist = user2.getFriends();
        friendslist.add(user1);
        user2.setFriends(friendslist);

        User userIn = userDao.create(user1);
        User userIn2 = userDao.create(user2);

        return Response.ok(userIn).build();
    }

    @POST
    @Path("/{id}/add/{id2}")
    @UnitOfWork
    public Response requestFriend(@PathParam("id") Long id, @PathParam("id2") Long id2) {
        User user1 = userDao.retrive(id);
        User user2 = userDao.retrive(id2);

        List<User> friendsRlist = user2.getFriendRequest();
        friendsRlist.add(user1);
        user2.setFriendRequest(friendsRlist);

        User userIn = userDao.create(user2);

        return Response.ok(userIn).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response deleteUser(@PathParam("id") Long id) {

        User user;
        List<User> friendslist;

        try {
            user = userDao.retrive(id);
            friendslist = user.getFriends();
        } catch (Exception e) {
            IFaceErrors error = new IFaceErrors(404, "User not found");
            return Response.accepted(error).build();
        }

        for(User x : friendslist) {
            List<User> xfriendslist = x.getFriends();
            xfriendslist.remove(user);
            x.setFriends(xfriendslist);
            x = userDao.update(x);

            friendslist.remove(x);
        }
        user.setFriends(friendslist);
        userDao.delete(user);
        return Response.ok().build();

    }

    @POST
    @UnitOfWork
    public Response createUser(User user) {
        User userIn = userDao.create(user);
        return Response.ok(userIn).build();
    }

    @POST
    @Path("/{id}")
    @UnitOfWork
    public Response updateUser(@PathParam("id") Long id,User user) {
        User userIn = userDao.retrive(id);

        userIn.setName(user.getName());
        userIn.setUsername(user.getUsername());
        userIn.setPassword(user.getPassword());
        userIn.setEmail(user.getEmail());

        userIn = userDao.update(userIn);
        return Response.ok(userIn).build();
    }
}
