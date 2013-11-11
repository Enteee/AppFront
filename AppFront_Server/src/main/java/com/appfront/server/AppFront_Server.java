package com.appfront.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * The main class.
 * 
 * @author ente
 */
@ComponentScan
@EnableAutoConfiguration
public class AppFront_Server {
    
    /**
     * Main().
     * 
     * @param args
     *            command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(AppFront_Server.class, args);
    }
}
