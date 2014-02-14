package kwetter.controller;

import kwetter.domain.Tweet;
import kwetter.domain.User;
import kwetter.service.KwetterService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Niek on 13/02/14.
 */
@Named
@RequestScoped
public class ProfileBean implements Serializable
{
    @Inject
    private SessionBean userSession;


    @Inject
    private KwetterService service;


    private User user;
    private List<Tweet> tweets = new ArrayList();

    public List<Tweet> getTweets()
    {
        return tweets;
    }

    public User getUser()
    {
        return user;
    }

    @PostConstruct
    public void init()
    {
        FacesContext context = FacesContext.getCurrentInstance();

        String requestedUserName = context.getExternalContext().getRequestParameterMap().get("username");
        if (requestedUserName.isEmpty())
        {
            user = userSession.getAuthenticatedUser();
        } else
        {
            user = service.find(requestedUserName);
        }

        if (user != null)
        {
            tweets = user.getTweets();
        }
    }
}
