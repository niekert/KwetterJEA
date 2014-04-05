package kwetter.controller;

import kwetter.domain.User;
import kwetter.events.AuthenticationEvent;
import kwetter.service.KwetterService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Niek on 12/02/14.
 */
@Named
@ViewScoped
public class SessionBean implements Serializable {

    @Inject
    private KwetterService service;

    @Inject @Any
    private Event<AuthenticationEvent> signoutEvent;

    private User authenticatedUser = null;


    public User getAuthenticatedUser() {

        User foundUser = null;

        FacesContext fc = FacesContext.getCurrentInstance();
        Principal principal = fc.getExternalContext().getUserPrincipal();
        if (principal != null) {
            foundUser = service.find(principal.getName());
        }

        return foundUser;
    }

    public void setAuthenticatedUser(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public void logOut() {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.invalidateSession();
            ec.redirect("/kwetter/");

        } catch (IOException e) {
            Logger.getLogger(SessionBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}