package com.example.demo.Util;

import com.example.demo.Entity.News;
import com.example.demo.Entity.SubCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Map;

@Component
public class RedisCache {
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String NEWS_CACHE_KEY = "NEWS";
    private static final String CATEGORY_SUB_CATEGORY_CACHE_KEY = "CATEGORY_SUB_CATEGORY";

    //jedis.sadd is used to add one or more elements to a Redis Set data structure.
    //Redis Sets are an unordered collection of unique elements.
    //When you use jedis.sadd, you can add one or more elements to an existing Set or create a new Set if the key does not exist.
    //---------------------------------------------------------------------------------------------------------------
    //jedis.set is used to set the value of a key in Redis.
    //Redis keys are associated with a value, which can be a string, a number, or other data types.
    //When you use jedis.set, you set the value of a specific key to a specific value.
    //If the key already exists, the existing value is overwritten with the new value. If the key does not exist, a new key-value pair is created.

    public Map<Integer, Map<String, List<SubCategory>>> updateCategorySubCategory(Map<Integer, Map<String, List<SubCategory>>> CategorySubCategory) {
        Jedis jedis = new Jedis("localhost");
        try {
            logger.info("Updating the CATEGORY_SUB_CATEGORY cache:::");
            logger.info("Deleting the old CATEGORY_SUB_CATEGORY cache:::");
            jedis.del(CATEGORY_SUB_CATEGORY_CACHE_KEY);
            logger.info("Setting the new CATEGORY_SUB_CATEGORY cache:::");
            String category_sub_category_json = objectMapper.writeValueAsString(CategorySubCategory);
            jedis.set(CATEGORY_SUB_CATEGORY_CACHE_KEY, category_sub_category_json);
        } catch (JedisException e) {
            logger.error("Jedis Exception: " + e.getMessage());
        } catch (JsonProcessingException e) {
            logger.error("JsonProcessing Exception: " + e.getMessage());
        } finally {
            logger.info("Closing the jedis connection:::");
            jedis.close();
        }
        return CategorySubCategory;
    }

    public List<News> updateNewsCache(List<News> newsList) throws JsonProcessingException {
        Jedis jedis = new Jedis("localhost");
        try{
            logger.info("Updating the News cache:::");
            logger.info("Deleting the old News cache:::");
            jedis.del(NEWS_CACHE_KEY);
            logger.info("Setting the new News cache:::");
            String news_json = objectMapper.writeValueAsString(newsList);
            jedis.set(NEWS_CACHE_KEY, news_json);
        }catch (JedisException e) {
            logger.error("Jedis Exception: " + e.getMessage());
        } finally {
            logger.info("Closing the jedis connection:::");
            jedis.close();
        }
        return newsList;
    }
}
