package kwetter.domain;

import kwetter.utils.Utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Tweet implements Serializable, Comparable<Tweet>{
    private static final long serialVersionUID = 2L;
    private String content;
    private Date postDate;
    private String postedFrom;
    private List<User> mentions = new ArrayList<User>();
    private User user;

    public Tweet() {
    }

    public Tweet(User user, String tweet, Date datum, String vanaf) {
        this.content = tweet;
        this.postDate = datum;
        this.postedFrom = vanaf;
        this.user = user;

        user.addTweet(this);
    }

    public List<User> getMentions() { return mentions; }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Date getDatum() {
        return postDate;
    }

    public void setDatum(Date datum) {
        this.postDate = datum;
    }

    public String getVanaf() {
        return postedFrom;
    }

    public void setVanaf(String vanaf) {
        this.postedFrom = vanaf;
    }

    public String returnBullshit()
    {
        return content;
    }

    public String getFriendlyDateFormat()
    {
        return Utilities.getTimeAgo(this.postDate.getTime());
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (content != null ? content.hashCode()+ postDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tweet)) {
            return false;
        }
        Tweet other = (Tweet) object;
        return this.hashCode() == other.hashCode();
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public int compareTo(Tweet tweet)
    {

        if(this == tweet) return 0;

        return tweet.getDatum().compareTo(this.getDatum());
    }
}
