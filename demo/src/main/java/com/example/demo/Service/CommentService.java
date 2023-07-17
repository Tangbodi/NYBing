package com.example.demo.Service;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.Entity.Comment;
import com.example.demo.Repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private IpService ipService;
    @Autowired
    private PostCommentsViewService postCommentsViewService;

//    @Async("MultiExecutor") //Async has to return a Future
    @Transactional(rollbackOn = Exception.class)
    public Comment saveComment(CommentDTO commentDTO, String postId) throws Exception {
            try{
                logger.info("Saving comment:::");
                Comment comment = new Comment();
                comment.setCommentContent(commentDTO.getCommentContent());
                comment.setFromId(commentDTO.getFromId());
                comment.setToId(commentDTO.getToId());
                comment.setFromIpvfour(commentDTO.getIpvFour());
                comment.setFromIpvsix(commentDTO.getIpvSix());
                comment.setFromName(commentDTO.getFromName());
                comment.setToName(commentDTO.getToName());
                comment.setPublishAt(Instant.now());
                comment.setPostId(postId);
                commentRepository.save(comment);
                logger.info("Comment saved successfully:::");
                Thread.sleep(1000);
                return comment;
            }catch (RuntimeException |InterruptedException e){
                logger.error("Error while saving comment:::"+e.getMessage(),e);
                throw new Exception(e.getMessage());
            }
    }
    @Transactional
    public List<Comment> findAllCommentsByPostId(String postId){
        try{
            return commentRepository.findAllByPostId(postId);
        }catch (RuntimeException e){
            logger.error("Error while fetching comments:::"+e.getMessage(),e);
            throw new RuntimeException(e.getMessage());
        }
    }
}

