package com.example.demo.Service;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.Entity.Comment;
import com.example.demo.Entity.CommentsView;
import com.example.demo.Entity.Post;
import com.example.demo.Exception.IpException;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Component
@Slf4j
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

//    @Async("MultiExecutor")
    @Transactional
    public void saveComment(HttpServletRequest request, Comment comment) throws Exception {
        logger.info("Saving comment:::");

            String ipStr = HttpUtils.getRequestIP(request);

            if(!ipService.isValidInet4Address(ipStr) && !ipService.isValidInet6Address(ipStr)){
                throw new IpException();
            }
            else{
                String[] ip = ipStr.split("\\.");
                if(ipService.isValidInet4Address(ipStr) && ipService.isValidInet6Address(ipStr)){
                    //convert ip from String to Long
                    Long ipv4 = (Long.valueOf(ip[0])<<24) +(Long.valueOf(ip[1])<<16)+(Long.valueOf(ip[2])<<8)+(Long.valueOf(ip[3]));
                    comment.setFromIpvfour(ipv4);
                    comment.setFromIpvsix(ip.toString());
                }
                else if(ipService.isValidInet4Address(ipStr)){
                    Long ipv4 = (Long.valueOf(ip[0])<<24) +(Long.valueOf(ip[1])<<16)+(Long.valueOf(ip[2])<<8)+(Long.valueOf(ip[3]));
                    comment.setFromIpvfour(ipv4);
                }
                else{
                    comment.setFromIpvsix(ip.toString());
                }
                commentRepository.save(comment);
                Thread.sleep(1000);

            }
    }
}

