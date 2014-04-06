package kwetter.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by Niek on 06/04/14.
 * © Aidas 2014
 */
@Named
@SessionScoped
public class OutOfMemory implements Serializable
{

    long[][] ary;

    @PostConstruct
    public void init(){
        this.ary = new long[Integer.MAX_VALUE][Integer.MAX_VALUE];
    }

    public void createOutOfMemory(){
        System.out.println("Crashing the server");
        for (int i = 0; i < 1000; i++)
        {
            this.ary = new long[Integer.MAX_VALUE][Integer.MAX_VALUE];
        }
    }
}
