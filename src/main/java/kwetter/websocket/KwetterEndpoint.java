package kwetter.websocket;

import kwetter.domain.Tweet;
import kwetter.domain.User;
import kwetter.service.KwetterService;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Niek on 05/04/14.
 * © Aidas 2014
 */
@ServerEndpoint("/websocket")
public class KwetterEndpoint
{
    @Inject
    private KwetterService service;

    @OnMessage
    public void onMessage(Session session, String msg){

        String username = session.getUserPrincipal().getName();
        String tweetContent = msg;
        User user = service.find(username);
        Tweet tweet = new Tweet(user, tweetContent, new Date(), "Kwetter push!");
        service.postNewTweet(tweet);
        List<User> followers = user.getFollowers();
        for (User u : followers) {
            Session s = service.getSession(u.getName());
            if (s != null) {
                s.getAsyncRemote().sendText("update");
            }
        }

    }

    @OnOpen
    public void open(Session session, EndpointConfig conf){
        String username = session.getUserPrincipal().getName();

        //Allow batch processing for incredibly famous kwetter pppl
        try
        {
            session.getAsyncRemote().setBatchingAllowed(true);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        service.setSession(username, session);
    }

    @OnClose
    public void close(Session session, CloseReason reason){
            String username = session.getUserPrincipal().getName();
            service.removeSession(username);
    }
}
