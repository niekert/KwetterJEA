package kwetter.controller;

import kwetter.domain.Tweet;
import kwetter.domain.User;
import kwetter.service.KwetterService;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Niek on 14-2-14.
 */
@Named
@ViewScoped
public class HomeBean {

    @Inject
    private KwetterService service;

    @Inject
    private SessionBean session;

    private User homeUser;
    private String searchText = "";
    private String newTweetContents;

    public List<Tweet> getTimeline() {
        return service.getTimeline(homeUser);
    }

    public String getSearchText() {
        return searchText;
    }

    public String getNewTweetContents() {
        return newTweetContents;
    }

    public void setNewTweetContents(String newTweetContents) {
        this.newTweetContents = newTweetContents;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public void validateLogin() throws IOException{
        if(session.getAuthenticatedUser() == null){
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect("login/");
        } else {
            this.homeUser = session.getAuthenticatedUser();
        }
    }

    public Tweet getLatestTweet() {
        return homeUser.getTweets().get(0);
    }

    public void postNewTweet(){

        this.homeUser.addTweet(new Tweet(session.getAuthenticatedUser(), newTweetContents, new Date(), "PC"));
        newTweetContents = "";
    }

    public List<Tweet> getMentions(){
        return service.getMentions(homeUser);
    }

}
