/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cst8218.turc0160leve0282.Assignment2;

import cst8218.turc0160leve0282.Assignment2.entity.Bouncer;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 *
 * @author Ryan Turcotte
 * 
 * This is the main class that drives program execution. Contains the application's main method. The @Startup annotation instantiates the class upon application deployment, and the @Singleton annotation
 * makes it a single instance belonging to the application. The classes lifespan matches that of the deployment of the application. It gains access to the Bouncer entities via an injection of the BouncerFacade.
 * The @PostConstruct annotation invokes the go() method upon class instantiation. Inside go() a new thread is created which invokes an instance of Runnable as its argument via anonymous inner class definition.
 * Inside runnable, an infinite while loop runs on the thread until the application is shut down. While the loop is running, the logic implements the passing of time on the bouncers by fetching all bouncers in the database,
 * calling their advanceOneFrame() method on each instance, and saving the updated bouncers back into the database for a given number of times per second dictated by the CHANGE_RATE constant.
 */
@Startup 
@Singleton
public class BouncerGame {
    
     public static final int CHANGE_RATE = 1;
    
    @Inject
    private BouncerFacade bouncerFacade;
    
    private List<Bouncer> bouncers;
    
    @PostConstruct
    public void go() {
        new Thread(new Runnable() {
            public void run() {
                
                while (true) {
                    //update all the bouncers and save changes to the database
                    bouncers = bouncerFacade.findAll();
                    for (Bouncer bouncer : bouncers) {
                        bouncer.advanceOneFrame();
                        bouncerFacade.edit(bouncer);
                    }
                    //sleep while waiting to process the next frame of the animation
                    try {
                        // wake up roughly CHANGE_RATE times per second
                        Thread.sleep((long)(1.0/CHANGE_RATE*1000));                               
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }).start();
    }

}