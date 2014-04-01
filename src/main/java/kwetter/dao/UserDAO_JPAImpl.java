package kwetter.dao;

import kwetter.domain.Role;
import kwetter.domain.User;
import kwetter.qualifiers.JPAQualifier;
import kwetter.utils.Utilities;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


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
        em.merge(user);
    }

    public void edit(User user)
    {
        em.merge(user);
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
        Query newQuery = this.em.createQuery("Select user from User user where user.name = :name");
        newQuery.setParameter("name", name);

        List<User> usersFound = newQuery.getResultList();
        return usersFound.isEmpty() ? null : usersFound.get(0);
    }

    @Override
    public User registerNewUser(String username, String email, String password){

        String hashedPassword = Utilities.hashPassword(password);

        User newUser = new User(username, "http", "Swaggerbuoi", email, hashedPassword);
        Role role = new Role("normal");
        em.merge(role);
        newUser.getRoles().add(role);
        em.merge(newUser);

        return newUser;
    }

    @Override
    public User findUserByRegistrationID(String activationID) {
        Query newQuery = this.em.createQuery("Select user from User user where user.activationLink = :activationID");
        newQuery.setParameter("activationID", activationID);

        List<User> usersfound  = newQuery.getResultList();
        return usersfound.isEmpty() ? null : usersfound.get(0);

    }

    @Override
    public User authenticateUser(String username, String password) {
        Query newQuery = this.em.createQuery("Select user from User user where user.name = :name and user.password = :password and user.activationLink is null");
        newQuery.setParameter("name", username);
        newQuery.setParameter("password", password);

        List<User> usersfound  = newQuery.getResultList();
        return usersfound.isEmpty() ? null : usersfound.get(0);
    }

}
