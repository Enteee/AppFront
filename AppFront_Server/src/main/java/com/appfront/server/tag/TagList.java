package com.appfront.server.tag;

import java.util.ArrayList;

import org.springframework.hateoas.ResourceSupport;

import com.appfront.server.resources.Tag;

/**
 * A tag list.
 * 
 * @author ente
 */
public class TagList extends ResourceSupport {
    
    private final ArrayList<Tag> tags = new ArrayList<Tag>();
    
    /**
     * Constructor of TagList.
     * 
     * @param tagsIterator
     *            add all tags from iterator to the tags list
     */
    public TagList(final Iterable<Tag> tagsIterator) {
        for (Tag tag : tagsIterator) {
            this.tags.add(tag);
        }
    }
    
    /**
     * Get the tags
     * 
     * @return tags
     */
    public ArrayList<Tag> getTags() {
        return tags;
    }
}
