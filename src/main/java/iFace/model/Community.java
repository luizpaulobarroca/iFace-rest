package iFace.model;


import com.fasterxml.jackson.annotation.JsonInclude;
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
public class Community {

    @Id
    @GeneratedValue
    protected long id;

    protected String name;
    protected String description;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "UserCommunity",
            joinColumns = @JoinColumn(name = "communityId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    protected List<User> members = new ArrayList<>();

    @ManyToOne
    protected User owner;

}
