package kwetter.jms;

import kwetter.domain.Tweet;
import kwetter.domain.User;
import kwetter.service.KwetterService;

import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Niek on 06/04/14.
 * © Aidas 2014
 */
@MessageDriven(mappedName = "KwetterGoQueue")
public class KwetterGO implements MessageListener
{

    @Inject
    private KwetterService service;

    @Override
    public void onMessage(Message message)
    {
        try
        {
            String username = message.getStringProperty("username");
            String tweetContent = message.getStringProperty("content");

            User user = service.find(username);
            if (user != null)
            {
                Tweet tweet = new Tweet(user, tweetContent, new Date(), "KwetterGO");
                service.postNewTweet(tweet);
            }
        } catch(JMSException jmsException){
            Logger.getLogger(KwetterGO.class.getName()).log(Level.SEVERE, null, jmsException);
        }
    }
}
