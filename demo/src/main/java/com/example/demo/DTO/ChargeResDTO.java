package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChargeResDTO {
    private String id;
    private String status;
    private String balance_transaction;
    @java.lang.Override
    public java.lang.String toString() {
        return "ChargeResDTO{" +
                " id='" + getId() + "'" +
                " status='" + getStatus() + "'" +
                " balance_transaction='" + getClass() + "'" +
                "}";
    }
}
