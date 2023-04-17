package com.example.demo.Controller;

import com.example.demo.Repository.PostRepository;
import com.example.demo.Service.UserService;
import com.example.demo.Util.SessionManagementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);


    @Autowired
    private UserService userService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SessionManagementUtil sessionManagementUtil;
    //------------------------------------------------------------------------------------------
    //edit post
//    @PostMapping("post/sendpost")
//    public Post writeArticle(HttpServletRequest request, @RequestBody Post post) throws IOException {
//        if (!this.sessionManagementUtil.doesSessionExist(request))
//        {
//            logger.info("Please login to access this page::");
//            throw new AuthException();
//        }
//        String userName = (String) request.getSession().getAttribute("user");
//        User user = userService.getProfileByUserName(userName);
//        return postService.saveArticle(post,user);
//    }
    //------------------------------------------------------------------------------------------
    //show post page
//    @GetMapping("/article/{articleId}")
//    public Post getArticle(@PathVariable Long articleId){
//
//        return postService.findArticleById(articleId);
//    }


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
