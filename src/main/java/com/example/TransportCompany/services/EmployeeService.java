package com.example.TransportCompany.services;

import com.example.TransportCompany.model.Course;
import com.example.TransportCompany.model.Employee;

import javax.servlet.http.HttpSession;

public interface EmployeeService {


    abstract void deleteCourseFromTable(Employee employee,int id);
    abstract boolean addEmployee(Employee employee);
    abstract void changeRole(int employeeId, String role);
    abstract void deleteEmployee(int employeeId);
    abstract boolean addCourse(int courseId, HttpSession httpSession);
    abstract boolean deleteCourse(int employeeId,int courseId);
    abstract boolean assignCar(int employeeId,int carId);
    abstract boolean unassignCar(int employeeId,int carId);

}