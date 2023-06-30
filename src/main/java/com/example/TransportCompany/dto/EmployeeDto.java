package com.example.TransportCompany.dto;

import com.example.TransportCompany.model.Role;

public record EmployeeDto(String name, String surname,String email,String mobileNumber,Role role) {
}

