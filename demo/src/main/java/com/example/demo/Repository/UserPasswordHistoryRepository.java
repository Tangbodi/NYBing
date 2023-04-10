package com.example.demo.Repository;

import com.example.demo.Entity.UserPasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordHistoryRepository extends JpaRepository<UserPasswordHistory,Long> {

}
