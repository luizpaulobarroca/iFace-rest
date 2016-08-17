package iFace.dao;

import iFace.model.Messages;
import org.hibernate.SessionFactory;

public class MessagesDAO extends MeuDAO<Messages> {
    public SessionFactory factory;
    public MessagesDAO(SessionFactory factory) {
        super(factory);
        this.factory = factory;
    }
}
