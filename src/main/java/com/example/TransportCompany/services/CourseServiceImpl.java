package com.example.TransportCompany.services;

import com.example.TransportCompany.constant.CourseType;
import com.example.TransportCompany.model.Client;
import com.example.TransportCompany.model.Course;
import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.repository.CourseRepository;
import lombok.SneakyThrows;
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

    @Autowired
    ClientService clientService;

    EmployeeService employeeService;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    OpenRouteService openRouteService;
    @SneakyThrows
    @Override
    public boolean saveCourse(Course course)  {
        if(course.getType()==null)
            course.setType(CourseType.OPEN);
        boolean isSaved=false;
        Optional<Client> clientEntity = Optional.ofNullable(clientService.getClient(course.getClientsId().getClientId()));
        if (clientEntity.isPresent()) {
            course.setDistance(openRouteService.calculateDistance(course.getFromWhere(), course.getToWhere()));
            Course course1 = courseRepository.save(course);
            clientEntity.get().getCourses().add(course1);
            clientService.updateClient(clientEntity.get().getClientId(),clientEntity.get());
            if (course.getEmployee()!=null)
            {
                assignCourse(course);
            }
        if(course1!=null && course1.getCourseId()>0)
            isSaved=true;
        }
        return isSaved;
    }

    @Override
    public List<Course> findCoursesWithType(String courseType ) {
        if(CourseType.isValid(courseType));
        List<Course> courseList= courseRepository.findByType(CourseType.valueOf(courseType));
        return courseList;
    }
    @Override
    public List<Course> findAllCourses() {
        List<Course> courseList= courseRepository.findAll();
        return courseList;
    }
    private void assignCourse( Course update)
    {

        var employeeEntity=employeeService.getEmployeeById(update.getEmployee().getEmployeeId());
        if (employeeEntity.isPresent())
        {
            employeeEntity.get().getCourses().add(update);
            employeeService.updateEmployee(employeeEntity.get());
        }
    }

    @Override
    public boolean updateCourse(int id , Course update) {
        boolean isUpdated=false;
        Optional<Course> courseEntity=courseRepository.findById(id);
        Course updatedCourse;
        if (!courseEntity.isEmpty() && courseEntity.get().getCourseId()>0)
        {
            try {
                update.setDistance(openRouteService.calculateDistance(update.getFromWhere(),update.getToWhere()));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (update.getClientsId().getClientId()!=courseEntity.get().getClientsId().getClientId())
            {
                changeClient(update,courseEntity.get());
            }
            if (update.getEmployee().getEmployeeId()!=courseEntity.get().getEmployee().getEmployeeId() || courseEntity.get().getEmployee()==null)
            {
                changeEmployee(update,courseEntity.get());
            }
            BeanUtils.copyProperties(update, courseEntity.get(), getNullPropertyNames(update));
            updatedCourse=  courseRepository.save(courseEntity.get());
        }
        else {
            throw new IllegalArgumentException("Course with the given id doesn't exists!");
        }
        if(updatedCourse!=null && updatedCourse.getUpdatedBy()!=null)
            isUpdated=true;
        return isUpdated;
    }
    private void changeEmployee(Course update, Course courseEntity) {
        if (courseEntity.getEmployee()==null)
        {
            assignCourse(courseEntity);
        }
        else {
        Employee updateEmployee= courseEntity.getEmployee();
        updateEmployee.getCourses().remove(courseEntity);
        Employee newEmployee=update.getEmployee();
        newEmployee.getCourses().add(update);
        employeeService.updateEmployee(courseEntity.getEmployee().getEmployeeId(),updateEmployee);
        employeeService.updateEmployee(newEmployee.getEmployeeId(),newEmployee);
        }
    }

    private  void changeClient(Course update,Course courseEntity)
    {
        Client updateClient= courseEntity.getClientsId();
        updateClient.getCourses().remove(courseEntity);
        Client newClient=update.getClientsId();
        newClient.getCourses().add(update);
        clientService.updateClient(courseEntity.getClientsId().getClientId(),updateClient);
        clientService.updateClient(newClient.getClientId(),newClient);
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
    public boolean deleteCourse(int id) {
        Optional<Course> course=courseRepository.findById(id);
    try {
        if(course.isPresent())
            if(course.get().getEmployee() != null){
            Optional<Employee> employeeEntity=employeeService.getEmployeeById(course.get().getEmployee().getEmployeeId());
            if (employeeEntity.isPresent()&& employeeEntity.get().getCourses().contains(course.get())){
            employeeService.deleteCourseFromTable(employeeEntity.get(),course.get().getCourseId());
            }
        }
        courseRepository.delete(course.get());
        return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Course> findCourse(int id) {
        return Optional.of(courseRepository.findById(id).get());
    }


}
