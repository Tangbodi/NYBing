package com.example.demo.Controller;

import com.example.demo.DTO.NewsApiResultDTO;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

//public class XmlParser extends DefaultHandler {
//    private StringBuilder contentBuilder;
//    private boolean insideElement;
//
//    public void parseXmlFile(String filePath) {
//        try {
//            SAXParserFactory factory = SAXParserFactory.newInstance();
//            SAXParser parser = factory.newSAXParser();
//            parser.parse(filePath, this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void startDocument() {
//        contentBuilder = new StringBuilder();
//    }
//
//    @Override
//    public void startElement(String uri, String localName, String qName, Attributes attributes) {
//        // Enter the logic to handle specific elements if needed
//        // For example, if you want to extract the contents of <title> elements:
//        if ("title".equals(qName)) {
//            insideElement = true;
//        }
//    }
//
//    @Override
//    public void characters(char[] ch, int start, int length) {
//        if (insideElement) {
//            contentBuilder.append(new String(ch, start, length));
//        }
//    }
//
//    @Override
//    public void endElement(String uri, String localName, String qName) {
//        // Exit the logic for handling specific elements
//        // For example, if you were extracting <title> elements:
//        if ("title".equals(qName)) {
//            insideElement = false;
//            String titleContent = contentBuilder.toString();
//            // Process the extracted title content as needed
//            System.out.println("Title: " + titleContent);
//            contentBuilder.setLength(0);
//        }
//    }
//
//    public static void main(String[] args) {
//        String filePath = "C:\\Users\\tangb\\Desktop\\RSS.xml";
//        XmlParser parser = new XmlParser();
//        parser.parseXmlFile(filePath);
//    }
//}
