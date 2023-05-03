package com.example.TransportCompany.services;


import com.example.TransportCompany.model.Car;

import java.util.List;

public interface CarService
{
     public boolean addCar(Car car);
     public List<Car> showUnassignedCars();
      public List<Car> showAllCars();
     public boolean deleteCar(int id);
}
