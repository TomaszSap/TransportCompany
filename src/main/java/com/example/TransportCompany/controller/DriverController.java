package com.example.TransportCompany.controller;

import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Controller
//@RequestMapping("driver")
public class DriverController extends RestEndpoint {
    @Autowired
    EmployeeService employeeService;
    @PatchMapping("/user")
    public ResponseEntity<String> updateUser(@RequestParam int id, @Valid @RequestBody Employee employee)
    { return super.updateUser(id,employee);
    }

    @GetMapping("/getCourses")
    public ResponseEntity<Set> getAllCourses(@RequestParam int id)
    {
        return  ResponseEntity.status(HttpStatus.OK).body(employeeService.getCoursesById(id));
    }
}
