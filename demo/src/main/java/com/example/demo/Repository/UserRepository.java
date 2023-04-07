package com.example.demo.Repository;

import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    @Query("SELECT c FROM User c WHERE c.email=?1")
    public User findByEmail(String email);
    @Query("SELECT c FROM User c WHERE c.userName=?1")
    public User findByUserName(String userName);
    public User findByResetPasswordToken(String token);
}