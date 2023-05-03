package com.example.TransportCompany.services;

import com.example.TransportCompany.constant.CourseType;
import com.example.TransportCompany.model.Course;

import java.util.List;

public interface CourseService {
     boolean saveCourse(Course course);
     List<Course> findCoursesWithType(String courseType);
     boolean updateCourseStatus(int id, CourseType courseType);
     String deleteCourse(int id);
     Course findCourse(int id);
}
