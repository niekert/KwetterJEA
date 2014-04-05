package kwetter.dto;

import javafx.animation.Timeline;

import javax.xml.bind.annotation.*;

/**
 * Created by Niek on 05/04/14.
 * © Aidas 2014
 */
@XmlRootElement(name = "timeline")
@XmlAccessorType(XmlAccessType.FIELD)
public class TimeLine
{
    @XmlElement
    private TweetDTO[] tweets;


    public TimeLine(){}

    public TimeLine(TweetDTO[] tweets){
        this.tweets =tweets;
    }
}
