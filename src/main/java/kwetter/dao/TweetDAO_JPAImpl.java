package kwetter.dao;

import kwetter.domain.Tweet;
import kwetter.domain.User;
import kwetter.utils.CaseInsensitiveSet;
import kwetter.utils.Constants;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Niek on 15/02/14.
 */

@Singleton
public class TweetDAO_JPAImpl
{

    @Inject
    private UserDAO userDAO;

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    private HashMap<String, List<Tweet>> hashtagCollection = new HashMap<String, List<Tweet>>();


    public int count()
    {
        Query query = em.createQuery("select count(tweet) from Tweet tweet");
        return (Integer)query.getSingleResult();
    }

    public void create(Tweet tweet)
    {

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
            if(this.hashtagCollection.containsKey(tag)){
                this.hashtagCollection.get(tag).add(tweet);
            } else {
                List<Tweet> tweetList = new ArrayList<Tweet>();
                tweetList.add(tweet);
                this.hashtagCollection.put(tag, tweetList);
            }
        }

        em.persist(tweet);
    }

    public void edit(Tweet tweet)
    {
        throw new NotImplementedException();
    }

    public void remove(Tweet tweet)
    {
        em.remove(tweet);
    }

    public List<Tweet> findAll()
    {
        Query query = em.createQuery("select tweet from Tweet tweet");

        return query.getResultList();
    }

    public Tweet find(Long id)
    {
        throw new NotImplementedException();
    }

    public List<Tweet> findTweets(String contains)
    {
        Query q = em.createQuery("select tweet from Tweet tweet where tweet.content LIKE :contents");
        q.setParameter("contents", contains);


        return q.getResultList();
    }


    public List<Tweet> findMentions(User user)
    {
        Query q = em.createQuery("select tweet from Tweet tweet join tweet.mentions mentions where mentions.id = :userid");
        q.setParameter("userid", user.getId());

        List<Tweet> mentionedTweets = q.getResultList();

        Collections.sort(mentionedTweets);
        return mentionedTweets;
    }

    public List<Tweet> getTweetsFromHashtag(String tag) {
        return this.hashtagCollection.get(tag);
    }

    public Map<String, Integer> getCurrentTrends() {
        HashMap<String, Integer> tagsAndOccurrences = new HashMap<String, Integer>();
        for (Map.Entry<String, List<Tweet>> entry : this.hashtagCollection.entrySet()) {
            tagsAndOccurrences.put(entry.getKey(), entry.getValue().size());
        }

        return tagsAndOccurrences;
    }
}
