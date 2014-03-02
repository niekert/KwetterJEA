package kwetter.dao;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kwetter.domain.User;
import kwetter.qualifiers.JPAQualifier;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Singleton
@Stateless
@JPAQualifier
public class UserDAO_JPAImpl implements UserDAO
{

    @PersistenceContext(unitName = "pu")
    private EntityManager em;


    public int count()
    {
        Query query = em.createQuery("select count(user) from User user");
        return (Integer)query.getSingleResult();
    }

    public void create(User user)
    {
        System.out.println("Creating");
        em.persist(user);
    }

    public void edit(User user)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<User> findAll()
    {
        Query q = em.createQuery("select user from User user");

        return q.getResultList();
    }

    public void remove(User user)
    {
        em.remove(user);
    }

    public User find(Long id)
    {
        Query q = em.createQuery("select user from User user where user.id = :id");
        q.setParameter("id", id);

        List<User> usersFound = q.getResultList();
        return usersFound.isEmpty() ? null : usersFound.get(0);
    }

    public User find(String name)
    {
        Query q = em.createQuery("select user from User user where user.name = :name");
        q.setParameter("name", name);


        List<User> usersFound = q.getResultList();
        return usersFound.isEmpty() ? null : usersFound.get(0);

    }

}
