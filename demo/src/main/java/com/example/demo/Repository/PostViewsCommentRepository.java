package com.example.demo.Repository;

import com.example.demo.Entity.PostViewsComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostViewsCommentRepository extends JpaRepository<PostViewsComment,String> {
}
