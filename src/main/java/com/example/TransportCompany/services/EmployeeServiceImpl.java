package com.example.TransportCompany.services;

import com.example.TransportCompany.config.PasswordEncoderConfig;
import com.example.TransportCompany.constant.RoleType;
import com.example.TransportCompany.model.Car;
import com.example.TransportCompany.model.Course;
import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.model.Role;
import com.example.TransportCompany.repository.EmployeeRepository;
import com.example.TransportCompany.repository.RoleRepository;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    CarService carService;
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

    @Override
    public boolean deleteEmployee(int employeeId) {
        boolean isDeleted=false;
        Optional<Employee> employee=employeeRepository.findById(employeeId);
        if(employee.isPresent()){
        for(Course course:employee.get().getCourses())
        {
            course.setEmployee(null);
            try {
                courseService.saveCourse(course);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
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
    @Override
    public boolean addCourse(int driverId, int courseId) {
        Optional<Employee> employeeEntity=employeeRepository.findById(driverId);
        Course courseEntity = courseService.findCourse(courseId);
        if (courseEntity == null && !(courseEntity.getCourseId() > 0)&& employeeEntity.isPresent()) //validation if is possible
        {
            if (courseEntity.getEmployee()!=null)
            {
                var x=employeeRepository.findById(courseEntity.getEmployee().getEmployeeId());
                x.get().getCourses().remove(courseEntity.getCourseId());
                employeeRepository.save(x.get());
                courseEntity.setEmployee(null);
            }
            courseEntity.setEmployee(employeeEntity.get());
            try {
                courseService.saveCourse(courseEntity);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            employeeEntity.get().getCourses().add(courseEntity);
            employeeRepository.save(employeeEntity.get());
            return true;
           }
        return false;
    }

    @Override
    public boolean deleteCourse(int employeeId,int courseId) {
        Optional <Employee> employeeEntity=employeeRepository.findById(employeeId);
        Optional<Course> courseEntity= Optional.ofNullable(courseService.findCourse(courseId));
        if(employeeEntity.isPresent()&&courseEntity.isPresent() && employeeEntity.get().getCourses().contains(courseEntity.get()))
        {
            employeeEntity.get().getCourses().remove(courseEntity.get());
            employeeRepository.save(employeeEntity.get());
            courseEntity.get().setEmployee(null);
            courseService.updateCourse(courseEntity.get());
            return true;
        }
        return false;
    }

    @Override
    public void assignCar(int employeeId,int carId) throws Exception {
        Optional <Employee> employeeEntity=employeeRepository.findById(employeeId);
        Optional<Car> carEntity= carService.findCarById(carId);
        if (employeeEntity.isPresent()
                && carEntity.isPresent()
                && carEntity.get().getEmployee()==null
                &&employeeEntity.get().getCar()==null){

            try {
                employeeEntity.get().setCar(carEntity.get());
                carEntity.get().setEmployee(employeeEntity.get());
                employeeRepository.save(employeeEntity.get());
                carService.updateCar(carEntity.get().getCarId(),carEntity.get());
            }
        catch (Exception e) {
            throw new Exception("Error updating entity: ", e);
        }
        }
       // else throw IllegalArgumentException
    }

    @Override
    public boolean unassignCar(int employeeId,int carId) {
        Optional <Employee> employeeEntity=employeeRepository.findById(employeeId);
        Optional<Car> carEntity= carService.findCarById(carId);
        if(employeeEntity.isPresent()&&carEntity.isPresent()  && carEntity.isPresent()
                && employeeEntity.get().getCar().getId()==carEntity.get().getId())
        {
            employeeEntity.get().setCar(null);
            carEntity.get().setEmployee(null);
            employeeRepository.save(employeeEntity.get());
            carService.updateCar(carEntity.get().getCarId(),carEntity.get());
            return true;
        }
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
