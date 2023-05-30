package com.example.TransportCompany.services;


import com.example.TransportCompany.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarService {
    boolean addCar(Car car);

    List<Car> showUnassignedCars();

    List<Car> showAllCars();

    boolean deleteCar(int id);

    Optional<Car> findCarById(int carId);

    void updateCar(int carId, Car update);
}
