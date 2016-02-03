package com.appfront.server.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.appfront.server.resources.User;

/**
 * Stores all the history user positions
 * 
 * @author ente
 */
public interface UserPositionRepository extends PagingAndSortingRepository<User.UserPosition, String> {
}
