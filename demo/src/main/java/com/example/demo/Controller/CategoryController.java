package com.example.demo.Controller;

import com.example.demo.DAO.PostDAO;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://192.168.1.23:3000/")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private PostDAO postDAO;

    @GetMapping("/categories")
    public List<Object[]> getTopFivePosts(HttpServletRequest request){
        String ip = HttpUtils.getRequestIP(request);
        //store visitors ip address
        request.getSession().setAttribute("visitor",ip);
//        request.getSession().getAttribute("visitor");
        System.out.println("Client IP Address: " + ip);
        return postDAO.getTopFivePosts();
    }
}
