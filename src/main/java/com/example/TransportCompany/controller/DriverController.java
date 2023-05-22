package com.example.TransportCompany.controller;

import com.example.TransportCompany.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("driver")
public class DriverController  {
  private static final Logger logger= LoggerFactory.getLogger(DriverController.class);

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/getAllCourses")
    public ResponseEntity<Set> getAllCourses(@RequestParam int id)
    {
        logger.debug("Called GET on endpoint /getCourses/");

        return  ResponseEntity.status(HttpStatus.OK).body(employeeService.getCoursesById(id));
    }
  @GetMapping("/getCourse")
  public ResponseEntity<Set> getCourse(@RequestParam int id,@RequestParam int courseId)
  {
    logger.debug("Called GET on endpoint /getCourse/");

    return  ResponseEntity.status(HttpStatus.OK).body(employeeService.getCoursesById(id));
  }
}
