package com.example.TransportCompany.controller;

import com.example.TransportCompany.constant.RoleType;
import com.example.TransportCompany.dto.CourseDTO;
import com.example.TransportCompany.model.Course;
import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.services.CourseService;
import com.example.TransportCompany.services.EmployeeService;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RestController
@RequestMapping("/forwarder")
public class ForwarderController {
    private static final Logger logger= LoggerFactory.getLogger(ForwarderController.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    CourseService courseService;
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/updateCourse")
    public ResponseEntity<String> updateCourse(@RequestParam int id, @RequestBody @Valid CourseDTO courseDto)
    {
        logger.debug("Called Post on endpoint forwarder/updateCourse/");

        Course course=modelMapper.map(courseDto,Course.class);

        boolean isAssigned=courseService.updateCourse(id,course);
        if (isAssigned)
        {
           return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("failed to assign Course !");
    }
    @PostMapping("/addCourse")
    public ResponseEntity<Boolean> addCourse(@Valid @RequestBody CourseDTO courseDto, Errors errors)
    {
        Course course=modelMapper.map(courseDto,Course.class);
        logger.debug("Called Post on endpoint forwarder/addCourse/");
        if (errors.hasErrors()) {
            logger.error("Course from validation failed due to: "+ errors);
            throw new ValidationException(String.valueOf(errors));
        }
        boolean isCreated;
        try {
            courseService.saveCourse(course);
            isCreated=true;
        }
         catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
        return ResponseEntity.ok(isCreated).status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, "redirect:/getOpenCourses")
                .build();
    }
    @DeleteMapping ("/deleteCourse")
    public ResponseEntity deleteCourse(@RequestParam  int courseId)
    {
        return ResponseEntity.ok().body(courseService.deleteCourse(courseId));
    }
    @GetMapping ("/getCourse")
    public ResponseEntity getCourse(@RequestParam  int courseId)
    {
        return ResponseEntity.ok().body(courseService.findCourse(courseId));
    }
    @GetMapping("/getCourses")
    public ResponseEntity<List<Course>> showCourses(@RequestParam String type)
    {
        return ResponseEntity.ok().body(courseService.findCoursesWithType(type));
    }
    @GetMapping("/getAllDrivers")
    public ResponseEntity<List<Employee>>  getAllDrivers()
    {
         return ResponseEntity.ok().body(employeeService.getEmployeersByRole(RoleType.DRIVER));
    }
    @GetMapping("/getAllCourses")
    public ResponseEntity<List<Course>> getAllCourses()
    {
        return ResponseEntity.ok().body(courseService.findAllCourses());
    }
}
