package com.example.demo.Service;

import com.example.demo.Entity.Comment;
import com.example.demo.Entity.Post;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.concurrent.Future;

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
    private PostCommentService postCommentService;

//    @Async("MultiExecutor") //Async has to return a Future
    @Transactional
    public void saveComment(Comment comment,String postId) throws Exception {
        logger.info("Saving comment:::");
            try{
                Post post = postService.findPostById(postId);
                comment.setParentId(0);
                comment.setPublishAt(Instant.now());
                comment.setPost(post);
                commentRepository.save(comment);
                Thread.sleep(1000);
            }catch (RuntimeException |InterruptedException e){
                logger.error(e.getMessage(),e);
            }
//            String res = "getFuture return value, delay" + 1000+"ms";
//            return new AsyncResult<String>(res);
    }
}

