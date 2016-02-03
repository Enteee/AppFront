package com.appfront.server.resources;

<<<<<<< HEAD
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.appfront.server.repositories.TagRepository;

>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
/**
 * A tag POJO
 * 
 * @author ente
 */
<<<<<<< HEAD
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
=======
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
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
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
<<<<<<< HEAD
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
=======
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
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
    }
}
