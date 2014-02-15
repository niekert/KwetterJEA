package kwetter.dao;

import kwetter.domain.Tweet;
import kwetter.domain.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Niek on 15/02/14.
 */
public interface TweetDAO
{
    int count();

    void create(Tweet tweet);

    void edit(Tweet tweet);

    void remove(Tweet tweet);

    List<Tweet> findAll();

    Tweet find(Long id);

    List<Tweet> findTweets(String contains);

    List<Tweet> findMentions(User user);

    List<Tweet> getTweetsFromHashtag(String tag);

    Map<String, Integer> getCurrentTrends();

}
