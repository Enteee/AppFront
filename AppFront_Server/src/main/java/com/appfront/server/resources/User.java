package com.appfront.server.resources;

import java.math.BigInteger;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Parent;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import com.appfront.server.AppFrontEnvironment;
import com.appfront.server.repositories.UserPositionRepository;
import com.appfront.server.repositories.UserRepository;
import com.appfront.server.tag.TagList;
import com.appfront.server.user.UserNotFoundException;

/**
 * User Pojo
 * 
 * @author ente
 */
@Document(indexName = User.INDEX, type = User.PARENT_TYPE, indexStoreType = "memory", shards = 1, replicas = 0, refreshInterval = "-1")
public class User extends com.appfront.server.resources.Resource {
    
    private UserRepository     userRepository;
    /**
     * Index name
     */
    public static final String INDEX       = "user-userposition";
    /**
     * Name of parent entity
     */
    public static final String PARENT_TYPE = "user-entity";
    /**
     * Name of child entity
     */
    public static final String CHILD_TYPE  = "userposition-entity";
    @Id
    private String             id;
    private final String       secret;
    private UserPosition       newestPosition;
    private TagList            tags;
    private final Date         created;
    
    /**
     * Construct a new user object, and make persistent.
     * 
     * @param userRepository
     *            the user repository
     * @param newestPositionPoint
     *            set newest position of this User
     */
    public User(final UserRepository userRepository, final GeoPoint newestPositionPoint) {
        this.userRepository = userRepository;
        // Generate a new secret identifier
        this.secret = new BigInteger(130, AppFrontEnvironment.RANDOM).toString(32);
        // set initial position
        this.newestPosition = new UserPosition(getId(), newestPositionPoint);
        this.tags = new TagList();
        this.created = new Date();
        // Save this new object to the user repository
        userRepository.save(this);
    }
    
    /**
     * Gets a user identified by id and secret from the user repository
     * 
     * @param userRepository
     *            the user repository
     * @param id
     *            id of the user to get
     * @param secret
     *            secret of the user to get
     */
    public User(final UserRepository userRepository, final String id, final String secret) {
        final User thisUser = userRepository.findByIdAndSecret(id, secret);
        if (thisUser == null) {
            throw new UserNotFoundException();
        }
        // initialize user
        this.id = thisUser.getId();
        this.secret = thisUser.getSecret();
        this.newestPosition = thisUser.getNewestPosition();
        this.tags = thisUser.getTags();
        this.created = thisUser.getCreated();
    }
    
    /**
     * Get the id
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Get the secret identifier for this user.
     * 
     * @return the secret identifier
     */
    public String getSecret() {
        return secret;
    }
    
    /**
     * Get all the known positions of the user
     * 
     * @return the positions
     */
    public UserPosition getNewestPosition() {
        return newestPosition;
    }
    
    /**
     * Update the uers's position
     * 
     * @param newPositionPoint
     *            the new position
     */
    public void setNewestPosition(final GeoPoint newPositionPoint) {
        long deltaTime__S = ((new Date()).getTime() - newestPosition.getTimestamp().getTime()) / 1000;
        // is new position in range?
        if (newestPosition.getDistance(newPositionPoint) / deltaTime__S < AppFrontEnvironment.MAX_USER_SPEED) {
            // this is a valid newest position: update
            newestPosition = new UserPosition(getId(), newPositionPoint);
            // persist
            userRepository.save(this);
        }
    }
    
    /**
     * Get all the known tags of the user
     * 
     * @return the tags
     */
    public TagList getTags() {
        return tags;
    }
    
    /**
     * Set a new tag list for this user
     * 
     * @param tags
     */
    public void setTags(final TagList tags) {
        this.tags = tags;
        // persist
        userRepository.save(this);
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
     * A User position at a given time
     * 
     * @author ente
     */
    @Document(indexName = User.INDEX, type = User.CHILD_TYPE, indexStoreType = "memory", shards = 1, replicas = 0, refreshInterval = "-1")
    public static final class UserPosition {
        
        /**
         * Document id
         */
        @Id
        private String                 id;
        /**
         * The parent identifier of this position.
         */
        @Parent(type = PARENT_TYPE)
        private final String           parentId;
        /**
         * The user position.
         */
        private final GeoPoint         position;
        /**
         * Date of the user position.
         */
        private final Date             timestamp = new Date();
        @Autowired
        private UserPositionRepository userPositionRepository;
        
        /**
         * Construct a new user position, and make persistent.
         * 
         * @param parentId
         *            the user's id this position belongs to
         * @param position
         *            the position to set
         */
        public UserPosition(final String parentId, final GeoPoint position) {
            this.parentId = parentId;
            this.position = position;
            // persist!
            userPositionRepository.save(this);
        }
        
        /**
         * Get the position
         * 
         * @return the position
         */
        public GeoPoint getPosition() {
            return this.position;
        }
        
        /**
         * Get this positions timestamp
         * 
         * @return the timestamp
         */
        public Date getTimestamp() {
            return timestamp;
        }
        
        /**
         * Get the distance between this and the provided position. Using the
         * Haversine formula.
         * 
         * @see http://www.movable-type.co.uk/scripts/latlong.html
         * @param toPoint
         *            destination
         * @return distance between this user position and a fixed point
         */
        public double getDistance(final GeoPoint toPoint) {
            double delta_lat = Math.toRadians(toPoint.getLat() - getPosition().getLat());
            double delta_long = Math.toRadians(toPoint.getLon() - getPosition().getLon());
            double a = Math.sin(delta_lat / 2) * Math.sin(delta_lat / 2) + Math.cos(getPosition().getLat()) * Math.cos(toPoint.getLat()) * Math.sin(delta_long / 2) * Math.sin(delta_long / 2);
            double c = Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return AppFrontEnvironment.EARTH_RADIUS * c;
        }
    }
}
