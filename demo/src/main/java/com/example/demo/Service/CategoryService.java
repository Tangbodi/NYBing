package com.example.demo.Service;

import com.example.demo.Entity.CategoriesSubCategoriesMap;
import com.example.demo.Entity.Category;
import com.example.demo.Entity.SubCategory;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.CategorySubCategoryMapRepository;
import com.example.demo.Repository.SubCategoryRepository;
import com.example.demo.Util.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {
        private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
        @Autowired
        private CategorySubCategoryMapRepository categorySubCategoryMapRepository;
        @Autowired
        private CategoryRepository categoryRepository;
        @Autowired
        private SubCategoryRepository subCategoryRepository;
        @Autowired
        private RedisCache redisCache;


        public Map<Byte,Map<String, List<SubCategory>>> getAllSubCategories(){
                try{
                        logger.info("Getting all sub categories:::");
                        logger.info("Getting all sub category sub map ids:::");
                        Map<Byte,List<Short>> getAllCategorySubMapIds = getAllCategorySubMapIds();
                        List<SubCategory> subCategories = subCategoryRepository.findAll();
                        logger.info("Create linked list of sub categories:::");
                        LinkedList<SubCategory> subCategoryLinkedList = new LinkedList<>(subCategories);
                        logger.info("Find all categories:::");
                        List<Category> categories = findAllCategories();
                        logger.info("Create map of category name and sub category list:::");
                        Map<Byte,Map<String,List<SubCategory>>> res = new HashMap();
                        for(Byte categoryId : getAllCategorySubMapIds.keySet()){
                                Integer index = categoryId -1;
                                String categoryName = categories.get(index).getCategoryName();
                                List<Short> subCategoryIds = getAllCategorySubMapIds.get(categoryId);

                                Map<String, List<SubCategory>> categoryNameMap = new HashMap<>();
                                List<SubCategory> subCategoryList= new ArrayList<>();

                                for(Short subCategoryId: subCategoryIds){
                                        for(SubCategory subCategory : subCategoryLinkedList){
                                                if(subCategory.getId().equals(subCategoryId)){
                                                        logger.info("subCategoryList.add(subCategory)");
                                                        subCategoryList.add(subCategory);
                                                        logger.info("subCategoryLinkedList.removeFirst()");
                                                        subCategoryLinkedList.removeFirst();
                                                        break;
                                                }
                                        }
                                        logger.info(" categoryNameMap.put(categoryName,subCategoryList)");
                                        categoryNameMap.put(categoryName,subCategoryList);
                                }
                                logger.info("res.put(categoryId,categoryNameMap)");
                                res.put(categoryId,categoryNameMap);
                                updateCategorySubCategoryRedisCache(res);
                        }
                        //every time we get all sub category we update redis cache
                        return res;
                }catch (Exception e) {
                        logger.error("Error while fetching all sub categories:::" + e.getMessage(), e);
                        throw new RuntimeException(e.getMessage(), e);
                }
        }

        public Map<Byte,List<Short>> getAllCategorySubMapIds() {
                try{
                        logger.info("Getting all category sub map ids:::");
                        List<CategoriesSubCategoriesMap>categorySubMaps = categorySubCategoryMapRepository.findAll();
                        Map<Byte,List<Short>> subCategoryWithSameCategoryId = new HashMap<>();
                        for (CategoriesSubCategoriesMap categorySubMap : categorySubMaps) {
                                List<Short> list = new ArrayList<>();
                                Byte categoryId = categorySubMap.getId().getCategoryId(); //1
                                Short subCategoryId = categorySubMap.getId().getSubCategoryid();//101
                                if(!subCategoryWithSameCategoryId.containsKey(categoryId)){
                                        list.add(subCategoryId);
                                        subCategoryWithSameCategoryId.put(categoryId,list);
                                }else{
                                        subCategoryWithSameCategoryId.get(categoryId).add(subCategoryId);
                                }
                        }
                        return subCategoryWithSameCategoryId;//[[1,[101,102]],[2,[201,202]]]
                }catch (Exception e){
                        logger.error("Error while fetching all category sub map ids:::"+e.getMessage(),e);
                        throw new RuntimeException(e.getMessage(),e);
                }

        }
        public List<Category> findAllCategories(){
                logger.info("Find all categories:::");
                try{
                        return categoryRepository.findAll();
                }catch (Exception e){
                        logger.error("Error while fetching all categories:::"+e.getMessage(),e);
                        throw new RuntimeException(e.getMessage(),e);
                }
        }
        public void updateCategorySubCategoryRedisCache(Map<Byte,Map<String, List<SubCategory>>> res){
                redisCache.updateCategorySubCategory(res);
        }

}
