package com.example.demo.Service;

import com.example.demo.Entity.Category;
import com.example.demo.Entity.CategorySubMap;
import com.example.demo.Entity.CategorySubMapId;
import com.example.demo.Entity.SubCategory;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.CategorySubMapRepository;
import com.example.demo.Repository.SubCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {
        private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
        @Autowired
        private CategorySubMapRepository categorySubMapRepository;
        @Autowired
        private CategoryRepository categoryRepository;
        @Autowired
        private SubCategoryRepository subCategoryRepository;
        public List<Category> findAllCategories(){
                logger.info("Getting all category:::");
                try{
                    return categoryRepository.findAll();
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }
                return null;
        }
        public Map<Integer,List<Integer>> getAllCategorySubMapIds() {
                List<CategorySubMap> categorySubMaps = categorySubMapRepository.findAll();
                //[
                //    {
                //        "id": {
                //            "categoryId": 1,
                //            "subCategoryid": 101
                //        }
                //    },
                //        "id": {
                //            "categoryId": 5,
                //            "subCategoryid": 530
                //        }
                //    }
                //]
                List<CategorySubMapId> categorySubMapIds = new ArrayList<>();
                for (CategorySubMap categorySubMap : categorySubMaps) {
                        categorySubMapIds.add(categorySubMap.getId());
                }
                //[
                //    {
                //        "categoryId": 1,
                //        "subCategoryid": 101
                //    },
                //    {
                //        "categoryId": 1,
                //        "subCategoryid": 102
                //    },
                //]
                Map<Integer,List<Integer>> subCategoryWithSameCategoryId = new HashMap<>();
                //{
                //    "1": [
                //        101,
                //        102
                //    ],
                //    "2": [
                //        200,
                //        201
                //    ]
                //}
                for(CategorySubMapId categorySubMapId : categorySubMapIds){
                        List<Integer> subCategoryId = new ArrayList<>();
                        Integer CategoryId = categorySubMapId.getCategoryId(); //"categoryId": 1
                        Integer SubCategoryId = categorySubMapId.getSubCategoryid();// "subCategoryid": 101
                        subCategoryId.add(SubCategoryId);
                        if(subCategoryWithSameCategoryId.containsKey(CategoryId)){
                               subCategoryWithSameCategoryId.get(CategoryId).add(SubCategoryId);
                        }
                        else{
                                subCategoryWithSameCategoryId.put(CategoryId,subCategoryId);
                        }

                }

//                Map<String,List<String>> res = new HashMap<>();
//                for(int i=0;i<subCategoryWithSameCategoryId.size();i++){
//                        //get category id "1"
//                        Integer key = i+1;
//                        //get categoryName "Community"
//                        String category = categoryRepository.findById(key).toString();
//
//                        //get subCategoryName list "pets,artists,childcare"
//                        List<Integer> list = subCategoryWithSameCategoryId.get(i); //[101,102]
//                        List<String> categoryName = new ArrayList<>();
//                        for(Integer integer:list){
//                                Optional<SubCategory> subCategory = subCategoryRepository.findById(integer);
//                                categoryName.add(subCategory.toString());
//                        }
//                        res.put(category,categoryName);
//
//                }

                return subCategoryWithSameCategoryId;
        }
        public Map<String,List<String>> getSubCategory(Map<Integer,List<Integer>> getAllCategorySubMapIds){

                Map<String,List<String>> subCategoryMap = new HashMap<>();
                for (Integer categoryId : getAllCategorySubMapIds.keySet()) {
                        List<String> subCategoryNameList = new ArrayList<>();
                        Optional<Category> category = categoryRepository.findById(categoryId);
                        String categoryStr = category.get().getCategoryName().toString();
                        List<Integer> subCategoryIds = getAllCategorySubMapIds.get(categoryId);
                        for(Integer subCategoryId : subCategoryIds){
                                Optional<SubCategory> subCategoryOptional = subCategoryRepository.findById(subCategoryId);
                                if (subCategoryOptional.isPresent()) {
                                        SubCategory subCategory = subCategoryOptional.get();
                                        String subCategoryName = subCategory.getSubCategoryname();
                                        subCategoryNameList.add(subCategoryName);
                                } else {
                                        logger.info("Sub-category not found for ID: " + subCategoryId);
                                }

                        }
                        subCategoryMap.put(categoryStr,subCategoryNameList);
                }
                return subCategoryMap;
        }
}
