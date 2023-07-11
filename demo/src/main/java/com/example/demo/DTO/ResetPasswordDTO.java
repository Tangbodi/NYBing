package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResetPasswordDTO {
    @Size(max = 60)
    private String password;
    @Size(max = 60)
    private String confirmPassword;
    private String token;
    @java.lang.Override
    public java.lang.String toString() {
        return "ResetPasswordDTO{" +
                " password='" + getPassword() + "'" +
                " confirmPassword='" + getConfirmPassword() + "'" +
                " token='" + getToken() + "'" +
                "}";
    }
}
