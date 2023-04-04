package com.example.demo.Controller;

import com.example.demo.Entity.Article;
import com.example.demo.Repository.ArticleRepository;
import com.example.demo.Service.ArticleService;
import com.example.demo.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;
    @Autowired
    private ArticleRepository articleRepository;

    @PostMapping("/article/writing")
    public Article writeArticle(HttpServletRequest request, @RequestBody Article article) throws IOException {

        return articleService.saveArticle(article);
    }
    @GetMapping("/article/{id}")
    public ModelAndView display(@PathVariable Long id, Model model){
        ModelAndView mv = new ModelAndView();
        String str = articleRepository.getById(id).getTextrender();
        model.addAttribute("render",str);
        mv.setViewName("forgot_password_form");
        return mv;
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
