package com.example.TransportCompany.model;

import com.example.TransportCompany.sql.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity(name = "cars")
public class Car extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private int carId;
    @NotBlank(message = "Brand must not be blank")
    @Size(min = 2,message = "Brand must be at least 2 characters long")
    private  String brand;
    @NotBlank(message = "Model must not be blank")
    @Size(min = 2,message = "Model must be at least 2 characters long")
    private String model;

    @NotBlank(message = "Registration  must not be blank")
    @Size(min = 3,message = "Registration must be at least 2 characters long")
    private String registration;
    private boolean isAssigned;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "employee_Id",referencedColumnName = "employee_Id",nullable = true)
    private Employee employee;


    public int getCarId() {
        return carId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public Car(){}
    public Car(int id, String brand, String model, String registration) {
        this.carId = id;
        this.brand = brand;
        this.model = model;
        this.registration = registration;
    }
    public int getId() {
        return carId;
    }

    public void setId(int id) {
        this.carId = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }
}
