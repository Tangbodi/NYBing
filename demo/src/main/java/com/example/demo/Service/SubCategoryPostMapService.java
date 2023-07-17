package com.example.demo.Service;

import com.example.demo.Entity.Post;
import com.example.demo.Entity.SubCategoryPostMap;
import com.example.demo.Entity.SubCategoryPostMapId;
import com.example.demo.Repository.SubCategoryPostMapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SubCategoryPostMapService {
    private static final Logger logger = LoggerFactory.getLogger(SubCategoryPostMapService.class);
    @Autowired
    private SubCategoryPostMapRepository subCategoryPostMapRepository;
    @Transactional(rollbackOn = Exception.class)
    public boolean saveSubCategoryPostMap(Post post,Integer subCategoryId,String commentId){
        try{
            logger.info("saveSubCategoryPostMap:::");
            SubCategoryPostMap subCategoryPostMap = new SubCategoryPostMap();
            SubCategoryPostMapId subCategoryPostMapId = new SubCategoryPostMapId();
            subCategoryPostMapId.setSubCategoryId(subCategoryId);
            subCategoryPostMapId.setPostId(post.getId().getPostId());
            subCategoryPostMap.setId(subCategoryPostMapId);
            subCategoryPostMap.setCommentId(commentId);
            subCategoryPostMapRepository.save(subCategoryPostMap);
            logger.info("SubCategoryPostMap saved successfully:::");
            return true;
        }catch (Exception e){
            logger.error("saveSubCategoryPostMap:::Exception:::"+e);
        }
        return false;
    }
}
