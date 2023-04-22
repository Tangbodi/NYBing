package com.example.demo.Controller;

import com.example.demo.DAO.PostDAO;
import com.example.demo.DTO.PostDTO;
import com.example.demo.Entity.Post;
import com.example.demo.Entity.User;
import com.example.demo.Exception.IpException;
import com.example.demo.Exception.NotFoundException;
import com.example.demo.Exception.PostNotFoundException;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Repository.PostViewsCommentRepository;
import com.example.demo.Service.IpService;
import com.example.demo.Service.PostService;
import com.example.demo.Service.UserService;
import com.example.demo.Util.HttpUtils;
import com.example.demo.Util.SessionManagementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
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

    @Autowired
    private IpService ipService;
    @Autowired
    private PostViewsCommentRepository postViewsCommentRepository;

    @GetMapping("/categories/{categoryId}/{postId}")
    public Post findPostByPostId(@PathVariable("categoryId")Integer categoryId,@PathVariable("postId") String postId){

        return postService.findPostByPostId(postId);
    }
    //------------------------------------------------------------------------------------------
    //edit post
    @PostMapping("/categories/{categoryId}/edit")
    public Post writeArticle(HttpServletRequest request, @RequestBody PostDTO postDTO, @PathVariable Integer categoryId) throws IOException {

        if (!this.sessionManagementUtil.doesSessionExist(request))
        {
            postDTO.setCategoryId(categoryId);
            return visitorPost(request,postDTO);
        }
        else{
            postDTO.setCategoryId(categoryId);
            return registerPost(request,postDTO);
        }
    }
    public Post registerPost(HttpServletRequest request,PostDTO postDTO){
        String userName = (String) request.getSession().getAttribute("user");
        User user = userService.getProfileByUserName(userName);
        postDTO.setUserName(userName);
        String ipStr = HttpUtils.getRequestIP(request);
        if(!ipService.isValidInet4Address(ipStr) && !ipService.isValidInet6Address(ipStr)){
            throw new IpException();
        }
        else{
            String[] ip = ipStr.split("\\.");
            if(ipService.isValidInet4Address(ipStr) && ipService.isValidInet6Address(ipStr)){
                Long ipv4 = (Long.valueOf(ip[0])<<24) +(Long.valueOf(ip[1])<<16)+(Long.valueOf(ip[2])<<8)+(Long.valueOf(ip[3]));
                postDTO.setIpvFour(ipv4);
                postDTO.setIpvSix(ip.toString());
            }
            else if(ipService.isValidInet4Address(ipStr)){
                Long ipv4 = (Long.valueOf(ip[0])<<24) +(Long.valueOf(ip[1])<<16)+(Long.valueOf(ip[2])<<8)+(Long.valueOf(ip[3]));
                postDTO.setIpvFour(ipv4);
            }
            else{
                postDTO.setIpvSix(ip.toString());
            }
        }
        return postService.savePost(postDTO,user);
    }
    public Post visitorPost(HttpServletRequest request,PostDTO postDTO){
        String ipStr = HttpUtils.getRequestIP(request);
        postDTO.setUserName("visitor");
        if(!ipService.isValidInet4Address(ipStr) && !ipService.isValidInet6Address(ipStr)){
            throw new IpException();
        }
        else{
            String[] ip = ipStr.split("\\.");
            if(ipService.isValidInet4Address(ipStr) && ipService.isValidInet6Address(ipStr)){
                //convert ip from String to Long
                Long ipv4 = (Long.valueOf(ip[0])<<24) +(Long.valueOf(ip[1])<<16)+(Long.valueOf(ip[2])<<8)+(Long.valueOf(ip[3]));
                postDTO.setIpvFour(ipv4);
                postDTO.setIpvSix(ip.toString());
            }
            else if(ipService.isValidInet4Address(ipStr)){
                Long ipv4 = (Long.valueOf(ip[0])<<24) +(Long.valueOf(ip[1])<<16)+(Long.valueOf(ip[2])<<8)+(Long.valueOf(ip[3]));
                postDTO.setIpvFour(ipv4);
            }
            else{
                postDTO.setIpvSix(ip.toString());
            }
        }
        return postService.savePost(postDTO,null);
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
