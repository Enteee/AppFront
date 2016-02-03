package com.appfront.server.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.appfront.server.resources.Tag;

/**
 * The access interface for the es tag repository.
 * 
 * @author ente
 */
<<<<<<< HEAD
public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {
=======
public interface TagRepository extends PagingAndSortingRepository<Tag, String> {
    
    /**
     * Get a tag
     * 
     * @param tag
     *            the tag to get
     * @return the tag
     */
    Tag findByTag(String tag);
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
}
