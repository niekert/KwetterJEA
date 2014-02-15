package kwetter.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import kwetter.dao.UserDAO;
import kwetter.dao.UserDAOCollectionImpl;
import kwetter.domain.Tweet;
import kwetter.domain.User;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;

@Stateless
public class KwetterService implements Serializable {

    private UserDAO userDAO = new UserDAOCollectionImpl();

    public KwetterService() {
        initUsers();
    }

    public void create(User user) {
        userDAO.create(user);
    }

    public void edit(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public User authenticateUser(String username, String password) {
        if (username.isEmpty()) {
            return null;
        }


        return userDAO.find(username);
    }

    public void remove(User user) {
        userDAO.remove(user);
    }

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public User find(Long id) {
        return userDAO.find(id);
    }

    public User find(String name) {
        return userDAO.find(name);
    }

    public int count() {
        return userDAO.count();
    }

    private void initUsers() {
        User u1 = new User("Niek", "http", "geboren 1");
        User u2 = new User("Frank", "httpF", "geboren 2");
        User u3 = new User("Tom", "httpT", "geboren 3");
        User u4 = new User("Hans", "httpS", "geboren 4");
        u1.addFollowing(u2);
        u2.addFollowing(u1);
        u3.addFollowing(u1);
        u4.addFollowing(u1);
        u2.addFollowing(u3);


        Tweet t1 = new Tweet("Watching TV OMG LOL #SoBoring", new Date(), "PC");
        Tweet t2 = new Tweet("New Flappy bird highscore #swag", new Date(), "PC");
        Tweet t3 = new Tweet("Niet", new Date(), "PC");
        Tweet t4 = new Tweet("Sittin' on a toilet", new Date(), "PC");
        Tweet t5 = new Tweet("Sittin' on a toilet", new Date(), "PC");
        Tweet t6 = new Tweet("Sittin' on a toilet", new Date(), "PC");
        Tweet t7 = new Tweet("Jawel", new Date(), "PC");
        Tweet t8 = new Tweet("Nee", new Date(), "PC");
        Tweet t9 = new Tweet("Joooo", new Date(), "PC");
        Tweet t10 = new Tweet("Tweet", new Date(), "PC");
        Tweet t11 = new Tweet("Twitter is better", new Date(), "PC");
        Tweet t12 = new Tweet("Twitter is better", new Date(), "PC");
        Tweet t13 = new Tweet("Twitter is better", new Date(), "PC");
        Tweet t14 = new Tweet("Twitter is better", new Date(), "PC");
        Tweet t15 = new Tweet("Twitter is better", new Date(), "PC");
        Tweet t16 = new Tweet("Twitter is better", new Date(), "PC");
        Tweet t17 = new Tweet("Twitter is better", new Date(), "PC");

        userDAO.create(u1);
        userDAO.create(u2);
        userDAO.create(u3);
        userDAO.create(u4);

        u1.addTweet(t1);
        u1.addTweet(t2);
        u1.addTweet(t3);
        u1.addTweet(t4);
        u1.addTweet(t5);
        u1.addTweet(t6);
        u1.addTweet(t7);
        u1.addTweet(t8);
        u1.addTweet(t9);
        u1.addTweet(t10);
        u1.addTweet(t11);
        u2.addTweet(t12);
        u2.addTweet(t13);
        u2.addTweet(t14);
        u2.addTweet(t15);
        u2.addTweet(t16);
        u2.addTweet(t17);

    }
}