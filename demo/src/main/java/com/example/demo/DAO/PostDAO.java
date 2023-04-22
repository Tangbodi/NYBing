package com.example.demo.DAO;

import com.example.demo.Entity.Post;
import com.example.demo.Repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Component
public class PostDAO {
    private static final Logger logger = LoggerFactory.getLogger(PostDAO.class);

}
