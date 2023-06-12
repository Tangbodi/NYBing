package com.example.demo.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CheckoutController {

    private String stripePublicKey = "pk_test_51MvAbYEeRjQ2hTl6GNs3KSmCtMdrcFYdjCIMaGgb9B0DsTjUcssevJ7OzTeD2zYXrGWKaFbq92YFUDa6p1HSM19v00SCKU5Ehf";
    private static final Logger logger = LoggerFactory.getLogger(StripeController.class);

    @RequestMapping(value = "/checkout")
    public String checkout(Model model) {
        logger.info("checkout:::");
        model.addAttribute("amount", 50 * 100); // in cents
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", "USD");
        return "checkout";
    }
}
