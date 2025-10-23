package com.url_service.url_service.repository;

import com.url_service.url_service.models.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Url u SET u.clickCount = u.clickCount + 1 WHERE u.id = :id")
    void updateClickCountByClickCount(Long id);

    Url findByShortCode(String code);
}
