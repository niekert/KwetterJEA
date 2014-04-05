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
@ServerEndpoint("/demoEndpoint")
public class DemoEndpoint
{
    @Inject
    private KwetterService service;

    @OnMessage
    public void onMessage(Session session, String msg)
    {
        String username = session.getUserPrincipal().getName();

        String result = username + " said: " + msg;
        System.out.println(result);
        session.getAsyncRemote().sendText(result);
    }

    @OnOpen
    public void open(Session session, EndpointConfig conf)
    {
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
    public void close(Session session, CloseReason reason)
    {
        String username = session.getUserPrincipal().getName();
        service.removeSession(username);
    }
}
