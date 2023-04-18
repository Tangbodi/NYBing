package com.example.demo.Controller;

import com.example.demo.Entity.Category;
import com.example.demo.Repository.CategoryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

//@RestController
//public class test {
//    private static final Logger logger = LoggerFactory.getLogger(test.class);
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @GetMapping("/test/category")
//    public void InsertJsonToMySQL() throws IOException {
//        Set<Integer> set = new HashSet<>();
//        try {
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/master", "root", "1992530");
//
//            ObjectMapper mapper = new ObjectMapper();
//
//            InputStream exampleInput = test.class.getClassLoader().getResourceAsStream("static/dadi360.json");
//            JsonNode rootNode = mapper.readTree(exampleInput);
//
//            for(int i=0;i<40;i++) {
//                int categoryId = rootNode.get(i).get("category_id").asInt();
//                JsonNode postsNode = rootNode.get(i).get("posts");
//                for (JsonNode postNode : postsNode) {
//                    int postId = postNode.get("post_id").asInt();
//                    if(set.contains(postId)){
//                        continue;
//                    }
//                    else{
//                        set.add(postId);
//                        String title = postNode.get("title").asText();
//                        String date = postNode.get("post_date").asText();
//                        String content = postNode.get("content").asText();
//                        String textjson = "";
//                        int userId = 27;
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//                        java.util.Date parsedDate = dateFormat.parse(date);
//                        Timestamp postDate = new java.sql.Timestamp(parsedDate.getTime());
//                        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Post (" + "postId," + "categoryId," + "title," + "textjson," + "textrender," + "publishAt," + "author," + "userId)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
//                        preparedStatement.setInt(1, postId);
//                        preparedStatement.setInt(2, categoryId);
//                        preparedStatement.setString(3, title);
//                        preparedStatement.setString(4, textjson);
//                        preparedStatement.setString(5, content);
//                        preparedStatement.setTimestamp(6, postDate);
//                        preparedStatement.setString(7, "tbd530");
//                        preparedStatement.setInt(8, userId);
//                        int rowsInserted = preparedStatement.executeUpdate();
//                        if (rowsInserted > 0) {
//                            System.out.println("A new row was inserted successfully!");
//                        }
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    @GetMapping("/test/article")
//    public JsonNode getArticle() throws SQLException, IOException {
//
//        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/master","root","1992530");
//
//        ObjectMapper mapper = new ObjectMapper();
//        InputStream exampleInput = test.class.getClassLoader().getResourceAsStream("static/dadi360.json");
//        JsonNode rootNode = mapper.readTree(exampleInput);
//        return rootNode;
//    }
//}
