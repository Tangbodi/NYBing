package com.example.demo.Repository;

import com.example.demo.Entity.CommentsView;
import com.example.demo.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface CommentsViewRepository extends JpaRepository<CommentsView, Integer> {

}
