package com.example.demo.Controller;

import com.example.demo.DAO.PostDAO;
import com.example.demo.DTO.CommentDTO;
import com.example.demo.DTO.PostDTO;
import com.example.demo.Entity.Comment;
import com.example.demo.Entity.Post;
import com.example.demo.Entity.User;
import com.example.demo.Exception.IpException;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Repository.PostViewsCommentRepository;
import com.example.demo.Service.CommentService;
import com.example.demo.Service.IpService;
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

    @Autowired
    private CommentService commentService;
    @GetMapping("/categories/{categoryId}/{postId}")
    public Post findPostByPostId(@PathVariable("categoryId")Integer categoryId,@PathVariable("postId") String postId){

        return postService.findPostByPostId(postId);
    }

    //------------------------------------------------------------------------------------------
    //edit comment
    @PostMapping("/categories/{categoryId}/{postId}")
    public Comment editComment(HttpServletRequest request, @PathVariable("categoryId")Integer categoryId, @PathVariable("postId") String postId, @RequestBody CommentDTO commentDTO) throws Exception {

        commentDTO.setCategoryId(categoryId);
        commentDTO.setPostId(postId);
        //if the user is a visitor
        if (!this.sessionManagementUtil.doesSessionExist(request))
        {
            return visitorComment(request,commentDTO);
        }
        else{
            return registerComment(request,commentDTO);
        }
    }
    //------------------------------------------------------------------------------------------
    //edit post
    @PostMapping("/categories/{categoryId}/edit")
    public Post editPost(HttpServletRequest request, @RequestBody PostDTO postDTO, @PathVariable Integer categoryId) throws Exception {

        postDTO.setCategoryId(categoryId);
        //if the user is a visitor
        if (!this.sessionManagementUtil.doesSessionExist(request))
        {
            return visitorPost(request,postDTO);
        }
        else{
            return registerPost(request,postDTO);
        }
    }
    //------------------------------------------------------------------------------------------

    public Post registerPost(HttpServletRequest request,PostDTO postDTO) throws Exception {
        String ipStr = HttpUtils.getRequestIP(request);
        if(!ipService.isValidInet4Address(ipStr) && !ipService.isValidInet6Address(ipStr)){
            throw new IpException();
        }
        else{
            try{
                String userName = (String) request.getSession().getAttribute("user");
                User user = userService.getProfileByUserName(userName);
                postDTO.setUserName(userName);
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
                return postService.savePost(postDTO,user);
            }catch (Exception e){
                e.getMessage();
            }

        }
        throw new Exception();
    }
    //------------------------------------------------------------------------------------------

    public Post visitorPost(HttpServletRequest request,PostDTO postDTO) throws Exception {
        String ipStr = HttpUtils.getRequestIP(request);
        if(!ipService.isValidInet4Address(ipStr) && !ipService.isValidInet6Address(ipStr)){
            throw new IpException();
        }
        else{
            try{
                postDTO.setUserName("visitor");
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
                return postService.savePost(postDTO,null);
            }catch (Exception e){
                e.getMessage();
            }
        }
        throw new Exception();
    }
    //------------------------------------------------------------------------------------------

    public Comment visitorComment(HttpServletRequest request,CommentDTO commentDTO) throws Exception {
        String ipStr = HttpUtils.getRequestIP(request);
        if(!ipService.isValidInet4Address(ipStr) && !ipService.isValidInet6Address(ipStr)){
            throw new IpException();
        }
        else{
            try{
                commentDTO.setFromName("visitor");
                String[] ip = ipStr.split("\\.");
                if(ipService.isValidInet4Address(ipStr) && ipService.isValidInet6Address(ipStr)){
                    //convert ip from String to Long
                    Long ipv4 = (Long.valueOf(ip[0])<<24) +(Long.valueOf(ip[1])<<16)+(Long.valueOf(ip[2])<<8)+(Long.valueOf(ip[3]));
                    commentDTO.setIpvFour(ipv4);
                    commentDTO.setIpvSix(ip.toString());
                }
                else if(ipService.isValidInet4Address(ipStr)){
                    Long ipv4 = (Long.valueOf(ip[0])<<24) +(Long.valueOf(ip[1])<<16)+(Long.valueOf(ip[2])<<8)+(Long.valueOf(ip[3]));
                    commentDTO.setIpvFour(ipv4);
                }
                else{
                    commentDTO.setIpvSix(ip.toString());
                }
                return commentService.saveComment(commentDTO);
            }catch (Exception e){
                e.getMessage();
            }
        }
        throw new Exception();
    }
    //------------------------------------------------------------------------------------------

    public Comment registerComment(HttpServletRequest request,CommentDTO commentDTO) throws Exception {
        String ipStr = HttpUtils.getRequestIP(request);
        if(!ipService.isValidInet4Address(ipStr) && !ipService.isValidInet6Address(ipStr)){
            throw new IpException();
        }
        else{
            try{
                String userName = (String) request.getSession().getAttribute("user");
                commentDTO.setFromName(userName);
                String[] ip = ipStr.split("\\.");
                if(ipService.isValidInet4Address(ipStr) && ipService.isValidInet6Address(ipStr)){
                    Long ipv4 = (Long.valueOf(ip[0])<<24) +(Long.valueOf(ip[1])<<16)+(Long.valueOf(ip[2])<<8)+(Long.valueOf(ip[3]));
                    commentDTO.setIpvFour(ipv4);
                    commentDTO.setIpvSix(ip.toString());
                }
                else if(ipService.isValidInet4Address(ipStr)){
                    Long ipv4 = (Long.valueOf(ip[0])<<24) +(Long.valueOf(ip[1])<<16)+(Long.valueOf(ip[2])<<8)+(Long.valueOf(ip[3]));
                    commentDTO.setIpvFour(ipv4);
                }
                else{
                    commentDTO.setIpvSix(ip.toString());
                }
                return commentService.saveComment(commentDTO);
            }catch (Exception e){
                e.getMessage();
            }
        }
        throw new Exception();
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
