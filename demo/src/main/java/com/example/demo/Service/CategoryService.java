package com.example.demo.Service;

import com.example.demo.Entity.Category;
import com.example.demo.Entity.CategorySubMap;
import com.example.demo.Entity.CategorySubMapId;
import com.example.demo.Entity.SubCategory;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.CategorySubMapRepository;
import com.example.demo.Repository.SubCategoryRepository;
import com.google.gson.Gson;
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
                Map<Integer,List<Integer>> subCategoryWithSameCategoryId = new HashMap<>();
                for (CategorySubMap categorySubMap : categorySubMaps) {
                        List<Integer> list = new ArrayList<>();
                        Integer categoryId = categorySubMap.getId().getCategoryId(); //1
                        Integer subCategoryId = categorySubMap.getId().getSubCategoryid();//101
                        if(!subCategoryWithSameCategoryId.containsKey(categoryId)){
                                list.add(subCategoryId);
                                subCategoryWithSameCategoryId.put(categoryId,list);
                        }else{
                                subCategoryWithSameCategoryId.get(categoryId).add(subCategoryId);
                        }
                }
                return subCategoryWithSameCategoryId;
        }
        public Map<String,List<String>>  getSubCategory(Map<Integer,List<Integer>> getAllCategorySubMapIds){

                Map<String,List<String>> subCategoryMap = new HashMap<>();
                for (Integer categoryId : getAllCategorySubMapIds.keySet()) {
                        List<String> subCategoryNameList = new ArrayList<>();
                        //find category name by category id
                        Optional<Category> category = categoryRepository.findById(categoryId);
                        String categoryStr = category.get().getCategoryName().toString();
                        List<Integer> subCategoryIds = getAllCategorySubMapIds.get(categoryId);
                        for(Integer subCategoryId : subCategoryIds){
                                Optional<SubCategory> subCategoryOptional = subCategoryRepository.findById(subCategoryId);
                                if (subCategoryOptional.isPresent()) {
                                        SubCategory subCategory = subCategoryOptional.get();
                                        String subCategoryName = subCategory.getSubCategoryName();
                                        subCategoryNameList.add(subCategoryName);
                                } else {
                                        logger.info("Sub-category not found for ID: " + subCategoryId);
                                }

                        }
                        subCategoryMap.put(categoryStr,subCategoryNameList);
                }
                return subCategoryMap;
        }
        public Map<Integer,Map<String, List<SubCategory>>> getAllSubCategory(){

                Map<Integer,List<Integer>> getAllCategorySubMapIds = getAllCategorySubMapIds();
                List<SubCategory> subCategories = subCategoryRepository.findAll();
                LinkedList<SubCategory> subCategoryLinkedList = new LinkedList<>(subCategories);
                Gson gson = new Gson();
                String json = gson.toJson(subCategories);
                logger.info("subCategories:::"+json);
                List<Category> categories = findAllCategories();
                Map<Integer,Map<String,List<SubCategory>>> res = new HashMap();
                for(Integer categoryId : getAllCategorySubMapIds.keySet()){
                        logger.info("categoryId:::"+categoryId);
                        Integer index = categoryId -1;
                        String categoryName = categories.get(index).getCategoryName();
                        logger.info("categoryName:::"+categoryName);
                        List<Integer> subCategoryIds = getAllCategorySubMapIds.get(categoryId);
                        logger.info("subCategoryIds:::"+subCategoryIds);

                        Map<String, List<SubCategory>> categoryNameMap = new HashMap<>();
                        List<SubCategory> subCategoryList= new ArrayList<>();

                        for(Integer subCategoryId: subCategoryIds){
                                logger.info("subCategoryId:::"+subCategoryId);
                                for(SubCategory subCategory : subCategoryLinkedList){
                                        logger.info("subCategory.getId:::"+subCategory.getId());
                                        if(subCategory.getId().equals(subCategoryId)){
                                                subCategoryList.add(subCategory);
                                                logger.info("subCategoryList.add-----------------");
                                                subCategoryLinkedList.removeFirst();
                                                break;
                                        }
                                }
                                categoryNameMap.put(categoryName,subCategoryList);
                        }
                        res.put(categoryId,categoryNameMap);
                }
                return res;
        }
}
