package com.example.demo.Repository;

import com.example.demo.Entity.PostCommentsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsCommentsViewRepository extends JpaRepository<PostCommentsView,String> {
}
