package kwetter.events;

import kwetter.domain.Tweet;

/**
 * Created by Niek on 1-3-14.
 * Event that is created and fired when a user pots a new tweet.
 */
public class NewTweetEvent {
    private Tweet tweet;

    public NewTweetEvent(Tweet tweet)
    {
        this.tweet = tweet;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }
}
