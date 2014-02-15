package kwetter.dao;

import kwetter.domain.Tweet;
import kwetter.domain.User;
import kwetter.utils.CaseInsensitiveSet;
import kwetter.utils.Constants;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Niek on 15/02/14.
 */
public class TweetDAOCollectionImpl implements TweetDAO
{
    private UserDAO userDAO;
    private List<Tweet> tweets = new ArrayList<Tweet>();
    private HashMap<String, Tweet> hashtagCollection = new HashMap<String, Tweet>();


    public TweetDAOCollectionImpl(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public int count()
    {
        return tweets.size();
    }

    @Override
    public void create(Tweet tweet)
    {
        tweets.add(tweet);

        Pattern mentionPattern = Pattern.compile(Constants.MENTIONS_REGEX, Pattern.CASE_INSENSITIVE);
        Pattern hashtagPattern = Pattern.compile(Constants.HASHTAG_REGEX);

        Matcher matcher = mentionPattern.matcher(tweet.getContent());


        Set<String> mentions = new CaseInsensitiveSet();
        while(matcher.find()){
            mentions.add(matcher.group());
        }

        //Loop through the mentions
        for (String mention :mentions)
        {
            User mentionedUser = userDAO.find(mention.substring(1));

            if(mentionedUser != null){
                tweet.getMentions().add(mentionedUser);
            }
        }

        Set<String> hashtags = new HashSet<String>();
        matcher = hashtagPattern.matcher(tweet.getContent());
        while(matcher.find()){
            hashtags.add(matcher.group());
        }

        //Iterate the hashtags
        for (String tag : hashtags)
        {
            this.hashtagCollection.put(tag.substring(1), tweet);
        }
    }

    @Override
    public void edit(Tweet tweet)
    {
        throw new NotImplementedException();
    }

    @Override
    public void remove(Tweet tweet)
    {
        tweets.remove(tweet);
    }

    @Override
    public List<Tweet> findAll()
    {
        return this.tweets;
    }

    @Override
    public Tweet find(Long id)
    {
        throw new NotImplementedException();
    }

    @Override
    public List<Tweet> findTweets(String contains)
    {
        List<Tweet> containedTweets = new ArrayList<Tweet>();
        for (Tweet tweet : tweets)
        {
            if(tweet.getContent().toLowerCase().contains(contains.toLowerCase())){
                containedTweets.add(tweet);
            }
        }

        return containedTweets;
    }


    @Override
    public List<Tweet> findMentions(User user)
    {
        List<Tweet> mentionedTweets = new ArrayList<Tweet>();
        for (Tweet tweet : tweets)
        {
            if(tweet.getMentions().contains(user)){
                mentionedTweets.add(tweet);
            }
        }

        return mentionedTweets;
    }
}
