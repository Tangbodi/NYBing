package com.example.demo.Repository;

import com.example.demo.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface PostViewsCommentRepository extends JpaRepository<Post,Long> {
    List<Post> findAll();

    @Query(value = "SELECT a.categoryid, b.categoryName, a.postId, a.title, a.textrender FROM master.post a\n" +
            "LEFT JOIN master.category b\n" +
            "ON a.categoryid = b.categoryId",nativeQuery = true)
    List<Map<String,Object>> categoryTopFivePost();
}
