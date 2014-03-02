package kwetter.service;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kwetter.dao.TweetDAO;
import kwetter.dao.UserDAO;
import kwetter.domain.Tweet;
import kwetter.domain.User;
import kwetter.events.AuthenticationEvent;
import kwetter.events.FollowingChangedEvent;
import kwetter.events.NewTweetEvent;
import kwetter.interceptors.TweetInterceptor;
import kwetter.qualifiers.JPAQualifier;
import kwetter.utils.CaseInsensitiveSet;
import kwetter.utils.Constants;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateful;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.interceptor.Interceptors;


@Startup
@Singleton
@Stateful
public class KwetterService implements Serializable {

    @Inject @JPAQualifier
    private UserDAO userDAO;

    @Inject @JPAQualifier
    private TweetDAO tweetDao;


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


        return userDAO.findByName(username);
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
        return userDAO.findByName(name);
    }

    public int count() {
        return userDAO.count();
    }

    public Map<String, Integer> getCurrentTrends(){
        return tweetDao.getCurrentTrends();
    }

    public List<Tweet> getTimeline(User user){
        List<Tweet> timelineTweets = new ArrayList<Tweet>();
        for (User existingUser : userDAO.findAll())
        {
            if(user.getFollowing().contains(existingUser) || user.equals(existingUser)){
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

    @Interceptors(TweetInterceptor.class)
    public void postNewTweet(Tweet postedTweet){
        Pattern mentionPattern = Pattern.compile(Constants.MENTIONS_REGEX, Pattern.CASE_INSENSITIVE);

        Matcher matcher = mentionPattern.matcher(postedTweet.getContent());

        Set<String> mentions = new CaseInsensitiveSet();
        while(matcher.find()){
            mentions.add(matcher.group());
        }

        //Loop through the mentions
        for (String mention :mentions)
        {
            User mentionedUser = null;
             mentionedUser = userDAO.findByName(mention.substring(1));

            if(mentionedUser != null){
                postedTweet.getMentions().add(mentionedUser);
            }
        }

        tweetDao.create(postedTweet);
    }

    public void initUsers() {

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

        userDAO.create(u1);
        userDAO.create(u2);
        userDAO.create(u3);
        userDAO.create(u4);

        Tweet t1 = new Tweet(u1, "Tweet Twoot #Kwette", new Date(), "PC");
        Tweet t2 = new Tweet(u1, "New Flappy bird highscore #HashTag #Highscore", new Date(), "PC");
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
        Tweet t13 = new Tweet(u2,"Twitter is better than #Kwetter", new Date(), "PC");
        Tweet t14 = new Tweet(u3,"Twitter is better than #Kwetter", new Date(), "PC");
        Tweet t15 = new Tweet(u3,"Twitter is better", new Date(), "PC");
        Tweet t16 = new Tweet(u3,"Twitter is better", new Date(), "PC");
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

    public void onAuthenticationEvent(@Observes AuthenticationEvent event){

        switch (event.getAuthenticationType())
        {
            case SIGNIN:
                System.out.println("User: " + event.getUsername() + " Tried authenticating at " + event.getTime().toString());
                break;
            case SIGNOUT:
                System.out.println("User: " + event.getUsername() + " Signed out at " + event.getTime().toString());
                break;
        }
    }


    public void onFollowChangeEvent(@Observes FollowingChangedEvent event)
    {
        //If user invoking is already following, remove the users
        if(event.getInvokingUser().getFollowing().contains(event.getTargetedUser())){
            userDAO.removeFollowing(event.getInvokingUser(), event.getTargetedUser());
            System.out.println(event.getInvokingUser().getName() + " Unfollowed " + event.getTargetedUser().getName());
        }
        //Else add the user
        else {
            userDAO.addFollowing(event.getInvokingUser(), event.getTargetedUser());
            System.out.println(event.getInvokingUser().getName() + " Followed " + event.getTargetedUser().getName());

        }
    }

    public void onNewTweetPostEvent(@Observes NewTweetEvent event){
        this.postNewTweet(event.getTweet());
        System.out.println("A new tweet was poted at: " + event.getTweet().getDatum().toString());
    }
}