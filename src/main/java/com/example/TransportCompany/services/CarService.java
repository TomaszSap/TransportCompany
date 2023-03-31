package com.example.TransportCompany.services;


import com.example.TransportCompany.model.Car;

import java.util.List;

public interface CarService
{
    abstract public boolean addCar(Car car);
    abstract public List<Car> showUnassignedCars();
    abstract  public List<Car> showAllCars();
    abstract public boolean deleteCar(int id);
}
