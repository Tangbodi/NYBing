package com.example.demo.Controller;

import com.example.demo.DTO.CreatePaymentResponseDTO;
import com.example.demo.DTO.ItemDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

@RestController
@CrossOrigin(origins ="http://localhost:3000")

public class StripeController {
    private static Gson gson = new Gson();
    @PostMapping("/create_payment_intent")
    public String createPaymentResponseDTO (@RequestBody ItemDTO itemDTO) throws StripeException {
        Stripe.apiKey ="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";

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder().setAmount((long)calculateOrderAmount(itemDTO.getItems()))
                .setCurrency("USD").setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                ).build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return gson.toJson(new CreatePaymentResponseDTO(paymentIntent.getClientSecret()));
    }
    public int calculateOrderAmount(Object[] items){
        return 1400;
    }


}
