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
    @NotBlank
    @Size(max = 18)
    private String userName;
    @Size(max = 36)
    private String firstName;
    @Size(max = 36)
    private String lastName;
    @Size(max = 36)
    private String middleName;
    @NotBlank
    @Size(max = 45)
    private String email;
    @Size(max = 11)
    private String phone;
    @NotBlank
    @Size(max = 60)
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
