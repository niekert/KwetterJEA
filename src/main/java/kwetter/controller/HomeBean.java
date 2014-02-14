package kwetter.controller;

import kwetter.domain.User;
import kwetter.service.KwetterService;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

/**
 * Created by Niek on 14-2-14.
 */
@Named
@ViewScoped
public class HomeBean {

    @Inject
    private KwetterService service;

    @Inject
    private SessionBean session;

    private User homeUser;

    public void validateLogin() throws IOException{
        if(session.getAuthenticatedUser() == null){
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect("login/");
        }
    }

}
