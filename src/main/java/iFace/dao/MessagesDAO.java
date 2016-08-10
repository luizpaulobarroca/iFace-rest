package iFace.dao;

import iFace.model.Messages;
import org.hibernate.SessionFactory;

public class MessagesDAO extends MeuDAO<Messages> {
    public MessagesDAO(SessionFactory factory) {
        super(factory);
    }
}
