package com.appfront.server;

/**
 * Environment (public final static) stuff
 * 
 * @author ente
 */
public final class AppFrontEnvironment {
    
    /**
     * The google API public key. For app-appfront.com
     */
    public final static String GOOGLE_API_PUBLIC_KEY  = "6LfcTOsSAAAAALw3bdcwFYsKDAMrs0qse0kEUn1b";
    /**
     * The google API private key. For app-appfront.com
     */
    public final static String GOOGLE_API_PRIVATE_KEY = "6LfcTOsSAAAAAE1f2ASnwbxD5xgM1tQNQVrYhNKp";
    /**
     * Earth radius from http://de.wikipedia.org/wiki/Erdradius
     */
    public final static int    EARTH_RADIUS           = 6371000;
    /**
     * Maximum user speed in m/s
     */
    public final static int    MAX_USER_SPEED         = 30;
    /**
     * Range in which we send other user positions to the user.
     */
    public final static String MAX_REQUEST_DISTANCE   = "500m";
}
