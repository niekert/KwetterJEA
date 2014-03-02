package kwetter.dao;

import com.google.common.base.Functions;
import com.google.common.collect.Ordering;
import kwetter.domain.Tweet;
import kwetter.domain.User;
import kwetter.utils.CaseInsensitiveSet;
import kwetter.utils.Constants;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Niek on 15/02/14.
 */
@Singleton
public class TweetDAOCollectionImpl implements TweetDAO, Serializable
{
    @Inject
    private UserDAO userDAO;

    private List<Tweet> tweets = new ArrayList<Tweet>();
    private HashMap<String, List<Tweet>> hashtagCollection = new HashMap<String, List<Tweet>>();



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
            User mentionedUser = userDAO.findByName(mention.substring(1));

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
            if(this.hashtagCollection.containsKey(tag)){
                this.hashtagCollection.get(tag).add(tweet);
            } else {
                List<Tweet> tweetList = new ArrayList<Tweet>();
                tweetList.add(tweet);
                this.hashtagCollection.put(tag, tweetList);
            }
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

        Collections.sort(mentionedTweets);
        return mentionedTweets;
    }

    @Override
    public List<Tweet> getTweetsFromHashtag(String tag) {
        return this.hashtagCollection.get(tag);
    }

    @Override
    public Map<String, Integer> getCurrentTrends() {
        HashMap<String, Integer> tagsAndOccurrences = new HashMap<String, Integer>();
        for (Map.Entry<String, List<Tweet>> entry : this.hashtagCollection.entrySet()) {
            tagsAndOccurrences.put(entry.getKey(), entry.getValue().size());
        }

        return tagsAndOccurrences;
    }
}
