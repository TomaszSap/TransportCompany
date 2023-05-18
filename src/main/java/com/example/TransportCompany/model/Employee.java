package com.example.TransportCompany.model;

import com.example.TransportCompany.sql.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Entity
@Table(name = "employers")
public class Employee extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "employee_id")
    int employeeId;
    @NotBlank(message = "Name must be not blank")
    @Size(min=2 , max=100, message = "Name should be min 2 symbols and less than 100")
    String name;
    @NotBlank(message = "Surname must be not blank")
    @Size(min=2 , max=100, message = "Surname should be min 2 symbols and less than 100")
    String surname;
    @NotBlank(message = "Email must not be blank")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
            "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
            "|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\" +
            "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?" +
            ":[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",message = "Please provide a valid email address")

    String email;
    @NotBlank(message = "Confirm email must not be blank")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
            "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
            "|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\" +
            "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?" +
            ":[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",message = "Please provide a valid email address")
    @Transient
    private String confirmEmail;
    @NotBlank(message = "Mobile number must not be blank")
    @Pattern(regexp = "(^$|[0-9]{9})",message = "Mobile number must be 9 digits")

    private String mobileNumber;
    @NotBlank(message = "PESEL must not be blank")
    @Pattern(regexp = "(^$|[0-9]{11})",message = "PESEL  must be 11 digits")

    String pesel;
    @OneToOne(fetch =FetchType.EAGER,cascade = CascadeType.PERSIST,targetEntity = Car.class)
    @JoinColumn(name ="car_id",referencedColumnName = "car_id",nullable = true)
    Car car;
    @OneToOne(fetch =FetchType.EAGER,cascade = CascadeType.PERSIST,targetEntity = Role.class)
    @JoinColumn(name ="role_id",referencedColumnName = "role_id",nullable = false)
    private Role role;
    @NotBlank(message = "Password must not be blank")
    @Size(min = 8,message = "Password must be at least 8 characters long")
    //@PasswordValidator
    @JsonIgnore
    private String pwd;
    @NotBlank(message = "Confirm password must not be blank")
    @Size(min = 8,message = " Confirm password must be at least 8 characters long")
    @Transient
    private String confirmPwd;
    @OneToMany(mappedBy ="employeeId",fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, targetEntity = Course.class)
    // @JoinColumn(name = "employee_id",referencedColumnName = "personId",nullable = true)
    private Set<Course> courses;
}
