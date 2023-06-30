package com.example.TransportCompany.mapper;

import com.example.TransportCompany.constant.CourseType;
import com.example.TransportCompany.dto.ClientForwarderDTO;
import com.example.TransportCompany.dto.CourseDTO;
import com.example.TransportCompany.dto.EmployeeDto;
import com.example.TransportCompany.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CourseDTOMapper implements Function<Course, CourseDTO> {
    @Autowired
    ClientForwarderDTOMapper clientForwarderDTOMapper;
    @Autowired
    EmployeeDTOMapper employeeDTOMapper;
    @Override
    public CourseDTO apply(Course course) {
        ClientForwarderDTO clientForwarderDTO = clientForwarderDTOMapper.apply(course.getClientsId());

        EmployeeDto employeeDto = null;
        if (course.getType() != CourseType.OPEN) {
            employeeDto = employeeDTOMapper.apply(course.getEmployee());
            return new  CourseDTO(
                    course.getFromWhere(),
                    course.getToWhere(),
                    course.getType(),
                    employeeDto,
                    clientForwarderDTO
            ) ;
        }
        return new  CourseDTO(
                course.getFromWhere(),
                course.getToWhere(),
                course.getType(),
                employeeDto,
                clientForwarderDTO
        ) ;
    }
}
