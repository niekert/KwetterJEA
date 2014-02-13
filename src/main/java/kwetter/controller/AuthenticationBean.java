package kwetter.controller;

import kwetter.domain.User;
import kwetter.service.KwetterService;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Niek on 12/02/14.
 */
@Named
@RequestScoped
public class AuthenticationBean {

    @Inject
    private SessionBean sessionBean;

    @Inject
    private KwetterService service;


    private boolean HasFailures = false;
    private String username;
    private String password;

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

    public String AuthenticatePerson(){
        User authenticatedPerson = service.authenticateUser(this.getUsername(), this.getPassword());

        if(authenticatedPerson == null){
            this.setLoginFailed(true);
            return null;
        }

        sessionBean.setAuthenticatedUser(authenticatedPerson);

        this.setLoginFailed(false);
        return null;

    }
}
