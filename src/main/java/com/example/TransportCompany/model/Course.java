package com.example.TransportCompany.model;

import com.example.TransportCompany.constant.CourseType;
import com.example.TransportCompany.sql.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "courses")
//ToDo
public class Course extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "course_id")
    private int courseId;
    private String fromWhere;
    private  String toWhere;
    private CourseType type;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "employee_id",referencedColumnName = "employee_id",nullable = true)
    private Employee employeeId;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "clients_id", referencedColumnName = "client_id", nullable = true)
    private Client clientsId;
}
