package com.example.demo.Repository;

import com.example.demo.Entity.CategorySubMap;
import com.example.demo.Entity.CategorySubMapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorySubMapRepository extends JpaRepository<CategorySubMap, CategorySubMapId> {


}
