package com.appfront.server.tag;

import java.util.Map;

import org.springframework.hateoas.ResourceSupport;

/**
 * A tag list.
 * 
 * @author ente
 */
public class TagList extends ResourceSupport {
    
    private final Map<Integer, String> tags;
    
    /**
     * Constructor of TagList.
     * 
     * @param setTags
     *            the tags to hold
     */
    public TagList(final Map<Integer, String> setTags) {
        tags = setTags;
    }
    
    /**
     * Get the tags
     * 
     * @return tags
     */
    public Map<Integer, String> getTags() {
        return tags;
    }
}
