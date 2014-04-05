package kwetter.webapi;

import kwetter.domain.User;
import kwetter.dto.UserFollowingDTO;
import kwetter.service.KwetterService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niek on 5-4-14.
 */
@Path("/followingAPI")
public class FollowingAPI
{

    private static final String BASEURL = "profile/";
    @Inject
    private KwetterService service;

    @GET
    @Path("/search/{username}")
    @Produces("application/json")
    public List<UserFollowingDTO> getUserFollowers(@PathParam("username") String username)
    {

        List<UserFollowingDTO> followingUsers = new ArrayList<>();

        User user = service.find(username);

        if (user != null)
        {
            for (User follower : user.getFollowers())
            {
                boolean alsoFollowing = follower.getFollowers().contains(user);
                followingUsers.add(new UserFollowingDTO(follower.getName(), follower.getWeb(), alsoFollowing, BASEURL + follower.getName()));
            }
        }

        return followingUsers;
    }
}
