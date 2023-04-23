package com.example.demo.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://192.168.1.23:3000/")
public class CommentController {
//    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
//
//    @Autowired
//    private CommentService commentService;
//    @Autowired
//    private SessionManagementUtil sessionManagementUtil;
//
//    @GetMapping("/comment/{articleId}")
//    public List<Comment> getAllComments(HttpServletRequest request, @PathVariable Long articleId, Model model){
//        if (!this.sessionManagementUtil.doesSessionExist(request))
//        {
//            logger.info("Please login to access this page::");
//            throw new AuthException();
//        }
//        return commentService.getAllCommentsByArticleId(articleId);
//    }
//    @PostMapping("/categories/{categoryId}/{postId}")
//    public Comment postComment(@RequestBody CommentFrontDTO commentFrontDTO, @PathVariable Long articleId, HttpServletRequest request){
//        if (!this.sessionManagementUtil.doesSessionExist(request))
//        {
//            logger.info("Please login to access this page::");
//            throw new AuthException();
//        }
//        allArticleService.updateCommentAndView(articleId);
//        return commentService.saveComment(commentFrontDTO,articleId);
//    }
}
