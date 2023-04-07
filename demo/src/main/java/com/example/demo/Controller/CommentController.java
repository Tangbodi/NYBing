package com.example.demo.Controller;

import com.example.demo.DTO.CommentFrontDTO;
import com.example.demo.Entity.Comment;
import com.example.demo.Repository.ArticleRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.CommentService;
import com.example.demo.Util.SessionManagementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;
    @Autowired
    private SessionManagementUtil sessionManagementUtil;
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/comment/{articleId}")
    public List<Comment> getAllComments(HttpServletRequest request, @PathVariable Long articleId, Model model){
        if (!this.sessionManagementUtil.doesSessionExist(request))
        {
            logger.info("Please login to access this page");

        }
        return commentService.getAllCommentsByArticleId(articleId);
    }
    @PostMapping("/comment/{articleId}")
    public Comment postComment(@RequestBody CommentFrontDTO commentFrontDTO, @PathVariable Long articleId, HttpServletRequest request){
        if (!this.sessionManagementUtil.doesSessionExist(request))
        {
            logger.info("Please login to access this page");

        }

        return commentService.saveComment(commentFrontDTO,articleId);
    }
}
