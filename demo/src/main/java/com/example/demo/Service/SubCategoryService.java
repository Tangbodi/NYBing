package com.example.demo.Service;

import com.example.demo.Entity.SubCategory;
import com.example.demo.Repository.SubCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubCategoryService {
    private static final Logger logger = LoggerFactory.getLogger(SubCategoryService.class);
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    public SubCategory findSubCategoryById(Integer subCategoryId){
        try{
            logger.info("findSubCategoryById:::");
            SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElse(null);
            logger.info("SubCategory found successfully:::");
            return subCategory;
        }catch (Exception e){
            logger.error("findSubCategoryById:::Exception:::"+e);
        }
        return null;
    }

}
