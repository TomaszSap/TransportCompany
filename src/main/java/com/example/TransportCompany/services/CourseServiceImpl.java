package com.example.TransportCompany.services;

import com.example.TransportCompany.constant.CourseType;
import com.example.TransportCompany.model.Course;
import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
@Service
public class CourseServiceImpl implements CourseService{

    EmployeeService employeeService;
    @Autowired
    CourseRepository courseRepository;

    @Override
    public boolean saveCourse(Course course) {
        if(course.getType()==null)
            course.setType(CourseType.OPEN);
        boolean isSaved=false;
        Course course1=courseRepository.save(course);
        if(course1!=null && course1.getCourseId()>0)
            isSaved=true;
        return isSaved;
    }

    @Override
    public List<Course> findCoursesWithType(String courseType ) {
        if(CourseType.isValid(courseType));
        List<Course> courseList= courseRepository.findByType( CourseType.valueOf(courseType));
        return courseList;
    }


    @Override
    public boolean updateCourseStatus(int id , CourseType type) {
        boolean isUpdated=false;
        Optional<Course> course=courseRepository.findById(id);
        if (!course.isEmpty() && course.get().getCourseId()>0)
        {
            course.get().setType(type);
        }
        Course updatedCourse=courseRepository.save(course.get());
        if(updatedCourse!=null && updatedCourse.getUpdatedBy()!=null)
            isUpdated=true;
        return isUpdated;
    }

    @Override
    public String deleteCourse(int id) {
        Optional<Course> course=courseRepository.findById(id);
        Employee employee= course.get().getEmployeeId();
        course.get().setEmployeeId(null);
        courseRepository.save(course.get());
        employeeService.deleteCourseFromTable(employee,course.get().getCourseId());
        return null;//"redirect:/admin/displayStudents?employeeId="+ course.get().getCourseId();
    }

    @Override
    public Course findCourse(int id) {
        Optional<Course> course=courseRepository.findById(id);
        return course.get();
    }

}
