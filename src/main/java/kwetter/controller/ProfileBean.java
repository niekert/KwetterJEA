package kwetter.controller;

import com.google.common.collect.Lists;
import kwetter.domain.Tweet;
import kwetter.domain.User;
import kwetter.service.KwetterService;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Niek on 13/02/14.
 */
@Named
@javax.faces.view.ViewScoped
public class ProfileBean implements Serializable {
    @Inject
    private SessionBean userSession;


    @Inject
    private KwetterService service;
    private boolean loadTweets = true;
    private User user;
    private List<User> renderedUsers = new ArrayList<User>();
    private String headerText;
    private List<Tweet> tweets = new ArrayList();


    public List<User> getRenderedUsers() {
        return renderedUsers;
    }

    public String getHeaderText() {
        return headerText;
    }

    public List<Tweet> getTweets() {
        return Lists.reverse(tweets);
    }

    public User getUser() {
        return user;
    }

    public boolean isLoadTweets() {
        return loadTweets;
    }

    public void showFollowing() {
        if (this.user == null) {
            return;
        }

        this.renderedUsers = this.user.getFollowing();
        this.headerText = "followings";
        this.loadTweets = false;
    }

    public void showFollowers() {
        if (this.user == null) {
            return;
        }

        this.renderedUsers = this.user.getFollowers();
        this.headerText = "followers";
        this.loadTweets = false;
    }

    public void showTweets() {
        this.loadTweets = true;
    }

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();

        String requestedUserName = context.getExternalContext().getRequestParameterMap().get("username");
        if (requestedUserName.isEmpty()) {
            user = userSession.getAuthenticatedUser();
        } else {
            user = service.find(requestedUserName);
        }

        if (user != null) {
            tweets = user.getTweets();
        }
    }
}
