package com.example.demo.Repository;

import com.example.demo.Entity.CommentsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsViewRepository extends JpaRepository<CommentsView, Integer> {

}
