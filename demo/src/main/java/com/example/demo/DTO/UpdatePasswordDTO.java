package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatePasswordDTO {

    @NotBlank
    @Size(min = 8, max = 63)
    private String oldPassword;
    @NotBlank
    @Size(min = 8, max = 63)
    private String newPassword;

    @java.lang.Override
    public java.lang.String toString() {
        return "UserPasswordDTO{" +
                " inputPassword='" + getOldPassword() + "'" +
                " newPassword='" + getNewPassword() + "'" +
                "}";
    }
}
