package com.example.demo.Controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

//@Controller
//public class IPController {
//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "/client-ip-address",
//            produces = MediaType.TEXT_PLAIN_VALUE
//    )
//    @ResponseBody
//    public String getClientIPAddress(HttpServletRequest request) {
//        String ip = HttpUtils.getRequestIP(request);
//        return "Client IP Address: " + ip;
//    }
//}
