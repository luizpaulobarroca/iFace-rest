package iFace.dao;


import iFace.model.User;
import org.hibernate.SessionFactory;

public class UserDAO extends MeuDAO<User> {

    public UserDAO(SessionFactory factory) {
        super(factory);
    }


//    UTILIZAR PARA BUSCAR POR NOME
//    public User getUserById(int id) {
//        session = sessionFactory.openSession();
//
//        Query query = session.createQuery("from User where id = :id");
//        query.setParameter("id", id);
//
//        User u = (User) query.uniqueResult();
//
//        session.close();
//        return u;
//    }

}
