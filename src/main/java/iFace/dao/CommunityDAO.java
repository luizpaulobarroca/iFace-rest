package iFace.dao;

import iFace.model.Community;
import org.hibernate.SessionFactory;

public class CommunityDAO extends MeuDAO<Community> {
    public SessionFactory factory;
    public CommunityDAO(SessionFactory factory) {
        super(factory);
        this.factory = factory;
    }
}
