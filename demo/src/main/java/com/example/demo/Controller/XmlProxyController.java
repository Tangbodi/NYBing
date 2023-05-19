package com.example.demo.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@RestController
//@CrossOrigin(origins ="http://192.168.1.13:3000/")
public class XmlProxyController {
    @GetMapping(value = "/rss_xml",produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> proxyXml() {
        try {
            // Replace the file path with the actual path to your XML file
            String filePath = "C:\\Users\\tangb\\Desktop\\RSS.xml";

            // Read the XML file with UTF-8 encoding
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                contentBuilder.append(line);
            }
            bufferedReader.close();

            // Get the XML content as a string
            String xmlContent = contentBuilder.toString();

            // Return the XML content as the response
            System.out.println(xmlContent);
            return ResponseEntity.ok().body(xmlContent);
        } catch (Exception e) {
            // Handle any exceptions that occur during file reading
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

