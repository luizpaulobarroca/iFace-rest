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
        Hibernate.initialize(user.getFriendRequest());
        Hibernate.initialize(user.getCommunities());
        Hibernate.initialize(user.getManagedCommunities());

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

        if(!user1.getFriendRequest().contains(user2)) {
            return Response.noContent().build();
        }

        List<User> friendslist = user1.getFriends();
        friendslist.add(user2);
        user1.setFriends(friendslist);

        friendslist = user1.getFriendRequest();
        friendslist.remove(user2);
        user1.setFriendRequest(friendslist);

        User userIn = userDao.create(user1);

        return Response.ok(userIn).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response deleteUser(@PathParam("id") Long id) {

        User user = userDao.retrive(id);

        if (user != null) {

            if(user.getFriends() != null) {
                List<User> friendslist = user.getFriends();
                for(User x : friendslist) {
                    List<User> xfriendslist = x.getFriends();
                    xfriendslist.remove(user);
                    x.setFriends(xfriendslist);
                    userDao.create(x);
                    friendslist.remove(x);
                }
                user.setFriends(friendslist);
            }
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
