package com.appfront.server.resources;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A tag POJO
 * 
 * @author ente
 */
@Document(indexName = "appfront", type = "tag")
public class Tag {
    
    @Id
    private String id;
    private String tag;
    
    /**
     * Convert tag to string
     */
    @Override
    public String toString() {
        return String.format("Tag[id=%d, tag='%s']", id, tag);
    }
    
    /**
     * Get the id of this tag.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Set the id of this tag.
     * 
     * @param id
     *            the id to set
     */
    public void setId(final String id) {
        this.id = id;
    }
    
    /**
     * Get the tag
     * 
     * @return the tag
     */
    public String getTag() {
        return tag;
    }
    
    /**
     * Set a new tag
     * 
     * @param tag
     *            the tag to set
     */
    public void setTag(final String tag) {
        this.tag = tag;
    }
}
