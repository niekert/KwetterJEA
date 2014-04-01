package kwetter.domain;

import javax.persistence.*;

/**
 * Created by Niek on 22/03/14.
 */
@Entity(name = "PermissionRole")
public class Role {

    @Id @Column(name = "name")
    private String name;

    public Role(){}

    public Role(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
