package kwetter.dto;

import kwetter.domain.Tweet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by Niek on 05/04/14.
 * © Aidas 2014
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class TweetDTO
{
    @XmlAttribute(required = true)
    private String content;

    @XmlAttribute(required = true)
    private Date posted;

    @XmlAttribute(required = true)
    private String username;

    @XmlAttribute(required = true)
    private String from;

    public TweetDTO(){};

    public TweetDTO(Tweet tweet)
    {
        this.content = tweet.getContent();
        this.posted = tweet.getDatum();
        this.username = tweet.getUser().getName();
        this.from = tweet.getVanaf();
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Date getPosted()
    {
        return posted;
    }

    public void setPosted(Date posted)
    {
        this.posted = posted;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }
}
