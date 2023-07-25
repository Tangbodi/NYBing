package com.example.demo.Service;

import com.example.demo.Entity.SubCategory;
import com.example.demo.Repository.SubCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SubCategoryService {
    private static final Logger logger = LoggerFactory.getLogger(SubCategoryService.class);
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Transactional
    public SubCategory findSubCategoryById(Short subCategoryId){
        try{
            logger.info("findSubCategoryById:::");
            SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElse(null);
            return subCategory;
        }catch (Exception e){
            logger.error("findSubCategoryById:::Exception:::"+e);
            throw new RuntimeException("Failed to find subCategory by id",e);
        }
    }
}
