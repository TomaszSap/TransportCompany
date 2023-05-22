package com.example.TransportCompany.controller;

import com.example.TransportCompany.model.Course;
import com.example.TransportCompany.services.CourseService;
import com.example.TransportCompany.services.EmployeeService;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
//@RequestMapping("forwarded")
public class ForwarderController {
    private static final Logger logger= LoggerFactory.getLogger(ForwarderController.class);

    @Autowired
    CourseService courseService;
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/assignCourse")
    public ResponseEntity<String> assignCourse(@RequestParam int driverId, @RequestBody @Valid int courseId)
    {

        boolean isAssigned=employeeService.addCourse(driverId,courseId);
        if (isAssigned)
        {
           return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("failed to assign Course !");
    }
    @PostMapping("/forwardCourse")
    public ResponseEntity<String> forwardCourse(@RequestParam int driverId, @RequestBody @Valid int courseId)
    {

        boolean isAssigned=employeeService.addCourse(driverId,courseId);
        if (isAssigned)
        {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("failed to forward Course !");
    }
    @PostMapping("/addCourse")
    public ResponseEntity<Boolean> addCourse(@Valid @RequestBody Course course)
    {
        boolean isCreated=false;
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

    @GetMapping("/getCourses")
    public ResponseEntity<List> showCourses(@RequestParam String type)
    {
        return ResponseEntity.ok().body(courseService.findCoursesWithType(type));
    }
}
