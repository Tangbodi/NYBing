package com.example.demo.Service;

//@Service
//public class PostService {
//    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
//
//    @Autowired
//    private PostDAO postDAO;
//
//    @Autowired
//    private UserService userService;
//    public Post saveArticle(Post post, User user) throws IOException {
//        post.setPost_date(Instant.now());
//        post.setAuthor(user.getUserName());
//        post.setUser(user);
//        return postDAO.savePost(post);
//    }
//    public Optional<Post> findArticleById(Long postId){
//        return postDAO.findPostById(postId);
//    }
//}
