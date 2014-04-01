package kwetter.service;

import kwetter.dao.TweetDAO;
import kwetter.dao.UserDAO;
import kwetter.domain.Role;
import kwetter.domain.Tweet;
import kwetter.domain.User;
import kwetter.events.AuthenticationEvent;
import kwetter.events.FollowingChangedEvent;
import kwetter.events.NewTweetEvent;
import kwetter.interceptors.TweetInterceptor;
import kwetter.qualifiers.JPAQualifier;
import kwetter.utils.CaseInsensitiveSet;
import kwetter.utils.Constants;
import kwetter.utils.Utilities;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateful;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.mail.Transport;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.rmi.CORBA.Util;


@Startup
@Singleton
public class KwetterService implements Serializable {

    @Inject
    @JPAQualifier
    private UserDAO userDAO;

    @Inject
    @JPAQualifier
    private TweetDAO tweetDao;

    @Resource(name = "mail/kwetter")
    private Session session;


    /**
     * Creates a new user in the DAO
     *
     * @param user User object to persist
     */
    public void create(User user) {
        userDAO.create(user);
    }

    /**
     * Edits the user specified
     *
     * @param user edit
     */
    public void edit(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Authenticates a user, based on username (and password in the future)
     *
     * @param username Username specified
     * @param password password
     * @return TODO: Password authentication
     */
    public User authenticateUser(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }

        String hashedPassword = Utilities.hashPassword(password);
        password = null;

        return userDAO.authenticateUser(username, hashedPassword);
    }

    /**
     * Remove the user specified from the database
     *
     * @param user User to remove
     */
    public void remove(User user) {
        userDAO.remove(user);
    }

    /**
     * Finds all tweets with search text specified
     *
     * @param searchText Tweet contents to search for
     * @return
     */
    public List<Tweet> findTweets(String searchText) {
        return tweetDao.findTweets(searchText);
    }

    /**
     * Find all existing users
     *
     * @return All existing users in a list
     */
    public List<User> findAll() {
        return userDAO.findAll();
    }

    /**
     * Find a user based on the identifier
     *
     * @param id Identifier of the user
     * @return Found user
     */
    public User find(Long id) {
        return userDAO.find(id);
    }

    /**
     * Find a user based on name
     *
     * @param name Username of user
     * @return The user found
     */
    public User find(String name) {
        return userDAO.findByName(name);
    }

    /**
     * Count the number of users
     *
     * @return
     */
    public int count() {
        return userDAO.count();
    }

    /**
     * Get the current trending tweets (hashtags)
     *
     * @return
     */
    public Map<String, Integer> getCurrentTrends() {
        return tweetDao.getCurrentTrends();
    }

    /**
     * Creates the timeline for the user
     * TODO: Use efficient query
     *
     * @param user User ti create tge timeline for
     * @return
     */
    public List<Tweet> getTimeline(User user) {
        List<Tweet> timelineTweets = new ArrayList<Tweet>();
        for (User existingUser : userDAO.findAll()) {
            if (user.getFollowing().contains(existingUser) || user.equals(existingUser)) {
                for (Tweet tweet : existingUser.getTweets()) {
                    timelineTweets.add(tweet);
                }

            }
        }

        List<Tweet> sortedTweets = new ArrayList<Tweet>(timelineTweets);
        Collections.sort(sortedTweets);
        return sortedTweets;

    }

    /**
     * Get the tweets in which the user specified is mentioned
     *
     * @param user user to get mentions from
     * @return
     */
    public List<Tweet> getMentions(User user) {
        return tweetDao.findMentions(user);
    }

    /**
     * Post a new tweet and save it
     *
     * @param postedTweet The tweet posted
     */
    public void postNewTweet(Tweet postedTweet) {
        Pattern mentionPattern = Pattern.compile(Constants.MENTIONS_REGEX, Pattern.CASE_INSENSITIVE);

        Matcher matcher = mentionPattern.matcher(postedTweet.getContent());

        Set<String> mentions = new CaseInsensitiveSet();
        while (matcher.find()) {
            mentions.add(matcher.group());
        }

        //Loop through the mentions
        for (String mention : mentions) {
            User mentionedUser = null;
            mentionedUser = userDAO.findByName(mention.substring(1));

            if (mentionedUser != null) {
                postedTweet.getMentions().add(mentionedUser);
            }
        }

        tweetDao.create(postedTweet);
    }

    public void registerNewUser(String username, String email, String password) throws MessagingException, UnsupportedEncodingException {

        User newUser = userDAO.registerNewUser(username, email, password);

        //Send an activation email
        this.sendActivationEmail(newUser);
    }

    public boolean ActivateUserWithID(String activationID){
        boolean success = false;
        User foundUser = userDAO.findUserByRegistrationID(activationID);
        if(foundUser != null){
            foundUser.setActivationLink(null);
            userDAO.edit(foundUser);
            success = true;
        }

        return success;

    }

    private void sendActivationEmail(User user) {
        try {
            // Create email and headers.
            Message msg = new MimeMessage(session);
            msg.setSubject("Kwetter activation");
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress((user.getEmail()), user.getName()));
            msg.setFrom(new InternetAddress(session.getProperty("mail.from")));
            // Body text.
            String activationlink = "http://localhost:8080/kwetter/activate?id=" + user.getActivationLink();
            msg.setText("Hey, " + user.getName() + ". Please press the following activation link to activate your account " + activationlink);

            Transport.send(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Initializes users and tweets for testing purposes.
     */
    public void initUsers() {

        Role role = new Role("normal");
        Role adminRole = new Role("admin");
        System.out.println("Initializing users");
        User u1 = new User("Niek", "http", "geboren 1");
        User u2 = new User("Frank", "httpF", "geboren 2");
        User u3 = new User("Tom", "httpT", "geboren 3");
        User u4 = new User("Hans", "httpS", "geboren 4");
        userDAO.addFollowing(u1, u2);
        userDAO.addFollowing(u2, u1);
        userDAO.addFollowing(u3, u1);
        userDAO.addFollowing(u4, u1);
        userDAO.addFollowing(u2, u3);

        u1.getRoles().add(role);
        u1.getRoles().add(adminRole);
        u2.getRoles().add(role);
        u3.getRoles().add(role);
        u4.getRoles().add(role);

        userDAO.create(u1);
        userDAO.create(u2);
        userDAO.create(u3);
        userDAO.create(u4);

        Tweet t1 = new Tweet(u1, "Tweet Twoot #Kwette", new Date(), "PC");
        Tweet t2 = new Tweet(u1, "New Flappy bird highscore #HashTag #Highscore", new Date(), "PC");
        Tweet t3 = new Tweet(u1, "Niet", new Date(), "PC");
        Tweet t4 = new Tweet(u1, "Sittin' on a toilet", new Date(), "PC");
        Tweet t5 = new Tweet(u1, "Sittin' on a toilet", new Date(), "PC");
        Tweet t6 = new Tweet(u2, "Sittin' on a toilet", new Date(), "PC");
        Tweet t7 = new Tweet(u1, "Jawel", new Date(), "PC");
        Tweet t8 = new Tweet(u3, "Nee", new Date(), "PC");
        Tweet t9 = new Tweet(u4, "Joooo", new Date(), "PC");
        Tweet t10 = new Tweet(u3, "Tweet", new Date(), "PC");
        Tweet t11 = new Tweet(u4, "Twitter is better", new Date(), "PC");
        Tweet t12 = new Tweet(u1, "Twitter is better", new Date(), "PC");
        Tweet t13 = new Tweet(u2, "Twitter is better than #Kwetter", new Date(), "PC");
        Tweet t14 = new Tweet(u3, "Twitter is better than #Kwetter", new Date(), "PC");
        Tweet t15 = new Tweet(u3, "Twitter is better", new Date(), "PC");
        Tweet t16 = new Tweet(u3, "Twitter is better", new Date(), "PC");
        Tweet t17 = new Tweet(u4, "Twitter is better", new Date(), "PC");
        this.postNewTweet(t1);
        this.postNewTweet(t2);
        this.postNewTweet(t3);
        this.postNewTweet(t4);
        this.postNewTweet(t5);
        this.postNewTweet(t6);
        this.postNewTweet(t7);
        this.postNewTweet(t8);
        this.postNewTweet(t9);
        this.postNewTweet(t10);
        this.postNewTweet(t11);
        this.postNewTweet(t12);
        this.postNewTweet(t13);
        this.postNewTweet(t14);
        this.postNewTweet(t15);
        this.postNewTweet(t16);
        this.postNewTweet(t17);
    }

    /**
     * Executed when an AuthenticationEvent is detected
     *
     * @param event
     */
    public void onAuthenticationEvent(@Observes AuthenticationEvent event) {

        switch (event.getAuthenticationType()) {
            case SIGNIN:
                System.out.println("User: " + event.getUsername() + " Tried authenticating at " + event.getTime().toString());
                break;
            case SIGNOUT:
                System.out.println("User: " + event.getUsername() + " Signed out at " + event.getTime().toString());
                break;
        }
    }


    /**
     * Fired when a followChangeEvent is detected
     *
     * @param event
     */
    public void onFollowChangeEvent(@Observes FollowingChangedEvent event) {
        //If user invoking is already following, remove the users
        if (event.getInvokingUser().getFollowing().contains(event.getTargetedUser())) {
            userDAO.removeFollowing(event.getInvokingUser(), event.getTargetedUser());
            System.out.println(event.getInvokingUser().getName() + " Unfollowed " + event.getTargetedUser().getName());
        }
        //Else add the user
        else {
            userDAO.addFollowing(event.getInvokingUser(), event.getTargetedUser());
            System.out.println(event.getInvokingUser().getName() + " Followed " + event.getTargetedUser().getName());

        }
    }

    /**
     * Executed when a newtweetpost event is detected
     *
     * @param event
     */
    @Interceptors(TweetInterceptor.class)
    public void onNewTweetPostEvent(@Observes NewTweetEvent event) {
        this.postNewTweet(event.getTweet());
        System.out.println("A new tweet was poted at: " + event.getTweet().getDatum().toString());
    }


    public void removeTweet(Tweet tweet)
    {
        tweetDao.remove(tweet);
    }
}