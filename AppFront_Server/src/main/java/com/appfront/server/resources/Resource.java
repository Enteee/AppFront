package com.appfront.server.resources;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A simple resource.
 * 
 * @author ente
 * @param <T>
 *            type of the data in the repository
 * @param <R>
 *            Repository type
 */
public abstract class Resource {
    
    private final Logger              logger       = Logger.getLogger(this.getClass());
    /**
     * The object mapper used for object<->json
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * represent resource as json
     */
    @Override
    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            logger.error("Failed writing json", e);
            return "";
        }
    }
}
