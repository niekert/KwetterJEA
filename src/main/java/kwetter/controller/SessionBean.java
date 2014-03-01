package kwetter.controller;

import kwetter.domain.User;
import kwetter.events.SignoutEvent;
import kwetter.service.KwetterService;
import kwetter.utils.Constants;

import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Niek on 12/02/14.
 */
@Named
@SessionScoped
public class SessionBean implements Serializable {

    @Inject
    private KwetterService service;

    @Inject @Any
    private Event<SignoutEvent> signoutEvent;

    private User authenticatedUser = null;


    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public void logOut() throws IOException{

        this.signoutEvent.fire(new SignoutEvent(this.authenticatedUser));
        this.authenticatedUser = null;

        FacesContext.getCurrentInstance().getExternalContext().redirect("/kwetter/");
    }
}
