package com.example.demo.Service;

import com.example.demo.Entity.Category;
import com.example.demo.Repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class CategoryService {
        private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
        @Autowired
        private CategoryRepository categoryRepository;
        public List<Category> findAllCategories(){
                logger.info("Getting all category:::");
                try{
                    return categoryRepository.findAll();
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }
                return null;
        }
}
