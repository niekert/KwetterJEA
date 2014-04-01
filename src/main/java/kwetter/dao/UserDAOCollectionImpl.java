package kwetter.dao;

import kwetter.domain.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.Singleton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class UserDAOCollectionImpl implements UserDAO, Serializable
{

    private List<User> users;

    public UserDAOCollectionImpl()
    {
        users = new ArrayList();
    }

    @Override
    public int count()
    {
        return users.size();
    }

    @Override
    public void create(User user)
    {
        users.add(user);
    }

    @Override
    public void edit(User user)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<User> findAll()
    {
        return new ArrayList(users);
    }

    @Override
    public void remove(User user)
    {
        users.remove(user);
    }

    @Override
    public boolean addFollowing(User invokingUser, User targetedUser) {
        return false;
    }

    @Override
    public boolean removeFollowing(User invokingUser, User targetUser) {
        return false;
    }

    @Override
    public User registerNewUser(String username, String email, String password) {
        return null;
    }

    @Override
    public User findUserByRegistrationID(String activationID) {
        return null;
    }

    @Override
    public User authenticateUser(String username, String password) {
        return null;
    }

    @Override
    public User find(Long id)
    {
        throw new NotImplementedException();
    }

    @Override
    public User findByName(String name)
    {
        User foundUser = null;
        for (User user : users)
        {
            if (user.getName().toLowerCase().equals(name.toLowerCase()))
            {
                foundUser = user;
                break;
            }
        }

        return foundUser;
    }
}
