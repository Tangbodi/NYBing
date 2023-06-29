package com.example.demo.Controller;

import com.example.demo.DTO.PostDTO;
import com.example.demo.DTO.PostWithCommentDTO;
import com.example.demo.Entity.*;
import com.example.demo.Exception.IpException;

import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.PostCommentsRepository;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Service.*;
import com.example.demo.Util.ApiResponse;
import com.example.demo.Util.HttpUtils;
import com.example.demo.Util.SessionManagementUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<ApiResponse<PostWithCommentDTO>> findPostAndCommentByPostId(HttpServletRequest request, @PathVariable("categoryId")Integer categoryId, @PathVariable("postId") String postId){
        try {
            postViewService.updatePostViews(postId);
            List<Comment> comments = commentService.findAllCommentsByPostId(postId);
            Post post = postService.getPostData(postId);
            PostWithCommentDTO postWithCommentDTO = new PostWithCommentDTO();
            postWithCommentDTO.setComments(comments);
            postWithCommentDTO.setPost(post);
            ApiResponse<PostWithCommentDTO> apiResponse = ApiResponse.success(postWithCommentDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<PostWithCommentDTO> errorResponse = ApiResponse.error(500, "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    //------------------------------------------------------------------------------------------
    @PostMapping(value = "/categories/{categoryId}/edit",produces = {"application/json;charset=UTF-8", "text/html;charset=UTF-8"})
    public ResponseEntity<ApiResponse<Post>> editPost(HttpServletRequest request, @RequestBody PostDTO postDTO, @PathVariable Integer categoryId) throws Exception {
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
            try{
                User user = userService.getProfileByUserName(postDTO.getUserName());
                postDTO.setUserName(user.getUserName());
                Post post = postService.settingPost(request,postDTO,user);
                ApiResponse<Post> apiResponse = ApiResponse.success(post);
                return ResponseEntity.ok(apiResponse);
            }catch (Exception e){
                ApiResponse<Post> errorResponse = ApiResponse.error(500, "Internal Server Error", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }

        }
        else{
            try{
                postDTO.setUserName("visitor");
                Post post = postService.settingPost(request,postDTO,null);
                ApiResponse<Post> apiResponse = ApiResponse.success(post);
                return ResponseEntity.ok(apiResponse);
            }catch (Exception e){
                ApiResponse<Post> errorResponse = ApiResponse.error(500, "Internal Server Error", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }
    }
    //------------------------------------------------------------------------------------------
    //edit comment
    @PostMapping("/categories/{categoryId}/{postId}")
    public ResponseEntity<ApiResponse<Comment>> editComment(HttpServletRequest request, @PathVariable("categoryId")Integer categoryId, @PathVariable("postId") String postId, @RequestBody Comment comment) throws Exception {
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
                ApiResponse<Comment> errorResponse = ApiResponse.error(451, "Unavailable For Legal Reasons", "Unavailable For Legal Reasons");
                return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(errorResponse);
            }
        }
        if(comment.getFromName()!=null){
            try{
                User user = userService.getProfileByUserName(comment.getFromName());
                comment.setFromName(user.getUserName());
                comment.setFromId(user.getId());
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
        else{
            comment.setFromName("visitor");
        }

        comment.setCategoryId(categoryId);
        try{
            postCommentService.updatePostComments(postId);
            Comment commentRes = commentService.saveComment(comment,postId);
            ApiResponse<Comment> apiResponse = ApiResponse.success(commentRes);
            return ResponseEntity.ok(apiResponse);
        }catch (Exception e){
            ApiResponse<Comment> errorResponse = ApiResponse.error(500, "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
