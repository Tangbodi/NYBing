package com.example.demo.Repository;

import com.example.demo.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

//    @Query(value = "SELECT a.postId, a.categoryId, title, publishAt, userName, userId, b.views, b.last_comment\n" +
//            "FROM master.posts a\n" +
//            "LEFT JOIN master.post_views_comments b\n" +
//            "ON a.postId = b.postId\n" +
//            "where a.categoryId =:categoryId",nativeQuery = true)
//     List<Map<String,Object>> allPostsUnderOneCategory(@Param("categoryId") Integer categoryId);
}
