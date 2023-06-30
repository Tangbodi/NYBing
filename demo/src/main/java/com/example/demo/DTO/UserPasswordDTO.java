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
public class UserPasswordDTO {
    private String inputPassword;
    private String newPassword;

    @java.lang.Override
    public java.lang.String toString() {
        return "UserPasswordDTO{" +
                " inputPassword='" + getInputPassword() + "'" +
                " newPassword='" + getNewPassword() + "'" +
                "}";
    }
}
