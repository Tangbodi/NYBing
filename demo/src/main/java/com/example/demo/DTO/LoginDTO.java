package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginDTO {

    @NotEmpty
    @Size(max = 18)
    private String userName ;
    @NotEmpty
    @Size(max = 60)
    private String password;

    @java.lang.Override
    public java.lang.String toString() {
        return "LoginDTO{" +
                " userName='" + getUserName() + "'" +
                " password='" + getPassword() + "'" +
                "}";
    }
}
