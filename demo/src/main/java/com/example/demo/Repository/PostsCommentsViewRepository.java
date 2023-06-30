package com.example.demo.Repository;

import com.example.demo.Entity.PostsCommentsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsCommentsViewRepository extends JpaRepository<PostsCommentsView,String> {
}
