package com.example.demo.Repository;

import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String>{

    @Query("SELECT c FROM User c WHERE c.email=?1")
    Optional<User> findByEmail(String email);
//    @Query("SELECT c FROM User c WHERE c.userName=?1")
//    public User findByUserName(String userName);
    Optional<User> findByToken(String token);
    Optional<User> findByUserName(String userName);
}
