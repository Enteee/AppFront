package com.appfront.server.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.appfront.server.resources.Tag;

/**
 * The access interface for the es tag repository.
 * 
 * @author ente
 */
public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {
}
