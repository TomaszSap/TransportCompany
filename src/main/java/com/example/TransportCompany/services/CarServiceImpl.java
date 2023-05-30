package com.example.TransportCompany.services;

import com.example.TransportCompany.model.Car;
import com.example.TransportCompany.model.Employee;
import com.example.TransportCompany.repository.CarRepository;
import com.example.TransportCompany.repository.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.TransportCompany.constant.AppConstants.getNullPropertyNames;

@Service
public class CarServiceImpl implements CarService {
    CarRepository carRepository;

    EmployeeRepository employeeRepository;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, EmployeeRepository employeeRepository) {
        this.carRepository = carRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public boolean addCar(Car car) {
        boolean isSaved = false;
        car.setAssigned(false);
        Car result = carRepository.save(car);
        if (result.getId() > 0)
            isSaved = true;
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
        boolean isDeleted = false;
        Optional<Car> carEntity = carRepository.findById(id);
        if (carEntity.isPresent() && carEntity.get().getEmployee() == null) {
            carRepository.delete(carEntity.get());
            isDeleted = true;
        } else if (carEntity.get().getEmployee() != null) {
            Optional<Employee> employee = employeeRepository.findById(carEntity.get().getEmployee().getEmployeeId());
            employee.get().setCar(null);
            employeeRepository.save(employee.get());
            carRepository.delete(carEntity.get());
            isDeleted = true;
        }
        return isDeleted;
    }

    @Override
    public Optional<Car> findCarById(int carId) {
        return carRepository.findById(carId);
    }

    @Override
    public void updateCar(int carId, Car update) {
        var carEntity = carRepository.findById(carId);
        if (carEntity.isPresent()) {
            if (update.getEmployee().getEmployeeId() != carEntity.get().getEmployee().getEmployeeId()) {
                changeCarOwner(carEntity.get().getEmployee().getEmployeeId(), update);
            }
            update.setId(carEntity.get().getId());
            BeanUtils.copyProperties(update, carEntity, getNullPropertyNames(update));
            carRepository.save(update);
        }
    }

    private void changeCarOwner(int employeeId, Car update) {
        var employee = employeeRepository.findById(employeeId);
        var newEmployee = employeeRepository.findById(update.getEmployee().getEmployeeId());
        if (employee.isPresent()&&newEmployee.isPresent()&&newEmployee.get().getCar() == null){
        employee.get().setCar(null);
        employeeRepository.save(employee.get());
            newEmployee.get().setCar(update);
            employeeRepository.save(newEmployee.get());
        }
        }
    }
