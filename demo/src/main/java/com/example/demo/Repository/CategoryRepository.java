package com.example.demo.Repository;

import com.example.demo.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAll();

    @Query("SELECT a.id, b.categoryName, a.id, a.title FROM Article a\n" +
            "LEFT JOIN Category b\n" +
            "ON a.id = b.id")
     List<Category> categoryTopFivePost();
}
