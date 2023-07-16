package com.example.demo.Repository;

import com.example.demo.Entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News,String> {
    @Query(value = "SELECT * FROM news WHERE pubDate >= DATE_SUB(CURDATE(), INTERVAL 3 DAY)",nativeQuery = true)
    List<News> findNewsByPublishDate();
}
