package kwetter.domain;

import kwetter.utils.Utilities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tweet")
public class Tweet implements Serializable, Comparable<Tweet>{
    private static final long serialVersionUID = 2L;

    @Column(name = "id")
    @Id @GeneratedValue
    private Long ID;

    @Column(name = "content")
    private String content;

    @Column(name = "post_date")
    @Temporal( TemporalType.DATE )
    private Date postDate;

    @Column(name = "posted_from")
    private String postedFrom;

    @OneToMany
    private List<User> mentions = new ArrayList<User>();

    @ElementCollection
    private List<String> trends = new ArrayList<>();

    @JoinColumn(name = "user_id")
    @ManyToOne
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

    public Long getID() {
        return ID;
    }

    public User getUser() {
        return user;
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


    public List<String> getTrends()
    {
        return trends;
    }

    public void setTrends(List<String> trends)
    {
        this.trends = trends;
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
