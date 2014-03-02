package kwetter.interceptors;

import kwetter.domain.Tweet;
import kwetter.events.NewTweetEvent;

import javax.interceptor.AroundInvoke;
import javax.interceptor.AroundTimeout;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Created by Niek on 01/03/14.
 * Interceptor that modifies lame words used in tweets
 */
public class TweetInterceptor {

    @AroundInvoke
    public Object modifyTweet(InvocationContext context){
        Object[] parameters = context.getParameters();

        NewTweetEvent postedTweetEvent = (NewTweetEvent) parameters[0];

        Tweet postedTweet = postedTweetEvent.getTweet();
        String tweetContents = postedTweet.getContent();

        postedTweet.setContent(tweetContents.replace("vet", "hard").replace("cool", "dik"));

        parameters[0] = postedTweetEvent;

        context.setParameters(parameters);
        try {

            return context.proceed();
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
