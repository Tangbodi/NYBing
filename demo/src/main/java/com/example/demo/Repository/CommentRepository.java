package com.example.demo.Repository;

import com.example.demo.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,String> {
    List<Comment> findAllByPostId(String postId);
    //    @Query(value = "SELECT * FROM posts WHERE categoryId =:categoryId",nativeQuery = true)
//    List<Map<String, Object>> findPostsByCategoryId(@Param("categoryId") Integer categoryId);

}
