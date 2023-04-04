package com.example.demo.Service;

import com.example.demo.Entity.Content;
import com.example.demo.Entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;

//@Service
//public class ImageService {
//    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
//
//    @Autowired
//    private ImageDAO imageDAO;
//    @Autowired
//    private UserService userService;
//    public Image saveImage(MultipartFile multipartFile, String fileName, User user) throws IOException {
//
//        Content content = new Content();
//        content.setImages(multipartFile.getBytes());
//    }
//}
