package kwetter.controller;

import kwetter.events.AuthenticationEvent;
import kwetter.service.KwetterService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by Niek on 12/02/14.
 */
@Named
@RequestScoped
public class AuthenticationBean implements Serializable {

    @Inject
    private SessionBean sessionBean;

    @Inject
    private KwetterService service;

    @Inject
    @Any
    private Event<AuthenticationEvent> authenticationEvent;

    @ManagedProperty(value = "#{param.id}")
    private String activationID;


    private boolean HasFailures = false;
    private String username;
    private String password;
    private boolean isActivated = false;

    public boolean isLoginFailed() {
        return HasFailures;
    }

    public void setLoginFailed(boolean hasFailures) {
        HasFailures = hasFailures;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivated() {
        return isActivated;
    }



    @PostConstruct
    public void init() {
        Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = map.get("id");

        String error = map.get("error");

        if (id != null && !id.isEmpty()) {
            //Activate the user
            this.isActivated = service.ActivateUserWithID(id);
        }

        if(error != null && ! error.isEmpty()){
            this.setLoginFailed(true);
        }
    }
}
