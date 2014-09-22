package com.appfront.server.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.appfront.server.resources.User;

/**
 * Access interface for the es user repository.
 * 
 * @author ente
 */
public interface UserRepository extends PagingAndSortingRepository<User, String> {
    
    /**
     * Finds a user by it's id and secret
     * 
     * @param id
     *            the users id
     * @param secret
     *            the users secret
     * @return the user found
     */
    User findByIdAndSecret(String id, String secret);
}
