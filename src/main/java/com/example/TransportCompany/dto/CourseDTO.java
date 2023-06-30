package com.example.TransportCompany.dto;

import com.example.TransportCompany.constant.CourseType;

public record CourseDTO(String fromWhere, String toWhere, CourseType type,EmployeeDto employeeDto, ClientForwarderDTO clientsId) {
}
