package com.example.demo.Controller;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins ="http://192.168.1.13:3000/")
public class XmlProxyController {
    @GetMapping(value = "/rss_xml",produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> proxyXml() {
        String rssFeedUrl = "https://rsshub.app/ifeng/news";
//        try {
            // Replace the file path with the actual path to your XML file
//            String filePath = "C:\\Users\\tangb\\Desktop\\RSS.xml";
//            String filePath = "/opt/tomcat/webapps/RSS.xml";

            // Read the XML file with UTF-8 encoding
//            File file = new File(filePath);
//            FileInputStream fileInputStream = new FileInputStream(file);
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//            StringBuilder contentBuilder = new StringBuilder();
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                contentBuilder.append(line);
//            }
//            bufferedReader.close();

            // Get the XML content as a string
//            String xmlContent = contentBuilder.toString();

            // Return the XML content as the response
//            return ResponseEntity.ok().body(xmlContent);

//        } catch (Exception e) {
//            // Handle any exceptions that occur during file reading
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
            try {
                org.apache.http.client.HttpClient httpClient = HttpClientBuilder.create().build();
                HttpGet httpGet = new HttpGet(rssFeedUrl);
                org.apache.http.HttpResponse response = httpClient.execute(httpGet);

                if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                    String rssFeed = org.apache.http.util.EntityUtils.toString(response.getEntity());
                    return ResponseEntity.ok(rssFeed);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }
}

