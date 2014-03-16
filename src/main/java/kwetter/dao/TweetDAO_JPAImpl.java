package kwetter.dao;

import kwetter.domain.Tweet;
import kwetter.domain.User;
import kwetter.qualifiers.JPAQualifier;
import kwetter.utils.CaseInsensitiveSet;
import kwetter.utils.Constants;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
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
@Stateless
@JPAQualifier
public class TweetDAO_JPAImpl implements TweetDAO
{

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    private HashMap<String, List<Tweet>> hashtagCollection = new HashMap<String, List<Tweet>>();



    public int count()
    {
        Query query = em.createQuery("select count(tweet) from Tweet tweet");
        return (Integer)query.getSingleResult();
    }

    /**
     * Creates a new tweet and handles the popular tags
     * @param tweet the tweet that needs to be perssised
     */
    public void create(Tweet tweet)
    {

        Pattern mentionPattern = Pattern.compile(Constants.MENTIONS_REGEX, Pattern.CASE_INSENSITIVE);
        Pattern hashtagPattern = Pattern.compile(Constants.HASHTAG_REGEX);

        Matcher matcher = hashtagPattern.matcher(tweet.getContent());


        Set<String> hashtags = new HashSet<String>();
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
        em.merge(tweet.getUser());


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
        Tweet tweet = em.find(Tweet.class, id);

        return tweet;
    }

    /**
     * Find tweets based on text
     * @param contains The text the tweet should contain
     * @return
     */
    public List<Tweet> findTweets(String contains)
    {
        Query q = em.createQuery("select tweet from Tweet tweet where tweet.content LIKE :contents");
        q.setParameter("contents", "%"+contains+"%");


        return q.getResultList();
    }


    /**
     * Find the tweets in which the user specified is mentioned.
     * @param user user
     * @return
     */
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
        HashMap<String, Integer> resultSet = new HashMap<String, Integer>();

        TagComparator comparer = new TagComparator(this.hashtagCollection);

        TreeMap<String, List<Tweet>> sortedMap = new TreeMap<String, List<Tweet>>(comparer);
        sortedMap.putAll(this.hashtagCollection);


        int i = sortedMap.size();
        if(i > 10){
            i = 10;
        }
        for (Map.Entry<String, List<Tweet>> entry : sortedMap.entrySet()) {
            if(i == 0){
                break;
            }
            resultSet.put(entry.getKey(), i);
            i--;
        }

        return resultSet;
    }

    class TagComparator implements Comparator<String> {

        Map<String, List<Tweet>> base;
        public TagComparator(Map<String, List<Tweet>> base){
            this.base =base;
        }

        @Override
        public int compare(String o1, String o2) {
            if(base.get(o1).size() >= base.get(o2).size()){
                return -1;
            } else {
                return 1;
            }
        }
    }
}
