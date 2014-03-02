package kwetter.events;

import kwetter.domain.User;

/**
 * Created by Niek on 01/03/14.
 */
public class FollowingChangedEvent {
    private final User invokingUser;
    private final User targetedUser;

    public FollowingChangedEvent(User invokingUser, User targetedUser){
        this.invokingUser = invokingUser;
        this.targetedUser = targetedUser;
    }

    public User getInvokingUser() {
        return invokingUser;
    }

    public User getTargetedUser() {
        return targetedUser;
    }
}
