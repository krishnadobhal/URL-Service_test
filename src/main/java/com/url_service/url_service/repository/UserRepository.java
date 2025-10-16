package com.url_service.url_service.repository;

import com.url_service.url_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  UserRepository extends JpaRepository<User, Integer> {
}
