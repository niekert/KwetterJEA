package kwetter.controller;

import kwetter.domain.Tweet;
import kwetter.service.KwetterService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Niek on 31/03/14.
 * © Aidas 2014
 */
@Named
@ViewScoped
public class AdminBean implements Serializable
{

    @Inject
    private KwetterService service;

    public List<Tweet> getTweets() {
        return service.findTweets("");
    }

    public void removeTweet(Tweet tweet) {
        service.removeTweet(tweet);
    }



}