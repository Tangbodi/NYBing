package com.example.demo.Repository;

import com.example.demo.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    @Query(value = "SELECT a.postId, a.categoryId, title, publishAt, userName, userId, b.views\n" +
            "FROM master.posts a\n" +
            "LEFT JOIN master.post_views b\n" +
            "ON a.postId = b.postId\n" +
            "where a.categoryId =:categoryId ORDER BY publishAt DESC",nativeQuery = true)
    List<Map<String,Object>> allPostsUnderOneCategory(@Param("categoryId") Integer categoryId);

    @Query(value = "SELECT * FROM master.posts WHERE textrender LIKE %:keyword% OR title LIKE %:keyword% ORDER BY publishAt DESC",nativeQuery = true)
    List<Post> findByKeyword(@Param("keyword") String keyword);


}
