package com.example.TransportCompany.controller;

import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

public class RestEndpoint {

    @Autowired
    EmployeeService employeeService;

    @PatchMapping("/user")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> updateUser(@RequestParam int id, @RequestBody Employee employee) {
        Optional<Employee> existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            employee.setEmployeeId(id);
            employeeService.updateEmployee(employee);
            return ResponseEntity.ok("User updated successfully");
        }
    }
}
