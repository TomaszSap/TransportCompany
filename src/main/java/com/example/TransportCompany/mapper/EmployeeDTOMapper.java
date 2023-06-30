package com.example.TransportCompany.mapper;

import com.example.TransportCompany.dto.EmployeeDto;
import com.example.TransportCompany.model.Employee;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EmployeeDTOMapper implements Function<Employee,EmployeeDto> {
    @Override
    public EmployeeDto apply(Employee employee)
    {
        return new EmployeeDto(
                employee.getName(),
                employee.getSurname(),
                employee.getEmail(),
                employee.getMobileNumber(),
                employee.getRole()
        );
    }
}
