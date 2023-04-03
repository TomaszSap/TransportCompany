package com.example.TransportCompany.controller;

import com.example.TransportCompany.annotation.FieldsValueMatch;
import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.repository.RoleRepository;
import com.example.TransportCompany.services.EmployeeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
//@RequestMapping("admin")
public class AdminController {
    private static final Logger logger= LoggerFactory.getLogger(AdminController.class);
    @Autowired
    EmployeeServiceImpl employeeService;
    @Autowired
    RoleRepository roleRepository;
    @PostMapping (value = "/createUser")
    public ResponseEntity<String> createUser(@Valid @RequestBody Employee employee, Errors errors)
    {
        logger.info("Called POST on endpoint /admin/deleteUser/ for argument {} ",employee);

        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .header(HttpHeaders.LOCATION, "redirect:/createUser")
                    .build();
        boolean isSaved=false;
        if(employee!=null)
        { isSaved=employeeService.addEmployee(employee);}

        if(isSaved)
           return ResponseEntity.status(HttpStatus.CREATED)
                    .header(HttpHeaders.LOCATION, "redirect:/login?register=true")
                    .build();
        else
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .header(HttpHeaders.LOCATION, "redirect:/login?register=false")
                    .build();

    }
    @GetMapping("/getUserById")
    public Employee getUserById()
    {
        return null;
    }
    @DeleteMapping  (value = "/deleteUser")
    public ResponseEntity<Boolean> deleteUser(@RequestBody int employeeid)
    {
        logger.info("Called DELETE on endpoint /admin/deleteUser/ for argument {} ",String.valueOf(employeeid));
        boolean isDeleted=employeeService.deleteEmployee(employeeid);

        return ResponseEntity.ok().header(HttpHeaders.LOCATION,"redirect:/getUsers").body(isDeleted);
    }
    @PatchMapping("/changeUserRole")
    public ResponseEntity<String> changeEmployeeRole(@Valid @RequestBody int employeeid,String role)
    {
        employeeService.changeRole(employeeid,role);
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.LOCATION,"redirect:/getUserById").build();
    }
    @GetMapping("/getUsers")
    public ResponseEntity getAllUsers()
    {
        return ResponseEntity.ok().body(employeeService.getAllEmployers());
    }
}
