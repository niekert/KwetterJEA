package kwetter.utils;

import kwetter.service.KwetterService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

/**
 * Created by Niek on 1-3-14.
 */
@ManagedBean(eager=true)
@ApplicationScoped
public class ApplicationStartup {
    @Inject
    private KwetterService service;

    @PostConstruct
    public void initUsers(){
        service.initUsers();
    }
}
