package com.example.TransportCompany.services;

import com.example.TransportCompany.constant.CourseType;
import com.example.TransportCompany.model.Course;
import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.repository.CourseRepository;
import org.json.JSONException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.TransportCompany.constant.AppConstants.getNullPropertyNames;

@Service
public class CourseServiceImpl implements CourseService{

    EmployeeService employeeService;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    OpenRouteService openRouteService;
    @Override
    public boolean saveCourse(Course course) throws JSONException {
        if(course.getType()==null)
            course.setType(CourseType.OPEN);
        boolean isSaved=false;
        try {
            course.setDistance(openRouteService.calculateDistance(course.getFromWhere(), course.getToWhere()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public boolean updateCourse(int id , Course update) {
        boolean isUpdated=false;
        Optional<Course> course=courseRepository.findById(id);
        Course updatedCourse=new Course();
        if (!course.isEmpty() && course.get().getCourseId()>0)
        {
            updatedCourse= course.get();

            BeanUtils.copyProperties(update, course.get(), getNullPropertyNames(update));

            updatedCourse=  courseRepository.save(update);
        }
        else {
            throw new IllegalArgumentException("Course with the given id doesn't exists!");
        }
        if(updatedCourse!=null && updatedCourse.getUpdatedBy()!=null)
            isUpdated=true;
        return isUpdated;
    }

    @Override
    public void updateCourse(Course update) {
        Optional<Course> courseEntity=courseRepository.findById(update.getCourseId());
        if(courseEntity.isPresent())
        {
            courseRepository.save(update);
        }
        else
        {
            throw new IllegalArgumentException("Error during update entity:");
        }
    }

    @Override
    public void deleteCourse(int id) {
        Optional<Course> course=courseRepository.findById(id);
        Employee employee= course.get().getEmployeeId();
        course.get().setEmployeeId(null);
        courseRepository.save(course.get());
     employeeService.deleteCourseFromTable(employee,course.get().getCourseId());
    }

    @Override
    public Course findCourse(int id) {
        Optional<Course> course=courseRepository.findById(id);
        return course.get();
    }


}
