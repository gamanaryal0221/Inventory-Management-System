package com.ivs.user_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ivs.user_service.domain.User;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAllById(Iterable<String> ids);
}