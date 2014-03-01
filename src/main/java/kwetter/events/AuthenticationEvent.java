package kwetter.events;

import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import java.util.Date;

/**
 * Created by Niek on 1-3-14.
 */
public class AuthenticationEvent {
    private final String username;
    private final Date time = new Date();

    public AuthenticationEvent(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Date getTime() {
        return time;
    }
}
