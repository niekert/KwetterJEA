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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


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

    @Override
    public boolean addFollowing(User invokingUser, User targetedUser) {
        targetedUser.getFollowers().add(invokingUser);
        invokingUser.getFollowing().add(targetedUser);

        em.merge(invokingUser);
        em.merge(targetedUser);
        return true;
    }

    @Override
    public boolean removeFollowing(User invokingUser, User targetUser) {
        targetUser.getFollowers().remove(invokingUser);
        invokingUser.getFollowing().remove(targetUser);

        em.merge(invokingUser);
        em.merge(targetUser);

        return true;
    }

    public User find(Long id)
    {
        Query q = em.createQuery("select user from User user where user.id = :id");
        q.setParameter("id", id);

        List<User> usersFound = q.getResultList();
        return usersFound.isEmpty() ? null : usersFound.get(0);
    }

    @Override
    public User findByName(String name)
    {
        Query newQuery = this.em.createQuery("Select user from User as user where user.name = :name");
        newQuery.setParameter("name", name);

        Object o = newQuery.getSingleResult();
        return o == null ? null : (User) o;
    }

}
