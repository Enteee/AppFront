package com.appfront.server.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.appfront.server.resources.Tag;

/**
 * The access interface for the es tag repository.
 * 
 * @author ente
 */
public interface TagRepository extends PagingAndSortingRepository<Tag, String> {
    
    /**
     * Get a tag
     * 
     * @param tag
     *            the tag to get
     * @return the tag
     */
    Tag findByTag(String tag);
}
