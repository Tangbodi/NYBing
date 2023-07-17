package com.example.demo.Util;

import com.example.demo.Service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CategoryScheduler {
    private static final Logger logger = LoggerFactory.getLogger(CategoryScheduler.class);
    @Autowired
    private CategoryService categoryService;
    @Scheduled(cron = "0 0 8 * * *")// Runs at 8:00 AM every day
    public void CategorySchedulerTask(){
        try{
            logger.info("CategorySchedulerTask started:::");
            categoryService.getAllSubCategories();
            logger.info("CategorySchedulerTask ended:::");
        }catch (Exception e){
            logger.error("CategorySchedulerTask:::Exception:::"+e);
        }
    }
}
