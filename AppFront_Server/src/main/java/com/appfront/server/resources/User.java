package com.appfront.server.resources;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.appfront.server.AppFrontEnvironment;
import com.appfront.server.user.UserPosition;

/**
 * User Pojo
 * 
 * @author ente
 */
@Document(indexName = "appfront", type = "user")
public class User {
    
    @Id
    private String                        id;
    private UserPosition                  newestPosition;
    private final ArrayList<UserPosition> positions = new ArrayList<UserPosition>();
    private final ArrayList<Tag>          tags      = new ArrayList<Tag>();
    private final Date                    created   = new Date();
    private Date                          activated = null;
    
    /**
     * Get the id
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Set the id
     * 
     * @param id
     *            the id
     */
    public void setId(final String id) {
        this.id = id;
    }
    
    /**
     * Update the uers's position
     * 
     * @param newPosition
     *            the new position
     */
    public void updatePosition(final UserPosition newPosition) {
        positions.add(newPosition);
        // did we got a more recent position?
        if (newestPosition.getTimestamp().after(newPosition.getTimestamp()) && newPosition.getTimestamp().before(new Date())) {
            // is new position in range?
            if (newestPosition.getDistance(newPosition) / (newPosition.getTimestamp().getTime() / 1000) < AppFrontEnvironment.MAX_USER_SPEED) {
                // this is a valid newest position: update
                newestPosition = newPosition;
            }
        }
    }
    
    /**
     * Get all the known positions of the user
     * 
     * @return the positions
     */
    public UserPosition getLastPosition() {
        return newestPosition;
    }
    
    /**
     * Get all the known tags of the user
     * 
     * @return the tags
     */
    public ArrayList<Tag> getTags() {
        return tags;
    }
    
    /**
     * Get the time this user was created
     * 
     * @return the time the user was created
     */
    public Date getCreated() {
        return created;
    }
    
    /**
     * Activate the user
     */
    public void activate() {
        this.activated = new Date();
    }
    
    /**
     * Get the time the user was activated
     * 
     * @return time the user was activated
     */
    public Date getActivated() {
        return activated;
    }
}
