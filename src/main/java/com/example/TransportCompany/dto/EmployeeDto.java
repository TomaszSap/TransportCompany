package com.example.TransportCompany.dto;

import com.example.TransportCompany.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
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

