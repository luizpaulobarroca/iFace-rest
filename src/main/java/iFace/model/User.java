package iFace.model;


import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    protected String name;
    protected String username;
    protected String password;

    protected String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "UserFriends", joinColumns = @JoinColumn(name = "User1"), inverseJoinColumns = @JoinColumn(name = "User2"))
    protected List<User> friends = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "UserFriendRequest", joinColumns = @JoinColumn(name = "UserRequested"), inverseJoinColumns = @JoinColumn(name = "User"))
    protected List<User> friendRequest = new ArrayList<User>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "members")
    protected List<Community> communities = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    protected List<Community> managedCommunities = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver")
    protected  List<Messages> messagesReceived = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender")
    protected  List<Messages> messagesSent = new ArrayList<>();
}
