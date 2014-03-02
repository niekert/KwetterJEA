package kwetter.controller;

import kwetter.domain.Tweet;
import kwetter.domain.User;
import kwetter.events.NewTweetEvent;
import kwetter.service.KwetterService;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudItem;
import org.primefaces.model.tagcloud.TagCloudModel;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Inject @Any
    private Event<NewTweetEvent> newTweetEvent;

    private User homeUser;
    private String searchText = "";
    private String newTweetContents;
    private int tabIndex = 0;
    private boolean searchResults = false;
    private List<Tweet> filteredTweets = new ArrayList<Tweet>();

    public List<Tweet> getTimeline() {
        return service.getTimeline(homeUser);
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public List<Tweet> getFilteredTweets() {
        return filteredTweets;
    }

    public boolean isSearchResults() {
        return searchResults;
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


    public void validateLogin() throws IOException {
        if (session.getAuthenticatedUser() == null) {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect("login/");
        } else {
            this.homeUser = session.getAuthenticatedUser();
        }
    }

    /**
     * Get the latest tweet posted  by the user.
     * @return
     */
    public Tweet getLatestTweet() {
        return homeUser.getTweets().get(0);
    }

    /**
     * Searches tweets based on the text entered
     */
    public void searchTweets() {
        if (searchText.isEmpty()) {
            this.tabIndex = 0;
            this.filteredTweets = new ArrayList<Tweet>();
            this.searchResults = false;
            return;
        } else {
            this.tabIndex = 2;
            this.searchResults = true;
            this.filteredTweets = service.findTweets(searchText);
        }

    }

    /**
     * Fires a new NewTweetEvent when the post new tweet button is clicked.
     */
    public void postNewTweet() {

        if (this.newTweetContents.isEmpty()) return;

        User userPosting = service.find(session.getAuthenticatedUser().getId());

        newTweetEvent.fire(new NewTweetEvent(new Tweet(userPosting, newTweetContents, new Date(), "pc")));
        newTweetContents = "";
    }

    /**
     * Gets all the mentions from this user from the kwetterservice
     * @return
     */
    public List<Tweet> getMentions() {
        return service.getMentions(homeUser);
    }

    /**
     * Creates a tagcloudModel for popular hashtags #YOLO, #SWAG.
     * @return
     */
    public TagCloudModel getModel(){
        TagCloudModel tagModel = new DefaultTagCloudModel();

        Map<String, Integer> trends = service.getCurrentTrends();
        for (Map.Entry<String, Integer> entry : trends.entrySet()) {
            tagModel.addTag(new DefaultTagCloudItem(entry.getKey(), entry.getValue()));
        }

        return tagModel;
    }

    /**
     * Fired when a tag in the TagCloud is clicked, searches for tweets containing that tag.
     * @param event
     */
    public void searchTag(SelectEvent event){
        TagCloudItem item = (TagCloudItem) event.getObject();
        this.searchText = item.getLabel();
        this.searchTweets();
    }

}
