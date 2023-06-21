package com.example.TransportCompany.controller;

import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class DashboardController {
    @Autowired
    private EmployeeRepository personRepository;
    @RequestMapping("/dashboard")
    public String displayDashboard(Model model, Authentication authentication, HttpSession httpSession) {
        Employee employee=personRepository.findByEmail(authentication.getName());
        model.addAttribute("username",employee.getName());
        model.addAttribute("roles",authentication.getAuthorities().toString());
        httpSession.setAttribute("loggedInPerson",employee);
        return "dashboard.html";
    }
}
