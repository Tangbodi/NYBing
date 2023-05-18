package com.example.demo.Service;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service
public class IpService {
    private static final Logger logger = LoggerFactory.getLogger(IpService.class);
    private static final InetAddressValidator VALIDATOR = InetAddressValidator.getInstance();
    public boolean isValidInet4Address(String ip){
        logger.info("Checking if IP is IPV4:::");
        return VALIDATOR.isValidInet4Address(ip);
    }
    public boolean isValidInet6Address(String ip){
        logger.info("Checking if IP is IPV6:::");
        return VALIDATOR.isValidInet6Address(ip);
    }
}
