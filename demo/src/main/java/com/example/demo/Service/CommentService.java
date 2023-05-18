package com.example.demo.Service;

import com.example.demo.Entity.Comment;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.concurrent.Future;

@Service
@Async
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private IpService ipService;

    @Async("MultiExecutor") //Async has to return a Future
    @Transactional
    public Future<String> saveComment(Comment comment) throws Exception {
        logger.info("Saving comment:::");
            try{
                commentRepository.save(comment);
                Thread.sleep(1000);

            }catch (NullPointerException e){
                e.printStackTrace();
            }
            String res = "getFuture return value, delay" + 1000+"ms";
            return new AsyncResult<String>(res);
    }
}

