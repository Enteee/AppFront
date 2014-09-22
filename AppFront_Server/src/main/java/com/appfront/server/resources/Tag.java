package com.appfront.server.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.appfront.server.repositories.TagRepository;

/**
 * A tag POJO
 * 
 * @author ente
 */
@Document(indexName = Tag.INDEX, type = Tag.TYPE, indexStoreType = "memory", shards = 1, replicas = 0, refreshInterval = "-1")
public class Tag extends Resource {
    
    /**
     * Index name
     */
    public static final String INDEX = "tag";
    /**
     * Name of parent entity
     */
    public static final String TYPE  = "tag";
    @Autowired
    private TagRepository      tagRepository;
    @Id
    private String             id;
    private final String       tag;
    private Boolean            active;
    
    /**
     * Construct a new tag object, and make persistent.
     * 
     * @param tag
     *            the name of the tag
     */
    public Tag(final String tag) {
        final Tag thisTag = tagRepository.findByTag(tag);
        if (thisTag == null) {
            // new tag
            this.tag = tag;
            this.active = false;
        } else {
            // known tag
            this.tag = thisTag.tag;
            this.active = thisTag.active;
        }
        tagRepository.save(this);
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
     * Get the tag
     * 
     * @return the tag
     */
    public String getTag() {
        return tag;
    }
    
    /**
     * Get a tags active state
     * 
     * @return {@code true} if active, @{code false} otherwise
     */
    public Boolean getActive() {
        return active;
    }
    
    /**
     * Set a tags active state
     * 
     * @param active
     *            {@code true} = tag is active, @{code false} = tag is inactive
     */
    public void setActivate(final Boolean active) {
        this.active = active;
        tagRepository.save(this);
    }
}
