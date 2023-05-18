package com.example.TransportCompany.services;

import com.example.TransportCompany.model.Course;
import org.json.JSONException;

import java.util.List;

public interface CourseService {
     boolean saveCourse(Course course) throws JSONException;
     List<Course> findCoursesWithType(String courseType);
      boolean updateCourse(int id , Course update);
      void updateCourse(Course update);
     void deleteCourse(int id);
     Course findCourse(int id);
}
