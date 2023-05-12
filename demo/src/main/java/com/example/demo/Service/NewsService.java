package com.example.demo.Service;

import com.example.demo.DTO.NewsApiResponseDTO;
import com.example.demo.DTO.NewsApiResultDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import static com.example.demo.Enumeration.Endpoint.NEWS_API;

@Service
public class NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    private String NewsType = "新闻";
    @Value("$cb53bd41a74ef445a2d1b7fcfebe6fa0")
    private String NEWS_API_KEY;
    public NewsApiResultDTO getNewsInfo(String page) throws IOException {

        //set news api and key
        URL url = new URL(NEWS_API.url()+"type="+NewsType+"&page="+page+"&page_size=30&is_filter=0&key="+NEWS_API_KEY);
        logger.info("The NEWS API URL::"+url);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
        StringBuilder jsonString = new StringBuilder();
        String readAIPResponse="";
        while((readAIPResponse = bufferedReader.readLine())!= null){
            jsonString.append(readAIPResponse);
        }
        String json = jsonString.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        NewsApiResponseDTO newsApiResponseDTO = objectMapper.readValue(json,NewsApiResponseDTO.class);
        return newsApiResponseDTO.getResult();
    }
}

