package com.example.TransportCompany.controller;

import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;

public class RestEndpoint {

    @Autowired
EmployeeService employeeService;
    @PatchMapping("/user")
    public ResponseEntity<String> updateUser( int id, Employee employee)
    {   Employee existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee == null) {
            return ResponseEntity.notFound().build();
        } else {
            employee.setEmployeeId(id);
            employeeService.updateEmployee(employee);
            return ResponseEntity.ok("User updated successfully");
        }
    }
}
