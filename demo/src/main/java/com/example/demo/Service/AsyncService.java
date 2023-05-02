package com.example.demo.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {
    private static final Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Async("MultiExecutor")
    public String MultiExecutor(String massage) throws InterruptedException {
        logger.info("MultiExecutor is running:: message={}",massage);
        try{
            Thread.sleep(1000);

        }catch (InterruptedException e){
            e.getMessage();
        }
        return massage;
    }
}
