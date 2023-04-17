package com.example.demo.Repository;

import com.example.demo.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAll();

    @Query(value = "SELECT a.categoryid, b.categoryName, a.postId, a.title, a.textrender FROM master.post a\n" +
            "LEFT JOIN master.category b\n" +
            "ON a.categoryid = b.categoryId",nativeQuery = true)
     List<Map<String,Object>> categoryTopFivePost();
}
