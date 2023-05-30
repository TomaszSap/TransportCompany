package com.example.TransportCompany.dto;

import com.example.TransportCompany.annotation.FieldsValueMatch;
import com.example.TransportCompany.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field="pwd",
                fieldMatch = "confirmPwd",
                message = "Passwords do not match!"
        ),
        @FieldsValueMatch(
                field="email",
                fieldMatch = "confirmEmail",
                message = "Email addresses do not match!"
        )
})
public class EmployeeDto {
    @JsonProperty("name")
    String name;
    @JsonProperty("surname")
    String surname;
    @JsonProperty("email")
    String email;
    @JsonProperty("confirmEmail")
    private String confirmEmail;
    @JsonProperty("mobileNumber")
    private String mobileNumber;
    @JsonProperty("pesel")
    String pesel;
    @JsonProperty("role")
    private Role role;
    @JsonProperty("pwd")
    private String pwd;
    @JsonProperty("confirmPwd")
    private String confirmPwd;
}

