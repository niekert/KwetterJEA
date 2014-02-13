package kwetter.controller;

import kwetter.domain.User;
import kwetter.service.KwetterService;
import kwetter.utils.Constants;

import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by Niek on 12/02/14.
 */
@Named
@SessionScoped
public class SessionBean implements Serializable {

    @Inject
    private KwetterService service;

    private User authenticatedUser = null;


    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }





}
