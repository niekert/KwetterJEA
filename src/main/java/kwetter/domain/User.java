package kwetter.domain;

import kwetter.utils.Utilities;

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
    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "web")
    private String web;

    @Column(name = "bio")
    private String bio;

    @Column(name = "actionvationLink")
    private String activationLink;

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

    @ManyToMany (cascade = CascadeType.PERSIST)
    private List<Role> roles = new ArrayList<>();

    public User()
    {
    }

    public User(String naam)
    {
        this.name = naam;
    }

    public User(String naam, String web, String bio, String email, String hashedPassword)
    {
        this.name = naam;
        this.web = web;
        this.bio = bio;
        this.email = email;
        this.password = hashedPassword;
        this.activationLink = Utilities.hashPassword(naam + email);
    }

    public User(String naam, String web, String bio){
        this.name = naam;
        this.web = web;
        this.bio = bio;
        this.email = "MockEmail@gmail.com";
        this.password = Utilities.hashPassword("password");
    }


    public List<User> getFollowers()
    {
        return followers;
    }

    public void setFollowers(List<User> followers)
    {
        this.followers = followers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActivationLink() {
        return activationLink;
    }

    public void setActivationLink(String activationLink) {
        this.activationLink = activationLink;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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
