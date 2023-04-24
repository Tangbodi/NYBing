//package com.example.demo.Service;
//
//import com.example.demo.Entity.Image;
//import com.example.demo.Entity.Post;
//import com.example.demo.Exception.PostNotFoundException;
//import com.example.demo.Repository.ImageRepository;
//import com.example.demo.Repository.PostRepository;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.Base64;
//import java.util.UUID;
//
//@Service
//public class ImageService {
//
//    @Autowired
//    private ImageRepository imageRepository;
//    @Autowired
//    private PostService postService;
//    @Autowired
//    private PostRepository postRepository;

//    public void savePostData(String text) throws IOException {
//        Document document = Jsoup.parse(text);
//        for(Element imageElement: document.select("img")){
//            Image image = new Image();
//            String imageUrl = imageElement.attr("src");
//            byte[] imageData = getImageData(imageUrl);
//            image.setImageUrl(imageData);
//            String imageId = UUID.randomUUID().toString();
//            image.setId(imageId);
//            imageRepository.save(image);
//            imageElement.attr("src",imageId);
//        }
//        String updateHTML = document.html();
//        Post post = new Post();
//        post.setTextrender(updateHTML);
//        post.setCategoryId(31);
//        post.setTitle("HTML TEST");
//        postRepository.save(post);
//        System.out.println(updateHTML);
//    }
//    public String getPostData(String postId){
//        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
//        String textrender = post.getTextrender();
//        Document document = Jsoup.parse(textrender);
//        for(Element imageElement: document.select("img")){
//            String imageId = imageElement.attr("src");
//            Image image = imageRepository.findById(imageId).orElseThrow(()->new RuntimeException());
//            String imageUrl = Base64.getEncoder().encodeToString(image.getImageUrl());
//            imageElement.attr("src",imageUrl);
//        }
//        String updateHTML = document.html();
//        return updateHTML;
//    }
//    private byte[] getImageData(String imageUrl) throws IOException {
//        String base64Image = imageUrl.substring(imageUrl.indexOf(",") + 1);
//        return Base64.getDecoder().decode(base64Image);
//    }
//}
