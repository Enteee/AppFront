package com.appfront.server.user;

import java.util.Date;

import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import com.appfront.server.AppFrontEnvironment;

/**
 * A User position at a given time
 * 
 * @author ente
 */
public final class UserPosition {
    
    /**
     * The user position.
     */
    private final GeoPoint position;
    /**
     * Date of the user position.
     */
    private final Date     timestamp;
    
    /**
     * Construct a new user position.
     * 
     * @param position
     *            the position to set
     * @param timestamp
     *            the timestamp to set
     */
    public UserPosition(final GeoPoint position, final Date timestamp) {
        this.position = position;
        this.timestamp = timestamp;
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
     * Get this position's timestamp
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
     * @param toPosition
     *            the other position
     * @return distance between two UserPositions
     */
    public double getDistance(final UserPosition toPosition) {
        double delta_lat = Math.toRadians(toPosition.getPosition().getLat() - getPosition().getLat());
        double delta_long = Math.toRadians(toPosition.getPosition().getLon() - getPosition().getLon());
        double a = Math.sin(delta_lat / 2) * Math.sin(delta_lat / 2) + Math.cos(getPosition().getLat()) * Math.cos(toPosition.getPosition().getLat()) * Math.sin(delta_long / 2)
                * Math.sin(delta_long / 2);
        double c = Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return AppFrontEnvironment.EARTH_RADIUS * c;
    }
}
