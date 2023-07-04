package com.example.demo.Repository;

import com.example.demo.Entity.CategoriesSubCategoriesMap;
import com.example.demo.Entity.CategoriesSubCategoriesMapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorySubCategoryMapRepository extends JpaRepository<CategoriesSubCategoriesMap, CategoriesSubCategoriesMapId> {

}
