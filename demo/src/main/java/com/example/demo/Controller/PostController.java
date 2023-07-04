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

    @GetMapping("/categories/{categoryId}/{postId}")
    public ResponseEntity<ApiResponse<PostWithCommentDTO>> findPostAndCommentByPostId(HttpServletRequest request, @PathVariable("categoryId")Integer categoryId, @PathVariable("postId") String postId){
        PostId id = new PostId();
        id.setSubCategoryId(categoryId);
        id.setPostId(postId);
//        Optional<Post> post = postService.findPostById(postId);
        Post post = postRepository.findById(id).orElse(null);
        if(post==null){
            ApiResponse errorResponse = ApiResponse.error(404 , "Post Not Found", "Not Found");
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
    @PostMapping(value = "/categories/{categoryId}/edit",produces = {"application/json;charset=UTF-8", "text/html;charset=UTF-8"})
    public ResponseEntity editPost(HttpServletRequest request, @RequestBody PostDTO postDTO, @PathVariable Integer categoryId) throws Exception {
        logger.info("controller postDTO:::"+postDTO.getTextrender());
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
        Integer subCategoryId = categoryId;
        postDTO.setSubCategoryId(categoryId);
        CategoriesSubCategoriesMapId mapId = new CategoriesSubCategoriesMapId();
        mapId.setSubCategoryId(subCategoryId);
        //find categoryId by subCategoryId
//        Optional<CategoriesSubCategoriesMap> mappingOptional = categorySubCategoryMapRepository.findById(mapId);
//        Integer categoryid = mappingOptional.get().getId().getCategoryId();
//        postDTO.setCategoryId(categoryid);
        if(postDTO.getUserName()!=null){
            User user = userService.getProfileByUserName(postDTO.getUserName());
            postDTO.setUserName(user.getUserName());
            Post post = postService.savePost(request,postDTO,user);
            if(post!=null) {
                usersPostsMapService.saveUsersPostsMap(user,post);
                subCategoryPostMapService.saveSubCategoryPostMap(post,subCategoryId,null);
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }else{
            postDTO.setUserName("visitor");
            Post post = postService.savePost(request,postDTO,null);
            if(post!=null) {
                subCategoryPostMapService.saveSubCategoryPostMap(post,subCategoryId,null);
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
    //------------------------------------------------------------------------------------------
    //edit comment
    @PostMapping("/categories/{categoryId}/{postId}")
    public ResponseEntity editComment(HttpServletRequest request, @PathVariable("categoryId")Integer categoryId, @PathVariable("postId") String postId, @RequestBody CommentDTO commentDTO) throws Exception {
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

        if(commentDTO.getFromName()!=null){
            User user = userService.getProfileByUserName(commentDTO.getFromName());
            commentDTO.setFromName(user.getUserName());
            commentDTO.setFromId(user.getId());
        }else{
            commentDTO.setFromName("visitor");
        }
        Comment comment = commentService.saveComment(commentDTO, postId);
        if(comment!=null){
            postCommentsViewService.updatePostComments(postId);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
