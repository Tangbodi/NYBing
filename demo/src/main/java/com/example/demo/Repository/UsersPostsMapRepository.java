package com.example.demo.Repository;

import com.example.demo.Entity.UsersPostsMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersPostsMapRepository extends JpaRepository<UsersPostsMap,String> {

}
