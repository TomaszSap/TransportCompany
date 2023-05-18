package com.example.TransportCompany.services;

import com.example.TransportCompany.model.Car;
import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.repository.CarRepository;
import com.example.TransportCompany.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService
{
    CarRepository carRepository;

    EmployeeRepository employeeRepository;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, EmployeeRepository employeeRepository) {
        this.carRepository = carRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public boolean addCar(Car car) {
        boolean isSaved=false;
        car.setAssigned(false);
        Car result=carRepository.save(car);
        if(result!=null && result.getId()>0)
            isSaved=true;
        return isSaved;
    }

    @Override
    public List<Car> showUnassignedCars() {
        return carRepository.findByIsAssigned(false);
    }

    @Override
    public List<Car> showAllCars() {
        return carRepository.findAll();
    }

    @Override
    public boolean deleteCar(int id) {
        boolean isDeleted=false;
        Optional<Car> carEntity=carRepository.findById(id);
        if(carEntity.isPresent() && carEntity.get().getEmployee()==null)
        {carRepository.delete(carEntity.get());
            isDeleted=true;
        }
        else if(carEntity.get().getEmployee()!=null)
        {
           Optional<Employee> employee = employeeRepository.findById(carEntity.get().getEmployee().getEmployeeId());
           employee.get().setCar(null);
           employeeRepository.save(employee.get());
           carRepository.delete(carEntity.get());
            isDeleted=true;
        }
        return isDeleted;
    }

    @Override
    public Optional<Car> findCarById(int carId) {
       return carRepository.findById(carId);
    }

    @Override
    public void updateCar(int carId,Car update) {
        var carEntity=carRepository.findById(carId);
        if(carEntity.isPresent())
        {
            update.setId(carEntity.get().getId());
            carRepository.save(update);
        }
    }

}
