package com.example.TransportCompany.services;

import com.example.TransportCompany.constant.CourseType;
import com.example.TransportCompany.model.Course;

import java.util.List;

public interface CourseService {
    abstract boolean saveCourse(Course course);
    abstract List<Course> findCoursesWithStatus(String courseType);
    abstract boolean updateCourseStatus(int id, CourseType courseType);
    abstract String deleteCourse(int id);
    abstract Course findCourse(int id);
}
