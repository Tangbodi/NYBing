package com.example.demo.Controller;

import com.example.demo.DTO.ChargeRequest;
import com.example.demo.Service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://192.168.1.10:3000/")
public class StripeController {
    private static final Logger logger = LoggerFactory.getLogger(StripeController.class);
    @Autowired
    private StripeService stripeService;
    @PostMapping("/charge")
    public String createPayment(ChargeRequest chargeRequest, Model model) throws StripeException {
        logger.info("createPayment:::");
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(ChargeRequest.Currency.EUR);
        Charge charge  = stripeService.charge(chargeRequest);
//        ChargeResDTO chargeResDTO = new ChargeResDTO();
//        chargeResDTO.setId(charge.getId());
//        chargeResDTO.setStatus(charge.getStatus());
//        chargeResDTO.setBalance_transaction(charge.getBalanceTransaction());
        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
        return "result";
    }
}
