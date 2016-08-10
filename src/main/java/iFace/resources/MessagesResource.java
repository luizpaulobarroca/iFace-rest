package iFace.resources;

import iFace.dao.MessagesDAO;

public class MessagesResource {

    private final MessagesDAO messagesDAO;

    public MessagesResource(MessagesDAO messagesDAO) {
        this.messagesDAO = messagesDAO;
    }
}
