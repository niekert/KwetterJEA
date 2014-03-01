package kwetter.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kwetter.domain.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Singleton
public class UserDAOCollectionImpl implements UserDAO
{

    @PersistenceContext(unitName = "pu")
    private EntityManager em;


    @Override
    public int count()
    {
        Query query = em.createQuery("select count(user) from User user");
        return (Integer)query.getSingleResult();
    }

    @Override
    public void create(User user)
    {
            em.persist(user);
    }

    @Override
    public void edit(User user)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<User> findAll()
    {
        Query q = em.createQuery("select user from User user");

        return q.getResultList();
    }

    @Override
    public void remove(User user)
    {
        em.remove(user);
    }

    @Override
    public User find(Long id)
    {
        Query q = em.createQuery("select user from User user where user.id = :id");
        q.setParameter("id", id);

        List<User> usersFound = q.getResultList();
        return usersFound.isEmpty() ? null : usersFound.get(0);
    }

    @Override
    public User find(String name)
    {
        Query q = em.createQuery("select user from User user where user.name = :name");
        q.setParameter("name", name);


        List<User> usersFound = q.getResultList();
        return usersFound.isEmpty() ? null : usersFound.get(0);

    }

}
