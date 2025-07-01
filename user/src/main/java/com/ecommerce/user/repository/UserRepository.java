package com.ecommerce.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.user.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
        
}
