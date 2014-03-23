package kwetter.domain;

import javax.persistence.*;

/**
 * Created by Niek on 22/03/14.
 */
@Entity
public class Role {
    @Id @GeneratedValue
    private int roleID;

    @Enumerated(EnumType.STRING)
    private RoleType type;

    public enum RoleType {
        Administrator,
        Moderator,
        user
    }
}
