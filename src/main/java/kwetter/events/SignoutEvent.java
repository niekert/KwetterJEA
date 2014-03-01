package kwetter.events;

import kwetter.domain.User;

import java.util.Date;

/**
 * Created by Niek on 1-3-14.
 */
public class SignoutEvent {
    private final Date time = new Date();
    private final User user;

    public SignoutEvent(User userSigningOut){
        this.user = userSigningOut;
    }

    public Date getTime() {
        return time;
    }

    public User getUser() {
        return user;
    }
}
