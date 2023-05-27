package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
public class ChargeRequest {

    public enum Currency {
        EUR, USD;
    }
    private String description;
    private int amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;
}
