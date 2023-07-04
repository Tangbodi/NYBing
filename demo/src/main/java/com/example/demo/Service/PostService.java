package com.example.demo.Service;

import com.example.demo.DAO.PostDAO;
import com.example.demo.DTO.PostDTO;
import com.example.demo.Entity.*;
import com.example.demo.Exception.PostNotFoundException;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.PostRepository;

import com.example.demo.Repository.PostsCommentsViewRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@Service
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
//    private static final String IMAGE_FOLDER ="/opt/tomcat/webapps/IMAGE_FOLDER/";
    private static final String IMAGE_FOLDER_PATH="/opt/tomcat/webapps/IMAGE/";
    private static final String IMAGE_URL="/IMAGE/";
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private IpService ipService;
    @Autowired
    private PostsCommentsViewRepository postsCommentsViewRepository;
    @Autowired
    private PostDAO postDAO;

    public Post savePost(HttpServletRequest request, PostDTO postDTO, User user) throws Exception {
            try{
                logger.info("Setting post:::");
                logger.info("postDTO:::"+postDTO.getTextrender());
                return settingPost(postDTO,user,request);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
            return null;
    }

    public Post settingPost(PostDTO postDTO, User user, HttpServletRequest request) throws IOException {
//        String input = "<p><strong><span style=\"color: #e03e2d;\">adfasdf</span></strong></p>\n" +
//                " <p><img src=\"data:image/png;base64,
//                FIPtyYnMEp4AAAAASUVORK5CYII=\" alt=\"\" /></p>";
        try{
            logger.info("Saving post into database:::");
            boolean finished = false;
            //parsing post content, convert string to html to parse
            Document document = Jsoup.parse(postDTO.getTextrender());
            logger.info("parsing document:::"+document);
            for(Element imageElement:document.select("img")){
                Image image = new Image();
                //get image info from src
                String imageCode = imageElement.attr("src");
                //get image type, for example "data:image/png;base64,"
                //data:image/jpeg;base64, -> jpeg
                String imageType = imageCode.substring(0,imageCode.indexOf(",")+1);
                image.setImageType(imageType);
                logger.info(imageType);
                logger.info("Saving image data into database:::");
                byte[] imageData = getImageData(imageCode);
                //Generate UUID for image
                String imageId = UUID.randomUUID().toString();
                image.setId(imageId);
                // Generate a unique filename
                String filename = imageId+"."+imageType.substring(11,imageType.indexOf(";")); //1efc15fc-fe83-4ef1-9f1a-420ecd408f20.jpeg
//            String filename = imageId+".png";
//                System.out.println(filename);
//            image.setImageName(filename);
                Path imagePath = Paths.get(IMAGE_FOLDER_PATH, filename);
//                System.out.println("imagePath:::"+imagePath);
                image.setImagePath(imagePath.toString());
                FileOutputStream fos = new FileOutputStream(imagePath.toFile());
                fos.write(imageData);
                //http://31.220.21.110:8080
//            String requestPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
                ///api/1efc15fc-fe83-4ef1-9f1a-420ecd408f20.jpeg
                String imageURL = IMAGE_URL+filename;
                image.setImageURL(imageURL);
                Image ima = imageRepository.save(image);
                if(ima!=null){
                    logger.info("Image saved successfully:::");
                    imageElement.attr("src",imageURL);
                    finished = true;
                }else {
                    logger.info("Image not saved:::");
                    return null;
                }
            }
            logger.info("Image completely saved:::");
            String updatedHTML = document.html();
            logger.info("updatedHTML:::"+updatedHTML);
            logger.info("Creating UUID for post, and post views entity:::");
            UUID uuid = UUID.randomUUID();
            String uuId = uuid.toString();
            //--------------------post-----------------
            logger.info("Setting Post:::"+uuId);
            //set postId and subCategoryId
            Post post = new Post();
            PostId postId = new PostId();
            postId.setPostId(uuId);
            postId.setSubCategoryId(postDTO.getSubCategoryId());
            post.setId(postId);
            post.setTitle(postDTO.getTitle());
//        post.setTextrender(postDTO.getTextrender());
//        keep the content between only <body> tag
            int start = updatedHTML.indexOf("<body>")+6;
            int end = updatedHTML.lastIndexOf("</body>");
            updatedHTML = updatedHTML.substring(start,end);
            logger.info("Removed tags from updatedHTML:::"+updatedHTML);
            post.setTextrender(updatedHTML);
            Instant time = Instant.now();
            logger.info("Time:::"+time);
            post.setPublishAt(time);

            post.setIpvFour(postDTO.getIpvFour());
            post.setIpvSix(postDTO.getIpvSix());
            post.setUserName(postDTO.getUserName());

            logger.info("Saving post:::");
            Post savedPost = postRepository.save(post);
            if(savedPost!=null){
                logger.info("Post saved successfully:::");
            }else{
                logger.info("Post not saved:::");
                return null;
            }
            //--------------------postCommentView------------------
            logger.info("Setting PostView:::"+uuId);
            PostCommentsView postCommentsView = new PostCommentsView();
            //Setting PostView entity
            postCommentsView.setId(uuId.toString());
            postCommentsView.setViews(1);
            postCommentsView.setLastCommentAt(time);
            postCommentsView.setComments(0);
            PostCommentsView savedPostCommentsView = postsCommentsViewRepository.save(postCommentsView);
            if(savedPostCommentsView!=null){
                logger.info("PostCommentsView saved successfully:::");
            }else{
                logger.info("PostCommentsView not saved:::");
                return null;
            }
            return savedPost;
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private byte[] getImageData(String imageCode) throws IOException {
        try{
            logger.info("Parsing image data:::");
            String base64Image = imageCode.substring(imageCode.indexOf(",") + 1);
            return Base64.getDecoder().decode(base64Image);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
//    public List<Object[]> getAllTopFivePostsUnderEveryCategory(){
//        try{
//            logger.info("Getting all top five posts under every category:::");
//            List<Object[]> postList = postDAO.getAllTopFivePostsUnderEveryCategory();
//            return postList;
//        }catch (Exception e){
//            logger.error(e.getMessage(),e);
//        }
//        return null;
//    }
    public  Optional<Post> findPostById(String postId){
        try{
            logger.info("Getting post by id:::"+postId);
            return postRepository.findByIdPostId(postId);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
//    public List<Post> findAllPostByKeyword(String keyword){
//        try{
//            logger.info("Search post via keyword:::"+keyword);
//            List<Post> list = postRepository.findByKeyword(keyword);
//            return list;
//        }catch (Exception e){
//            logger.error(e.getMessage(),e);
//        }
//        return null;
//    }
}
