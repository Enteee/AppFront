package com.appfront.server.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.appfront.server.resources.User;

/**
 * Access interface for the es user repository.
 * 
 * @author ente
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
}
