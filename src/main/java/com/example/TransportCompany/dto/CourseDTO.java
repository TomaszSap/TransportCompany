package com.example.TransportCompany.dto;

import com.example.TransportCompany.constant.CourseType;
import com.example.TransportCompany.model.Client;
import com.example.TransportCompany.model.Employee;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CourseDTO {
    @JsonProperty("fromWhere")
    private String fromWhere;
    @JsonProperty("toWhere")
    private  String toWhere;
    @JsonProperty("type")
    @Enumerated(EnumType.STRING)
    private CourseType type;
    @JsonProperty("employee")
    private Employee employee;
    @JsonProperty("clientsId")
    private Client clientsId;
}
