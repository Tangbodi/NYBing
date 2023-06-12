package com.example.demo.Controller;

import com.example.demo.DTO.PostDTO;
import com.example.demo.DTO.PostWithCommentDTO;
import com.example.demo.Entity.*;
import com.example.demo.Exception.IpException;

import com.example.demo.Exception.PostNotFoundException;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.PostCommentsRepository;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Service.*;
import com.example.demo.Util.HttpUtils;
import com.example.demo.Util.SessionManagementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://192.168.1.10:3000/")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private SessionManagementUtil sessionManagementUtil;
    @Autowired
    private IpService ipService;
    @Autowired
    private PostCommentsRepository postCommentsRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostViewService postViewService;
    @Autowired
    private PostCommentService postCommentService;
    @Autowired
    private CommentRepository commentRepository;
    @GetMapping("/categories/{categoryId}/{postId}")
    public PostWithCommentDTO findPostAndCommentByPostId(HttpServletRequest request, @PathVariable("categoryId")Integer categoryId, @PathVariable("postId") String postId){
        postViewService.updatePostViews(postId);
        List<Comment> comments = commentService.findAllCommentsByPostId(postId);
        Post post = postService.getPostData(postId);
        PostWithCommentDTO postWithCommentDTO = new PostWithCommentDTO();
        postWithCommentDTO.setComments(comments);
        postWithCommentDTO.setPost(post);
        return postWithCommentDTO;
    }
    //------------------------------------------------------------------------------------------
    @PostMapping(value = "/categories/{categoryId}/edit",produces = {"application/json;charset=UTF-8", "text/html;charset=UTF-8"})
    public Post editPost(HttpServletRequest request, @RequestBody PostDTO postDTO, @PathVariable Integer categoryId) throws Exception {
        logger.info("controller postDTO:::"+postDTO.getTextrender());
        String ipStr = HttpUtils.getRequestIP(request);
        if(ipService.isValidInet4Address(ipStr) || ipService.isValidInet6Address(ipStr)){
            if(ipService.isValidInet4Address(ipStr)){
                String[] ip = ipStr.split("\\.");
                logger.info("ipv4:::"+ Arrays.toString(ip));
                //convert ip from String to Long
                Long ipv4 = (Long.valueOf(ip[0])<<24) +(Long.valueOf(ip[1])<<16)+(Long.valueOf(ip[2])<<8)+(Long.valueOf(ip[3]));
                postDTO.setIpvFour(ipv4);
            }
            else if(ipService.isValidInet6Address(ipStr)){
                String[] ip = ipStr.split(":");
                logger.info("ipv6:::"+Arrays.toString(ip));
                postDTO.setIpvSix(ip.toString());
            }
            else{
                throw new IpException();
            }
        }
        postDTO.setCategoryId(categoryId);
        if(postDTO.getUserName()!=null){
            User user = userService.getProfileByUserName(postDTO.getUserName());
            postDTO.setUserName(user.getUserName());
            return postService.settingPost(request,postDTO,user);
        }
        else{
            postDTO.setUserName("visitor");
            return postService.settingPost(request,postDTO,null);
        }
    }
    //------------------------------------------------------------------------------------------
    //edit comment
    @PostMapping("/categories/{categoryId}/{postId}")
    public Comment editComment(HttpServletRequest request, @PathVariable("categoryId")Integer categoryId, @PathVariable("postId") String postId, @RequestBody Comment comment) throws Exception {
        String ipStr = HttpUtils.getRequestIP(request);
        if(ipService.isValidInet4Address(ipStr) || ipService.isValidInet6Address(ipStr)){
            if(ipService.isValidInet4Address(ipStr)){
                String[] ip = ipStr.split("\\.");
                logger.info("ipv4:::"+Arrays.toString(ip));
                //convert ip from String to Long
                Long ipv4 = (Long.valueOf(ip[0])<<24) +(Long.valueOf(ip[1])<<16)+(Long.valueOf(ip[2])<<8)+(Long.valueOf(ip[3]));
                comment.setFromIpvfour(ipv4);
            }
            else if(ipService.isValidInet6Address(ipStr)){
                String[] ip = ipStr.split(":");
                logger.info("ipv6:::"+Arrays.toString(ip));
                comment.setFromIpvsix(ip.toString());
            }
            else{
                throw new IpException();
            }
        }
        if(comment.getFromName()!=null){
            User user = userService.getProfileByUserName(comment.getFromName());
            comment.setFromName(user.getUserName());
            comment.setFromId(user.getId());
        }
        else{
            comment.setFromName("visitor");
        }
        comment.setCategoryId(categoryId);

        postCommentService.updatePostComments(postId);
        return commentService.saveComment(comment,postId);
    }
}
