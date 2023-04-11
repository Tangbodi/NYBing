package com.example.demo.Util;

import com.example.demo.Service.AllArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Random;
//@Component
//public class Task {
//
//    public static Random random = new Random();
//    private static final Logger logger = LoggerFactory.getLogger(Task.class);
//    @Async
//    public void doTaskOne() throws Exception{
//        logger.info("Start task one");
//        long start = System.currentTimeMillis();
//        Thread.sleep(random.nextInt(10000));
//        long end = System.currentTimeMillis();
//        logger.info("Task one is done, spent: "+ (end-start)+"ms");
//    }
//    @Async
//    public void doTaskTwo() throws Exception{
//        logger.info("Start task two");
//        long start = System.currentTimeMillis();
//        Thread.sleep(random.nextInt(10000));
//        long end = System.currentTimeMillis();
//        logger.info("Task two is done, spent: "+ (end-start)+"ms");
//    }
//    @Async
//    public void doTaskThree() throws Exception{
//        logger.info("Start task three");
//        long start = System.currentTimeMillis();
//        Thread.sleep(random.nextInt(10000));
//        long end = System.currentTimeMillis();
//        logger.info("Task three is done, spent: "+ (end-start)+"ms");
//    }
//
//}
