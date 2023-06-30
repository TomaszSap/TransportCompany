package com.example.TransportCompany.services;

import com.example.TransportCompany.dto.CourseDTO;
import com.example.TransportCompany.model.Course;

import java.util.List;

public interface CourseService {
    boolean saveCourse(Course course);

    List<CourseDTO> findAllCourses();

    List<CourseDTO> findCoursesWithType(String courseType);

    boolean updateCourse(int id, Course update);

    boolean deleteCourse(int id);
    Course findCourse(int id);
    CourseDTO findCourseDto(int id);
}
