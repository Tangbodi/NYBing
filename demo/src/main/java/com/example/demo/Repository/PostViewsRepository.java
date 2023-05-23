package com.example.demo.Repository;

import com.example.demo.Entity.PostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostViewsRepository extends JpaRepository<PostView,String> {
}
