package kwetter.domain;

import java.io.Serializable;
import java.util.Date;


public class Tweet implements Serializable{
    private static final long serialVersionUID = 2L;
    private String content;
    private Date postDate;
    private String postedFrom;

    public Tweet() {
    }

    public Tweet(String tweet) {
        this.content = tweet;
    }

    public Tweet(String tweet, Date datum, String vanaf) {
        this.content = tweet;
        this.postDate = datum;
        this.postedFrom = vanaf;
    }

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

}
