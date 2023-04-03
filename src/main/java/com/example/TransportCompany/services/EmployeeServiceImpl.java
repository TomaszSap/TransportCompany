package com.example.TransportCompany.services;

import com.example.TransportCompany.config.PasswordEncoderConfig;
import com.example.TransportCompany.constant.RoleType;
import com.example.TransportCompany.model.Course;
import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.model.Role;
import com.example.TransportCompany.repository.EmployeeRepository;
import com.example.TransportCompany.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
           role=roleRepository.getByRoleName(employee.getRole().getRoleName());
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

    private void changeRole(int employeeId, String role) {
        Optional<Employee> employeeEntity=employeeRepository.findById(employeeId);
        var roleEntity=roleRepository.getByRoleName(role);
        if(!employeeEntity.isEmpty()){
        employeeEntity.get().getRole().setRoleId(roleEntity.getRoleId());
        }
    }

    //here
    @Override
    //ToDo
    public boolean deleteEmployee(int employeeId) {
        boolean isDeleted=false;
        Optional<Employee> employee=employeeRepository.findById(employeeId);
        if(employee.isPresent()){
        for(Course course:employee.get().getCourses())
        {
            course.setEmployeeId(null);
            courseService.saveCourse(course);
        }
        employee.get().setRole(null);
      employeeRepository.deleteById(employeeId);
      isDeleted=true;
       }
        return isDeleted;
        //ModelAndView modelAndView=new ModelAndView("redirect:/admin/displayClasses");
    //    return modelAndView;
    }

    public boolean updateEmployee(Employee employee)
    {
        boolean isUpdated=false;
        Optional<Employee> employeeEntity=employeeRepository.findById(employee.getEmployeeId());
        if(employeeEntity.isPresent()){
            if(employee.getRole().getRoleName()!=employeeEntity.get().getRole().getRoleName() &&  RoleType.isValid(employee.getRole().getRoleName()))
                changeRole(employeeEntity.get().getEmployeeId(), employee.getRole().toString());
            else
                return isUpdated;
            employee.setUpdatedAt(LocalDateTime.now());
            //ToDo
            employee.setUpdatedBy("ADMIN");
            employee=employeeRepository.save(employee);
            if(employee!=null && employee.getEmployeeId()>0)
                isUpdated=true;
        }
        return isUpdated;
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

    @Override
    public List<Employee> getAllEmployers() {
        return employeeRepository.findAll();
    }
    @Override
    public Employee getEmployeeById(int employeeId) {
        Optional<Employee> employeeEntity=employeeRepository.findById(employeeId);
        if(employeeEntity.isPresent())
        return employeeEntity.get();
        else return null;
    }

    @Override
    public Set<Course> getCoursesById(int id) {
        Optional<Employee> employeeEntity=employeeRepository.findById(id);
        if(employeeEntity.isPresent())
            return employeeEntity.get().getCourses();
        else return null;

}
}
