package com.example.demo.Controller;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.DTO.PostDTO;
import com.example.demo.Entity.Comment;
import com.example.demo.Entity.Post;
import com.example.demo.Entity.PostWithCommentsResponse;
import com.example.demo.Entity.User;
import com.example.demo.Exception.IpException;

import com.example.demo.Exception.PostNotFoundException;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Repository.PostViewsCommentRepository;
import com.example.demo.Service.*;
import com.example.demo.Util.HttpUtils;
import com.example.demo.Util.SessionManagementUtil;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins ="${ORIGINS}")
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
    private PostViewsCommentRepository postViewsCommentRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostViewsCommentService postViewsCommentService;
    @Autowired
    private CommentRepository commentRepository;
    @GetMapping("/categories/{categoryId}/{postId}")
    public PostWithCommentsResponse findPostAndCommentByPostId(HttpServletRequest request, @PathVariable("categoryId")Integer categoryId, @PathVariable("postId") String postId){
        postViewsCommentService.updatePostViews(postId);
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        Post post = postService.getPostData(postId);

        PostWithCommentsResponse postWithCommentsResponse = new PostWithCommentsResponse();
        postWithCommentsResponse.setPost(post);
        postWithCommentsResponse.setComments(comments);
        return postWithCommentsResponse;
    }
    //------------------------------------------------------------------------------------------
    @PostMapping("/categories/{categoryId}/edit")
    public ResponseEntity editPost(HttpServletRequest request, @RequestBody PostDTO postDTO, @PathVariable Integer categoryId) throws Exception {

        postDTO.setCategoryId(categoryId);
        if(postDTO.getUserName()!=null){
            User user = userService.getProfileByUserName(postDTO.getUserName());
            postDTO.setUserName(user.getUserName());
            postService.settingPost(request,postDTO,user);
        }
        else{
            postDTO.setUserName("visitor");
            postService.settingPost(request,postDTO,null);
        }
        return ResponseEntity.ok().build();
    }
    //------------------------------------------------------------------------------------------
    //edit comment
    @PostMapping("/categories/{categoryId}/{postId}")
    public ResponseEntity editComment(HttpServletRequest request, @PathVariable("categoryId")Integer categoryId, @PathVariable("postId") String postId, @RequestBody Comment comment) throws Exception {

        if(comment.getFromName()!=null){
            User user = userService.getProfileByUserName(comment.getFromName());
            comment.setFromName(user.getUserName());
            comment.setFromId(user.getId());
        }
        else{
            comment.setFromName("visitor");
        }
        comment.setCategoryId(categoryId);
        try{
            Post post = postRepository.findById(postId).orElseThrow(()->new PostNotFoundException(postId));
            comment.setParentId(0);
            comment.setPublishAt(Instant.now());
            comment.setPost(post);
            commentService.saveComment(request,comment);
        }catch (Exception e){
            e.getMessage();
        }
        return ResponseEntity.ok().build();
    }
}
