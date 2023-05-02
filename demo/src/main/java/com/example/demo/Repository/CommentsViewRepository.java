package com.example.demo.Repository;

import com.example.demo.Entity.CommentsView;
import com.example.demo.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsViewRepository extends JpaRepository<CommentsView, Integer> {
}
