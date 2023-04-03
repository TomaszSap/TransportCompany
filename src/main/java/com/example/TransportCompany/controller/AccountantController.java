package com.example.TransportCompany.controller;

import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
//@RequestMapping("accountant")
public class AccountantController  extends RestEndpoint{
    @Autowired
    EmployeeService employeeService;
    @PatchMapping("/user")
    public ResponseEntity<String> updateUser(@RequestParam int id, @Valid @RequestBody Employee employee)
    {  return super.updateUser(id,employee);
    }

}
