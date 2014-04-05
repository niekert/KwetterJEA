package kwetter.dto;

/**
 * Created by Niek on 5-4-14.
 */
public class UserFollowingDTO {
    private String username;
    private String web;
    private boolean isFollowing;
    private String url;

    public UserFollowingDTO(){};

    public UserFollowingDTO(String username, String web, boolean isFollowing, String url){
        this.username = username;
        this.web = web;
        this.isFollowing = isFollowing;
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
