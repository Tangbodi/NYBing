package com.example.demo.Repository;

import com.example.demo.Entity.CatePost;
import com.example.demo.Entity.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
//    @Query(value = "SELECT p.categoryId, c.categoryName, p.postId, p.title " +
//            "FROM ( " +
//            "  SELECT categoryId, postId, title, " +
//            "  ROW_NUMBER() OVER (PARTITION BY categoryId ORDER BY publishAt DESC) as row_num " +
//            "  FROM master.post " +
//            ") p " +
//            "JOIN master.category c ON p.categoryId = c.categoryId " +
//            "WHERE p.row_num <= 5", nativeQuery = true)
    Pageable pageable = PageRequest.of(0,3, Sort.Direction.DESC,"categoryId");
    @Query(value = "SELECT p.categoryId, c.categoryName, p.postId, p.title FROM master.post JOIN master.category c ON p.categoryId = c.categoryId WHERE p.row_num <= 5",
            countQuery = "SELECT categoryId, postId, title,ROW_NUMBER() OVER (PARTITION BY categoryId ORDER BY publishAt DESC) as row_num FROM master.post",
            nativeQuery = true)
    List<Map<String,Object>> findLatestPostsByCategory();
}
