package com.example.TransportCompany.model;

import com.example.TransportCompany.constant.CourseType;
import com.example.TransportCompany.services.OpenRouteService;
import com.example.TransportCompany.sql.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

@Entity
@Data
@Table(name = "courses")
//ToDo
public class Course extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "course_id")
    private int courseId;
    @NotBlank(message = "Loading place must be not blank")
    private String fromWhere;
    @NotBlank(message = "Direct city must be not blank")
    private  String toWhere;
    private  double distance;
    @Column(name = "course_type")
    @Enumerated(EnumType.STRING)
    private CourseType type;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "employee_id",referencedColumnName = "employee_id",nullable = true)
    private Employee employee;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "clients_id", referencedColumnName = "client_id", nullable = true)
    private Client clientsId;
}
