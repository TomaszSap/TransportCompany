package com.example.TransportCompany.services;

import com.example.TransportCompany.model.Course;
import com.example.TransportCompany.model.Employee;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

public interface EmployeeService {


     void deleteCourseFromTable(Employee employee,int id);
     boolean addEmployee(Employee employee);

     boolean deleteEmployee(int employeeId);
     boolean addCourse(int courseId, HttpSession httpSession);
     boolean deleteCourse(int employeeId,int courseId);
     void assignCar(int employeeId,int carId) throws Exception;
     boolean unassignCar(int employeeId,int carId);
     List<Employee> getAllEmployers();
      boolean updateEmployee(Employee employee);
     Employee getEmployeeById(int employeeId);
     Set<Course> getCoursesById(int id);

}
