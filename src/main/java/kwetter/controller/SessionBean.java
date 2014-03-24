package kwetter.controller;

import kwetter.domain.User;
import kwetter.events.AuthenticationEvent;
import kwetter.service.KwetterService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Niek on 12/02/14.
 */
@Named
@SessionScoped
public class SessionBean implements Serializable {

    @Inject
    private KwetterService service;

    @Inject @Any
    private Event<AuthenticationEvent> signoutEvent;

    private User authenticatedUser = null;


    public User getAuthenticatedUser() {
        if(this.authenticatedUser == null) return null;

       return service.find(this.authenticatedUser.getId());
    }

    public void setAuthenticatedUser(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public void logOut() throws IOException {

        this.signoutEvent.fire(new AuthenticationEvent(this.getAuthenticatedUser().getName(), AuthenticationEvent.AuthenticationType.SIGNOUT));
        this.authenticatedUser = null;

        FacesContext.getCurrentInstance().getExternalContext().redirect("/kwetter/");
    }
}