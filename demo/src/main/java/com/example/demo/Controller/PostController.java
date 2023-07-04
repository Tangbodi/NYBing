package com.example.demo.Controller;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.DTO.PostDTO;
import com.example.demo.DTO.PostWithCommentDTO;
import com.example.demo.Entity.*;

import com.example.demo.Repository.*;
import com.example.demo.Service.*;
import com.example.demo.Util.ApiResponse;
import com.example.demo.Util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://192.168.1.10:3000/")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private IpService ipService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostCommentsViewService postCommentsViewService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CategorySubCategoryMapRepository categorySubCategoryMapRepository;
    @Autowired
    private UsersPostsMapService usersPostsMapService;
    @Autowired
    private SubCategoryPostMapService subCategoryPostMapService;

    @GetMapping("/categories/{subCategoryId}/{postId}")
    public ResponseEntity<ApiResponse<PostWithCommentDTO>> findPostAndCommentByPostId(@PathVariable("subCategoryId")Integer subCategoryId, @PathVariable("postId") String postId){
        PostId id = new PostId();
        id.setSubCategoryId(subCategoryId);
        id.setPostId(postId);
        Post post = postRepository.findById(id).orElse(null);
        if(post==null){
            ApiResponse errorResponse = ApiResponse.error(404 , "No Such Post", "Not Found");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }else{
            postCommentsViewService.updatePostViews(postId);
            List<Comment> comments = commentService.findAllCommentsByPostId(postId);
            PostWithCommentDTO postWithCommentDTO = new PostWithCommentDTO();
            postWithCommentDTO.setComments(comments);
            postWithCommentDTO.setPost(post);
            ApiResponse<PostWithCommentDTO> apiResponse = ApiResponse.success(postWithCommentDTO);
            return ResponseEntity.ok(apiResponse);
        }
    }
    //------------------------------------------------------------------------------------------
    @PostMapping(value = "/categories/{subCategoryId}/post_edit",produces = {"application/json;charset=UTF-8", "text/html;charset=UTF-8"})
    public ResponseEntity<ApiResponse> editPost(HttpServletRequest request, @RequestBody PostDTO postDTO, @PathVariable Integer subCategoryId) throws Exception {
        if(postDTO.getTitle().isBlank()||postDTO.getTextrender().isBlank()){
            ApiResponse errorResponse = ApiResponse.error(406,"Title Or Content Is Blank","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        String ipStr = HttpUtils.getRequestIP(request);
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
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).build();
        }
        postDTO.setSubCategoryId(subCategoryId);
        if(!postDTO.getUserName().isBlank()){
            User user = userService.getProfileByUserName(postDTO.getUserName());
            postDTO.setUserName(user.getUserName());
            Post post = postService.savePost(request,postDTO,user);
            if(post!=null) {
                if(usersPostsMapService.saveUsersPostsMap(user,post)){
                    logger.info("userPostsMap saved:::");
                }
                if( subCategoryPostMapService.saveSubCategoryPostMap(post,subCategoryId,null)){
                    logger.info("subCategoryPostMap saved:::");
                }
                ApiResponse<Post> apiResponse = ApiResponse.success(post);
                return ResponseEntity.ok(apiResponse);
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }else{
            postDTO.setUserName("visitor");
            Post post = postService.savePost(request,postDTO,null);
            if(post!=null) {
                if(subCategoryPostMapService.saveSubCategoryPostMap(post,subCategoryId,null)){
                    logger.info("subCategoryPostMap saved:::");
                }
                ApiResponse<Post> apiResponse = ApiResponse.success(post);
                return ResponseEntity.ok(apiResponse);
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
    //------------------------------------------------------------------------------------------
    //edit comment
    @PostMapping("/categories/{subCategoryId}/{postId}/comment_edit")
    public ResponseEntity editComment(HttpServletRequest request, @PathVariable("postId") String postId, @RequestBody CommentDTO commentDTO) throws Exception {
        if(commentDTO.getCommentContent().isBlank()){
            ApiResponse errorResponse = ApiResponse.error(406,"Content Of Comment Is Blank","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        String ipStr = HttpUtils.getRequestIP(request);
        if(ipService.isValidInet4Address(ipStr)){
            String[] ip = ipStr.split("\\.");
            logger.info("ipv4:::"+Arrays.toString(ip));
            //convert ip from String to Long
            Long ipv4 = (Long.valueOf(ip[0])<<24) +(Long.valueOf(ip[1])<<16)+(Long.valueOf(ip[2])<<8)+(Long.valueOf(ip[3]));
            commentDTO.setIpvFour(ipv4);
        }else if(ipService.isValidInet6Address(ipStr)){
            String[] ip = ipStr.split(":");
            logger.info("ipv6:::"+Arrays.toString(ip));
            commentDTO.setIpvSix(ip.toString());
        }else{
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).build();
        }
        if(!commentDTO.getFromName().isBlank()){
            User user = userService.getProfileByUserName(commentDTO.getFromName());
            commentDTO.setFromName(user.getUserName());
            commentDTO.setFromId(user.getId());
        }else{
            commentDTO.setFromName("visitor");
        }
        Comment comment = commentService.saveComment(commentDTO, postId);
        if(comment!=null){
            if(postCommentsViewService.updatePostComments(postId)!=null){
                logger.info("Updated post comments successfully:::");
            }
            ApiResponse<Comment> apiResponse = ApiResponse.success(comment);
            return ResponseEntity.ok(apiResponse);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
