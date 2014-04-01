package kwetter.controller;


import kwetter.service.KwetterService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Niek on 23/03/14.
 */
@RequestScoped
@Named
public class RegistrationBean {

    @Inject
    private KwetterService service;

    private String registrationError = null;
    private boolean registerSuccess = false;

    private String username;
    private String email;
    private String password;


    //region Properties
    public String getRegistrationError() {
        return registrationError;
    }

    public void setRegistrationError(String registrationError) {
        this.registrationError = registrationError;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRegisterSuccess() {
        return registerSuccess;
    }

    //endregion

    public void Register() {
        if (this.getUsername() == null || this.getUsername().isEmpty()) {
            this.registrationError = "The username field is required";
            return;
        }

        if (this.getPassword() == null || this.getPassword().isEmpty()) {
            this.setRegistrationError("The password field is required");
            return;
        }

        if (this.getEmail() == null || this.getEmail().isEmpty()) {
            this.setRegistrationError("The password field is required");
            return;
        }

        try {
            service.registerNewUser(this.getUsername(), this.getEmail(), this.getPassword());
            this.registerSuccess=true;
        } catch (Exception ex) {
            this.registrationError = ex.getMessage();
            this.registerSuccess=false;
        }

    }
}
