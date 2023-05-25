package com.example.demo.Service;

import com.example.demo.DAO.PostDAO;
import com.example.demo.DTO.PostDTO;
import com.example.demo.Entity.*;
import com.example.demo.Exception.PostNotFoundException;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.PostCommentsRepository;
import com.example.demo.Repository.PostRepository;

import com.example.demo.Repository.PostViewsRepository;
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
    private PostCommentsRepository postCommentsRepository;
    @Autowired
    private PostViewsRepository postViewsRepository;
    @Autowired
    private PostDAO postDAO;

    public void settingPost(HttpServletRequest request, PostDTO postDTO, User user) throws Exception {
        logger.info("Setting post:::");
        try{
            savePost(postDTO,user,request);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    public void savePost(PostDTO postDTO, User user, HttpServletRequest request) throws IOException {
//        String input = "<p><strong><span style=\"color: #e03e2d;\">adfasdf</span></strong></p>\n" +
//                " <p><img src=\"data:image/png;base64,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\" alt=\"\" /></p>";
        logger.info("Saving post into database:::");
        //parsing post content
        Document document = Jsoup.parse(postDTO.getTextrender());
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
            System.out.println(filename);
//            image.setImageName(filename);
            Path imagePath = Paths.get(IMAGE_FOLDER_PATH, filename);
            System.out.println("imagePath:::"+imagePath);
            image.setImagePath(imagePath.toString());
            try (FileOutputStream fos = new FileOutputStream(imagePath.toFile())) {
                fos.write(imageData);
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }
            //http://31.220.21.110:8080
//            String requestPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
            ///api/1efc15fc-fe83-4ef1-9f1a-420ecd408f20.jpeg
            String imageURL = IMAGE_URL+filename;
            image.setImageURL(imageURL);
            imageRepository.save(image);
            imageElement.attr("src",imageURL);
        }
        String updateHTML = document.html();
        logger.info(updateHTML);
        logger.info("Creating UUID for post, and post views entity:::");
        Post post = new Post();
        PostView postView = new PostView();
        PostComment postComment = new PostComment();
        UUID uuid = UUID.randomUUID();
        //Setting post entity

        post.setId(uuid.toString());
        post.setTitle(postDTO.getTitle());
        post.setTextrender(updateHTML);
        Instant time = Instant.now();
        post.setPublishAt(time);
        post.setUser(user);
        post.setIpvFour(postDTO.getIpvFour());
        post.setIpvSix(postDTO.getIpvSix());
        post.setUserName(postDTO.getUserName());
        post.setCategoryId(postDTO.getCategoryId());
        logger.info("Setting PostView:::");
        //Setting PostView entity
        postView.setId(uuid.toString());
        postView.setViews(1);
        logger.info("Setting PostComment:::");
        //Setting PostComment entity
        postComment.setId(uuid.toString());
        postComment.setLastComment(time);
        logger.info("Saving post:::");
        postRepository.save(post);
        logger.info("Saving post views:::");
        postViewsRepository.save(postView);
        logger.info("Saving post comments:::");
        postCommentsRepository.save(postComment);
    }
    public Post getPostData(String postId){
        logger.info("Getting data of post:::" + postId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        logger.info(post.getTextrender());
//        String textrender = post.getTextrender();
//        Document document = Jsoup.parse(textrender);
//        for(Element imageElement: document.select("img")){
//            String imageId = imageElement.attr("src");
//            Image image = imageRepository.findById(imageId).orElseThrow(()->new RuntimeException());
//            String ImagePath = image.getImagePath();
//            String imageType = image.getImageType();
//            imageElement.attr("src","file:///"+ImagePath);
//        }
//
//        String updateHTML = document.html();
//        int start = updateHTML.indexOf("<body>");
//        int end = updateHTML.lastIndexOf("</body>");
//        updateHTML = updateHTML.substring(start,end);
//        post.setTextrender(updateHTML);

        return post;
    }
    private byte[] getImageData(String imageCode) throws IOException {
        logger.info("Parsing image data:::");
        String base64Image = imageCode.substring(imageCode.indexOf(",") + 1);
        return Base64.getDecoder().decode(base64Image);
    }
    public List<Object[]> getAllTopFivePostsUnderEveryCategory(){
        logger.info("Getting all top five posts under every category:::");
        return postDAO.getAllTopFivePostsUnderEveryCategory();
    }
    public Post findPostById(String postId){
        return postRepository.findById(postId).orElseThrow(()->new PostNotFoundException(postId));
    }
    public List<Post> findAllPostByKeyword(String keyword){
        logger.info(keyword);
        return postRepository.findByKeyword(keyword);
    }
}
