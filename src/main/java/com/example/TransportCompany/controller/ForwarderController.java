package com.example.TransportCompany.controller;

import com.example.TransportCompany.constant.CourseType;
import com.example.TransportCompany.model.Course;
import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.services.CourseService;
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
public class ForwarderController extends RestEndpoint {
    private static final Logger logger= LoggerFactory.getLogger(ForwarderController.class);

    @Autowired
    CourseService courseService;
    @PatchMapping("/user")
    public ResponseEntity<String> updateUser(@RequestParam int id, @Valid @RequestBody Employee employee)
    {  return super.updateUser(id,employee);
    }
  /*  public ResponseEntity<Boolean> addCourseToDriver()
    {
    }*/


    @PostMapping("/addCourse")
    public ResponseEntity<Boolean> addCourse(@Valid @RequestBody Course course)
    {
        boolean isCreated=false;
        try {
            courseService.saveCourse(course);
            isCreated=true;
        }
        catch (IllegalArgumentException e)
        {
            logger.error(String.valueOf(e));
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
