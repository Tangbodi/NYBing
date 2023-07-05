package com.example.demo.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//@Component
//public class SessionManagementUtil {
//    private static final Logger logger = LoggerFactory.getLogger(SessionManagementUtil.class);
//
//    public Boolean doesSessionExist(HttpServletRequest request){
//        logger.debug("doesSessionExists::INSIDE");
//        if(request.getSession(false)!=null){
//            logger.debug("Session exists");
//            if(request.getSession().getAttribute("user")!=null){
//                logger.debug("User exists");
//                return true;
//            }
//            else{
//                logger.debug("user does not exists");
//                return false;
//            }
//        }
//        logger.debug("doesSessionExits::FALSE");
//        return false;
//    }
//    public HttpSession createNewSessionForUser(HttpServletRequest request, String userName){
//        logger.debug("createNewSessionForUser::INSIDE"+userName);
//        if(!doesSessionExist(request)){
//            HttpSession session = request.getSession();
//            session.setAttribute("user",userName);
//            return session;
//        }
//        else{
//            logger.debug("createNewSessionForUser::Return current session::");
//            return request.getSession();
//        }
//    }
//
//}
