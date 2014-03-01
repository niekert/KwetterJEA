package kwetter.events;

import kwetter.domain.Tweet;

/**
 * Created by Niek on 1-3-14.
 */
public class NewTweetEvent {
    private final Tweet tweet;

    public NewTweetEvent(Tweet tweet)
    {
        this.tweet = tweet;
    }

    public Tweet getTweet() {
        return tweet;
    }
}
