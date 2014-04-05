package kwetter.webapi;

import kwetter.dao.TweetDAO;
import kwetter.domain.Tweet;
import kwetter.domain.User;
import kwetter.dto.TimeLine;
import kwetter.dto.TweetDTO;
import kwetter.service.KwetterService;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Niek on 05/04/14.
 * © Aidas 2014
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class PostAPI
{
    @Inject
    private KwetterService service;

    @WebMethod(operationName = "postNewTweet")
    public String postNewTweet(String username, String content){
        User userToPost = service.find(username);

        if(userToPost != null)
        {
            Tweet tweet = new Tweet(userToPost, content, new Date(), "KwetterSOAP");
            service.postNewTweet(tweet);
        }

        return content;
    }

    @WebMethod(operationName = "getUsertimeline")
    public TimeLine getUserTimeLine(String username){

        List<TweetDTO> timeline = new ArrayList<>();
        User userToPost = service.find(username);

        if(userToPost != null){
           List<Tweet> tweets =  service.getTimeline(userToPost);

            for (Tweet tweet :tweets)
            {
                 timeline.add(new TweetDTO(tweet));
            }

        }


        TweetDTO[] dtoArray = new TweetDTO[timeline.size()];
        dtoArray = timeline.toArray(dtoArray);
        return new TimeLine(dtoArray);
    }
}
