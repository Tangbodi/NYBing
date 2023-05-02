package com.example.demo.Repository;

import com.example.demo.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
//    @Query(value = "SELECT * FROM master.comments WHERE postId =:postId",nativeQuery = true)
//    List<Map<String, Object>> findAllCommentsByPostId(@Param("postId") String postId);
    //    @Query(value = "SELECT * FROM posts WHERE categoryId =:categoryId",nativeQuery = true)
//    List<Map<String, Object>> findPostsByCategoryId(@Param("categoryId") Integer categoryId);
}
