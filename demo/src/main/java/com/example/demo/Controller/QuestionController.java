package com.example.demo.Controller;

import com.example.demo.DTO.CommentBackDTO;
import com.example.demo.DTO.CommentFrontDTO;
import com.example.demo.Entity.Comment;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.CommentBackService;
import com.example.demo.Util.SessionManagementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private CommentBackService commentBackService;
    @Autowired
    private CommentFrontDTO commentFrontDTO;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionManagementUtil sessionManagementUtil;
    @GetMapping("/comment/{articleId}")
    public List<CommentBackDTO> displayComments(@PathVariable Long id, Model model){
        List<CommentBackDTO> commentBackDTO = commentBackService.getAllCommentsByArticleId(id);
        return
    }
    public Comment writingArticle(@RequestBody CommentFrontDTO commentFrontDTO, HttpServletRequest request){
        //get User
        if (!this.sessionManagementUtil.doesSessionExist(request))
        {
            logger.info("Please login to access this page");
        }
        commentBackService.
        Comment comment = new Comment();
        comment.setArticleType(commentFrontDTO.getArticleType());
        comment.setContent(commentFrontDTO.getContent());
        comment.setFromId(commentFrontDTO.getFromId());
        comment.setToId(commentFrontDTO.getToId());
        comment.setPublishAt(Instant.now());

    }
}
