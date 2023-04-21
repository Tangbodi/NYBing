package com.example.demo.Controller;

import com.example.demo.DAO.PostDAO;
import com.example.demo.DTO.PostDTO;
import com.example.demo.Entity.Post;
import com.example.demo.Entity.User;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Service.PostService;
import com.example.demo.Service.UserService;
import com.example.demo.Util.HttpUtils;
import com.example.demo.Util.SessionManagementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://192.168.1.23:3000/")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);


    @Autowired
    private UserService userService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private SessionManagementUtil sessionManagementUtil;
    @Autowired
    private PostDAO postDAO;
    //------------------------------------------------------------------------------------------
    //edit post
    @PostMapping("posts/{categoryId}/sendpost")
    public Post writeArticle(HttpServletRequest request, @RequestBody PostDTO postDTO, @PathVariable Integer categoryId) throws IOException {
        if (!this.sessionManagementUtil.doesSessionExist(request))
        {
            String ip = HttpUtils.getRequestIP(request);

//            logger.info("Please login to access this page::");
//            throw new AuthException();
        }
//        String userName = (String) request.getSession().getAttribute("user");
//        User user = userService.getProfileByUserName(userName);
//        postDTO.setTextjson("aasaaaasa");
        return null;
//        return postRepository.save(post);
    }
    //------------------------------------------------------------------------------------------
    //show all posts under specific category
    @GetMapping("/posts/{categoryId}")
    public List<Map<String, Object>> findAllPostsUnderCategory(@PathVariable Long categoryId){
        //        if (!this.sessionManagementUtil.doesSessionExist(request))
//        {
//            logger.info("Please login to access this page::");
//            throw new AuthException();
//        }
        return postService.findPostsByCategoryId(categoryId);
    }
    //------------------------------------------------------------------------------------------
    @GetMapping("/posts/{categoryId}/{postId}")
    public Optional<Post> findOnePostByPostId(@PathVariable("categoryId")Integer categoryId,@PathVariable("postId") String postId){
        //        if (!this.sessionManagementUtil.doesSessionExist(request))
//        {
//            logger.info("Please login to access this page::");
//            throw new AuthException();
//        }
//        return postRepository.findPostByPostIdAndCategoryId(categoryId,postId);
        return postRepository.findById(postId);
//        return postRepository.findPostByPostIdAndCategoryId(postId1.getCategoryId(),postId1.getPostId());
    }
//    @PostMapping("/images/save")
//    public Image saveImage(HttpServletRequest request, @RequestParam("image") MultipartFile multipartFile) throws IOException {
//
//        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//        Long id = (Long) request.getSession().getAttribute("user");
//        User user = userService.getUserProfileById(id);
//
//        return imageService.saveImage(multipartFile,fileName,user);
//
//        String uploadDir = "user-images/" + newImage.getId();
//
//        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//    }
}
