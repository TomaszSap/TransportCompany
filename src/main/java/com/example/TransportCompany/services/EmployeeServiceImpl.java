package com.example.TransportCompany.services;

import com.example.TransportCompany.config.PasswordEncoderConfig;
import com.example.TransportCompany.constant.AppConstants;
import com.example.TransportCompany.model.Course;
import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.model.Role;
import com.example.TransportCompany.repository.EmployeeRepository;
import com.example.TransportCompany.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Optional;
//coś do dokończenia
@Service
public class EmployeeServiceImpl implements EmployeeService{
    private static final Logger logger= LoggerFactory.getLogger(EmployeeServiceImpl.class);
    @Autowired
    private PasswordEncoderConfig passwordEncoder;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CourseService courseService;
    @Override
    public void deleteCourseFromTable(Employee employee, int id) {
        Course course=employee.getCourses().stream().filter(item ->item.getCourseId()==id).findFirst().orElse(null);
        if (course!=null)
        {
            employee.getCourses().remove(course);
            employeeRepository.save(employee);
        }
    }

    @Override
    public boolean addEmployee(Employee employee) {
        boolean isSaved=false;
       return addEmployee(employee,isSaved);

    }

    private boolean addEmployee( Employee employee,boolean isSaved)
    {
        Optional<Employee> isAccount=Optional.ofNullable(employeeRepository.findByEmail(employee.getEmail()));
        if(isAccount.isEmpty())
        {
            Role role=new Role();
            try{
           role=roleRepository.getByRoleName(employee.getRole().toString());
            employee.setRole(role);}
            catch (IllegalArgumentException e)
            {
                logger.error("illegal role exception"+ role);
            }
            employee.setPwd(passwordEncoder.encode(employee.getPwd()));
            employee=employeeRepository.save(employee);
        }
        else
        {
            logger.error("Email is in usage !");
            return isSaved;
        }
        if(employee!=null && employee.getEmployeeId()>0)
            isSaved=true;
        return isSaved;
    }

    @Override
    public void changeRole(int employeeId, String role) {
        Optional<Employee> employeeEntity=employeeRepository.findById(employeeId);
        if(!employeeEntity.isEmpty()){
        employeeEntity.get().getRole().setRoleName(role);
        }
    }

    //here
    @Override
    public void deleteEmployee(int employeeId) {
        Optional<Employee> employee=employeeRepository.findById(employeeId);
        for(Course course:employee.get().getCourses())
        {
            course.setEmployeeId(null);
            courseService.saveCourse(course);
        }
        employeeRepository.deleteById(employeeId);
        //ModelAndView modelAndView=new ModelAndView("redirect:/admin/displayClasses");
    //    return modelAndView;
    }

    
    //here
    @Override
    public boolean addCourse(int courseId, HttpSession httpSession) {
        Employee employee = (Employee) httpSession.getAttribute("employee");
        Course courseEntity = courseService.findCourse(courseId);

        if (courseEntity == null || !(courseEntity.getCourseId() > 0)||courseEntity.getEmployeeId()!=null) //validation if is possible
        {
           // modelAndView.setViewName("redirect:/admin/displayStudents?classId="+ schoolClass.getClassId()+"&error=true");
            //return modelAndView;
            //
            return false;}
        courseEntity.setEmployeeId(employee);
        courseService.saveCourse(courseEntity);
        employee.getCourses().add(courseEntity);
        employeeRepository.save(employee);
        return true;
    }

    @Override
    public boolean deleteCourse(int employeeId,int courseId) {
        return false;
    }

    @Override
    public boolean assignCar(int employeeId,int carId) {
        return false;
    }

    @Override
    public boolean unassignCar(int employeeId,int carId) {
        return false;
    }
}
