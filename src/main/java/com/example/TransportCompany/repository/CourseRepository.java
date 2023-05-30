package com.example.TransportCompany.repository;

import com.example.TransportCompany.constant.CourseType;
import com.example.TransportCompany.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByType(CourseType status);
}
