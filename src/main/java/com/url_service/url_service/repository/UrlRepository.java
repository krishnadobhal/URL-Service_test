package com.url_service.url_service.repository;

import com.url_service.url_service.models.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url, Integer> {
}
