package com.example.TransportCompany.services;

import com.example.TransportCompany.constant.CourseType;
import com.example.TransportCompany.dto.CourseDTO;
import com.example.TransportCompany.exception.ApiRequestException;
import com.example.TransportCompany.mapper.CourseDTOMapper;
import com.example.TransportCompany.model.Client;
import com.example.TransportCompany.model.Course;
import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.repository.CourseRepository;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.TransportCompany.constant.AppConstants.getNullPropertyNames;

@Service
 class CourseServiceImpl implements CourseService {

    @Autowired
    ClientService clientService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    OpenRouteService openRouteService;
    @Autowired
    CourseDTOMapper courseDTOMapper;

    @SneakyThrows
    @Override
    public boolean saveCourse(Course course) {
        if (course.getType() == null)
            course.setType(CourseType.OPEN);
        boolean isSaved = false;
        Optional<Client> clientEntity = clientService.getClient(course.getClientsId().getClientId());
        if (clientEntity.isPresent()) {
            course.setDistance(openRouteService.calculateDistance(course.getFromWhere(), course.getToWhere()));
            Course course1 = courseRepository.save(course);
            clientEntity.get().getCourses().add(course1);
            clientService.updateClient(clientEntity.get().getClientId(), clientEntity.get());
            if (course.getEmployee() != null) {
                assignCourse(course);
            }
            if (course1 != null && course1.getCourseId() > 0)
                isSaved = true;
        }
        return isSaved;
    }

    @Override
    public List<CourseDTO> findCoursesWithType(String courseType) {
        if (CourseType.isValid(courseType)) ;
        return courseRepository.findByType(CourseType.valueOf(courseType)).stream().map(courseDTOMapper).collect(Collectors.toList());

    }

    @Override
    public List<CourseDTO> findAllCourses() {
        return courseRepository.findAll().stream().map(courseDTOMapper).collect(Collectors.toList());

    }

    private void assignCourse(Course update) {

        var employeeEntity = employeeService.getEmployeeById(update.getEmployee().getEmployeeId());
        if (employeeEntity.isPresent()) {
            employeeEntity.get().getCourses().add(update);
            employeeService.updateEmployee(employeeEntity.get());
        }
    }

    @Override
    public boolean updateCourse(int id, Course update) {
        boolean isUpdated = false;
        Optional<Course> courseEntity = courseRepository.findById(id);
        update.setCourseId(id);
        Course updatedCourse;
        if (courseEntity.isPresent() && courseEntity.get().getCourseId() > 0) {
            if (update.getToWhere() != null && update.getToWhere() != null && (!update.getToWhere().equals(courseEntity.get().getToWhere()) || !update.getFromWhere().equals(courseEntity.get().getFromWhere()))) {
                update.setDistance(openRouteService.calculateDistance(update.getFromWhere(), update.getToWhere()));
            }
            if (update.getClientsId() != null && update.getClientsId().getClientId() != courseEntity.get().getClientsId().getClientId()) {
                changeClient(update, courseEntity.get());
            }
            if (courseEntity.get().getEmployee() != null && update.getEmployee() != null) {
                if ((update.getEmployee().getEmployeeId() != courseEntity.get().getEmployee().getEmployeeId())) {
                    changeEmployee(update, courseEntity.get());
                }
            } else if (update.getEmployee() != null && courseEntity.get().getEmployee() == null) {
                courseEntity.get().setEmployee(update.getEmployee());
                assignCourse(courseEntity.get());

            }

            BeanUtils.copyProperties(update, courseEntity.get(), getNullPropertyNames(update));
            updatedCourse = courseRepository.save(courseEntity.get());
        } else {
            throw new IllegalArgumentException("Course with the given id doesn't exists!");
        }
        if (updatedCourse != null && updatedCourse.getUpdatedBy() != null)
            isUpdated = true;
        return isUpdated;
    }

    private void changeEmployee(Course update, Course courseEntity) {


        Employee updateEmployee = courseEntity.getEmployee();
        updateEmployee.getCourses().remove(courseEntity);
        courseEntity.setEmployee(update.getEmployee());
        var emp = employeeService.getEmployeeById(courseEntity.getEmployee().getEmployeeId());
        if (emp.isPresent()) {
            emp.get().getCourses().add(courseEntity);
            employeeService.updateEmployee(updateEmployee.getEmployeeId(), updateEmployee);
            employeeService.updateEmployee(emp.get().getEmployeeId(), emp.get());
        }

    }

    private void changeClient(Course update, Course courseEntity) {
        Client updateClient = courseEntity.getClientsId();
        updateClient.getCourses().remove(courseEntity);
        Client newClient = update.getClientsId();
        newClient.getCourses().add(update);
        clientService.updateClient(courseEntity.getClientsId().getClientId(), updateClient);
        clientService.updateClient(newClient.getClientId(), newClient);
    }

    @Override
    public boolean deleteCourse(int id) {
        Optional<Course> course = courseRepository.findById(id);
        try {
            if (course.isPresent())
            {  if (course.get().getEmployee() != null) {
                    Optional<Employee> employeeEntity = employeeService.getEmployeeById(course.get().getEmployee().getEmployeeId());
                    if (employeeEntity.isPresent() && employeeEntity.get().getCourses().contains(course.get())) {
                        employeeService.deleteCourseFromTable(employeeEntity.get(), course.get().getCourseId());
                    }
                }
            courseRepository.delete(course.get());
            return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Course findCourse(int id) {
        return courseRepository.findById(id).orElseThrow(()->new ApiRequestException("Course doesn't exists!"));
    }
    @Override
    public CourseDTO findCourseDto(int id) {
        CourseDTO courseDTO;
        var course=courseRepository.findById(id);
        if (course.isPresent()) {
            courseDTO= courseDTOMapper.apply(course.get());
        }
        else {
            throw new ApiRequestException("Given course doesn't exists");
        }
        return courseDTO;
    }

}
