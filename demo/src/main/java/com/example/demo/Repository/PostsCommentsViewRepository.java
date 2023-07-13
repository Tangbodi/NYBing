package com.example.demo.Repository;

import com.example.demo.Entity.PostsCommentsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PostsCommentsViewRepository extends JpaRepository<PostsCommentsView,String> {
    @Query(value = "SELECT * FROM posts_comments_views  WHERE subCategoryId = :subCategoryId",nativeQuery = true)
    List<PostsCommentsView> findBySubCategoryId(@Param("subCategoryId") Integer subCategoryId);
//    @Query(value = "SELECT a.postId, a.categoryId, title, publishAt, userName, userId, b.views, b.last_comment\n" +
//            "FROM master.posts a\n" +
//            "LEFT JOIN master.post_views_comments b\n" +
//            "ON a.postId = b.postId\n" +
//            "where a.categoryId =:categoryId",nativeQuery = true)
//     List<Map<String,Object>> allPostsUnderOneCategory(@Param("categoryId") Integer categoryId);
    @Query(value = "SELECT posts_comments_views.*, posts.textrender\n" +
            "FROM posts_comments_views\n" +
            "LEFT JOIN posts ON posts_comments_views.postId = posts.postId\n" +
            "WHERE posts.subCategoryId =:subCategoryId",nativeQuery = true)
    List<Map<String,Object>> combineByTextRender(@Param("subCategoryId") Integer subCategoryId);
}
