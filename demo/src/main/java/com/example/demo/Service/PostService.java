package com.example.demo.Service;

import com.example.demo.DAO.PostDAO;
import com.example.demo.DTO.PostDTO;
import com.example.demo.Entity.*;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.PostsRepository;

import com.example.demo.Repository.PostsCommentsViewRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import org.apache.commons.codec.binary.Base64;

@Service
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
//    private static final String IMAGE_FOLDER ="/opt/tomcat/webapps/IMAGE_FOLDER/";
    private static final String IMAGE_FOLDER_PATH="/opt/tomcat/webapps/IMAGE/";
    private static final String IMAGE_URL="/IMAGE/";
    @Autowired
    private PostsRepository postRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private PostCommentsViewService postCommentsViewService;

    @Transactional(rollbackOn = Exception.class)
    public Post savePost(HttpServletRequest request, PostDTO postDTO, User user) throws Exception {
            try{
                logger.info("Setting post:::");
                logger.info("postDTO:::"+postDTO.getTextrender());
                return settingPost(postDTO,user,request);
            }catch (Exception e){
                logger.error("Error occurred while saving post",e);
                throw new Exception("Failed to save post",e);
            }

    }
    @Transactional(rollbackOn = Exception.class)
    public Post settingPost(PostDTO postDTO, User user, HttpServletRequest request) throws IOException {
//        String input = "<p><strong><span style=\"color: #e03e2d;\">adfasdf</span></strong></p>\n" +
//                " <p><img src=\"data:image/png;base64,
//                FIPtyYnMEp4AAAAASUVORK5CYII=\" alt=\"\" /></p>";
        try{
            logger.info("Parsing post:::");
            boolean finished = false;
            //parsing post content, convert string to html to parse
            Document document = Jsoup.parse(postDTO.getTextrender());
            for(Element imageElement:document.select("img")){
                logger.info("Parsing image:::");
                Image image = new Image();
                //get image info from src
                String imageCode = imageElement.attr("src");
                //get image type, for example "data:image/png;base64,"
                //data:image/jpeg;base64, -> jpeg
                String imageType = imageCode.substring(0,imageCode.indexOf(",")+1);
                image.setImageType(imageType);
                // Check if imageCode is a valid Base64 string
                if (!Base64.isBase64(imageCode)) {
                    logger.info("Invalid Base64 string::: " + imageCode);
                    logger.info("Skipping getImageData():::");
                    continue; // Skip processing this image
                }
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
                logger.info("Saving image data into MySQL:::");
                if(ima!=null){
                    logger.info("Image saved successfully:::");
                    imageElement.attr("src",imageURL);
                    finished = true;
                }else {
                    logger.info("Image not saved:::");
                    return null;
                }
            }
            String updatedHTML = document.html();
            logger.info("HTML updated:::");
            logger.info("Creating UUID for post, and post_views entity:::");
            UUID uuid = UUID.randomUUID();
            String uuId = uuid.toString();
            //--------------------post-----------------
            logger.info("Create a new Post:::"+uuId);
            //set postId and subCategoryId
            Post post = new Post();
            logger.info("Create a new postId:::");
            PostId postId = new PostId();
            logger.info("Setting Post postId:::");
            postId.setPostId(uuId);
            logger.info("Setting Post subCategoryId:::");
            postId.setSubCategoryid(postDTO.getSubCategoryId());
            logger.info("Setting Post id:::");
            post.setId(postId);
            logger.info("Setting Post title:::");
            post.setTitle(postDTO.getTitle());
//        post.setTextrender(postDTO.getTextrender());
//        keep the content between only <body> tag
            logger.info("Removing tags from updatedHTML:::");
            int start = updatedHTML.indexOf("<body>")+6;
            int end = updatedHTML.lastIndexOf("</body>");
            updatedHTML = updatedHTML.substring(start,end);
            logger.info("Removed tags from updatedHTML and setting textRender:::");
            post.setTextrender(updatedHTML);
            Instant time = Instant.now();
            logger.info("Setting Post publishAt:::");
            post.setPublishAt(time);
            logger.info("Setting Post IPV4:::");
            post.setIpvFour(postDTO.getIpvFour());
            logger.info("Setting Post IPV6:::");
            post.setIpvSix(postDTO.getIpvSix());
            logger.info("Setting Post username:::");
            post.setUserName(postDTO.getUserName());
            logger.info("Saving Post:::");
            Post savedPost = postRepository.save(post);
            if(savedPost!=null){
                logger.info("Post saved successfully:::");
            }else{
                logger.info("Post not saved:::");
                return null;
            }
            //--------------------postCommentView------------------
            if(postCommentsViewService.savePostCommentsView(uuId,postDTO,time)!=null){
                logger.info("PostCommentsView saved successfully:::");
            }else {
                //NEVER TOUCH THIS
            }
            return savedPost;
        }catch (Exception e) {
            logger.error("Error occurred while setting post", e);
            throw new IOException("Failed to set post", e);
        }
    }
    @Transactional
    private byte[] getImageData(String imageCode) throws IOException {
        try{
            logger.info("Parsing image data:::");
            String base64Image = imageCode.substring(imageCode.indexOf(",") + 1);
            return java.util.Base64.getDecoder().decode(base64Image.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            logger.error("Error occurred while parsing image data", e);
        }
        return null;
    }
    @Transactional(rollbackOn = Exception.class)
    public List<Post> findAllPostByKeyword(String keyword) throws Exception {
        try{
            logger.info("Finding Post by keyword:::");
            return postRepository.findByKeyword(keyword);
        }catch (Exception e){
            logger.error("Error occurred while finding posts by keyword", e);
            throw new Exception("Failed to find posts by keyword", e);
        }

    }
}
