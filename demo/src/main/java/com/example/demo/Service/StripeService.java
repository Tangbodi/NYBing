package com.example.demo.Service;

import com.example.demo.DTO.ChargeRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {
    private static final Logger logger = LoggerFactory.getLogger(StripeService.class);

    private String secretKey="sk_test_51MvAbYEeRjQ2hTl6LCMGlQT5a9VmFqFOpYZsJsywE6IyFQG7uw4KkRMRgFXQgHl8w6ly74a6MGC0uSQrHNHdWdFc00jW1ljc0A";

    @PostConstruct
    public void init(){
        Stripe.apiKey = secretKey;
    }
    public Charge charge(ChargeRequest chargeRequest) throws StripeException {
        logger.info("charge:::");
        System.out.println(chargeRequest.getStripeToken());
        Map<String,Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());
        return Charge.create(chargeParams);
    }
}
