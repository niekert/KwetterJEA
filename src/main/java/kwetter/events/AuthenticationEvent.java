package kwetter.events;

import java.util.Date;

/**
 * Created by Niek on 1-3-14.
 */
public class AuthenticationEvent {
    private final String username;
    private final AuthenticationType authenticationType;
    private final Date time = new Date();

    public AuthenticationEvent(String username, AuthenticationType authenticationType){
        this.username = username;
        this.authenticationType = authenticationType;
    }

    public String getUsername() {
        return username;
    }

    public Date getTime() {
        return time;
    }

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public enum AuthenticationType {
        SIGNIN, SIGNOUT
    }
}

