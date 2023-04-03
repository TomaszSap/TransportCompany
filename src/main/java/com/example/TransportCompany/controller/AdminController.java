package com.example.TransportCompany.controller;

import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.repository.RoleRepository;
import com.example.TransportCompany.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
//@RequestMapping("admin")
public class AdminController {
    private static final Logger logger= LoggerFactory.getLogger(AdminController.class);
    @Autowired
    EmployeeService employeeService;
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
    @GetMapping("/user")
    public ResponseEntity getUserById(@RequestParam int employeeId)
    {
        return ResponseEntity.ok().body(employeeService.getEmployeeById(employeeId));
    }
    @DeleteMapping  (value = "/deleteUser")
    public ResponseEntity<Boolean> deleteUser(@RequestParam int employeeId)
    {
        logger.info("Called DELETE on endpoint /admin/deleteUser/ for argument {} ",String.valueOf(employeeId));
        boolean isDeleted=employeeService.deleteEmployee(employeeId);

        return ResponseEntity.ok().header(HttpHeaders.LOCATION,"redirect:/getUsers").body(isDeleted);
    }
    @PatchMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestParam int id, @Valid @RequestBody Employee employee)
    {   Employee existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee == null) {
            return ResponseEntity.notFound().build();
        } else {
            employee.setEmployeeId(id);
            employeeService.updateEmployee(employee);
            return ResponseEntity.ok("User updated successfully");
        }
    }
    @GetMapping("/getUsers")
    public ResponseEntity<List> getAllUsers()
    {
        return ResponseEntity.ok().body(employeeService.getAllEmployers());
    }
}
