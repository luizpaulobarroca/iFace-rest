package iFace.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Messages {

    @Id
    @GeneratedValue
    protected long id;

    protected String message;

    @ManyToOne(fetch = FetchType.EAGER)
    protected Community community;

    @ManyToOne(fetch = FetchType.EAGER)
    protected User sender;

    @ManyToOne(fetch = FetchType.EAGER)
    protected User receiver;

}
