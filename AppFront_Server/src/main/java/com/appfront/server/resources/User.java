package com.appfront.server.resources;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.appfront.server.AppFrontEnvironment;
import com.appfront.server.user.UserPosition;
=======
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
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42

/**
 * User Pojo
 * 
 * @author ente
 */
<<<<<<< HEAD
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
=======
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
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
    }
    
    /**
     * Get all the known positions of the user
     * 
     * @return the positions
     */
<<<<<<< HEAD
    public UserPosition getLastPosition() {
=======
    public UserPosition getNewestPosition() {
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
        return newestPosition;
    }
    
    /**
<<<<<<< HEAD
=======
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
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
     * Get all the known tags of the user
     * 
     * @return the tags
     */
<<<<<<< HEAD
    public ArrayList<Tag> getTags() {
=======
    public TagList getTags() {
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
        return tags;
    }
    
    /**
<<<<<<< HEAD
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
=======
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
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
    }
}
