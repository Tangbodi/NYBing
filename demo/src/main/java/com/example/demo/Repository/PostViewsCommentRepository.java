package com.example.demo.Repository;

import com.example.demo.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostViewsCommentRepository extends JpaRepository<Post,Long> {
}
