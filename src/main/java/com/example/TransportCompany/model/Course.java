package com.example.TransportCompany.model;

import com.example.TransportCompany.constant.CourseType;
import com.example.TransportCompany.sql.BaseEntity;
import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "courses")
//ToDo
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "courseId")

public class Course extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "course_id")
    private int courseId;
    @NotBlank(message = "Loading place must be not blank")
    @JsonProperty("fromWhere")
    private String fromWhere;
    @NotBlank(message = "Direct city must be not blank")
    @JsonProperty("toWhere")
    private String toWhere;
    private double distance;
    @Column(name = "course_type")
    @Enumerated(EnumType.STRING)
    private CourseType type;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id", nullable = true)
    @JsonIgnoreProperties(value = {"courses", "hibernateLazyInitializer"})

    private Employee employee;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "clients_id", referencedColumnName = "client_id", nullable = true)
    @NotNull
    @JsonIgnoreProperties(value = {"courses", "hibernateLazyInitializer"})

    private Client clientsId;
}
