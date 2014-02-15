package kwetter.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import kwetter.dao.TweetDAO;
import kwetter.dao.TweetDAOCollectionImpl;
import kwetter.dao.UserDAO;
import kwetter.dao.UserDAOCollectionImpl;
import kwetter.domain.Tweet;
import kwetter.domain.User;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;

@Stateless
public class KwetterService implements Serializable {

    private UserDAO userDAO = new UserDAOCollectionImpl();
    private TweetDAO tweetDao = new TweetDAOCollectionImpl(userDAO);

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

    public List<Tweet> findTweets(String searchText) {
        return tweetDao.findTweets(searchText);
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

    public List<Tweet> getTimeline(User user){
        List<Tweet> timelineTweets = new ArrayList<Tweet>();
        for (User existingUser : userDAO.findAll())
        {
            if(user.getFollowing().contains(existingUser)){
                for (Tweet tweet : existingUser.getTweets())
                {
                    timelineTweets.add(tweet);
                }

            }
        }

        List<Tweet> sortedTweets = new ArrayList<Tweet>(timelineTweets); Collections.sort(sortedTweets);
        return sortedTweets;

    }

    public List<Tweet> getMentions(User user){
        return tweetDao.findMentions(user);
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


        userDAO.create(u1);
        userDAO.create(u2);
        userDAO.create(u3);
        userDAO.create(u4);

        Tweet t1 = new Tweet(u1, "Watching TV OMG LOL #SoBoring @Niek @Swek @niek", new Date(), "PC");
        Tweet t2 = new Tweet(u1, "New Flappy bird highscore #swag #TAGGGGGGG", new Date(), "PC");
        Tweet t3 = new Tweet(u1, "Niet", new Date(), "PC");
        Tweet t4 = new Tweet(u1, "Sittin' on a toilet", new Date(), "PC");
        Tweet t5 = new Tweet(u1,"Sittin' on a toilet", new Date(), "PC");
        Tweet t6 = new Tweet(u2, "Sittin' on a toilet", new Date(), "PC");
        Tweet t7 = new Tweet(u1, "Jawel", new Date(), "PC");
        Tweet t8 = new Tweet(u3,"Nee", new Date(), "PC");
        Tweet t9 = new Tweet(u4, "Joooo", new Date(), "PC");
        Tweet t10 = new Tweet(u3, "Tweet", new Date(), "PC");
        Tweet t11 = new Tweet(u4, "Twitter is better", new Date(), "PC");
        Tweet t12 = new Tweet(u1,"Twitter is better", new Date(), "PC");
        Tweet t13 = new Tweet(u2,"Twitter is better", new Date(), "PC");
        Tweet t14 = new Tweet(u3,"Twitter is better", new Date(), "PC");
        Tweet t15 = new Tweet(u3,"Twitter is better", new Date(), "PC");
        Tweet t16 = new Tweet(u3,"Twitter is better", new Date(), "PC");
        Tweet t17 = new Tweet(u4, "Twitter is better", new Date(), "PC");
        tweetDao.create(t1);
        tweetDao.create(t2);
        tweetDao.create(t3);
        tweetDao.create(t4);
        tweetDao.create(t5);
        tweetDao.create(t6);
        tweetDao.create(t7);
        tweetDao.create(t8);
        tweetDao.create(t9);
        tweetDao.create(t10);
        tweetDao.create(t11);
        tweetDao.create(t12);
        tweetDao.create(t13);
        tweetDao.create(t14);
        tweetDao.create(t15);
        tweetDao.create(t16);
        tweetDao.create(t17);

    }
}