package iFace.dao;

import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.util.Generics;
import org.hibernate.SessionFactory;

import java.util.List;

public class MeuDAO<T> extends AbstractDAO<T> {

    private Class<?> entityClass;

    public MeuDAO(SessionFactory factory) {
        super(factory);

        this.entityClass = Generics.getTypeParameter(getClass());

    }

    public T retrive(Long id) {
        return get(id);
    }

    public List<T> retrieveAll() {
        return list(currentSession().createQuery("SELECT u FROM " + this.entityClass.getName() + " u"));
    }

    public T create(T obj) {
        return persist(obj);
    }

    public T update(T obj) {
        return persist(obj);
    }

    public void delete(T obj) {
        currentSession().delete(obj);
    }

}
