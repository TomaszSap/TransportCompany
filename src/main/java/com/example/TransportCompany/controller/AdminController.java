package com.example.TransportCompany.controller;

import com.example.TransportCompany.dto.EmployeeDto;
import com.example.TransportCompany.model.Company;
import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.repository.CompanyRepository;
import com.example.TransportCompany.repository.RoleRepository;
import com.example.TransportCompany.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.example.TransportCompany.constant.AppConstants.getNullPropertyNames;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger= LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    CompanyRepository companyRepository;
  //  @Autowired
   // ModelMapper modelMapper;
    @Autowired
    RoleRepository roleRepository;
    @PostMapping (value = "/createUser")
    public ResponseEntity<String> createUser(@Valid @RequestBody EmployeeDto employeeDto, Errors errors)
    {
        logger.info("Called POST on endpoint /admin/createUser/ for argument {} ",employeeDto);
     Employee employee = modelMapper.map(employeeDto, Employee.class);
        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .header(HttpHeaders.LOCATION, "redirect:/createUser")
                    .build();
        boolean isSaved=false;
      if(employee!=null)
       {
            isSaved=employeeService.addEmployee(employee);
        }

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
    public ResponseEntity<Employee> getUserById(@RequestParam int employeeId)
    {
        logger.debug("Called GET on endpoint /admin/user/ for argument employeeId: {} ",employeeId);
        return ResponseEntity.ok().body(employeeService.getEmployeeById(employeeId).get());
    }
    @DeleteMapping  (value = "/deleteUser")
    public ResponseEntity<Boolean> deleteUser(@RequestParam int employeeId)
    {
        logger.info("Called DELETE on endpoint /admin/deleteUser/ for argument {} ", employeeId);
        boolean isDeleted=employeeService.deleteEmployee(employeeId);

        return ResponseEntity.ok().header(HttpHeaders.LOCATION,"redirect:/getUsers").body(isDeleted);
    }
    @PostMapping   (value = "/assignCompany")
    public ResponseEntity<String> assignCompany(@Valid @RequestBody Company company)
    {
        logger.debug("Called POST on endpoint /admin/assignCompany/ for argument {} ", company);
            companyRepository.save(company);
        return ResponseEntity.ok().body("Success");
    }
    @PostMapping   (value = "/updateCompany")
    public ResponseEntity<Boolean> updateCompany(@RequestParam int companyId,@RequestBody Company company)
    {
        logger.debug("Called POST on endpoint /admin/assignCompany/ for argument {} ", company);
        var companyEntity=companyRepository.findById(companyId);
        if (companyEntity.isPresent())
        {
            BeanUtils.copyProperties(company,companyEntity.get(),getNullPropertyNames(company));
            companyRepository.save(companyEntity.get());
        }
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestParam int id, @Valid @RequestBody EmployeeDto employeeDto)
    {
        Employee employee = modelMapper.map(employeeDto, Employee.class);

        logger.info("Called PATCH on endpoint /admin/updateUser/ for ID: {} and argument: {} ",id,employee);
        Optional<Employee> existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            employee.setEmployeeId(id);
            employeeService.updateEmployee(employee);
            return ResponseEntity.ok("User updated successfully");
        }
    }
    @GetMapping("/getUsers")
    public ResponseEntity<List<Employee>> getAllUsers()
    {
        logger.info("Called GET on endpoint /admin//getUsers/");

        return ResponseEntity.ok().body(employeeService.getAllEmployers());
    }
}
