package kwetter.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Entity
@Table(name="user")
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "web")
    private String web;

    @Column(name = "bio")
    private String bio;

    @JoinTable(name = "followers", joinColumns = {
            @JoinColumn(name = "followers")
    }, inverseJoinColumns = {
            @JoinColumn(name = "following")
    })
    @ManyToMany
    private List<User> following = new ArrayList();


    @ManyToMany(mappedBy = "following")
    private List<User> followers = new ArrayList<User>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Tweet> tweets = new ArrayList();

    public User()
    {
    }

    public User(String naam)
    {
        this.name = naam;
    }

    public User(String naam, String web, String bio)
    {
        this.name = naam;
        this.web = web;
        this.bio = bio;
    }

    public Long getId() {
        return id;
    }

    public List<User> getFollowers()
    {
        return followers;
    }

    public void setFollowers(List<User> followers)
    {
        this.followers = followers;
    }

    public String getBio()
    {
        return bio;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getWeb()
    {
        return web;
    }

    public void setWeb(String web)
    {
        this.web = web;
    }

    public List<User> getFollowing()
    {
        return following;
    }

    public void setFollowing(List<User> following)
    {
        this.following = following;
    }

    public List<Tweet> getTweets()
    {
        List<Tweet> sortedTweets = this.tweets; Collections.sort(sortedTweets);
        return sortedTweets;
    }

    public void setTweets(List<Tweet> tweets)
    {
        this.tweets = tweets;
    }



    public Boolean addTweet(Tweet tweet)
    {
        return this.tweets.add(tweet);
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (name != null ? name.hashCode() + bio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the name fields are not set
        if (!(object instanceof User))
        {
            return false;
        }
        User other = (User) object;
        return this.hashCode() == other.hashCode();
    }

    @Override
    public String toString()
    {
        return "twitter.domain.User[naam=" + name + "]";
    }

}
