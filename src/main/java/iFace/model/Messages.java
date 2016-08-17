package iFace.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    protected String message;

    @ManyToOne(fetch = FetchType.EAGER)
    protected Community community;

    @ManyToOne(fetch = FetchType.EAGER)
    protected User sender;

    @ManyToOne(fetch = FetchType.EAGER)
    protected User receiver;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date date;

}
