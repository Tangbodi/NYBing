package com.example.demo.Service;

import com.example.demo.Entity.News;
import com.example.demo.Repository.NewsRepository;
import com.example.demo.Util.RedisCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.transaction.Transactional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

@Service
public class NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    private static final String FoxNews = "Fox_News";
    private static final String rssFeedUrl = "http://feeds.foxnews.com/foxnews/national";
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private NewsRepository newsRepository;

    public void proxyXml() throws IOException {
        try{
            logger.info("Parsing rss feed from rssFeedUrl:::");
            HttpClient httpClient = HttpClientBuilder.create().build();
            logger.info("HttpClient created:::");
            HttpGet httpGet = new HttpGet(rssFeedUrl);
            logger.info("HttpGet created:::");
            HttpResponse response = httpClient.execute(httpGet);
            logger.info("httpClient executed:::");
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                logger.info("response was ok:::");
                String rssFeed = EntityUtils.toString(response.getEntity(),"UTF-8");
                saveNewsXmlToDatabase(rssFeed);
            }
        }catch (Exception e){
            logger.error("Error in proxyXml:::",e);
        }
    }
    @Transactional
    public void saveNewsXmlToDatabase(String rssFeed){
        //parse xml string
        try{
            logger.info("saving News Xml to MySQL:::");
            // Create a ByteArrayInputStream from the XML string
            logger.info("Create a ByteArrayInputStream from the XML string:::");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(rssFeed.getBytes());

            // Load and parse the XML document
            logger.info("Load and parse the XML document:::");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(inputStream);

            // Get the root element of the XML
            logger.info("Get the root element of the XML:::");
            Element root = document.getDocumentElement();

            //Get the 'item' nodes from the XML
            logger.info("Get the 'item' nodes from the XML:::");
            NodeList itemNodes = root.getElementsByTagName("item");
            //Iterate through the 'item' nodes
            logger.info("Iterate through the 'item' nodes:::");
            for(int i = 0; i < itemNodes.getLength(); i++){
                Element item = (Element) itemNodes.item(i);
                logger.info("Creating a new News entity:::");
                News news = new News();
                String identifier =item.getElementsByTagName("category").item(0).getTextContent();

                System.out.println("Identifier Number: " + identifier);
                news.setId(identifier);
                logger.info("Setting title for news:::");
                String title = item.getElementsByTagName("title").item(0).getTextContent();
                System.out.println(title);
                news.setTitle(title);

                logger.info("Setting today's date for news:::");
                LocalDate today = LocalDate.now();
                news.setDate(today);

                logger.info("Setting source for news:::");
                news.setSource(FoxNews);

                logger.info("Setting item_link for news:::");
                String link = item.getElementsByTagName("guid").item(0).getTextContent();
                System.out.println(link);
                news.setItemLink(link);

                logger.info("Setting description for news:::");
                String description = item.getElementsByTagName("description").item(0).getTextContent();
                System.out.println(description);
                news.setDescription(description);

                logger.info("Setting content_encoded for news:::");
                String contentEncoded = item.getElementsByTagName("content:encoded").item(0).getTextContent();
                System.out.println(contentEncoded);
                news.setContentEncoded(contentEncoded);

                logger.info("Setting media_content_url for news:::");
                String mediaContentUrl = item.getElementsByTagName("media:content").item(0).getAttributes().getNamedItem("url").getTextContent();
                System.out.println(mediaContentUrl);
                news.setMediaContentUrl(mediaContentUrl);

                logger.info("Setting pubDate for news:::");
                String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
                System.out.println("pubDate:::"+pubDate);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z");
                TemporalAccessor temporalAccessor = formatter.parse(pubDate);
                Instant publish_date = Instant.from(temporalAccessor);
                System.out.println("publish_date:::"+publish_date);
                news.setPubDate(publish_date);

                logger.info("Setting run_time for news:::");
                Instant run_time = Instant.now();
                System.out.println(run_time);
                news.setRunTime(run_time);
                newsRepository.save(news);
                logger.info("News saved to MySQL:::");
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public List<News> getAllNewsByPublishDate() throws JsonProcessingException {
        try{
            logger.info("Getting all News by publish date:::");
            List<News> newsList = newsRepository.findNewsByPublishDate();
            redisCache.updateNewsCache(newsList);
            return newsList;
        }catch (Exception e){
            logger.error("Error in getAllNewsByPublishDate:::",e);
            throw new RuntimeException(e);
        }
    }
    public News getNewsByNewsId(String newsId){
        return newsRepository.findById(newsId).orElseThrow(null);
    }
}
