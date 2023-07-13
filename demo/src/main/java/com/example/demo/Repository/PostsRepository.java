package com.example.demo.Repository;

import com.example.demo.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Post, String> {
    @Query(value = "SELECT textrender FROM master.posts WHERE postId IN :postIds",nativeQuery = true)
    List<String> findTextRenderByPostIds(@Param("postIds") List<String> postIds);
    @Query(value = "SELECT * FROM master.posts WHERE textrender LIKE %:keyword% OR title LIKE %:keyword% ORDER BY publishAt DESC",nativeQuery = true)
    List<Post> findByKeyword(@Param("keyword") String keyword);

}
