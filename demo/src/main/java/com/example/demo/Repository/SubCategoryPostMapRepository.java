package com.example.demo.Repository;

import com.example.demo.Entity.SubCategoryPostMap;
import com.example.demo.Entity.SubCategoryPostMapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryPostMapRepository extends JpaRepository<SubCategoryPostMap, SubCategoryPostMapId> {

}
