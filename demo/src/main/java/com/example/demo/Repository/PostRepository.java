package com.example.demo.Repository;

import com.example.demo.Entity.Post;
import com.example.demo.Entity.PostId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, PostId> {
    List<Post> findByIdSubCategoryId(Integer subCategoryId);
    Optional<Post> findByIdPostId(String postId);
    @Query(value = "SELECT * FROM master.posts WHERE textrender LIKE %:keyword% OR title LIKE %:keyword% ORDER BY publishAt DESC",nativeQuery = true)
    List<Post> findByKeyword(@Param("keyword") String keyword);

}
