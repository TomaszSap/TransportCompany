package com.example.TransportCompany.repository;

import com.example.TransportCompany.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    List<Car> findByIsAssigned(boolean isAssigned);
}
