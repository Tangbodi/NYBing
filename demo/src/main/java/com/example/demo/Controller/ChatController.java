package com.example.demo.Controller;

import com.example.demo.Entity.Chat;
import com.example.demo.Repository.ChatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatRepository chatRepository;
    @GetMapping("/get_chat")
    public List<Chat> getChat(){
        return chatRepository.findAll();
    }
}
