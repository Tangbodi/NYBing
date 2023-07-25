package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    @Size(min = 1, max = 31)
    private String userName;
    @Size(min = 1, max = 31)
    private String firstName;
    @Size(min = 1, max = 31)
    private String lastName;
    @Size(min = 1, max = 31)
    private String middleName;

    @Size(max = 63)
    private String email;
    @Size(max = 11)
    private String phone;


    @Size(min = 8, max = 63)
    private String password;

    @java.lang.Override
    public java.lang.String toString() {
        return "UserDTO{" +
                " userName='" + getUserName() + "'" +
                " firstName='" + getFirstName() + "'" +
                " lastName='" + getLastName() + "'" +
                " middleName='" + getMiddleName() + "'" +
                " email='" + getEmail() + "'" +
                " phone='" + getPhone() + "'" +
                "}";
    }
}
