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
public class UserDTO {
    private String firstName;
    private String lastName;
    private String middleName;
    private String inputPassword;
    private String newPassword;

    private String email;

    @java.lang.Override
    public java.lang.String toString() {
        return "TradeDTO{" +
                " firstName='" + getFirstName() + "'" +
                " lastName='" + getLastName() + "'" +
                " middleName='" + getMiddleName() + "'" +
                " inputPassword='" + getInputPassword() + "'" +
                " newPassword='" + getNewPassword() + "'" +
                " email='" + getEmail() + "'" +

                "}";
    }
}
